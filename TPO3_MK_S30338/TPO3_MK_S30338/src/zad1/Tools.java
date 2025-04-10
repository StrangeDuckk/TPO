/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tools {
    public static <Map> Options createOptionsFromYaml(String fileName) throws Exception{
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Object> dane = new LinkedHashMap<String,Object>();
        try (InputStream inputStream = Files.newInputStream(Paths.get(fileName))) {
            dane = yaml.load(inputStream);
        }
        Options options = new Options(
                (String) dane.get("host"),
                (Integer) dane.get("port"),
                (Boolean) dane.get("concurMode"),
                (Boolean) dane.get("showSendRes"),
                (java.util.Map<String, List<String>>) dane.get("clientsMap")
        );

//      testy tez dzialaja
//        System.out.println("---------------testy ----------------");
//        System.out.println( Time.passed( "2000-01-01", "2020-04-01"));
//        System.out.println( Time.passed( "2018-01-01", "2020-02-02"));
//        System.out.println( Time.passed( "2019-01-01", "2020-04-03"));
//        System.out.println( Time.passed( "2020-04-01T10:00", "2020-04-01T13:00"));
//        System.out.println( Time.passed( "2020-03-27T10:00", "2020-03-28T10:00") );// przed zmianą czasu);
//        System.out.println( Time.passed( "2020-03-28T10:00", "2020-03-29T10:00")); // po zmianie czasu);
//        System.out.println( Time.passed( "2020-03-28T10", "2020-03-29T10:00"));
//        System.out.println( Time.passed( "2019-02-29", "2020-04-03"));

        return options;
    }
}
