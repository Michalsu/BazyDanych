

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


class Client extends JFrame implements ActionListener, Runnable{

    private static final long serialVersionUID = 1L;

    final static String LOGINPANEL = "Login layout";
    final static String REGISTERPANEL = "Register layout";
    final static String CLIENTPANEL = "Client panel layout";
    final static String CARTPANEL = "Cart layout";
    final static String ORDERPANEL = "Order layout";
    final static String VIEWOFORDERS = "View of client orders layout";
    static Vector<Product> produkty = new Vector<>();
    static Vector<Product> calabaza= new Vector<>();
    static Vector<String> categories = new Vector<>();
    static Vector<Pair<Product,Integer>> productsInCart = new Vector<>();
    private static boolean permission;
    JPanel cards; //a panel that uses CardLayout


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public void setPermission(boolean permission) {
        this.permission = permission;
    }


    public void addComponentToPane(JFrame pane) {
        JList categoryList =  new JList();
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
                    boolean sendrequest=true;
                    StringBuilder combinedMessage = new StringBuilder();
                    if(DataSecurity.containIllegalSymbols(login.getText())){
                        combinedMessage.append("login zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest=false;
                    }
                    if(DataSecurity.containIllegalSymbols(password.getText())){
                        combinedMessage.append("hasło zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest=false;
                    }
                    if(!DataSecurity.passwordValid(password.getText())) {
                        combinedMessage.append("hasło nie spełnia minimalnych wymagan: dlugość >=8, litery, cyfry, znaki specjalne");
                        sendrequest=false;
                    }
                    if(sendrequest){
                        StringBuilder sb = new StringBuilder();
                        sb.append("LOGINUSER#");
                        sb.append(login.getText()+ "#");
                        sb.append(password.getText());
                        sendMessage(sb.toString());
                    }else JOptionPane.showMessageDialog(null, combinedMessage.toString());
                }else JOptionPane.showMessageDialog(null, "Musisz wprowadzić dane logowania");

                   // CardLayout cl = (CardLayout)(cards.getLayout());
                //    cl.show(cards,CLIENTPANEL);
                   // pane.setSize(new Dimension(850,500));

                sendMessage("SEARCH#");int i=0;
                //(categories.size()==0 && i < 10000){
                  //  i++;
               // }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                categoryList.setListData(categories);
                calabaza=new Vector<>(produkty);
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
                        homeNumberField.getText().isEmpty())
                    JOptionPane.showMessageDialog(null, "Musisz wprowadzić wszystkie dane");
                else {
                    StringBuilder combinedMessage = new StringBuilder();
                    boolean sendrequest = true;
                    if (DataSecurity.containIllegalSymbols(loginField.getText())) {
                        combinedMessage.append("login zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    if (DataSecurity.containIllegalSymbols(passwordField.getText())) {
                        combinedMessage.append("hasło zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    if (!DataSecurity.passwordValid(passwordField.getText())) {
                        combinedMessage.append("hasło nie spełnia minimalnych wymagan: dlugość >=8, litery, cyfry, znaki specjalne\n");
                        sendrequest = false;
                    }
                    if (DataSecurity.containIllegalSymbols(nameField.getText())) {
                        combinedMessage.append("imię zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    if (DataSecurity.containIllegalSymbols(surnameField.getText())) {
                        combinedMessage.append("nazwisko zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    try{
                        int numer = Integer.parseInt(phoneField.getText());
                        if(phoneField.getText().length()!=9) {
                            combinedMessage.append("numer telefonu powinien mieć długość 9\n");
                            sendrequest = false;
                        }
                    }catch (NumberFormatException e){
                        combinedMessage.append("numer telefonu powinien być liczbą\n");
                        sendrequest = false;
                    }
                    if (DataSecurity.containIllegalSymbols(mailField.getText())) {
                        combinedMessage.append("nazwisko zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    if (!(mailField.getText().contains("@"))) {
                        combinedMessage.append("podaj prawidłowy adres email\n");
                        sendrequest = false;
                    }
                    try{
                        int numer = Integer.parseInt(postCodeField.getText());
                        if(postCodeField.getText().length()!=5) {
                            combinedMessage.append("kod pocztowy powinien miec dlugosc 5\n");
                            sendrequest = false;
                        }
                    }catch (NumberFormatException e){
                        combinedMessage.append("kod powinien byc zapisany jako 5 cyfr\n");
                        sendrequest = false;
                    }
                    if (DataSecurity.containIllegalSymbols(streetField.getText())) {
                        combinedMessage.append("ulica zawiera niedozwolone symbole (; \' \" \\ [ ] { } / ) #\n");
                        sendrequest = false;
                    }
                    try{
                        int numer = Integer.parseInt(homeNumberField.getText());
                    }catch (NumberFormatException e){
                        combinedMessage.append("numer domu powinien byc liczba");
                        sendrequest = false;
                    }
                    if(sendrequest){
                        StringBuilder sb = new StringBuilder();
                        sb.append("REGISTER#");
                        sb.append(loginField.getText()+ "#");
                        sb.append(passwordField.getText()+ "#");
                        sb.append(nameField.getText()+ "#");
                        sb.append(surnameField.getText()+ "#");
                        sb.append(phoneField.getText()+ "#");
                        sb.append(mailField.getText()+ "#");
                        sb.append(postCodeField.getText()+ "#");
                        sb.append(streetField.getText()+ "#");
                        sb.append(homeNumberField.getText());
                        sendMessage(sb.toString());
                    }else JOptionPane.showMessageDialog(null, combinedMessage.toString());


                   // JOptionPane.showMessageDialog(null, "Zarejestrowano");

                }
            }

        });

        //layout panelu użytkownika
        JPanel panelLayout = new JPanel();

        //wektor na kategorie, wczytane z bazy


        //vector na listy przedmiotów z każdej kategorii
        //tutaj trzeba wczytać poszczególne przedmioty z bazy danych i ich opisy
        Vector<Pair<DefaultListModel,DefaultListModel>> vectOfItemsByCategory= new Vector<Pair<DefaultListModel,DefaultListModel>>();



        JLabel categoryLabel = new JLabel("Kategoria");
        JLabel itemsLabel = new JLabel("Przedmioty");
        //miejsce na opis przedmiotu
        JTextArea itemsDescrition = new JTextArea();


        JButton addToCartButton = new JButton("Dodaj do koszyka");
        JLabel countLabel = new JLabel("Ilość");
        JTextField countField = new JTextField();



        itemsDescrition.setDisabledTextColor(Color.BLACK);



        //Panel użykownika

        JButton viewCartButton = new JButton("Wyświetl koszyk");
        JButton viewOrdersButton = new JButton("Zamówienia");
        JButton logoutButton = new JButton("Wyloguj");


        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sendMessage("LOGOUT");
                permission=false;
                CardLayout cl = (CardLayout) (cards.getLayout());
                cl.show(cards, LOGINPANEL);
                pane.setSize(new Dimension(355, 300));
                login.setText("");
                password.setText("");
            }
        });

