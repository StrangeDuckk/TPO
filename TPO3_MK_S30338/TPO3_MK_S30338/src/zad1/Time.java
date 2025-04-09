/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

//zastosowac klasy Java Time API (pakiet java.time) m.in.
//LocalDate, LocalDateTime, ZonedDateTime, ChronoUnit, Period.

import java.time.LocalDateTime;

public class Time {
    public static String passed(String poczatek, String koniec) {
        //dates = {ArrayList@975}  size = 4
        // 0 = "2016-03-30T12:00 2020-03-30T:10:15"
        // 1 = "2019-01-10 2020-03-01"
        // 2 = "2020-03-27T10:00 2020-03-28T10:00"
        // 3 = "2016-03-30T12:00 2020-03-30T10:15"

        //2016-03-30T12:00
        //2020-03-30T:10:15
        if (poczatek.length()==16 || koniec.length() == 16){//pierwsza linijka gotowa
            try{
                LocalDateTime localPoczatek = LocalDateTime.parse(poczatek);
                LocalDateTime localKoniec = LocalDateTime.parse(koniec);

                //todo wypisanie dat i ile pomiedzy czasu
            }
            catch (Exception e){
                return e.toString();
            }
        }
        //todo dla innej dlugosci dat tez wypisanie dat

        return poczatek+koniec;
        //todo implementacja
    }
}
