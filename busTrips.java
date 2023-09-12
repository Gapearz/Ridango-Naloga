import java.io.*;

public class busTrips {
    public static void main(String[] args) {
        prihodi(postaja(args[0]), args[1], args[2]);
    }

    public static String[] postaja(String id){
        try{
            File file = new File("gtfs\\stops.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String vrstica;
            br.readLine(); //preskočimo prvo vrstico, saj ima ta le oznake stolpcev
            while ((vrstica = br.readLine()) != null){
                String[] element = vrstica.split(",");
                if(element[0].equals(id)){
                    System.out.println(element[2]);
                    String[] kor = {element[4], element[5]};
                    return kor;
                }
            }
            br.close();
            throw new Exception("Postaja s id-jom: " + id + " ne obstaja"); //V primeru, da postaja s idjom ne obstaja vržemo Exception
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void prihodi(String[] koordinate, String num, String relAbs){
        try{
            File file = new File("gtfs\\shapes.txt");
            BufferedReader brShapes = new BufferedReader(new FileReader(file));
            String vrstica;
            brShapes.readLine(); //preskočimo prvo vrstico, saj ima ta le oznake stolpcev
            while ((vrstica = brShapes.readLine()) != null){
                String[] element = vrstica.split(",");
                if(element[1].equals(koordinate[0]) && element[2].equals(koordinate[1])){
                    System.out.println(element[0]);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}