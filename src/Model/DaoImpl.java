package Model;

import java.awt.Toolkit;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by tom on 11-5-2017.
 */
public class DaoImpl extends AbstractCrudDao implements Dao {

  private Data data;

  public DaoImpl(Data data) {
    this.data = data;
  }

  public void setData(Data data2) {
    newPatient(new CrudAction() {
      @Override
      public void doAction() {
        data = data2;
      }
    });
  }

  @Override
  public List<HeartRate> getPatientHeartRateList(int idWristband) {
    for (int i = 0; i < getNumberOfPatients(); i++) {
      if (data.getPatients().get(i).getIdWristband() == idWristband) {
        return data.getPatients().get(i).getHeartRateList();
      }
    }
    return new ArrayList<HeartRate>();
  }

  @Override
  public int getPatientID(int index) {
    return data.getPatients().get(index).getIdWristband();
  }

  @Override
  public boolean patientHeartRateListIsNotEmpty(int idWristband) {
    return !getPatientHeartRateList(idWristband).isEmpty();
  }

  @Override
  public String getPatientName(int idWristband) {
    for (int i = 0; i < getNumberOfPatients(); i++) {
      if (data.getPatients().get(i).getIdWristband() == idWristband) {
        return data.getPatients().get(i).getName();
      }
    }
    return null;
  }

  @Override
  public void setPatientName(int idWristband, String newName) {
    updatePatientName(new CrudAction() {
      @Override
      public void doAction() {
        for (int i = 0; i < getNumberOfPatients(); i++) {
          if (data.getPatients().get(i).getIdWristband() == idWristband) {
            data.getPatients().get(i).setName(newName);
          }
        }
      }
    });
  }

  @Override
  public int getNumberOfPatients() {
    return data.getPatients().size();
  }

  @Override
  public List<Patient> getAllPatients() {
    return data.getPatients();
  }

  @Override
  public void addNewPatient(Patient patient) {
    newPatient(new CrudAction() {
      @Override
      public void doAction() {
        data.getPatients().add(patient);
      }
    });
  }

  @Override
  public void clearPatients() {
    dataCleared(new CrudAction() {
      @Override
      public void doAction() {
        data.getPatients().clear();
      }
    });
  }

  @Override
  public void addNewPatientHeartRateData(String data) {
    newData(new CrudAction() {
      int idWristband;
      int heartbeat;
      Date date;
      boolean hasPatient;

      @Override
      public void doAction() {
        try {
          StringTokenizer stringTokenizer;
          hasPatient = false;
          stringTokenizer = new StringTokenizer(data);
          if (stringTokenizer.countTokens() == 3) {
            setIdWristband(Integer.parseInt(stringTokenizer.nextToken()));
            for (Patient patient : getAllPatients()) {
              if (patient.getIdWristband() == idWristband) {
                setHeartbeat(stringTokenizer.nextToken());
                setDate((String) stringTokenizer.nextElement());
                getPatientHeartRateList(idWristband).add(new HeartRate(date, heartbeat));
                hasPatient = true;
                break;
              }
            }
            if (!hasPatient) {
              setHeartbeat(stringTokenizer.nextToken());
              setDate(stringTokenizer.nextToken());
              addNewPatient(new Patient(idWristband, Integer.toString(idWristband)));
              getPatientHeartRateList(idWristband).add(new HeartRate(date, heartbeat));
            }
          }
        } catch (ParseException | NumberFormatException e) {
          e.printStackTrace();
        }
      }

      private void setIdWristband(int stringIdWristband) {
        idWristband = stringIdWristband;
      }

      private void setHeartbeat(String stringHeartbeat) {
        heartbeat = Integer.parseInt(stringHeartbeat);
      }

      private void setDate(String stringDate) throws ParseException {
        DateFormat timeFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        stringDate = stringDate.replace("!", "");
        date = timeFormat.parse(stringDate);
      }
    });


  }

  @Override
  public Patient getPatient(int idWristband) {
    for (int i = 0; i < getNumberOfPatients(); i++) {
      if (data.getPatients().get(i).getIdWristband() == idWristband) {
        return data.getPatients().get(i);
      }
    }
    return null;
  }

  public void addNewPatientHeartRateDataConnect(String dataString) {
    addNewPatientHeartRateData(dataString);
    StringTokenizer stringTokenizer = new StringTokenizer(dataString);
    if (stringTokenizer.countTokens() == 3) {
      int idWristband = Integer.parseInt(stringTokenizer.nextToken());
      List<HeartRate> heartRateList = getPatientHeartRateList(idWristband);
      int listSize = heartRateList.size();
      int total = 0;
      if (listSize > 5) {
        for (int i = listSize - 5; i < listSize; i++) {
          total = total + heartRateList.get(i).getHeartBeat();
        }
        int average = total / 5;
        System.out.println(getPatient(idWristband).getMinumumHeartrate()+ "\t" + average + "\t" + getPatient(idWristband).getMaximumHeartrate() );
        if (average < getPatient(idWristband).getMinumumHeartrate() || average > getPatient(idWristband).getMaximumHeartrate()) {
          if(!getPatientIsCritical(idWristband)) {
            setPatientIsCritical(idWristband, true);
          }
        }else{
          setPatientIsCritical(idWristband, false);
        }
      }
    }
  }

  private void setPatientIsCritical(int idWristband, boolean isCritical){
    String patientName = getPatientName(idWristband);
    updateCritical(new CrudAction2() {
      @Override
      public void doAction() {
        try {
          Patient patient = getPatient(idWristband);
          patient.isCritical = isCritical;
        }catch (IndexOutOfBoundsException e){
          e.printStackTrace();
        }
      }

      @Override
      public int getIdWristband() {
        return idWristband;
      }

      @Override
      public String getPatientName() {
        return patientName;
      }

      @Override
      public boolean getIsCritical() {
        return isCritical;
      }
    });


  }

  private boolean getPatientIsCritical(int idWristband) {
    try {
      for (int i = 0; i < getNumberOfPatients(); i++) {
        if (data.getPatients().get(i).getIdWristband() == idWristband) {
          return data.getPatients().get(i).isCritical;
        }
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      return false;
    }
    System.out.println("couldnt find patient is critical");
    return false;
  }
}
