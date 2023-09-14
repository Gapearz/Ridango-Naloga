import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class busTrips {
    public static void main(String[] args) {
        prihodi(postaja(args[0]), args[1], args[2]);
    }

    public static String postaja(String id){
        try{
            File file = new File("gtfs\\stops.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String vrstica;
            br.readLine(); //preskočimo prvo vrstico, saj ima ta le oznake stolpcev
            while ((vrstica = br.readLine()) != null){
                String[] element = vrstica.split(",");
                if(element[0].equals(id)){
                    System.out.println("Postajališče " + element[2]);
                    return element[0];
                }
            }
            br.close();
            throw new Exception("Postaja s id-jom: " + id + " ne obstaja"); //V primeru, da postaja s idjom ne obstaja vržemo Exception
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void prihodi(String stopID, String num, String relAbs){
        try{
            Map<String, TreeSet<LocalTime>> busSchedule = new HashMap<>();
            File file = new File("gtfs\\stop_times.txt");
            BufferedReader brStopsT = new BufferedReader(new FileReader(file));
            String vrstica;
            brStopsT.readLine(); //preskočimo prvo vrstico, saj ima ta le oznake stolpcev
            while ((vrstica = brStopsT.readLine()) != null){
                String[] element = vrstica.split(",");
                if(element[3].equals(stopID)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime casPrihoda = LocalTime.parse(element[1], formatter);
                    LocalTime trenutnaUra = LocalTime.now();
                    LocalTime maxOdmik = trenutnaUra.plusHours(2);
                    if(casPrihoda.isBefore(maxOdmik) && casPrihoda.isAfter(trenutnaUra)){
                        String[] tripID = element[0].split("_");
                        addBusArrivalTime(busSchedule, tripID[2], casPrihoda, Integer.parseInt(num));
                    }
                }
            }
            printPrihodi(busSchedule, relAbs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void addBusArrivalTime(Map<String, TreeSet<LocalTime>> schedule, String stop, LocalTime time, int num){
        if(schedule.containsKey(stop)){
            TreeSet<LocalTime> times = schedule.get(stop);
            times.add(time);
            while(times.size() > num){
                times.pollLast();
            }
        } else {
            TreeSet<LocalTime> times = new TreeSet<>();
            times.add(time);
            schedule.put(stop, times);
        }
    }

    public static void printPrihodi(Map<String, TreeSet<LocalTime>> schedule, String relAbs){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Map.Entry<String, TreeSet<LocalTime>> entry : schedule.entrySet()) {
            String stop = entry.getKey();
            TreeSet<LocalTime> times = entry.getValue();
            boolean firstTime = true;
            System.out.print(stop + ": ");
            for (LocalTime time : times) {
                if(!firstTime){
                    System.out.print(", ");
                }
                if (relAbs.equals("relative")) {
                    // Print in relative format (time since now)
                    LocalTime currentTime = LocalTime.now();
                    long minutesUntilArrival = currentTime.until(time, java.time.temporal.ChronoUnit.MINUTES);
                    System.out.print(minutesUntilArrival + " min");
                } else {
                    // Print in absolute format
                    System.out.print(time.format(formatter));
                }
                firstTime = false;
            }
            System.out.println();
        }
    }
}