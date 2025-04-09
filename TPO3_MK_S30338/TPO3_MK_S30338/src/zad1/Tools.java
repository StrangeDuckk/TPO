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
            System.out.println(dane);
        }
        Options options = new Options(
                (String) dane.get("host"),
                (Integer) dane.get("port"),
                (Boolean) dane.get("concurMode"),
                (Boolean) dane.get("showSendRes"),
                (java.util.Map<String, List<String>>) dane.get("clientsMap")
        );
        return options;
    }
}
