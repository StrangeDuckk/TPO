/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

//zastosowac klasy Java Time API (pakiet java.time) m.in.
//LocalDate, LocalDateTime, ZonedDateTime, ChronoUnit, Period.

import jdk.nashorn.internal.runtime.ECMAException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {
    public static String passed(String poczatek, String koniec) {

        if (poczatek.length()==16 || koniec.length() == 16){
            LocalDateTime localPoczatek;
            LocalDateTime localKoniec;
            try{
                localPoczatek = LocalDateTime.parse(poczatek);
                localKoniec = LocalDateTime.parse(koniec);
                return naglowek(localPoczatek,localKoniec) + obliczenia(localPoczatek, localKoniec);
            }
            catch (Exception e){
                return "*** "+e;
            }
        }
        else {
            LocalDate localPoczatek;
            LocalDate localKoniec;
            try{
                localPoczatek = LocalDate.parse(poczatek);
                localKoniec = LocalDate.parse(koniec);
                return naglowek(localPoczatek,localKoniec) +obliczenia(localPoczatek,localKoniec);
            }
            catch (Exception e){
                return "*** "+e;
            }
        }
    }

    private static String naglowek(LocalDate localPoczatek, LocalDate localKoniec) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", new Locale("pl"));
        return "Od " + localPoczatek.format(dateFormatter) + " do " + localKoniec.format(dateFormatter) + "\n";
    }
    private static String naglowek(LocalDateTime localPoczatek, LocalDateTime localKoniec) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy (EEEE)", new Locale("pl"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return "Od " + localPoczatek.format(dateFormatter) + " godz. " + localPoczatek.format(timeFormatter) +
                " do " + localKoniec.format(dateFormatter) + " godz. " + localKoniec.format(timeFormatter) + "\n";
    }

    private static String obliczenia(LocalDateTime localPoczatek, LocalDateTime localKoniec) {
        long dniPomiedzy = ChronoUnit.DAYS.between(localPoczatek.toLocalDate(), localKoniec.toLocalDate());
        if (localKoniec.toLocalTime().isAfter(localPoczatek.toLocalTime()))
            dniPomiedzy++;
        double tygodnie = Math.round((dniPomiedzy / 7.0) * 100) / 100.0;
        long godziny = ChronoUnit.HOURS.between(localPoczatek.atZone(ZoneId.of("Europe/Warsaw")), localKoniec.atZone(ZoneId.of("Europe/Warsaw")));
        long minuty = ChronoUnit.MINUTES.between(localPoczatek.atZone(ZoneId.of("Europe/Warsaw")), localKoniec.atZone(ZoneId.of("Europe/Warsaw")));

        Period period = Period.between(localPoczatek.toLocalDate(),localKoniec.toLocalDate());
        return formatowanieMija(dniPomiedzy,tygodnie) + formatowanieGodzin(godziny,minuty) + formatowanieKalendarzowo(period);
    }
    private static String obliczenia(LocalDate localPoczatek,LocalDate localKoniec) {
        long dniPomiedzy = ChronoUnit.DAYS.between(localPoczatek, localKoniec);
        double tygodnie = Math.round((dniPomiedzy / 7.0) * 100) / 100.0;

        Period period = Period.between(localPoczatek,localKoniec);
        return formatowanieMija(dniPomiedzy,tygodnie) + formatowanieKalendarzowo(period);
    }

    private static String formatowanieMija(long dniPomiedzy, double tygodnie) {
        String wynik = " - mija: ";
        if (dniPomiedzy>1)
            wynik += dniPomiedzy+" dni,";
        else
            wynik += dniPomiedzy+" dzień,";

        if (tygodnie == 1)
            wynik += tygodnie + " tydzien\n";
        else if (tygodnie>1 && tygodnie < 5)
            wynik += " tygodne " + tygodnie + "\n";
        else
            wynik+= " tygodni " + tygodnie + "\n";

        return wynik;
    }
    private static String formatowanieGodzin(long godziny, long minuty) {
        return " - godzin: " + godziny + ", minut: " + minuty +"\n";
    }
    private static String formatowanieKalendarzowo(Period period) {
        String wynik = " - kalendarzowo: ";

        if(period.getYears() != 0)
            if (period.getYears() == 1)
                wynik+= period.getYears() + " rok";
            else if (period.getYears() >1 && period.getYears() < 5)
                wynik+= period.getYears() + " lata";
            else
                wynik+= period.getYears() + " lat";
        if(period.getMonths() !=0)
            if (period.getMonths() == 1)
                wynik+= period.getYears() != 0?(", "+ period.getMonths() + " miesiąc"):(period.getMonths() + " miesiąc");
            else if (period.getMonths() > 1 &&period.getMonths() < 5)
                wynik+= period.getYears() != 0?(", "+ period.getMonths() + " miesiące"):(period.getMonths() + " miesiące");
            else
                wynik+= period.getYears() != 0?(", "+ period.getMonths() + " miesięcy"):(period.getMonths() + " miesięcy");
        if (period.getDays() != 0)
            if (period.getDays() == 1)
                wynik+= period.getMonths() != 0?(", " + period.getDays() + " dzień"):(period.getDays() + " dzień");
            else if (period.getDays() > 1)
                wynik+= period.getMonths() != 0?(", " + period.getDays() + " dni"):(period.getDays() + " dni");

        return wynik;
    }


}
