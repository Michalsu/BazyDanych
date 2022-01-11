import java.sql.*;
import java.util.Arrays;

public class Request {

    private static final String INSERT_DANE_OSOBOWE = "INSERT INTO dane_osobowe(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (?,?,?,?,?)";
    private static final String INSERT_KLIENT = "INSERT INTO klient(koszyk_ID, `dane_osobowe_ID`, login, haslo) VALUES (?,?,?,?)";
    private static final String INSERT_ADRES = "INSERT INTO adres(kod_pocztowy, ulica, numer) VALUES (?,?,?)";
    private static final String INSERT_EMPLOYEE = "INSERT INTO pracownik(dane_osobowe_ID, placowka_id, login, haslo, funkcja) VALUES (?,?,?,?,?)";
    private static final String SELECT_LOGIN = "SELECT login, haslo FROM klient WHERE login = ?";

    public static void parseRequest(String request, Connection con){
        if(request == "")return;
        String[] substrings = request.split("#");
        int parimeters=substrings.length;
        switch(substrings[0]){
            case "LOGIN":
                login(con, substrings[1],substrings[2]);
                break;
            case "SEARCH":
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT * FROM produkt WHERE nazwa LIKE '%");
                for(int i=1;i<parimeters-1;i++){
                    sb.append(substrings[i]);
                    sb.append("%' OR '%");
                }
                sb.append("%';");
                String query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "ADDTOCART":

                sb = new StringBuilder();
                sb.append("SELECT koszyk_id FROM klient WHERE login LIKE '%" + substrings[1] +"%'");

                 query=sb.toString();

                 int koszyk_id = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    koszyk_id = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb.setLength(0);
                sb.append("SELECT COUNT(*) FROM koszyk_produkt WHERE koszyk_ID = "
                                 + koszyk_id + " AND  produkt_ID = " + substrings[2]);


                query=sb.toString();
                int count = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    count = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(count==0){
                    sb.setLength(0);
                    sb.append("UPDATE koszyk \n" +
                            "SET produkt_list = CASE WHEN CAST(produkt_list AS CHAR) = '[]'  \n" +
                            "                 THEN JSON_SET('{}', '$.produkty'," +substrings[2]+" )\n" +
                            "                 ELSE JSON_ARRAY_APPEND(produkt_list, '$.produkty'," +substrings[2]+" ) \n" +
                            "              END\n" +
                            "where koszyk_ID = " + koszyk_id );
                    query=sb.toString();
                    try {
                        Statement stmt=con.createStatement();
                        int rs=stmt.executeUpdate(query);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sb.setLength(0);
                    sb.append("INSERT INTO koszyk_produkt VALUES( '" + koszyk_id +"' , '" + substrings[2] + "' , '"+ substrings[3]+ "' )"  );
                query=sb.toString();

                try {
                    Statement stmt=con.createStatement();
                    int rs=stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }}
                else
                {
                    sb.setLength(0);
                    sb.append("UPDATE koszyk_produkt SET liczba_sztuk = " + substrings[3] + " where koszyk_ID = " +koszyk_id +
                            " AND produkt_id = " + substrings[2] );
                    query=sb.toString();

                    try {
                        Statement stmt=con.createStatement();
                        int rs=stmt.executeUpdate(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
                updatePrice(koszyk_id,Float.parseFloat(substrings[3]),con);



                break;
            case "DELETEFROMCART":
                sb =  new StringBuilder();
                sb.append("SELECT koszyk_id FROM klient WHERE login LIKE '%" + substrings[1] +"%'");

                query=sb.toString();

                 koszyk_id = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    koszyk_id = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb.setLength(0);
                sb.append("SELECT JSON_EXTRACT(produkt_list, \"$.produkty\") FROM koszyk where koszyk_ID = "+ koszyk_id);
                query=sb.toString();
                String produkty = null;
                float wartosc_koszyka =0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    produkty = rs.getString(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                if(produkty.length()-2==1)
                {

                    sb.setLength(0);
                    sb.append("update koszyk set produkt_list = JSON_REMOVE(produkt_list, '$.produkty[0]') where koszyk_ID =" + koszyk_id);
                    query = sb.toString();
                    try {
                        Statement stmt = con.createStatement();
                        int rs = stmt.executeUpdate(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                else if(produkty.length()-2>1)
                {
                    int[] products = Arrays.stream(produkty.substring(1, produkty.length() - 1).split(","))
                            .map(String::trim).mapToInt(Integer::parseInt).toArray();


                    int index = 0;
                    for (int i = 0; i < products.length; i++) {
                        if (products[i] == Integer.parseInt(substrings[2]))
                            index = i;
                    }
                    sb.setLength(0);
                    sb.append("update koszyk set produkt_list = JSON_REMOVE(produkt_list, '$.produkty[" + index
                            + "]') where koszyk_ID =" + koszyk_id);
                    query = sb.toString();
                    try {
                        Statement stmt = con.createStatement();
                        int rs = stmt.executeUpdate(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                updatePrice(koszyk_id,Float.parseFloat(substrings[2]),con);

                sb.setLength(0);
                sb.append("DELETE FROM koszyk_produkt WHERE koszyk_ID = "+ koszyk_id +
                        " and produkt_id = " + substrings[2]);
                query = sb.toString();
                try {
                    Statement stmt = con.createStatement();
                    int rs = stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                break;
            case "BUY":
                //TODO
                //
                //
                //
                //
                break;
            case "LOGOUT":
                //TODO
                //
                //
                //
                //
                break;
            case "COMPARE":
                StringBuilder sb2 = new StringBuilder();
                sb = new StringBuilder();
                sb.append("SELECT cena FROM produkt WHERE produkt_id = " + substrings[1] + " ;");
                sb2.append("SELECT cena FROM produkt WHERE produkt_id = " + substrings[2] + " ;");
                query=sb.toString();
                System.out.println(sb);
                String query2 = sb2.toString();
                int price1=0;
                int price2=0;

                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                    price1 = rs.getInt(1);
                     stmt=con.createStatement();
                    ResultSet rs2 = stmt.executeQuery(query2);
                    rs2.next();
                    price2 = rs2.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "REGISTER":
                int reg=-1;
                if(substrings.length==10){
                    reg = register(con,substrings[1],substrings[2],substrings[3],substrings[4],
                            Integer.parseInt(substrings[5]),substrings[6],Integer.parseInt(substrings[7]),
                            substrings[8],Integer.parseInt(substrings[9]));
                }

                else System.out.println("Błędny ciąg");
                break;
            case "ADDPRODUCT":
                 sb = new StringBuilder();
                sb.append("INSERT INTO produkt(produkt_id, cena, promocja, nazwa, opis, kategoria_id) VALUES");
                sb.append("(' " + substrings[1] + "',");
                sb.append("' " + substrings[2] + "',");
                sb.append("' " + substrings[3] + "',");
                sb.append("' " + substrings[4] + "',");
                sb.append("' " + substrings[5] + "',");
                sb.append("' " + substrings[6] + "');");
                 query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    int rs=stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "CHANGEPRODUCT":
                sb = new StringBuilder();
                sb.append("UPDATE produkt SET ");
                sb.append("cena = ' " + substrings[2]+ "', ");
                sb.append("Promocja = ' " + substrings[3]+ "', ");
                sb.append("nazwa = ' " + substrings[4]+ "', ");
                sb.append("opis = ' " + substrings[5]+ "', ");
                sb.append("cena = ' " + substrings[6]+ "' ");
                sb.append("WHERE produkt_id = " + substrings[1]+ ";");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    int rs=stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();

                }
                break;
            case "ADDEMPLOYEE":

                break;
        }
    }

    private static void updatePrice(int koszyk_id, float produkt_id, Connection con){

        StringBuilder sb =  new StringBuilder();
        String query;
        sb.setLength(0);
        sb.append("SELECT JSON_EXTRACT(produkt_list, \"$.produkty\") FROM koszyk where koszyk_ID = "+ koszyk_id);
        query=sb.toString();

        String produkty = null;
        float wartosc_koszyka =0;

        try {

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
            produkty = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(produkty.length()-2==0) {
            wartosc_koszyka = 0;
            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = " + wartosc_koszyka + "WHERE (koszyk_ID =" + koszyk_id + " )");
            query = sb.toString();
            try {
                Statement stmt = con.createStatement();
                int rs = stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();


            }
        }
       else if(produkty.length()-2==1)
        {
            int[] products = Arrays.stream(produkty.substring(1, produkty.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();

            sb.setLength(0);
            sb.append("SELECT cena FROM produkt WHERE produkt_id = " + products[0]);
            query=sb.toString();

            float cena = 0;
            try {

                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                rs.next();
                cena = rs.getFloat(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int productc_count = 0;
            sb.setLength(0);
            sb.append("SELECT liczba_sztuk FROM koszyk_produkt WHERE produkt_id = " +products[0] +
                    " and koszyk_ID = "+ koszyk_id);
            query=sb.toString();

            try {

                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                rs.next();
                productc_count = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            wartosc_koszyka = productc_count * cena;


            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = "+ wartosc_koszyka +  "WHERE (koszyk_ID =" + koszyk_id + " )");
            query=sb.toString();
            try {
                Statement stmt=con.createStatement();
                int rs=stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();


            }}
        else
        {
            int[] products = Arrays.stream(produkty.substring(1, produkty.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();



            for(int i = 0; i<products.length;i++ )
            {
                sb.setLength(0);
                sb.append("SELECT cena FROM produkt WHERE produkt_id = " +products[i]);
                query=sb.toString();

                float cena = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    cena = rs.getFloat(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                 int productc_count = 0;
                sb.setLength(0);
                sb.append("SELECT liczba_sztuk FROM koszyk_produkt WHERE produkt_id = " +products[i] +
                        " and koszyk_ID = "+ koszyk_id);
                query=sb.toString();

                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    productc_count = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                wartosc_koszyka = wartosc_koszyka +  productc_count * cena;
            }

            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = "+ wartosc_koszyka +  "WHERE (koszyk_ID =" + koszyk_id + " )");
            query=sb.toString();
            try {
                Statement stmt=con.createStatement();
                int rs=stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();
            }}
    }
    private static int deleteFromCart(){
        return -1;
    }
    private static int compare(){
        return -1;
    }
    private static int logout(){
        return -1;
    }
    private static int buy(){
        return -1;
    }


    /**
     *
     * @param con connection
     * @param nickname of the user trying to login
     * @param password to check
     * @return 0 - password correct, -1 - password incorrect, -2 nickname dont exists
     *
     */
    public static int login(Connection con, String nickname, String password){
        String salt = null;
        String haslo = null;
        if(DataSecurity.containIllegalSymbols(nickname)) return -2;
        try {
            Statement stmt=con.createStatement();
            PreparedStatement ps = con.prepareStatement(SELECT_LOGIN);
            ps.setString(1, nickname);
            ResultSet rs= ps.executeQuery();
            if (rs.next() == false) {
                return -2;
            }
            haslo = rs.getString("haslo");
            salt = haslo.split(":")[1];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(DataSecurity.checkPasswords(password,salt, haslo)) return 0;
        return -1;
    }


    public static int register(Connection con, String logi, String haslo, String imie, String nazwisko, int numerTel,String mail, int kodPoczt, String ulica, int numerMiesz){
        if(DataSecurity.containIllegalSymbols(logi)) return -3;
        if(DataSecurity.containIllegalSymbols(haslo)) return -4;

        try {

            Statement stmt;
            PreparedStatement ps = con.prepareStatement(SELECT_LOGIN);
            ps.setString(1, logi);
            //executeQuery("SELECT login FROM klient WHERE login = 'logi'");
            ResultSet rs= ps.executeQuery();
            if (rs.next() == true) {
                return -2;
            } else {
                stmt=con.createStatement();


                //stmt.executeUpdate("INSERT INTO adres(kod_pocztowy, ulica, numer) VALUES (kodPoczt,'Szkolna',12)");

                ps = con.prepareStatement(INSERT_ADRES);
                ps.setInt(1,kodPoczt);
                ps.setString(2,ulica);
                ps.setInt(3, numerMiesz);
                ps.executeUpdate();


                //rs = stmt.executeQuery("SELECT auto_increment FROM information_schema.tables WHERE table_schema = 'baza' AND table_name = 'adres';");
                rs=stmt.executeQuery("SELECT adres_id FROM adres order by adres_id desc limit 1");
                rs.next();
                int adresid= rs.getInt(1);
                ps = con.prepareStatement(INSERT_DANE_OSOBOWE);
                ps.setInt(1,adresid);
                ps.setString(2,imie);
                ps.setString(3,nazwisko);
                ps.setInt(4,numerTel);
                ps.setString(5,mail);
                ps.executeUpdate();

                //stmt.executeUpdate("INSERT INTO 'dane osobowe'(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES ()");
                rs=stmt.executeQuery("SELECT dane_osobowe_ID FROM dane_osobowe order by dane_osobowe_ID desc limit 1");
                rs.next();
                int daneosid= rs.getInt(1);

                ps=con.prepareStatement(INSERT_KLIENT);
                stmt.executeUpdate("INSERT INTO koszyk(produkt_list,wartosc_koszyka) VALUES (JSON_ARRAY(), 0);");
                rs=stmt.executeQuery("SELECT koszyk_ID FROM koszyk order by koszyk_ID desc limit 1");
                rs.next();
                int koszykid= rs.getInt(1);
                ps.setInt(1,koszykid);
                ps.setInt(2,daneosid);
                ps.setString(3,logi);
                ps.setString(4,DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()));
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int addEmployee(Connection con, String logi, String haslo, String imie, String nazwisko, int numerTel,String mail, int kodPoczt, String ulica, int numerMiesz, int placowka, String funkcja){
        try {

            Statement stmt;
            PreparedStatement ps = con.prepareStatement(SELECT_LOGIN);
            ps.setString(1, logi);
            ResultSet rs= ps.executeQuery();
            if (rs.next() == true) {
                return -2;
            } else {
                stmt=con.createStatement();

                ps = con.prepareStatement(INSERT_ADRES);
                ps.setInt(1,kodPoczt);
                ps.setString(2,ulica);
                ps.setInt(3, numerMiesz);
                ps.executeUpdate();

                rs=stmt.executeQuery("SELECT adres_id FROM adres order by adres_id desc limit 1");
                rs.next();
                int adresid= rs.getInt(1);
                ps = con.prepareStatement(INSERT_DANE_OSOBOWE);
                ps.setInt(1,adresid);
                ps.setString(2,imie);
                ps.setString(3,nazwisko);
                ps.setInt(4,numerTel);
                ps.setString(5,mail);
                ps.executeUpdate();
                
                rs=stmt.executeQuery("SELECT dane_osobowe_ID FROM dane_osobowe order by dane_osobowe_ID desc limit 1");
                rs.next();
                int daneosid= rs.getInt(1);

                ps=con.prepareStatement(INSERT_EMPLOYEE);
                ps.setInt(1,daneosid);
                ps.setInt(2,placowka);
                ps.setString(3,logi);
                ps.setString(4,DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()));
                ps.setString(5, funkcja);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return -1;
    }


}
