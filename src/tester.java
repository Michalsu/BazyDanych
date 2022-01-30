import javax.swing.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class tester {
    private static final String INSERT_EMPLOYEE = "INSERT INTO pracownik(dane_osobowe_ID, placowka_id, login, haslo, funkcja) VALUES (?,?,?,?,?)";

    public static void main(String[] args) throws Exception {

        Connection con= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");



     //   Client c1 = new Client("name1","");
      //  Client c2 = new Client("name2","");

     //   System.out.println(Request.parseRequest("SEARCH#12",con));
    //    System.out.println(Request.parseRequest("SEARCH#123'; Drop table klient -- ",con));
     //   System.out.println(Request.parseRequest("LOGINEMP#LoginPracownik31#A!2345678",con));
     //   System.out.println(Request.parseRequest("LOGINUSER#Login1#haslo!1",con));
      //  Request.parseRequest("REGISTER#Adam#Haslo!123#Adam#Adam#693#abcdef234@mail.com#12554#Grzybowa#7",con);
      //  System.out.println(Request.parseRequest("LOGINUSER#NowyKlient#Haslo!122",con));
      //  System.out.println(Request.parseRequest("LOGINUSER#NowyKlient#Haslo!123",con));
     //   System.out.println(Request.parseRequest("CHANGEPASSUSER#NowyKlient#Haslo!123#Haslo!122",con));
     //   System.out.println(Request.parseRequest("LOGINUSER#NowyKlient#Haslo!122",con));






























        //   Request.parseRequest(req, con);
        // new Client("c1","localhost");
        //   new Client("c2","localhost");
        //Request.addOutpost(con,10000, 54321, "Granatowa",22,"Prowizoryczna");
        // Request.addEmployee(con, "LoginPracownik32", "A!2345678","Imie32","Nazwisko32",123456781,"nazoimie@poczta.pl",12345,"Poziowa",123,1,"sprzedawca");
        //Request.setPassword(con, "Login1", "haslo!1","user");


        //stmt.executeUpdate("INSERT INTO `dane osobowe`(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (1, 'Adam', 'Nowak' ,1243 ,'adaaa@bat.pl')");

        //int register = Request.register(con, "Adamnowak", "A!23456789", "Adam", "Nowak", 321, "abat@gail.com", 10120, "Kasztanowa", 11);
       // System.out.println(register);
 //  Request.parseRequest("ADDTOCART#Login1#1#50", con);
       // Request.parseRequest("ADDTOCART#Login1#16#5", con);
        //Request.parseRequest("ADDTOCART#Login1#4#2", con);
    // Request.parseRequest("ADDTOCART#Adamnowak#2#2", con);
    //   Request.parseRequest("ADDTOCART#Adamnowak#1#2", con);
    //    Request.parseRequest("ADDTOCART#Adam#2#2", con);
     // Request.parseRequest("ADDTOCART#Login1#3#2", con);
       // Request.parseRequest("ADDPRODUCTTOMAGAZINE#2#1#1", con);
           // Request.parseRequest("DELETEPRODUCT#11#", con);
       // Request.parseRequest("ADDTOCART#Adam#1#5", con);
  //Request.parseRequest("ADDPRODUCTTOMAGAZINE#2#3#2", con);
       // Request.parseRequest("CHANGEPRODUCT#4#1#0#dasda#dsadas#1", con);
      //  Request.parseRequest("CHANGEPRODUCT#11#1#0#dasda#dsadas#1", con);
      //  Request.parseRequest("CHANGEPRODUCT#15#1#0#dasda#dsadas#1", con);
//Request.parseRequest("DELETEFROMCART#Login1#16", con);
      //  Request.parseRequest("DELETEFROMCART#Login1#4", con);
   // Request.parseRequest("BUY#Login1#gotowe#karta#dodomu", con);
      //Request.parseRequest("DELETEPRODUCT#2", con);


/*


for(int i = 1;i<500;i++){
    StringBuilder sb = new StringBuilder();
    String haslo = "haslo!" + i;
            sb.append("Update klient set haslo = '" + DataSecurity.getHashSHA512(haslo,DataSecurity.getSalt()) +
                    "' where klient_ID = " + i);

            String q = sb.toString();


    try {
        Statement stmt = con.createStatement();
        int rs = stmt.executeUpdate(q);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}*/



    }



}

