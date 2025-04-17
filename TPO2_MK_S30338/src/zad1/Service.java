/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Service {
    private String kraj;
    private String miasto;
    private String podanaWaluta = null;
    private final String firstAPI = "yourApi";
    private final String secondAPI = "yourApi";

    public Service(String kraj) {
        this.kraj = kraj;
    }
    public String getWeather(String town)
    {
        String countryResponse=fetchJson("http://api.openweathermap.org/geo/1.0/direct?q="+town+"&limit=1&appid="+firstAPI);
        int poczatek = countryResponse.indexOf("country\":")+("country\":").length()+1;
        int koniec = countryResponse.indexOf("\"",poczatek+1);
        String countryCode = countryResponse.substring(poczatek,koniec);

        // api key:
        String response = fetchJson("https://api.openweathermap.org/data/2.5/weather?q=" + town + "," + countryCode + "&appid=" + firstAPI);
        if (response == null)
            return "Dane dla podanego miasta nie istnieja!";
        System.out.println(response);
        return response;
    }
    public Double getRateFor(String waluta) {
        podanaWaluta = waluta;
        //strona: https://v6.exchangerate-api.com/v6/938bdbb9c4065a2b6f3875aa/latest/USD
        // api key:
        if (waluta.equals(walutaKraju())) {
            System.out.println(1.0);
            return 1.0;
        }
        String url = "https://v6.exchangerate-api.com/v6/"+secondAPI+"/latest/"+walutaKraju();

        double walutaPodana = getWalutaDouble(fetchJson(url),waluta);


        if (walutaPodana == 0.0) {
            System.out.println(0.0);
            return 0.0;
        }
        System.out.println(walutaPodana);
        return walutaPodana;
    }

    private double getWalutaDouble(String s,String waluta) {
        int index = s.indexOf(waluta);

        if (index ==-1){
            System.out.println("nie znaleziono wartosci dla "+waluta);
            return 0.0;
        }
        else{
            if (waluta.equals(walutaKraju()))
                index = s.indexOf(waluta,index+6);
            String temp = waluta;
            index += temp.length()+2;
            int zakonczenie = s.indexOf(",",index);
            if (zakonczenie == -1)
                zakonczenie = s.indexOf("}",index);

            temp = s.substring(index,zakonczenie).trim();

            return Double.parseDouble(temp);
        }
    }

    public String walutaKraju(){
        switch(kraj.toUpperCase()){
            case "POLAND": return "PLN";
            case "POLSKA": return "PLN";
            case "UK": return "GBP";
            case "USA": return "USD";
            default: return "EUR";
        }
    }

    private double getWynik(String response) {
        return Double.parseDouble(response.substring(response.indexOf("mid")+5,response.indexOf("}")).trim());
    }
    public Double getNBPRate() {
        if (walutaKraju().equals("PLN"))
        {
            System.out.println(1.0);
            return 1.0;
        }

        String response = fetchJson("https://api.nbp.pl/api/exchangerates/rates/A/" + walutaKraju() + "/?format=json");

        if (response == null){
            return 0.0;
        }

        double wynik = getWynik(response);
        System.out.println(wynik);
        return wynik;
    }
    private String fetchJson(String urlAdres) {
        try {
            URL url = new URL(urlAdres);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode()!= 200)
            {
                System.out.println("polaczenie z "+urlAdres+" zakonczylo sie niepowodzeniem");
                return null;
            }

            Scanner sc = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while(sc.hasNext()) {
                response.append(sc.nextLine());
            }
            sc.close();
            return String.valueOf(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMiasto(String miasto){
        this.miasto = miasto;
    }
    public void setPodanaWaluta(String waluta){
        this.podanaWaluta = waluta;
    }
}