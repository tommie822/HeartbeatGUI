package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Patient implements Serializable {

  private int idWristband;
  private String name;
  private List<HeartRate> heartRateList = new ArrayList<HeartRate>();

  Patient(int idWristband, String name) {
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
