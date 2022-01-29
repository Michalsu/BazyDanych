

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
    final static String CARTPANEL = "Cart layout";

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
                System.out.println(permission);
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
                if(!(permission || login.getText().isEmpty() || password.getText().isEmpty())){
                    //if(login.getText().isEmpty() || password.getText().isEmpty())
                    boolean sendrequest=true;
                    if(DataSecurity.containIllegalSymbols(login.getText())){
                        JOptionPane.showMessageDialog(null, "login zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #");
                        sendrequest=false;
                    }
                    if(DataSecurity.containIllegalSymbols(password.getText())){
                        JOptionPane.showMessageDialog(null, "hasło zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #");
                        sendrequest=false;
                    }
                    if(!DataSecurity.passwordValid(password.getText())) {
                        JOptionPane.showMessageDialog(null, "hasło nie spełnia minimalnych wymagan dlugość >=8, litery, cyfry, znaki specjalne");
                        sendrequest=false;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("LOGINUSER#");
                    sb.append(login.getText()+ "#");
                    sb.append(password.getText());
                    sendMessage(sb.toString());

                    //System.out.println(permission);
                    //if(Client.permission == true)
                    //{
                    //    JOptionPane.showMessageDialog(null,"Nieprawidłowe dane");
                    //}
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
        //tutaj trzeba wczytać poszczególne przedmioty z bazy danych i ich opisy
        Vector<Pair<DefaultListModel,DefaultListModel>> vectOfItemsByCategory= new Vector<Pair<DefaultListModel,DefaultListModel>>();

        for (int i = 0;i<20;i++) {
            DefaultListModel listOfItems = new DefaultListModel();
            DefaultListModel listOfItemsDesc = new DefaultListModel();

            String desc  = null;
            for (int j = 0; j < 40; j++) {
                String temp = "Przedmiot" + i;
                 desc  = "Opis" + i;
                listOfItems.addElement(temp);
                listOfItemsDesc.addElement(desc);

            }
            vectOfItemsByCategory.addElement(new Pair<>(listOfItems,listOfItemsDesc));

        }


        JLabel categoryLabel = new JLabel("Kategoria");
        JLabel itemsLabel = new JLabel("Przedmioty");
        //miejsce na opis przedmiotu
        JTextArea itemsDescrition = new JTextArea();


        JButton addToCartButton = new JButton("Dodaj do koszyka");
        JLabel countLabel = new JLabel("Ilość");
        JTextField countField = new JTextField();

        //Panel użykownika

        JButton viewCartButton = new JButton("Wyświetl koszyk");
        JButton viewOrdersButton = new JButton("Wyświetl zamówienia");
        JButton dataButton = new JButton("Wyświetl dane");
        JButton logoutButton = new JButton("Wyloguj");



        JList categoryList =  new JList(vect);
        JList itemsList = new JList();


        JScrollPane itemScroll = new JScrollPane(itemsList,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //aktualiacja przedmiotów dla wybranej listy
        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                itemsList.setModel(vectOfItemsByCategory.get(categoryList.getSelectedIndex()).getL());
                itemsList.setSelectedIndex(0);
                }

        });

      //aktualziacja opisu dla wybranego przedmiotu
        itemsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                    if(itemsList.getSelectedIndex()!=-1)
                    itemsDescrition.setText((String) vectOfItemsByCategory.get(categoryList.getSelectedIndex()).getR().get(itemsList.getSelectedIndex()));

            }

        });
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CARTPANEL);

            }
        });


       JScrollPane categoryScroll = new JScrollPane(categoryList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        categoryLabel.setBounds(10,10,80,30);
        categoryScroll.setBounds(10,40,150,vect.size()*10);
        itemsLabel.setBounds(180,10,80,30);
        itemScroll.setBounds(180,40,200,vect.size()*10);
        itemsDescrition.setBounds(10,270,350,100);
        countLabel.setBounds(10,380,40,30);
        countField.setBounds(45,380,50,30);
        addToCartButton.setBounds(100,380, 200,30);
        viewCartButton.setBounds(400,40, 130,30);
        viewOrdersButton.setBounds(400,75, 130,30);
        dataButton.setBounds(400,110, 130,30);
        logoutButton.setBounds(400,145, 130,30);

        panelLayout.add(categoryLabel);
        panelLayout.add(itemsLabel);
        panelLayout.add(itemScroll);
        panelLayout.add(categoryScroll);
        panelLayout.add(itemsDescrition);
        panelLayout.add(countLabel);
        panelLayout.add(countField);
        panelLayout.add(addToCartButton);
        panelLayout.add(viewCartButton);
        panelLayout.add(viewOrdersButton);
        panelLayout.add(dataButton);
        panelLayout.add(logoutButton);

        panelLayout.setLayout(null);

//Layout koszyka
        JPanel cartLayout = new JPanel();
        cartLayout.setLayout(null);

//tutaj trzeba ściągnąć rzeczy w koszyku z bazy
        Vector<Vector<String>> data = new Vector<>();
       // data.addElement(new Vector<>("przedmiot1","1"));
       // listOfItemsFromBase.addElement(new Pair<>("przedmiot2",2));

        JButton backButton = new JButton("Powrót do sklepu");
        JButton orderButton = new JButton("Zamów");
        JLabel priceOfCart = new JLabel();

        String[] columnNames = { "Przedmiot","Ilość" };

      //  JTable itemsInCartList = new JTable(, columnNames);



   //     JScrollPane cartScroll = new JScrollPane(itemsInCartList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
           //     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);



    //    cartLayout.add(cartScroll);





        //dodawanie widoków do ramki
        cards = new JPanel(new CardLayout());
        cards.add(loginLayout, LOGINPANEL);
        cards.add(RegisterLayout, REGISTERPANEL);
        cards.add(panelLayout,CLIENTPANEL);
        cards.add(cartLayout,CARTPANEL);
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
       // new Client("name", host);




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

    public void sendMessage(String message){
        try {
            outputStream.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                String[] substrings = message.split("#");
                int parimeters = substrings.length;
                switch (substrings[0]) {
                    case "LOGIN":
                        //Adam#Haslo!123
                        if(substrings[1].equals("SUCCESSFUL")){
                            setPermission(true);
                            CardLayout cl = (CardLayout)(cards.getLayout());
                            cl.show(cards,CLIENTPANEL);
                            this.setSize(new Dimension(600,500));
                        }

                        else if(substrings[1].equals("WRONGPASS"))
                            JOptionPane.showMessageDialog(null, "Podane hasło jest nieprawidłowe");
                        else if(substrings[1].equals("WRONGNAME"))
                            JOptionPane.showMessageDialog(null, "Podany użytkownik nie istnieje");
                        else if(!permission) JOptionPane.showMessageDialog(null, substrings[1]);
                        break;
                }
//                String[] actualValue = message.split("#");
//                System.out.println(permission);
//                printReceivedMessage(message);
//                if(actualValue[0].equals("LOGIN"))
//                {
//                    if
//                   setPermission(true);
//
//                    System.out.println(permission);
//                }


            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            setVisible(false);
            dispose();
        }
    }


    public int loginRequest(String req){

        String response = null;
        if(response =="LOGIN#SUCCESSFUL") return 0;
        if(response =="LOGIN#WRONGPASS") return -1;
        if(response =="LOGIN#WRONGNAME") return -2;
        else return Integer.parseInt(response.replace("LOGIN#ERROR: ",""));
    }

}
