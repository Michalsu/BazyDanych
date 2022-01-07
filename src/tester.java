import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class tester {

    public static void main(String[] args) throws Exception {

        Connection con= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");

        Request.register(con,"AdamNow","A!23456789","Adam","Nowak",997,"adam@gmail.com",50120,"Zielna",13);


        String salt = DataSecurity.getSalt();
        String hashedPass = DataSecurity.getHashSHA512("Password", salt);

        System.out.println(" salt -> " + salt);
        System.out.println(" hash -> " + hashedPass);



        System.out.println(DataSecurity.checkPasswords("password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("blababla",salt.toString(),hashedPass));
        System.out.println(DataSecurity.checkPasswords("Password",salt.toString(),hashedPass));












    }


}

