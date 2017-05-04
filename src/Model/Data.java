package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 28-3-2017.
 */
public class Data {
    private static Map<Integer , String> IDLinker = new HashMap<>(); //hasmap dat de ID numbers van wireless devices linkt aan de namen van de patienten

    public Data(int wirelessDevices){
        for(int index = 1; index <= wirelessDevices; index++){
            IDLinker.put(index, Integer.toString(index));
        }
    }


    public static Map<Integer, String> getIDLinker() {
        return IDLinker;
    }

}
