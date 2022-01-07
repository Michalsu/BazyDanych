import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class tester {

    public static void main(String[] args) throws Exception {

        Connection con= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");



        //stmt.executeUpdate("INSERT INTO `dane osobowe`(adres_id, Imie,Nazwisko,nr_telefonu,mail) VALUES (1, 'Adam', 'Nowak' ,1243 ,'adaaa@bat.pl')");

        //int register = Request.register(con, "Adamnowak", "A!23456789", "Adam", "Nowak", 321, "abat@gail.com", 10120, "Kasztanowa", 11);
       // System.out.println(register);

        String salt = DataSecurity.getSalt();
        String hashedPass = DataSecurity.getHashSHA512("Password", salt);

        System.out.println(" salt -> " + salt);
        System.out.println(" hash -> " + hashedPass);


        System.out.println(Request.login(con,"Adamnowak","A!23456789"));
        System.out.println(Request.login(con,"Adamnowak","A!23456788"));
        System.out.println(Request.login(con,"Adaadfa11mNdswa","A!23456789"));
        System.out.println(Request.login(con,"Adamnowk","A!23456789"));

        /*
        System.out.println(DataSecurity.checkPasswords("password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("blababla",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));


         */

    }


}

