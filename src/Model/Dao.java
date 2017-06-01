package Model;

import java.util.List;

public interface Dao {

  List<HeartRate> getPatientHeartRateList(int idWristband);

  List<HeartRate> getPatientHeartRateList(String name);

  boolean patientHeartRateListIsNotEmpty(int idWristband);

  String getPatientName(int idWristband);

  int getPatientID(int index);

  int getNumberOfPatients();

  List<Patient> getAllPatients();

  void addNewPatient(Patient patient);

  void clearPatients();

  void addNewPatientHeartRateData(String data);

  void setPatientName(int idWristband, String newName);
}
