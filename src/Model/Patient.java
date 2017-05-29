package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**The patient class its purpose is for data storing, In here you will find the patients ID and
 * Linked name that is given to him with the editor. There is also a list with all the heartrate data
 * This list is used by the listchart in the @HomePageController for showing the patients heartbeat.*/
class Patient implements Serializable{
    private int idWristband;
    private String name;
    private List<HeartRate> heartRateList = new ArrayList<HeartRate>();

    Patient(int idWristband, String name){
        this.idWristband = idWristband;
        this.name = name;
    }

    int getIdWristband() {
        return idWristband;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    List<HeartRate> getHeartRateList() {
        return heartRateList;
    }
}
