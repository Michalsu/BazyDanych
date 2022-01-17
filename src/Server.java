import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.stream.IntStream;

import javax.swing.*;

public class Server  extends JFrame implements  Runnable{
    private static final long serialVersionUID = 1L;

    static final int SERVER_PORT = 25000;
    static Connection con=null;

    public static void main(String[] args) throws Exception {

        new Server();
        con=DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/baza","root","root");



    }


    private JLabel clientLabel   = new JLabel("Odbiorca:");
    private JLabel textAreaLabel = new JLabel("Dialog:");
    private JComboBox<ClientThread> clientMenu = new JComboBox<ClientThread>();
    private JTextArea  textArea  = new JTextArea(15,18);
    private JScrollPane scroll = new JScrollPane(textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    Server() throws Exception {
        super("SERWER");
        setSize(300,340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.add(clientLabel);
        clientMenu.setPrototypeDisplayValue(new ClientThread("#########################"));
        panel.add(clientMenu);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(textAreaLabel);
        textArea.setEditable(false);
        panel.add(scroll);
        setContentPane(panel);
        setVisible(true);
        new Thread(this).start();// Uruchomienie dodatkowego watka
        // czekajacego na nowych klientow
        new otherThreads(24);    // odpalenie wątków do robienia kopii zapasowej oraz usuwania nieaktywnych klientow


    }

    synchronized public void printReceivedMessage(ClientThread client, String message){
        String text = textArea.getText();
        textArea.setText(client.getName() + " >>> " + message + "\n" + text);
    }

    synchronized public void printSentMessage(ClientThread client, String message){
        String text = textArea.getText();
        textArea.setText(client.getName() + " <<< " + message + "\n" + text);
    }

    synchronized void addClient(ClientThread client){
        clientMenu.addItem(client);
    }

    synchronized void removeClient(ClientThread client){
        clientMenu.removeItem(client);
    }

    public void run() {
        boolean socket_created = false;

        // inicjalizacja po��cze� sieciowych
        try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Serwer zosta� uruchomiony na hoscie " + host);
            socket_created = true;
            // koniec inicjalizacji po��cze� sieciowych

            while (true) {  // oczekiwanie na po��czenia przychdz�ce od klient�w
                Socket socket = serwer.accept();
                if (socket != null) {
                    // Tworzy nowy w�tek do obs�ugi klienta, kt�re
                    // w�a�nie po��czy� si� z serwerem.
                    new ClientThread(this, socket);

                }
            }
        } catch (IOException e) {
            System.out.println(e);
            if (!socket_created) {
                JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może byc utworzone");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }

        try{
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/baza","root","root");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from produkt");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+
                        rs.getString(3)+"  "+rs.getString(4)+"  "+rs.getString(5));
            con.close();
        }catch(Exception e){ System.out.println(e);}


    }



}

class ClientThread implements Runnable {
    private Socket socket;
    private String name;
    private Server myServer;


    private ObjectOutputStream outputStream = null;

    // UWAGA: Ten konstruktor tworzy nieaktywny obiekt ClientThread,
    // kt�ry posiada tylko nazw� prototypow�, potrzebn� dla
    // metody setPrototypeDisplayValue z klasy JComboBox
    ClientThread(String prototypeDisplayValue){
        name = prototypeDisplayValue;
    }

    ClientThread(Server server, Socket socket) {
        myServer = server;
        this.socket = socket;
        new Thread(this).start();  // Utworzenie dodatkowego watka
        // do obslugi komunikacji sieciowej
    }

    public String getName(){ return name; }

    public String toString(){ return name; }

    public void sendMessage(String message){
        try {
            outputStream.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        String message;
        final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/baza";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        final String MAX_POOL = "250";
      //  JavaSqlCommunication j = new JavaSqlCommunication(DATABASE_URL, USERNAME, PASSWORD, "baza");

        Connection con= null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baza","root","root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

      /*  try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

       */
        System.out.println("test1");
        String produkt= "mle";
        try {

            System.out.println("test2");
            Statement stmt= Server.con.createStatement();
            System.out.println("test3");
            //ResultSet rs=stmt.executeQuery("SELECT produkt_ID, Cena FROM produkt WHERE nazwa LIKE '%"+ produkt+"%';");
            ResultSet rs = stmt.executeQuery("SELECT * FROM produkt;");
            System.out.println("test4");
            System.out.println(rs.getInt(1)+"  "+rs.getInt(2)+
                    "  "+rs.getInt(3)+"  "+rs.getString(4)+"  "+rs.getString(5)+"  "+rs.getInt(6));
            System.out.println("test5");
        } catch (Exception e) {
            e.printStackTrace();
        }




        try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); )
        {
            outputStream = output;
            name = (String)input.readObject();
            myServer.addClient(this);
            while(true){
                message = (String)input.readObject();
                String[] actualValue = message.split(" ");
                myServer.printReceivedMessage(this,message);
                if (actualValue[0].equals("BYE")&& actualValue.length==1){
                    myServer.removeClient(this);
                    input.close();
                    output.close();
                    socket.close();
                    socket=null;
                }
                else if (actualValue[0].equals("CLOSE")&& actualValue.length==1)
                {
                    myServer.removeClient(this);
                    System.exit(0);
                    socket.close();
                    socket=null;
                }
                else if (actualValue[0].equals("buy1")&& actualValue.length==1)
                {
                     Request.parseRequest("BUY#Adamnowak#gotowe#karta#dodomu", con);

                }
                else if (actualValue[0].equals("buy")&& actualValue.length==1)
                {
                    Request.parseRequest("BUY#Adam#gotowe#karta#dodomu", con);

                }
            }

        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

}
class otherThreads {
    Timer timer;
int i =0;
    public otherThreads(int hours) {
        timer = new Timer();
        timer.schedule(new MakeBackup(), 60*60*hours*1000,60*60*hours*1000);    //urochomienie wątku co 24 godziny
        timer.schedule(new changeOrders() , 365*60*60*hours*1000,365* 60*60*hours*1000 ); //usuwanie danych nieatywnych klientow co rok

    }

