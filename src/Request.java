import java.sql.*;

public class Request {

    private static final String INSERT_DANE_OSOBOWE = "INSERT INTO 'dane osobowe'(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (?,?,?,?,?)";
    private static final String INSERT_KLIENT = "INSERT INTO klient(koszyk_ID, `dane_osobowe_ID`, login, haslo) VALUES (?,?,?,?)";

    public static void parseRequest(String request, Connection con){
        if(request == "")return;
        String[] substrings = request.split("#");
        int parimeters=substrings.length;
        switch(substrings[0]){
            case "LOGIN":
                DataSecurity.login(con, substrings[1],substrings[2]);
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

    public static int register(Connection con, String logi, String haslo, String imie, String nazwisko, int numerTel,String mail, int kodPoczt, String ulica, int numeerMiesz){
        if(DataSecurity.containIllegalSymbols(logi)) return -3;
        if(DataSecurity.containIllegalSymbols(haslo)) return -4;

        try {
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT login FROM klient;");
            if (rs.next() == true) {
                return -2;
            } else {
                stmt=con.createStatement();

                System.out.println("test1");

                stmt.executeUpdate("INSERT INTO adres(kod_pocztowy, ulica, numer) VALUES (50120,'Szkolna',12);");

                System.out.println("test2");
                rs = stmt.executeQuery("SELECT auto_increment FROM information_schema.tables WHERE table_schema = 'baza' AND table_name = 'adres';");
                //rs=stmt.executeQuery("SELECT LAST_INSERT_ID()");
                System.out.println("test2,5");
                int adresid= rs.getInt(1);
                System.out.println("test3");
                PreparedStatement ps = con.prepareStatement(INSERT_DANE_OSOBOWE);
                ps.setInt(1,adresid);
                ps.setString(2,imie);
                ps.setString(3,nazwisko);
                ps.setInt(4,numerTel);
                ps.setString(5,mail);
                ps.executeUpdate();
                System.out.println("test4");
                //stmt.executeUpdate("INSERT INTO 'dane osobowe'(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES ()");
                stmt.executeQuery("SELECT LAST_INSERT_ID()");
                System.out.println("test5");
                int daneosid= rs.getInt(1);
                System.out.println("test6");
                ps=con.prepareStatement(INSERT_KLIENT);
                stmt.executeUpdate("INSERT INTO koszyk('wartosc_koszyka') VALUES (null);");
                stmt.executeQuery("SELECT LAST_INSERT_ID()");
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


}
