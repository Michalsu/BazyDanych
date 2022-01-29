import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class tester  extends  JFrame  {
    private static final String INSERT_EMPLOYEE = "INSERT INTO pracownik(dane_osobowe_ID, placowka_id, login, haslo, funkcja) VALUES (?,?,?,?,?)";
    private static JFrame frame;

    // public static void main(String[] args) throws Exception {

       // Connection con= DriverManager.getConnection(
       //         "jdbc:mysql://127.0.0.1:3306/baza","root","root");



//Method came from the ItemListener class implementation,
//contains functionality to process the combo box item selecting


        //   Request.parseRequest(req, con);
      // new Client("c1","localhost");
        //new Client("c2","localhost");



        //dodanie do koszyka
      // Request.parseRequest("ADDTOCART#Login1#1#5", con);
       // Request.parseRequest("ADDTOCART#Login1#16#5", con);
        //usuniecie z koszyka
       // Request.parseRequest("DELETEFROMCART#Login1#1", con);

        //zmiana produktu
        // Request.parseRequest("CHANGEPRODUCT#16#1#0#nazwa#opis#1", con);
        //usuniecie produktu
        // Request.parseRequest("DELETEPRODUCT#11#", con);

        //aktulizcja magazynu
       // Request.parseRequest("ADDPRODUCTTOMAGAZINE#2#1#1", con);


        //kupno
      //  Request.parseRequest("ADDTOCART#Login3#6#23", con);
      //  Request.parseRequest("ADDTOCART#Login2#6#5", con);




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



   // }



}

