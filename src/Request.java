import java.sql.*;

public class Request {

    private static final String INSERT_DANE_OSOBOWE = "INSERT INTO dane_osobowe(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (?,?,?,?,?)";
    private static final String INSERT_KLIENT = "INSERT INTO klient(koszyk_ID, `dane_osobowe_ID`, login, haslo) VALUES (?,?,?,?)";
    private static final String INSERT_ADRES = "INSERT INTO adres(kod_pocztowy, ulica, numer) VALUES (?,?,?)";

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
                sb.append("SELECT * FROM products WHERE name LIKE '%");
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

                break;
            case "DELETEFROMCART":

                break;
            case "BUY":

                break;
            case "LOGOUT":

                break;
            case "COMPARE":

                break;
            case "REGISTER":

                break;
            case "ADDPRODUCT":

                break;
            case "CHANGEPRODUCT":

                break;
        }
    }

    private static int addToCart(int productID, int amount){

        return -1;
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
     * @return 0 - password correct, -1 - password incorrect, -2 nickname contain illegal symbols
     *
     */
    public static int login(Connection con, String nickname, String password){
        String hash = null;
        String salt = null;
        if(DataSecurity.containIllegalSymbols(nickname)) return -2;
        try {
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT haslo FROM klient WHERE login ='"+ nickname+"';");
            hash = rs.getString("haslo").split(":")[0];
            System.out.println(hash);
            salt = rs.getString("haslo").split(":")[1];
            System.out.println(salt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(DataSecurity.checkPasswords(password,salt, hash)) return 0;
        return -1;
    }


    public static int register(Connection con, String logi, String haslo, String imie, String nazwisko, int numerTel,String mail, int kodPoczt, String ulica, int numeerMiesz){
        if(DataSecurity.containIllegalSymbols(logi)) return -3;
        if(DataSecurity.containIllegalSymbols(haslo)) return -4;

        try {
            final String SELECT_LOGIN = "SELECT login FROM klient WHERE login = ?";
            Statement stmt=con.createStatement();
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
                ps.setInt(3,numeerMiesz);
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
                stmt.executeUpdate("INSERT INTO koszyk(wartosc_koszyka) VALUES (null);");
                rs=stmt.executeQuery("SELECT koszyk_ID FROM koszyk order by koszyk_ID desc limit 1");
                rs.next();
                int koszykid= rs.getInt(1);
                ps.setInt(1,koszykid);
                ps.setInt(2,daneosid);
                ps.setString(3,logi);
                ps.setString(4,DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()));

                System.out.println(DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()).length());
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
