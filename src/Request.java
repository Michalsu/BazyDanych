import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

public class Request {

    private static final String INSERT_DANE_OSOBOWE = "INSERT INTO dane_osobowe(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (?,?,?,?,?)";
    private static final String INSERT_KLIENT = "INSERT INTO klient(koszyk_ID, `dane_osobowe_ID`, login, haslo) VALUES (?,?,?,?)";
    private static final String INSERT_ADRES = "INSERT INTO adres(kod_pocztowy, ulica, numer) VALUES (?,?,?)";
    private static final String INSERT_MAGAZYN = "INSERT INTO magazyn(przestrzen_magazynowa, maksymalna_przestrzen_magazynowa) VALUES (?, ?)";
    private static final String INSERT_PLACOWKA = "INSERT INTO placowka(adres_id, magazyn_ID, nazwa) VALUES (?,?,?)";
    private static final String INSERT_EMPLOYEE = "INSERT INTO pracownik(dane_osobowe_ID, placowka_id, login, haslo, funkcja, zmianahasla) VALUES (?,?,?,?,?,?)";
    private static final String SELECT_LOGIN_USER = "SELECT login, haslo FROM klient WHERE login = ?";
    private static final String SELECT_LOGIN_EMP = "SELECT login, haslo, zmianahasla FROM pracownik WHERE login = ?";
    private static final String UPDATE_PASSWORD_EMP = "UPDATE pracownik SET haslo = ?, zmianahasla = ? WHERE login = ?";
    private static final String UPDATE_PASSWORD_USER = "UPDATE klient SET haslo = ? WHERE login = ?";

