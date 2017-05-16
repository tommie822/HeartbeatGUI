package Model;

import java.util.ArrayList;
import java.util.List;

/**The patient class its purpose is for data storing, In here you will find the patients ID and
 * Linked name that is given to him with the editor. There is also a list with all the heartrate data
 * This list is used by the listchart in the @HomePageController for showing the patients heartbeat.*/
public class Patient {
    private int idWristband;
    private String name;
    private List<HeartRate> heartRateList = new ArrayList<HeartRate>();

    public Patient(int idWristband, String name){
        this.idWristband = idWristband;
        this.name = name;
    }

    public int getIdWristband() {
        return idWristband;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HeartRate> getHeartRateList() {
        return heartRateList;
    }
}
