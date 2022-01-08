

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class Client extends JFrame implements ActionListener, Runnable{

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        String name;
        String host;

        //TODO klient start apki
        // okno pierwotne z opcjami: loguj rejestruj
        // option dialog dla login haslo
        // option dialog dla rejestracji : login haslo adres, imie nazwisko, numer telefonu, mail
        host = JOptionPane.showInputDialog("Podaj adres serwera");
        name = JOptionPane.showInputDialog("Podaj nazwe klienta");
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
        setSize(300, 310);
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

        //TODO klient okienko
        // pole do wppisania tekstu(wyszukiwanie) obok guzik szukaj
        // jakies pole do wyswietlania wynikow wyszukiwania(tabela bedzie raczej odpowiednia dla tego projektu)
        // dla kazdego wyniku obok guziki: dodaj do koszyka, dodaj do porownania
        // zamiast guzika moze byc klikniecie prawym przyciskiem i rozwijana lista z opcjami
        // guzik do koszyka
        // guzik do odpalenia porownywarki
        // guzik wyloguj

        //TODO metoda do sklejania żądania dla wyszukiwania, dodawania do koszyka i porownania
        // coś w stylu:
        // StringBuilder sb= new StringBuilder();
        // sb.append("SEARCH");
        // String[] param = searchField.getText().split(" ");
        // for(int i=0;i< param.length;i++){
        //      sb.append(param[i]);
        // }
        //

        //TODO widok koszyka
        // opcje: usun z koszyka, kup i moze cos jeszcze
        // przycisk wroc do sklepu(wyszukiwarki)
        JPanel panel = new JPanel();
        JLabel messageLabel = new JLabel("Napisz:");
        JLabel textAreaLabel = new JLabel("Dialog:");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(messageLabel);
        panel.add(messageField);
        messageField.addActionListener(this);
        panel.add(textAreaLabel);
        JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll_bars);
        setContentPane(panel);
        setVisible(true);
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
