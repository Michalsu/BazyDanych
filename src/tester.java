import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class tester {

    public static void main(String[] args) throws Exception {

        Connection con= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");



        //Request.addOutpost(con,10000, 54321, "Granatowa",22,"Prowizoryczna");
        //Request.addEmployee(con, "p252818", "A!2345678","Michał","Sujewicz",123456789,"ms@poczta.pl",12345,"Poziomkowa",12,1,"admin");



        //stmt.executeUpdate("INSERT INTO `dane osobowe`(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (1, 'Adam', 'Nowak' ,1243 ,'adaaa@bat.pl')");

        //int register = Request.register(con, "Adamnowak", "A!23456789", "Adam", "Nowak", 321, "abat@gail.com", 10120, "Kasztanowa", 11);
       // System.out.println(register);
        Request.parseRequest("ADDTOCART#Adamnowak#1#5", con);
        Request.parseRequest("ADDTOCART#Adamnowak#2#5", con);

        //    Request.parseRequest("ADDTOCART#adam#2#3", con);
       // Request.parseRequest("ADDTOCART#Adam#1#5", con);

        // Request.parseRequest("CHANGEPRODUCT#1#1000#1#dasda#dsadas#1", con);
      //       Request.parseRequest("DELETEFROMCART#Adamnowak#2", con);
      Request.parseRequest("BUY#Adamnowak#gotowe#karta#dodomu", con);
       // Request.parseRequest("BUY#Adam#gotowe#karta#dodomu", con);

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