        JButton searchButton = new JButton("Szukaj");
        JTextField searchField = new JTextField();


        JList itemsList = new JList();


        DefaultTableModel model = new DefaultTableModel();
        JTable itemsTable = new JTable(model);

        model.addColumn("Produkt");
        model.addColumn("Nazwa");
        model.addColumn("Cena");
        model.addColumn("Promocja");
        model.addColumn("Ilość");

        itemsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        itemsTable.getColumnModel().getColumn(0).setMinWidth(80);
        itemsTable.getColumnModel().getColumn(1).setMinWidth(155);
        itemsTable.getColumnModel().getColumn(2).setMinWidth(50);
        itemsTable.getColumnModel().getColumn(3).setMinWidth(30);
        itemsTable.getColumnModel().getColumn(4).setMinWidth(30);





        itemsTable.setDragEnabled(false);
        itemsTable.setDefaultEditor(Object.class, null);
        JScrollPane itemScroll = new JScrollPane(itemsTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollPane categoryScroll = new JScrollPane(categoryList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //aktualiacja przedmiotów dla wybranej listy

        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                model.setRowCount(0);

                if(!e.getValueIsAdjusting())
                    if(!categoryList.isSelectionEmpty())
                    for(int i = 0 ; i < produkty.size();i++)
                    if(produkty.get(i).kategoria.equals(categoryList.getSelectedValue().toString())) {
                        Vector<String> temp = new Vector<>();
                        temp.add(String.valueOf(produkty.get(i).ID));
                        temp.add(produkty.get(i).nazwa);
                        temp.add(String.valueOf(produkty.get(i).cena));
                        temp.add(String.valueOf(produkty.get(i).promocja));
                        temp.add(String.valueOf(produkty.get(i).ilosc));
                        model.addRow(temp);
                    }
               if(model.getRowCount()!=0)
               itemsTable.setRowSelectionInterval(0, 0);
                }

        });
      //aktualziacja opisu dla wybranego przedmiotu?8

        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {

                int productId = -1;
                if(itemsTable.getSelectedRow()!=-1)
                    productId = Integer.parseInt((String) itemsTable.getValueAt(itemsTable.getSelectedRow(),0));

//
              for(int i =0;i<produkty.size();i++)
                   if(produkty.get(i).ID == productId)
                             itemsDescrition.setText(produkty.get(i).opis);

            }
        });


        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,VIEWOFORDERS);
                pane.setSize(new Dimension(460,320));
            }
        });


        itemsDescrition.setEnabled(false);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("SEARCH#"+DataSecurity.sanitizeInput(searchField.getText()).replace("#",""));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                categoryList.setListData(categories);
            }
        });



        categoryLabel.setBounds(10,10,80,30);
        categoryScroll.setBounds(10,40,150,180);
        itemsLabel.setBounds(180,10,80,30);
        itemScroll.setBounds(180,40,480,180);
        searchField.setBounds(10,250,120,30);
        searchButton.setBounds(140,250,80,30);
        itemsDescrition.setBounds(10,290,350,100);
        countLabel.setBounds(10,400,40,30);
        countField.setBounds(45,400,50,30);
        addToCartButton.setBounds(100,400, 200,30);
        viewCartButton.setBounds(670,40, 130,30);
        viewOrdersButton.setBounds(670,75, 130,30);
        logoutButton.setBounds(670,110, 130,30);



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
        panelLayout.add(logoutButton);
        panelLayout.add(searchButton);
        panelLayout.add(searchField);
        panelLayout.setLayout(null);

