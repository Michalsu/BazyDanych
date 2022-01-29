

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


class Client extends JFrame implements ActionListener, Runnable{

    private static final long serialVersionUID = 1L;

    final static String LOGINPANEL = "Login layout";
    final static String REGISTERPANEL = "Register layout";
    final static String CLIENTPANEL = "Client panel layout";

    private static boolean permission;
    JPanel cards; //a panel that uses CardLayout


    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public void addComponentToPane(JFrame pane) {

        //layout startowy
        JPanel loginLayout = new JPanel();
        loginLayout.setLayout(null);
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


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(permission){
                    CardLayout cl = (CardLayout)(cards.getLayout());
                     cl.show(cards,CLIENTPANEL);
                      pane.setSize(new Dimension(600,500));
                }

            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,REGISTERPANEL);
                pane.setSize(new Dimension(350,500));
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




        loginLayout.add(loginLabel);
        loginLayout.add(label1);
        loginLayout.add(passwordLabel);
        loginLayout.add(loginButton);
        loginLayout.add(registerButton);
        loginLayout.add(password);
        loginLayout.add(login);
        loginLayout.add(new JButton("Register"));

        //layout rejestracji
        JPanel RegisterLayout = new JPanel();
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


        RegisterLayout.setLayout(null);

        RegisterLayout.add(registerLabel);
        RegisterLayout.add(loginField);
        RegisterLayout.add(passwordField);
        RegisterLayout.add(nameField);
        RegisterLayout.add(surnameField);
        RegisterLayout.add(phoneField);
        RegisterLayout.add(mailField);

        RegisterLayout.add(adressLabel);
        RegisterLayout.add(postCodeField);
        RegisterLayout.add(streetField);
        RegisterLayout.add(homeNumberField);

        RegisterLayout.add(register);


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
                    pane.setSize(new Dimension(355,300));
                }
            }
        });

        //layout panelu użytkownika
        JPanel panelLayout = new JPanel();

        //wektor na kategorie, wczytane z bazy
        Vector<String> vect = new Vector<>();
        for (int i = 0;i<20;i++) {
            String temp = "kategoria" + i;
             vect.add(temp);
        }

        //vector na listy przedmiotów z każdej kategorii
        //tutaj trzeba wczytać poszczególne przedmioty z bazy danych
        Vector<Pair<DefaultListModel,String>> vectOfItemsByCategory= new Vector<Pair<DefaultListModel,String>>();

        for (int i = 0;i<20;i++) {
            DefaultListModel list = new DefaultListModel();
            String desc = null;
            for (int j = 0; j < 40; j++) {
                String temp = "Przedmiot" + i;
                 desc  = "Opis" + j;
                list.addElement(temp);

            }
            vectOfItemsByCategory.addElement(new Pair<>(list,desc));
        }


        JLabel categoryLabel = new JLabel("Kategoria");
        JLabel itemsLabel = new JLabel("Przedmioty");
        //miejsce na opis przedmiotu
        JTextArea itemsDescrition = new JTextArea();
        String descrition = "opis";

        JList categoryList =  new JList(vect);
        JList itemsList = new JList();


        JScrollPane itemScroll = new JScrollPane(itemsList,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);



        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                    itemsList.setModel(vectOfItemsByCategory.get(categoryList.getSelectedIndex()).getL());

                }

        });
        itemsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            //   itemsDescrition.setText(vectOfItemsByCategory.get(itemsList.getSelectedIndex()).getR());

            }

        });
       JScrollPane categoryScroll = new JScrollPane(categoryList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        categoryLabel.setBounds(10,10,80,30);
        categoryScroll.setBounds(10,40,150,vect.size()*10);
        itemsLabel.setBounds(180,10,80,30);
        itemScroll.setBounds(180,40,200,vect.size()*10);
        itemsDescrition.setBounds(10,270,340,100);


        panelLayout.add(categoryLabel);
        panelLayout.add(itemsLabel);
        panelLayout.add(itemScroll);
        panelLayout.add(categoryScroll);
        panelLayout.add(itemsDescrition);



        panelLayout.setLayout(null);





        //dodawanie widoków do ramki
        cards = new JPanel(new CardLayout());
        cards.add(loginLayout, LOGINPANEL);
        cards.add(RegisterLayout, REGISTERPANEL);
        cards.add(panelLayout,CLIENTPANEL);
        pane.add(cards, BorderLayout.CENTER);


    }

    private void createAndShowGUI() {
        //Create and set up the window.


    }
    public static void main(String[] args) {
        String name = "klient";
        String host = "";
        String type ;


            new Client("1", host);

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
        this.permission = false;

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


        //Create and set up the content pane.
        addComponentToPane(this);
        setLocationRelativeTo(null);
        setSize(355,300);
        //Display the window.

        setVisible(true);
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        new Thread(this).start();  // Uruchomienie dodatkowego w�tka
        // do oczekiwania na komunikaty od serwera
    }


    public void actionPerformed(ActionEvent event)
    { String message;
        Object source = event.getSource();


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
                String[] actualValue = message.split(" ");


                if(actualValue[0].equals("login"))
                {
                   setPermission(true);
                }

            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            setVisible(false);
            dispose();
        }
    }


}
