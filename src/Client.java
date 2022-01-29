

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;


class Client extends JFrame implements ActionListener, Runnable{

    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    final static String LOGINPANEL = "Card with JButtons";
    final static String REGISTERPANEL = "Card with JTextField";
    JPanel cards; //a panel that uses CardLayout

    public Client() {

    }

    public void addComponentToPane(Container pane) {

        //layout startowy
        JPanel card1 = new JPanel();
        card1.setLayout(null);
        JLabel label1 = new JLabel("Logowanie", SwingConstants.CENTER);
        JLabel loginLabel = new JLabel("Login");
        JLabel passwordLabel = new JLabel("Hasło");

        label1.setFont(new Font("Serif", Font.BOLD, 14));
        JButton loginButton = new JButton("Zaloguj się");
        JButton registerButton = new JButton("Rejestracja");
        JPasswordField password = new JPasswordField();
        JTextField login = new JTextField();


        label1.setBounds(90,30,200,30);
        login.setBounds(90,65,200,30);
        password.setBounds(90,100,200,30);
        loginButton.setBounds(90,140,200,30);
        registerButton.setBounds(90,175,200,30);
        loginLabel.setBounds(50,65, 40,30);
        passwordLabel.setBounds(50,100,40,30);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,REGISTERPANEL);
                frame.setSize(new Dimension(350,500));
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(login.getText().isEmpty() || password.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Nieprawidłowe dane");
                }
            }
        });




        card1.add(loginLabel);
        card1.add(label1);
        card1.add(passwordLabel);
        card1.add(loginButton);
        card1.add(registerButton);
        card1.add(password);
        card1.add(login);
        card1.add(new JButton("Register"));

        //layout rejestracji
        JPanel card2 = new JPanel();
        JLabel registerLabel = new JLabel("Rejestracja",SwingConstants.CENTER);
        registerLabel.setFont(new Font("Serif", Font.BOLD, 14));
        JLabel adressLabel = new JLabel("Adres",SwingConstants.CENTER);
        adressLabel.setFont(new Font("Serif", Font.BOLD, 12));

        JTextField loginField = new JTextField("Login");
        JTextField passwordField = new JTextField("Hasło");
        JTextField nameField = new JTextField("Imię");
        JTextField surnameField = new JTextField("Nazwisko");
        JTextField phoneField = new JTextField("Telefon");
        JTextField mailField = new JTextField("E-mail");
        JTextField postCodeField = new JTextField("Kod pocztowy");
        JTextField streetField = new JTextField("Ulica");
        JTextField homeNumberField = new JTextField("Numer ulicy");

        JButton register = new JButton("Zarejestruj się");

        registerLabel.setBounds(65,10 ,200,30);

        loginField.setBounds(65,50,200,30);
        passwordField.setBounds(65,85,200,30);
        nameField.setBounds(65,120,200,30);
        surnameField.setBounds(65,155,200,30);
        phoneField.setBounds(65,190,200,30);
        mailField.setBounds(65,225,200,30);

        adressLabel.setBounds(65,260 ,200,30);

        postCodeField.setBounds(65,295,200,30);
        streetField.setBounds(65,330,200,30);
        homeNumberField.setBounds(65,365,200,30);

        register.setBounds(65,400,200,30);


        card2.setLayout(null);

        card2.add(registerLabel);
        card2.add(loginField);
        card2.add(passwordField);
        card2.add(nameField);
        card2.add(surnameField);
        card2.add(phoneField);
        card2.add(mailField);

        card2.add(adressLabel);
        card2.add(postCodeField);
        card2.add(streetField);
        card2.add(homeNumberField);

        card2.add(register);


        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(loginField.getText().isEmpty() || passwordField.getText().isEmpty()
                        || nameField.getText().isEmpty() || surnameField.getText().isEmpty() ||
                        phoneField.getText().isEmpty() || mailField.getText().isEmpty() ||
                        postCodeField.getText().isEmpty() ||  streetField.getText().isEmpty() ||
                        homeNumberField.getText().isEmpty()

                )
                {
                    JOptionPane.showMessageDialog(null,"Nieprawidłowe dane");
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Zarejestrowano");
                    CardLayout cl = (CardLayout)(cards.getLayout());
                    cl.show(cards,LOGINPANEL);
                    frame.setSize(new Dimension(355,300));
                }
            }
        });


        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, LOGINPANEL);
        cards.add(card2, REGISTERPANEL);
        pane.add(cards, BorderLayout.CENTER);


    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Klient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //   frame.setPreferredSize(new Dimension(400, 300));
        //Create and set up the content pane.

        Client demo = new Client();
        demo.addComponentToPane(frame.getContentPane());
        frame.setLocationRelativeTo(null);
        frame.setSize(355,300);
        //Display the window.
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        String name = "klient";
        String host = "";
        String type = "kilent";

        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        if (name != null && !name.equals("")) {
            new Client(name, host);

        }
    }



    private JTextField messageField = new JTextField(20);
    private JTextArea  textArea     = new JTextArea(15,18);

    static final int SERVER_PORT = 25000;
    private String name;
    private String serverHost;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    Client(String name, String host) {
        super(name);
        this.name = name;
        this.serverHost = host;


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            @Override
            public void windowClosed(WindowEvent event) {
                windowClosing(event);
            }
        });



        new Thread(this).start();  // Uruchomienie dodatkowego w�tka
        // do oczekiwania na komunikaty od serwera
    }

    synchronized public void printReceivedMessage(String message){
        String tmp_text = textArea.getText();
        textArea.setText(tmp_text + ">>> " + message + "\n");
    }

    synchronized public void printSentMessage(String message){
        String text = textArea.getText();
        textArea.setText(text + "<<< " + message + "\n");
    }

    public void actionPerformed(ActionEvent event)
    { String message;
        Object source = event.getSource();
        if (source==messageField) {
            try {
                message = messageField.getText();
                String[] actualValue = message.split(" ");
                outputStream.writeObject(message);
                printSentMessage(message);


            } catch (IOException e) {
                printReceivedMessage("ERROR " + e);
            }catch (IndexOutOfBoundsException exception)
            {
                printReceivedMessage("ERROR nieprawidłowe dane ");
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        repaint();
    }

    public void run(){
        if (serverHost.equals("")) {
            // po��cz z lokalnym komputerem
            serverHost = "localhost";
        }
        try{
            socket = new Socket(serverHost, SERVER_PORT);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(name);
        } catch(IOException e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
            setVisible(false);
            dispose();  // zwolnienie zasob�w graficznych
            // okno graficzne nie zostanie utworzone
            return;
        }
        try{
            while(true){
                String message = (String)inputStream.readObject();
                printReceivedMessage(message);

            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            setVisible(false);
            dispose();
        }
    }


}
