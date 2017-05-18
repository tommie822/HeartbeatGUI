package Model;

import java.util.List;

/**
 * Created by tom on 11-5-2017.
 */
public interface DataDao {
    List<HeartRate> getPatientHeartRateList(int idWristband);
    List<HeartRate> getPatientHeartRateList(String name);
    String getPatientName(int idWristband);
    int getPatientID(int index);
    int  getNumberOfPatients();
    List getAllPatients();
    void addNewPatient(Patient patient);
    void clearPatients();
    void addNewPatientHeartRateData(int idWristband, HeartRate heartRate);
}