//Layout koszyka
        JPanel cartLayout = new JPanel();
        cartLayout.setLayout(null);

//tutaj trzeba ściągnąć rzeczy w koszyku z bazy


        JButton backButton = new JButton("Powrót do sklepu");
        JButton deleteFromCartButton = new JButton("Usuń z koszyka");
        JButton orderButton = new JButton("Zamów");
        //tutaj trzeba wpisać pobraną cenę
        JLabel priceOfCart = new JLabel();
        JLabel priceLabel = new JLabel("Wartość koszyka");



        DefaultTableModel model1 = new DefaultTableModel();
        JTable itemsInCartList = new JTable(model1);

        model1.addColumn("Produkt");
        model1.addColumn("Ilość");
        model1.addColumn("Cena sztuka");
        model1.addColumn("Cena");




        itemsInCartList.setDragEnabled(false);
        itemsInCartList.setDefaultEditor(Object.class, null);
        itemsInCartList.getColumnModel().getColumn(0).setMinWidth(120);

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                // oczytanie wybranego elementu z listy/tablicy


                int id = Integer.parseInt((String) itemsTable.getValueAt(itemsTable.getSelectedRow(),0));
               // System.out.println(id);
                int count=0;
                int dostepna_ilosc_produktu=Integer.parseInt((String) itemsTable.getValueAt(itemsTable.getSelectedRow(),4));
                try{
                    count = Integer.parseInt(countField.getText());
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Ilość musi być liczbą");
                    count = -1;
                }
                if(count>=1 && count<=dostepna_ilosc_produktu){
                    sendMessage("ADDTOCART#"+login.getText()+"#"+id+"#"+count);
                }else JOptionPane.showMessageDialog(null, "Podano nieprawidłową ilość towaru");

            }
        });
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                float price = 0;

                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Nazwa");
                tableModel.addColumn("Ilość");
                tableModel.addColumn("Cena szt.");
                tableModel.addColumn("Cena");
                tableModel.setRowCount(0);

                sendMessage("GETITEMSFROMCART#" +login.getText());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CARTPANEL);
                pane.setSize(new Dimension(460,320));


                if(!productsInCart.isEmpty())

                    for( Pair<Product,Integer> p : productsInCart)
                    {

                        Vector<String> temp = new Vector<>();
                        temp.add(String.valueOf(p.getL().nazwa));
                        temp.add(String.valueOf(p.getR()));
                        temp.add(df.format((p.getL().cena)*((100.0-p.getL().promocja)/100)));
                        temp.add(df.format((p.getL().cena)*Integer.parseInt(temp.get(1))*((100.0-p.getL().promocja)/100)));
                        price += (p.getL().cena)*Integer.parseInt(temp.get(1))*((100.0-p.getL().promocja)/100);
                        tableModel.addRow(temp);
                    }

                tableModel.fireTableDataChanged();
                itemsInCartList.setModel(tableModel);
                priceOfCart.setText(String.valueOf(df.format(price)));
            }
            }
        );



        deleteFromCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

               if(itemsInCartList.getSelectedRowCount()==1) {

                     DefaultTableModel model = (DefaultTableModel) itemsInCartList.getModel();
                    int selectedRow = itemsInCartList.getSelectedRow();

                    for(Pair<Product,Integer> p : productsInCart)
                        if(p.getL().nazwa.equals(itemsInCartList.getValueAt(selectedRow,0) )){
                            sendMessage("DELETEFROMCART#" + login.getText() + "#" + p.getL().ID);
                            String price = priceOfCart.getText().toString();
                            price =  price.replaceAll(",",".");
                            float price1 = Float.parseFloat(price);
                             price = (model.getValueAt(selectedRow,3).toString());
                            price =  price.replaceAll(",",".");
                             float price2 = Float.parseFloat(price);

                            priceOfCart.setText(String.valueOf(df.format((price1-price2))));
                        }
                   model.removeRow(selectedRow);

                }else

                    JOptionPane.showMessageDialog(null,"Proszę zaznaczyć przedmiot do usunięcia");

            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CLIENTPANEL);
                pane.setSize(new Dimension(850,500));
            }
        });

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(itemsInCartList.getRowCount()!=0) {
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.show(cards, ORDERPANEL);
                    pane.setSize(new Dimension(280, 300));
                }
                else
                    JOptionPane.showMessageDialog(null,"Koszyk jest pusty");
            }
        });



        JScrollPane cartScroll = new JScrollPane(itemsInCartList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);



        cartScroll.setBounds(10,10,430,200);
        priceLabel.setBounds(10,210,100,30);
        priceOfCart.setBounds(115,210,80,30);
        deleteFromCartButton.setBounds(10,240,180,30);
        orderButton.setBounds(195,240,80,30);
        backButton.setBounds(280,240,150,30);

        cartLayout.add(priceLabel);
        cartLayout.add(cartScroll);
        cartLayout.add(priceOfCart);
        cartLayout.add(orderButton);
        cartLayout.add(backButton);
        cartLayout.add(deleteFromCartButton);

        //Layout zamówienia

        JPanel orderLayout = new JPanel();

        orderLayout.setLayout(null);



        JButton backToCartButton = new JButton("Powrót do koszyka");
        JButton confirmOrderButton = new JButton("Potwierdź zamówienie");
        JLabel  chooseDeliveryField = new JLabel("Wybierz sposób dostawy");
        JLabel  choosePayingField = new JLabel("Wybierz sposób płatności");


        String arr1[] = { "Przelew", "Blik", "Karta kredytowa" };
        String arr2[] = { "Paczkomat", "Kurier", "Odbiór osobisty" };

        JComboBox chooseDeliveryBox = new JComboBox(arr1);
        JComboBox choosePayingBox = new JComboBox(arr2);


        backToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CARTPANEL);
                pane.setSize(new Dimension(460,320));
            }
        });

                //tutaj trzeba wysłać zamówienie na serwer
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CLIENTPANEL);
                pane.setSize(new Dimension(850,500));

            }
        });


        chooseDeliveryField.setBounds(60,20,150,30);
        chooseDeliveryBox.setBounds(90,55,100,30);
        choosePayingField.setBounds(60,90,150,30);
        choosePayingBox.setBounds(90,125,100,30);
        backToCartButton.setBounds(50,170,180,30);
        confirmOrderButton.setBounds(50,210,180,30);

        orderLayout.add(chooseDeliveryField);
        orderLayout.add(chooseDeliveryBox);
        orderLayout.add(choosePayingField);
        orderLayout.add(choosePayingBox);
        orderLayout.add(backToCartButton);
        orderLayout.add(confirmOrderButton);





        //layout podglądu zamówień
        JPanel clientOrdersLayout = new JPanel();
        clientOrdersLayout.setLayout(null);

