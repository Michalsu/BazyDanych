import javax.swing.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class tester {
    private static final String INSERT_EMPLOYEE = "INSERT INTO pracownik(dane_osobowe_ID, placowka_id, login, haslo, funkcja) VALUES (?,?,?,?,?)";

    public static void main(String[] args) throws Exception {

        Connection con= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");


     //   new Client("c1","localhost");
     //   new Client("c2","localhost");
        //Request.addOutpost(con,10000, 54321, "Granatowa",22,"Prowizoryczna");
        //Request.addEmployee(con, "p252818", "A!2345678","Michał","Sujewicz",123456789,"ms@poczta.pl",12345,"Poziomkowa",12,1,"admin");



        //stmt.executeUpdate("INSERT INTO `dane osobowe`(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (1, 'Adam', 'Nowak' ,1243 ,'adaaa@bat.pl')");

        //int register = Request.register(con, "Adamnowak", "A!23456789", "Adam", "Nowak", 321, "abat@gail.com", 10120, "Kasztanowa", 11);
       // System.out.println(register);
    //   Request.parseRequest("ADDTOCART#Login5#1#5", con);
    // Request.parseRequest("ADDTOCART#Adamnowak#2#2", con);
    //   Request.parseRequest("ADDTOCART#Adamnowak#1#2", con);
    //    Request.parseRequest("ADDTOCART#Adam#2#2", con);
    //  Request.parseRequest("ADDTOCART#Adam#3#2", con);
       // Request.parseRequest("ADDPRODUCTTOMAGAZINE#2#1#1", con);
        //    Request.parseRequest("ADDTOCART#adam#2#3", con);
       // Request.parseRequest("ADDTOCART#Adam#1#5", con);
  //Request.parseRequest("ADDPRODUCTTOMAGAZINE#2#3#2", con);
        // Request.parseRequest("CHANGEPRODUCT#1#1000#1#dasda#dsadas#1", con);
      //       Request.parseRequest("DELETEFROMCART#Adamnowak#2", con);
  //  Request.parseRequest("BUY#Login5#gotowe#karta#dodomu", con);
    //  Request.parseRequest("LOGOUT", con);


/*
        String salt = DataSecurity.getSalt();
        String hashedPass = DataSecurity.getHashSHA512("Password", salt);

        System.out.println(" salt -> " + salt);
        System.out.println(" hash -> " + hashedPass);


        System.out.println(Request.login(con,"Adamnowak","A!23456789"));
        System.out.println(Request.login(con,"Adamnowak","A!23456788"));
        System.out.println(Request.login(con,"Adaadfa11mNdswa","A!23456789"));
        System.out.println(Request.login(con,"Adamnowk","A!23456789"));



        System.out.println(DataSecurity.checkPasswords("password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("blababla",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));


         */





    }



}

