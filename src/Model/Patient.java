package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ttigg on 11/05/2017.
 */
public class Patient {
    private int idWristband;
    private String name;
    private List<HeartRate> heartRateList = new ArrayList<>();

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
