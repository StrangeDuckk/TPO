/**
 *
 *  @author Mikusek Katarzyna S30338
 *
 */

package zad1;

//todo przerzucenie PassTimeOptions.yaml do user.home
public class Main {

  public static void main(String[] args) throws Exception {
//    String fileName = System.getProperty("user.home") + "/PassTimeOptions.yaml";
    String fileName ="PassTimeOptions.yaml";
    Options opts = Tools.createOptionsFromYaml(fileName);
    System.out.println(opts);
    opts.getClientsMap().forEach( (id, dates) -> {
      System.out.println(id);
      dates.forEach( dpair -> {
        String[] d = dpair.split(" +");
        String info = Time.passed(d[0], d[1]);
        System.out.println(info);
      });
    });
  }

}
