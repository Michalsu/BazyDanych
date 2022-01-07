import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        JavaSqlCommunication j = new JavaSqlCommunication(DATABASE_URL, USERNAME, PASSWORD, "baza");


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
                else if (actualValue[0].equals("sent")&& actualValue.length==1)
                {

                    String index = String.valueOf(j.selectLast("adres_id",  "adres")+1);

                    String c = "adres_id,kod_pocztowy,ulica,numer";
                    String a = index+ "', '321' ,'ds', '23";
                    j.insertText(a,c,"adres");

                }
            }

        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

}