    //
    public void checkOrders() throws SQLException { Connection con= DriverManager.getConnection(
            "jdbc:mysql://127.0.0.1:3306/baza","root","root");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        String[] d = date.split("-");
       int year = Integer.parseInt(d[0]) - 1 ;
       String newDate = Integer.parseInt(String.valueOf(year)) + "-" +d[1] +"-" + d[2];

StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        StringBuilder sb6 = new StringBuilder();
        String q2,q3,q4,q5,q6;
        sb.append("SELECT klient_ID FROM zamowienie WHERE Data = '" + newDate +"';");

        String query  =sb.toString();
        List<Integer> clientList = new ArrayList<>();
        try {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
                clientList.add(rs.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();

        }

        for(int i =0 ;i< clientList.size();i++)
        {
            boolean delete = true;
            sb = new StringBuilder();
            sb.append("SELECT zamowienie_ID FROM zamowienie WHERE Data > '" + newDate +"' and klient_ID = " + clientList.get(i));
            query  =sb.toString();
            try {

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next())
                    delete = false;

            } catch (SQLException e) {
                e.printStackTrace();

            }
            if(delete)
            {
                int personal_date = 0;
                sb = new StringBuilder();
                sb.append("SELECT dane_osobowe_ID FROM klient WHERE klient_ID = " + clientList.get(i));
                query  =sb.toString();
                try {

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                     personal_date=rs.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();

                }
                int cart = 0;
                sb = new StringBuilder();
                sb.append("SELECT koszyk_ID FROM klient WHERE klient_ID = " + clientList.get(i));
                query  =sb.toString();
                try {

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                    cart=rs.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();

                }
                int address_id = 0;
                sb = new StringBuilder();
                sb.append("SELECT adres_id FROM dane_osobowe WHERE dane_osobowe_ID = " + personal_date);
                query  =sb.toString();
                try {

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    rs.next();
                    address_id=rs.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();

                }

                sb = new StringBuilder();
                sb.append("DELETE FROM zamowienie where klient_ID =  " + clientList.get(i));
                sb2.append("DELETE FROM klient where klient_ID = " + clientList.get(i));
                sb3.append("DELETE FROM koszyk_produkt where koszyk_ID = " + cart);
                sb4.append("DELETE FROM koszyk where koszyk_ID = " + cart);
                sb5.append("DELETE FROM dane_osobowe where dane_osobowe_ID = " +personal_date);
                sb6.append("DELETE FROM adres where adres_ID = " + address_id);
                query  =sb.toString();
                q2  =sb2.toString();
                q3  =sb3.toString();
                q4  =sb4.toString();
                q5  =sb5.toString();
                q6  =sb6.toString();

                try {

                    Statement stmt = con.createStatement();
                    int rs = stmt.executeUpdate(query);
                     rs = stmt.executeUpdate(q2);
                     rs = stmt.executeUpdate(q3);
                     rs = stmt.executeUpdate(q4);
                     rs = stmt.executeUpdate(q5);
                     rs = stmt.executeUpdate(q6);


                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }


        }


    }
    public void Backupdbtosql() {
        try {

            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = Request.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();


            /*NOTE: Creating Database Constraints*/
            String dbName = "baza";
            String dbUser = "root";
            String dbPass = "root";

            /*NOTE: Creating Path Constraints for folder saving*/
            /*NOTE: Here the backup folder is created for saving inside it*/
            String folderPath = jarDir + "\\backup";

            /*NOTE: Creating Folder if it does not exist*/
            File f1 = new File(folderPath);
            f1.mkdir();


            /*NOTE: Creating Path Constraints for backup saving*/
            /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
            String savePath = "\"" + jarDir + "\\backup\\" + "backup.sql\"";

            /*NOTE: Used to create a cmd command*/
            String executeCmd = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u" + dbUser + " -p" + dbPass + " --databases " + dbName + " -r " + savePath;


            /*NOTE: Executing the command here*/

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();


            if (processComplete == 0) {
                System.out.println("Backup Complete");
            } else {
                System.out.println("Backup Failure");
            }


        } catch (URISyntaxException | IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(null, "Error at Backuprestore" + ex.getMessage());
        }
    }
    class MakeBackup extends TimerTask {
        public void run() {
            Backupdbtosql();

        }
    }

        class changeOrders extends TimerTask {
            public void run() {
                try {
                    checkOrders();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }


}
