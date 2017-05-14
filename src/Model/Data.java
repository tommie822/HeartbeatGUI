package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 28-3-2017.
 */
public class Data {
    private static Data instance = new Data();
    private List<Patient> patients = new ArrayList<Patient>();
    private Data(){

    }
    public static Data getInstance(){
        return instance;
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
