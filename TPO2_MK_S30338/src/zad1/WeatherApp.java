package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class WeatherApp {
    private JFrame frame;
    private JTextField kraj, miasto, waluta;
    private JTextArea pogoda, wymiana, nbp;
    private JFXPanel webPanel;

    public WeatherApp(){
        frame = new JFrame("WeatherAPP-TPO2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,800);
        frame.setLayout(new BorderLayout());

        frame.setLocationRelativeTo(null);

        //panel zapytan uzytkownika o kraj,miasto i walute:
        JPanel uzytkownikPanel = ZapytanieUzytkownika();

        //panel wynikow
        JPanel wynikiPanel = WyswietlenieWynikow();

        //panel strony wikipedii
        webPanel = new JFXPanel();
        loadWikiPage("https://pl.wikipedia.org/wiki/",miasto.getText());
        //todo dodac java fx recznie



        frame.add(uzytkownikPanel,BorderLayout.NORTH);
        frame.add(wynikiPanel,BorderLayout.EAST);
        frame.add(webPanel,BorderLayout.WEST);

        frame.setVisible(true);
    }
    private void loadWikiPage(String url,String miasto) {
        Platform.runLater(()->{
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(url+miasto);
            webPanel.setScene(new Scene(webView));
        });
    }
    public JPanel ZapytanieUzytkownika(){
        JPanel uzytkownikPanel = new JPanel(new GridLayout(4,2));
        kraj = new JTextField();
        miasto = new JTextField();
        waluta = new JTextField();
        uzytkownikPanel.add(new JLabel("kraj:"));
        uzytkownikPanel.add(kraj);
        uzytkownikPanel.add(new JLabel("miasto:"));
        uzytkownikPanel.add(miasto);
        uzytkownikPanel.add(new JLabel("waluta (skrot, np: PLN):"));
        uzytkownikPanel.add(waluta);

        JButton buttonSzukaj = new JButton("Szukaj");
        uzytkownikPanel.add(new JLabel());//pusty element do wyrownania
        uzytkownikPanel.add(buttonSzukaj);

        buttonSzukaj.addActionListener((e->fetchData()));

        return uzytkownikPanel;
    }
    public JPanel WyswietlenieWynikow(){
        pogoda = new JTextArea(5,30);
        pogoda.setEditable(false);
        wymiana = new JTextArea(1,30);
        wymiana.setEditable(false);
        nbp = new JTextArea(1,30);
        nbp.setEditable(false);

        JPanel wynikiPanel = new JPanel(new GridLayout(3,1));
        wynikiPanel.add(new JScrollPane(pogoda));
        wynikiPanel.add(new JScrollPane(wymiana));
        wynikiPanel.add(new JScrollPane(nbp));
        return wynikiPanel;
    }
    private void fetchData() {
        if (kraj.getText().trim().isEmpty() || miasto.getText().trim().isEmpty() || waluta.getText().trim().isEmpty())
            JOptionPane.showMessageDialog(frame, "Wprowadz niepuste dane!","Brak danych",JOptionPane.ERROR_MESSAGE);

        String krajString = kraj.getText().trim().toUpperCase();
        String miastoString = miasto.getText().trim().toUpperCase();
        String walutaString = waluta.getText().trim().toUpperCase();

        Service service = new Service(krajString);
        service.setMiasto(miastoString);
        service.setPodanaWaluta(walutaString);

        pogoda.setText(service.getWeather(miastoString));
        wymiana.setText("kurs wymiany dla waluty "+ walutaString +": "+ service.getRateFor(walutaString));
        if (walutaString.equals("PLN")) {
            nbp.setText("kurs NBP dla waluty "+ walutaString +": 1.0");
        }
        else {
            nbp.setText("kurs NBP dla waluty "+ walutaString +": " + service.getNBPRate());
        }
    }
}