    public static String parseRequest(String request, Connection con){
        if(request == "")return "ERROR NO REQUEST";
        //if(DataSecurity.containIllegalSymbols(request)) return "ERROR ILLEGAL SYMBOL";
        request= DataSecurity.sanitizeInput(request);
        String[] substrings = request.split("#");
        int parimeters=substrings.length;
        int exCode=-1;
        String response ="ERROR";
        switch(substrings[0]){
            case "LOGINUSER":
                exCode=login(con, substrings[1],substrings[2],"user");
                if(exCode==0) response = "LOGIN#SUCCESSFUL";
                else if(exCode==-1) response = "LOGIN#WRONGPASS";
                else if(exCode==-2) response = "LOGIN#WRONGNAME";
                else response = "LOGIN#ERROR: "+exCode;
                break;
            case "LOGINEMP":
                exCode=login(con, substrings[1],substrings[2],"emp");
                if(exCode==0) response = "LOGIN#SUCCESSFUL";
                else if(exCode==-1) response = "LOGIN#WRONGPASS";
                else if(exCode==-2) response = "LOGIN#WRONGNAME";
                else if(exCode==1) response = "LOGIN#OLDPASS";
                else response = "LOGIN#ERROR: "+exCode;
                break;
            case "SEARCH":
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT p.produkt_ID as ID, p.cena, p.Promocja, p.nazwa, p.opis, k.Nazwa as kategoria, m.dostepnailosc as ilosc FROM baza.produkt p JOIN baza.kategoria k ON p.kategoria_ID=k.kategoria_ID JOIN (SELECT produkt_ID ,sum(liczba_produktow) as dostepnailosc FROM baza.magazyn_produkt group by produkt_ID) m ON p.produkt_ID=m.produkt_ID WHERE p.nazwa LIKE '%");
                if(parimeters>1){
                    for(int i=1;i<parimeters-1;i++){
                        sb.append(substrings[i]);
                        sb.append("%' OR p.nazwa LIKE '%");
                    }
                    sb.append(substrings[parimeters-1]);
                }
                sb.append("%';");

                String query=sb.toString();
                System.out.println(query);
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    int i=0;
                    StringBuilder ab= new StringBuilder();
                    while(rs.next())
                    {
                        response = rs.getFloat("cena")+"#"+ rs.getInt("Promocja") +"#"+
                                rs.getString("nazwa")+ "#"+rs.getString("opis")+"#"+
                                rs.getString("kategoria")+"#"+
                                rs.getInt("ID")+"#"+rs.getInt(("ilosc"))+"#";
                        i++;
                        ab.append(response);

                    }
                    ab.insert(0,"SEARCH#"+i+"#");
                    response= ab.toString();
                   // System.out.println(response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "ADDTOCART":

                sb = new StringBuilder();
                sb.append("SELECT koszyk_id FROM klient WHERE login = '" + substrings[1] +"'");

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

                response = "Dodano do koszyka";
                updatePrice(koszyk_id,con);

                break;
            case "GETITEMSFROMCART":
                sb = new StringBuilder();
                sb.append("SELECT koszyk_id FROM klient WHERE login = '" + substrings[1] +"'");
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
                List<Pair<Integer,Integer>> products = new ArrayList<>();
                products =  getProductsFromCart(koszyk_id,con);
                StringBuilder resp = new StringBuilder();
                resp.append("GETITEMSFROMCART#");
                for(int i=0;i<products.size();i++)
                    resp.append(products.get(i).getR()+ "#"+products.get(i).getL() + "#");
                resp.deleteCharAt(resp.length()-1);
                System.out.println(resp.toString());
                response = resp.toString();
                break;
            case "DELETEFROMCART":
                sb = new StringBuilder();
                sb.append("SELECT koszyk_id FROM klient WHERE login = '" + substrings[1] +"'");
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
                deleteFromCart(Integer.parseInt(substrings[2]),koszyk_id,con);
                break;
            case "BUY":
                boolean ifEmpty = false;

                sb = new StringBuilder();
                sb.append("START TRANSACTION;");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb = new StringBuilder();
                sb.append("SELECT koszyk_id, klient_ID FROM klient WHERE login = '" + substrings[1] +"';");

                query=sb.toString();
                koszyk_id = 0;
                int klient_id = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    koszyk_id= rs.getInt(1);
                    klient_id = rs.getInt(2);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb = new StringBuilder();
                sb.append("SELECT liczba_sztuk FROM koszyk_produkt WHERE koszyk_ID = " + koszyk_id + ";");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(!rs.next())
                            ifEmpty = true;

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                if(!ifEmpty) {
                    List<Pair<Integer, Integer>> listOfProducts = getProductsFromCart(koszyk_id, con);
                    List<Pair<Integer, Integer>> availableListOfProducts = new ArrayList<>();
                    List<Integer> unavailableListOfProducts;

                    unavailableListOfProducts = checkAvailable(listOfProducts, con);


                    if (unavailableListOfProducts.size() == 0)
                        availableListOfProducts = listOfProducts;
                    else {
                        int i = 0;
                        while (unavailableListOfProducts.size() != 0) {
                            deleteFromCart(unavailableListOfProducts.get(i), koszyk_id, con);
                            unavailableListOfProducts.remove(i);
                            i++;
                        }
                        response = "Brak przedmiotów w magazynie";
                    }


                    if (availableListOfProducts.size() != 0) {
                        for (int i = 0; i < availableListOfProducts.size(); i++)
                            updateMagazine(availableListOfProducts.get(i).getR(), availableListOfProducts.get(i).getL(), con);


                        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                        StringBuilder arr = new StringBuilder();
                        arr.append("[");
                        for (int i = 0; i < availableListOfProducts.size(); i++) {
                            arr.append(availableListOfProducts.get(i).getR() + ", ");
                        }
                        arr.deleteCharAt(arr.length() - 2);
                        arr.append("]");

                        sb = new StringBuilder();
                        sb.append("SELECT wartosc_koszyka FROM koszyk WHERE koszyk_ID = " + koszyk_id + ";");

                        query = sb.toString();

                        int price = 0;
                        try {

                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            rs.next();
                            price = rs.getInt(1);

                        } catch (SQLException e) {
                            e.printStackTrace();

                        }

                        sb = new StringBuilder();
                        sb.append("INSERT INTO zamowienie(klient_ID, Data, Stan, Platnosc, SposobDostawy, produkt_list, Wartosc_zamowienia)" +
                                " Values( '" + klient_id + "' ,'" + date + "' , '" + substrings[2] + "' , '" + substrings[3] + "' , '" + substrings[4]
                                + "' ,'" + arr + "' ,'" + price + "' );");
                        query = sb.toString();
                        try {
                            Statement stmt = con.createStatement();
                            int rs = stmt.executeUpdate(query);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        sb = new StringBuilder();
                        sb.append("UPDATE koszyk SET produkt_list = '[]', `wartosc_koszyka` = '0' WHERE (koszyk_ID = " + koszyk_id + ");");
                        query = sb.toString();

                        try {
                            Statement stmt = con.createStatement();
                            int rs = stmt.executeUpdate(query);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sb = new StringBuilder();
                        sb.append("DELETE FROM koszyk_produkt WHERE (koszyk_ID = " + koszyk_id + ");");
                        query = sb.toString();

                        try {
                            Statement stmt = con.createStatement();
                            int rs = stmt.executeUpdate(query);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        response = "OK";
                    }


                    sb = new StringBuilder();
                    sb.append("COMMIT;");
                    query = sb.toString();
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    sb = new StringBuilder();
                    sb.append("SELECT magazyn_ID from magazyn;");
                    query = sb.toString();
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next())
                            updateStorageSpace(rs.getInt(1), con);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

                break;
            case "LOGOUT":

                break;
            case "COMPARE":
                StringBuilder sb2 = new StringBuilder();
                sb = new StringBuilder();
                sb.append("SELECT cena FROM produkt WHERE produkt_id = " + substrings[1] + " ;");
                sb2.append("SELECT cena FROM produkt WHERE produkt_id = " + substrings[2] + " ;");
                query=sb.toString();

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

                if(substrings.length==10){
                    exCode = register(con,substrings[1],substrings[2],substrings[3],substrings[4],
                            Integer.parseInt(substrings[5]),substrings[6],Integer.parseInt(substrings[7]),
                            substrings[8],Integer.parseInt(substrings[9]));
                }else System.out.println("Błędny ciąg");
                if(exCode==0) response = "REGISTER#SUCCESSFUL";
                else if (exCode==-1) response = "REGISTER#PHONE OR EMAIL USED";
                else if (exCode==-2) response = "REGISTER#USERNAME ALREADY USED";
                else response = "REGISTER#ERROR "+exCode;
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
            case "ADDPRODUCTTOMAGAZINE":

                boolean ifExist = false;
                sb = new StringBuilder();
                sb.append("SELECT liczba_produktow FROM magazyn_produkt where magazyn_ID = " + substrings[1] +
                        " and produkt_ID = " +substrings[2]);
                query=sb.toString();


                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    if(rs.next())
                    ifExist = true;


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb = new StringBuilder();
                sb.append("SELECT przestrzen_magazynowa FROM magazyn where magazyn_ID = " + substrings[1] );

                query=sb.toString();

            int space =0;
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    space = rs.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(space>=Integer.parseInt(substrings[3])){
                if(!ifExist){
                sb = new StringBuilder();
                sb.append(" INSERT INTO magazyn_produkt (magazyn_ID, produkt_ID, liczba_produktow) VALUES ('"+ substrings[1] + "' ,'"+
                        substrings[2] + "' ,'" + substrings[3] +  "');");
                query=sb.toString();

                try {
                    Statement stmt=con.createStatement();
                    int rs=stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }}
                else
                {
                    sb = new StringBuilder();
                    sb.append(" UPDATE magazyn_produkt SET liczba_produktow = " + substrings[3] + " where magazyn_ID = " + substrings[1] +
                            " and produkt_ID = " +substrings[2]);
                    query=sb.toString();

                    try {
                        Statement stmt=con.createStatement();
                        int rs=stmt.executeUpdate(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }}

                updateStorageSpace(Integer.parseInt(substrings[1]),con);
                }
                break;
            case "DELETEPRODUCT":

                sb = new StringBuilder();

                sb.append("START TRANSACTION");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                List<Integer> clients = new ArrayList<Integer>();
                sb = new StringBuilder();
                sb.append("SELECT koszyk_ID from koszyk;");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while(rs.next())
                        clients.add(rs.getInt(1));

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i<clients.size();i++)
                    deleteFromCart(Integer.parseInt(substrings[1]), clients.get(i), con);

                for(int i = 0; i<clients.size();i++)
                    updatePrice(clients.get(i), con);


                sb = new StringBuilder();
                sb.append("Update magazyn_produkt set liczba_produktow = '0' where produkt_ID = " + substrings[1]+";");
                query = sb.toString();
                try {
                    Statement stmt = con.createStatement();
                    int rs = stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                sb = new StringBuilder();
                sb.append("SELECT magazyn_ID from magazyn;");
                query = sb.toString();
                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next())
                        updateStorageSpace(rs.getInt(1), con);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                sb = new StringBuilder();
                sb.append("DELETE FROM produkt where produkt_ID = " + substrings[1]+";");
                query = sb.toString();
                try {
                    Statement stmt = con.createStatement();
                    int rs = stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                sb = new StringBuilder();
                sb.append("COMMIT;");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "CHANGEPRODUCT":

                sb = new StringBuilder();
              clients = new ArrayList<Integer>();
                sb.append("START TRANSACTION");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb.setLength(0);
                sb.append("SELECT koszyk_ID from koszyk FOR UPDATE;");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                   while(rs.next())
                       clients.add(rs.getInt(1));

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sb.setLength(0);
                sb = new StringBuilder();
                sb.append("UPDATE produkt SET ");
                sb.append("cena = ' " + substrings[2]+ "', ");
                sb.append("Promocja = ' " + substrings[3]+ "', ");
                sb.append("nazwa = ' " + substrings[4]+ "', ");
                sb.append("opis = ' " + substrings[5]+ "', ");
                sb.append("kategoria_ID = ' " + substrings[6]+ "' ");
                sb.append("WHERE produkt_id = " + substrings[1]+ ";");
                query=sb.toString();

                try {
                    Statement stmt=con.createStatement();
                    int rs=stmt.executeUpdate(query);


                } catch (SQLException e) {
                    e.printStackTrace();

                }
                for(int i = 0; i<clients.size();i++)
                    updatePrice(clients.get(i), con);

                sb.setLength(0);
                sb.append("COMMIT;");
                query=sb.toString();
                try {
                    Statement stmt=con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "ADDEMPLOYEE":

                    exCode = addEmployee(con,substrings[1],substrings[2],substrings[3],substrings[4],
                            Integer.parseInt(substrings[5]),substrings[6],Integer.parseInt(substrings[6]),
                            substrings[7],Integer.parseInt(substrings[8]),Integer.parseInt(substrings[9]),substrings[10]);
                    if(exCode==0) response="OK";
                    else response="ERROR"+exCode;
                break;
            case "ADDOUTPOST":
                exCode = addOutpost(con,Integer.parseInt(substrings[1]),Integer.parseInt(substrings[2]),
                            substrings[3],Integer.parseInt(substrings[4]),substrings[5]);
                if(exCode==0) response="OK";
                else response="ERROR"+exCode;
                break;

            case "CHANGEPASSEMP":
                exCode = changePassword(con, substrings[1], substrings[2], substrings[3], "emp");
                if(exCode==0) response="OK";
                else response="ERROR"+exCode;
                break;

            case "CHANGEPASSUSER":
                exCode = changePassword(con, substrings[1], substrings[2], substrings[3],"user");
                if(exCode==0) response="OK";
                else response="ERROR"+exCode;
                break;
        }
        return response;
    }

    public static int changeEmpPass(Connection con){

        return -1;
    }
    private static List<Pair<Integer,Integer>> getProductsFromCart(int koszyk_id,  Connection con)
    {
        List<Pair<Integer, Integer>> listOfProducts = new ArrayList<Pair<Integer, Integer>>();
        StringBuilder sb =  new StringBuilder();
        String query;
        sb.setLength(0);
        sb.append("SELECT JSON_EXTRACT(produkt_list, \"$.produkty\") FROM koszyk where koszyk_ID = "+ koszyk_id + " ;");
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

        if(produkty!=null) {
            String[] items = produkty.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

            int[] products = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    products[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                }
            }
            ;

            int count = 0;


            for (int i = 0; i < products.length; i++) {
                sb.setLength(0);
                sb.append("SELECT liczba_sztuk FROM koszyk_produkt where koszyk_ID = "
                        + koszyk_id + " and produkt_ID = " + products[i] + ";");
                query = sb.toString();
                Pair pair = new Pair();
                try {

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                    count = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                pair.setL(count);
                pair.setR(products[i]);
                listOfProducts.add(pair);

            }
        }
        return  listOfProducts;

    }

    private static void updateStorageSpace(int magazyn_ID,  Connection con)
    {
        StringBuilder sb =  new StringBuilder();
        String query;
        sb.append("SELECT maksymalna_przestrzen_magazynowa FROM magazyn where magazyn_ID = "+ magazyn_ID + ";");
        query=sb.toString();
        int storageSpace = 0;
        try {

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            rs.next();
           storageSpace= rs.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }


         sb =  new StringBuilder();
        sb.append("SELECT liczba_produktow, produkt_ID FROM magazyn_produkt where magazyn_ID = "+ magazyn_ID + ";");
        query=sb.toString();

        int allCount = 0;


        List <Pair<Integer, Integer>> products = new ArrayList<Pair<Integer, Integer>>();


        try {

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()) {
                Pair pair = new Pair(rs.getInt(1),rs.getInt(2));
                products.add(pair);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

for (int i = 0; i<products.size();i++)
    if(products.get(i).getL()==0) {

        sb.setLength(0);
        sb.append("DELETE FROM magazyn_produkt where magazyn_ID = " + magazyn_ID + " and produkt_ID = " + products.get(i).getR());
        query = sb.toString();
        products.remove(i);
        try {
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        StringBuilder sb2 =  new StringBuilder();

if(products.size()==0)
    sb2.append("[]");
else {
    for (Pair<Integer, Integer> p : products) {
        allCount = allCount + p.getL();
        sb2.append(p.getR() + ",");
    }

    sb2.insert(0,"[");
    sb2.deleteCharAt(sb2.length()-1);
    sb2.append("]");
}
         sb =  new StringBuilder();
        sb.append("UPDATE magazyn SET `lista_produktow_dostepnych` = '" + sb2 + "' , przestrzen_magazynowa = '" + (storageSpace-allCount) + "' WHERE magazyn_ID = " + magazyn_ID);
        query=sb.toString();

        try {
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





    private static void updatePrice(int koszyk_id,  Connection con){

        StringBuilder sb =  new StringBuilder();
        String query;
        sb.setLength(0);
        sb.append("SELECT JSON_EXTRACT(produkt_list, \"$.produkty\") FROM koszyk where koszyk_ID = "+ koszyk_id + ";");
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


        if(produkty!=null) {
            String[] items = produkty.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");


            int[] products = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    products[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                }
            }


        if(produkty.length()==1)
        {

            sb.setLength(0);
            sb.append("SELECT cena, Promocja FROM produkt WHERE produkt_id = " + products[0] + ";");
            query=sb.toString();

            float cena = 0;
            float promocja = 0;

            try {

                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                rs.next();
                cena = rs.getFloat(1);
                promocja = rs.getFloat(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int productc_count = 0;
            sb.setLength(0);
            sb.append("SELECT liczba_sztuk FROM koszyk_produkt WHERE produkt_id = " +products[0]+
                    " and koszyk_ID = "+ koszyk_id + ";");
            query=sb.toString();

            try {

                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                rs.next();
                productc_count = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }


            wartosc_koszyka = productc_count * cena* ((100-promocja)/100);


            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = "+ wartosc_koszyka +  "WHERE (koszyk_ID =" + koszyk_id + " );");
            query=sb.toString();
            try {
                Statement stmt=con.createStatement();
                int rs=stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();


            }}
        else if( produkty.length()>1)
        {


            for (int i = 0; i < items.length; i++) {
                try {
                    products[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                    //NOTE: write something here if you need to recover from formatting errors
                }};



            for(int i = 0; i<products.length;i++ )
            {
                sb.setLength(0);
                sb.append("SELECT cena, Promocja FROM produkt WHERE produkt_id = " +products[i] + ";");
                query=sb.toString();

                float cena = 0;
                float promocja = 0;
                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    cena = rs.getFloat(1);
                    promocja = rs.getFloat(2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                 int productc_count = 0;
                sb.setLength(0);
                sb.append("SELECT liczba_sztuk FROM koszyk_produkt WHERE produkt_id = " +products[i] +
                        " and koszyk_ID = "+ koszyk_id + ";");
                query=sb.toString();

                try {

                    Statement stmt=con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);
                    rs.next();
                    productc_count = rs.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                wartosc_koszyka = wartosc_koszyka +  productc_count * cena * ((100 - promocja)/100);

            }
            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = "+ wartosc_koszyka +  "WHERE (koszyk_ID =" + koszyk_id + " );");
            query=sb.toString();

            try {
                Statement stmt=con.createStatement();
                int rs=stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }}else
        {
            wartosc_koszyka = 0;
            sb.setLength(0);
            sb.append("UPDATE koszyk SET wartosc_koszyka = " + wartosc_koszyka + "WHERE (koszyk_ID =" + koszyk_id + " );");
            query = sb.toString();
            try {
                Statement stmt = con.createStatement();
                int rs = stmt.executeUpdate(query);

            } catch (SQLException e) {
                e.printStackTrace();


            }
        }
    }
    private static void updateMagazine(int produkt_id, int countOfProduct , Connection con){

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT liczba_produktow, magazyn_ID FROM magazyn_produkt WHERE produkt_ID = " + produkt_id + " FOR UPDATE;");

       String query=sb.toString();
        List <Pair<Integer, Integer>> products = new ArrayList<Pair<Integer, Integer>>();

        try {

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next())
            {
                Pair pair = new Pair(rs.getInt(1),rs.getInt(2));
                products.add(pair);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



       for( int i = 0 ;i<products.size();i++)
           if(products.get(i).getL() >= countOfProduct)
           {
                int prod = products.get(i).getL() - countOfProduct;
               sb.setLength(0);
               sb.append("UPDATE magazyn_produkt SET liczba_produktow = "+ prod +  " WHERE produkt_ID =" + produkt_id + " AND magazyn_ID = " +products.get(i).getR() +";");
               query=sb.toString();

               try {
                   Statement stmt=con.createStatement();
                   int rs=stmt.executeUpdate(query);

               } catch (SQLException e) {
                   e.printStackTrace();
               }


           }


    }
    private static List<Integer> checkAvailable(List<Pair<Integer,Integer>> productsFromCart , Connection con)
    {

        List<Integer> unvailableProducts = new ArrayList<>();

        for(int i =0; i<productsFromCart.size();i++)
        {
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT liczba_produktow, magazyn_ID FROM magazyn_produkt WHERE produkt_ID = " + productsFromCart.get(i).getR() + ";");
        int countOfMagazines = 0;
        int  countOfProducts =0;
        String query=sb.toString();

        try {

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next()) {
                if(rs.getInt(1) < productsFromCart.get(i).getL())
                    countOfProducts++;
                countOfMagazines++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(countOfMagazines== countOfProducts)
            unvailableProducts.add(productsFromCart.get(i).getR());

        }

            return  unvailableProducts;

    }
    private static void deleteFromCart(int produkt_id ,int koszyk_id, Connection con){
      StringBuilder  sb =  new StringBuilder();

        String  query=sb.toString();
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


        if(produkty!=null) {
            String[] items = produkty.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");


            int[] products = new int[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    products[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                }
            }


            boolean contains = false;
            for (int i = 0; i < products.length; i++)
                if (products[i] == produkt_id)
                    contains = true;

            if (contains) {
                if (products.length == 1) {

                    sb.setLength(0);
                    sb.append("update koszyk set produkt_list = '[]' where koszyk_ID =" + koszyk_id);
                    query = sb.toString();
                    try {
                        Statement stmt = con.createStatement();
                        int rs = stmt.executeUpdate(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (products.length > 1) {
                    //  int[] products = Arrays.stream(produkty.substring(1, produkty.length() - 1).split(","))
                    //  .map(String::trim).mapToInt(Integer::parseInt).toArray();


                    int index = 0;
                    for (int i = 0; i < products.length; i++) {
                        if (products[i] == produkt_id)
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
                updatePrice(koszyk_id, con);

                sb.setLength(0);
                sb.append("DELETE FROM koszyk_produkt WHERE koszyk_ID = " + koszyk_id +
                        " and produkt_id = " + produkt_id);
                query = sb.toString();
                try {
                    Statement stmt = con.createStatement();
                    int rs = stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

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
     * @return 0 - password correct, -1 - password incorrect, -2 nickname dont exists, 1 password is old
     *
     */
    public static int login(Connection con, String nickname, String password, String permission){
        String salt = null;
        String haslo = null;
        java.sql.Date baseDate=new java.sql.Date(new java.util.Date().getTime());
        if(DataSecurity.containIllegalSymbols(nickname)) return -2;
        try {
            Statement stmt=con.createStatement();
            PreparedStatement ps;
            if(permission=="emp") ps = con.prepareStatement(SELECT_LOGIN_EMP);
                else ps = con.prepareStatement(SELECT_LOGIN_USER);
            ps.setString(1, nickname);
            ResultSet rs= ps.executeQuery();
            if (rs.next() == false) {
                return -2;
            }
            haslo = rs.getString("haslo");
            salt = haslo.split(":")[1];
            if(permission=="emp") baseDate = rs.getDate("zmianahasla");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(DataSecurity.checkPasswords(password,salt, haslo)){
            if(permission=="emp"){
                Calendar c = Calendar.getInstance();
                c.setTime(new java.sql.Date(new java.util.Date().getTime()));
                c.add(Calendar.DATE, -180); //pol roku
                java.sql.Date date = new Date(c.getTimeInMillis());
                if(baseDate.before(date)){
                    return 1;
                }
            }
            return 0;
        }

        return -1;
    }


    public static int register(Connection con, String logi, String haslo, String imie, String nazwisko,
                               int numerTel,String mail, int kodPoczt, String ulica, int numerMiesz){
        if(DataSecurity.containIllegalSymbols(logi)) return -9;
        if(DataSecurity.containIllegalSymbols(haslo)) return -10;
        int exCode=-1;
        try {
            con.setAutoCommit(false);
            Statement stmt;
            PreparedStatement ps = con.prepareStatement(SELECT_LOGIN_USER);
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

                con.commit();
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();

            try{
                if(con!=null) con.rollback();

                }catch (SQLException se){
                    se.printStackTrace();
                    exCode = -3;
                }
        }finally{
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exCode;
    }


    public static int addOutpost(Connection con, int rozmiarMagazynu, int kodPoczt, String ulica, int numerBudynku, String nazwaPlacowki){
        try {
            PreparedStatement ps;
            Statement stmt=con.createStatement();
            ResultSet rs;

            ps = con.prepareStatement(INSERT_MAGAZYN);
            ps.setInt(1, rozmiarMagazynu);
            ps.setInt(2,rozmiarMagazynu);
            ps.executeUpdate();
            rs=stmt.executeQuery("SELECT magazyn_ID FROM magazyn order by magazyn_ID desc limit 1");
            rs.next();
            int magazynID= rs.getInt(1);

            ps = con.prepareStatement(INSERT_ADRES);
            ps.setInt(1,kodPoczt);
            ps.setString(2,ulica);
            ps.setInt(3, numerBudynku);
            ps.executeUpdate();
            rs=stmt.executeQuery("SELECT adres_id FROM adres order by adres_id desc limit 1");
            rs.next();
            int adresid= rs.getInt(1);

            ps = con.prepareStatement(INSERT_PLACOWKA);
            ps.setInt(1,adresid);
            ps.setInt(2,magazynID);
            ps.setString(3, nazwaPlacowki);
            ps.executeUpdate();

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int addEmployee(Connection con, String logi, String haslo, String imie, String nazwisko, int numerTel,String mail, int kodPoczt, String ulica, int numerMiesz, int placowka, String funkcja){
        try {
            Statement stmt;
            PreparedStatement ps = con.prepareStatement(SELECT_LOGIN_USER);
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

                String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                ps=con.prepareStatement(INSERT_EMPLOYEE);
                ps.setInt(1,daneosid);
                ps.setInt(2,placowka);
                ps.setString(3,logi);
                ps.setString(4,DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()));
                ps.setString(5, funkcja);
                ps.setString(6, date);
                ps.executeUpdate();

                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return -1;
    }
    public static int setPassword(Connection con, String nickname, String newPass, String permission){
        try {
            Statement stmt=con.createStatement();
            PreparedStatement ps;
            if(permission=="emp") ps = con.prepareStatement(UPDATE_PASSWORD_EMP);
            else ps = con.prepareStatement(UPDATE_PASSWORD_USER);

            ps.setString(1, DataSecurity.getHashSHA512(newPass,DataSecurity.getSalt()));
            ps.setString(2, nickname);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    public static int changePassword(Connection con, String nickname, String oldPass, String newPass, String permission){
        String salt = null;
        String haslo = null;
        if(DataSecurity.containIllegalSymbols(nickname)) return -2;
        if(DataSecurity.containIllegalSymbols(newPass)) return -3;
        try {
            Statement stmt=con.createStatement();
            PreparedStatement ps;
            if(permission=="emp") {
                 ps = con.prepareStatement(SELECT_LOGIN_EMP);
            }else  ps = con.prepareStatement(SELECT_LOGIN_USER);

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
        if(DataSecurity.checkPasswords(oldPass,salt, haslo)) {
            setPassword(con, nickname, newPass, permission);
            return 0;
        }
        return -1;
    }

}
 class Pair<L,R> {
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }

     public Pair() {

     }

     public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}