//tutaj trzeba ściągnąć rzeczy w koszyku z bazy
        DefaultListModel <String> rowOfOrdersData = new DefaultListModel <String>();

        rowOfOrdersData.addElement("zamówienie 1");
        rowOfOrdersData.addElement("zamówienie 2");

        JButton backFromOrdersButton = new JButton("Powrót do sklepu");
        JLabel ordersLabel = new JLabel("Zamówienia");


        JList ordersOfClientsList = new JList(rowOfOrdersData);


        ordersOfClientsList.setDragEnabled(false);
        JScrollPane ordersScroll = new JScrollPane(ordersOfClientsList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        backFromOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards,CLIENTPANEL);
                pane.setSize(new Dimension(850,500));
            }

        });
        ordersLabel.setBounds(170,5,100,30);
        ordersScroll.setBounds(10,40,400,200);
        backFromOrdersButton.setBounds(150,245,150,30);

        clientOrdersLayout.add(ordersLabel);
        clientOrdersLayout.add(ordersScroll);
        clientOrdersLayout.add(backFromOrdersButton);



        cards = new JPanel(new CardLayout());
        cards.add(loginLayout, LOGINPANEL);
        cards.add(RegisterLayout, REGISTERPANEL);
        cards.add(panelLayout,CLIENTPANEL);
        cards.add(cartLayout,CARTPANEL);
        cards.add(orderLayout, ORDERPANEL);
        cards.add(clientOrdersLayout,VIEWOFORDERS);
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
                            this.setSize(new Dimension(850,500));
                        }

                        else if(substrings[1].equals("WRONGPASS"))
                            JOptionPane.showMessageDialog(null, "Podane hasło jest nieprawidłowe");
                        else if(substrings[1].equals("WRONGNAME"))
                            JOptionPane.showMessageDialog(null, "Podany użytkownik nie istnieje");
                        else if(!permission) JOptionPane.showMessageDialog(null, substrings[1]);
                        break;
                    case "REGISTER":
                        if(substrings[1].equals("SUCCESSFUL")) {
                            JOptionPane.showMessageDialog(null, "Zarejestrowano");
                            CardLayout cl = (CardLayout) (cards.getLayout());
                            cl.show(cards, LOGINPANEL);
                            this.setSize(new Dimension(355, 300));
                        }
                        else JOptionPane.showMessageDialog(null, substrings[1]);
                        break;

                    case "SEARCH":
                        int ilosc= Integer.parseInt(substrings[1]);
                        produkty.clear();
                        categories= new Vector<String>();
                        for(int i =0;i<ilosc;i++){
                            produkty.add(new Product(Integer.parseInt(substrings[7*i+7]), substrings[7*i+4], substrings[7*i+5],
                                    Float.parseFloat(substrings[7*i+2]), Integer.parseInt(substrings[7*i+3]), substrings[7*i+6],Integer.parseInt(substrings[7*i+8])));
                            if(!categories.contains(substrings[7*i+6])){
                                categories.add(substrings[7*i+6]);
                            }
                        }
                        System.out.println(produkty);
                        break;
                    case "GETITEMSFROMCART":
                        productsInCart.clear();

                        if(substrings.length>1)
                        for(int i =1;i<substrings.length;i+=2){
                            for(Product p : calabaza)
                                if(p.ID == Integer.parseInt(substrings[i])){
                                    productsInCart.addElement(new Pair<Product,Integer>(p,Integer.parseInt(substrings[i+1])));}
                        }


                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"Komunikat z serwera "+message);
                        break;
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            setVisible(false);
            dispose();
        }
    }
}
