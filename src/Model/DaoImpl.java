package Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by tom on 11-5-2017.
 */
public class DaoImpl extends AbstractCrudDao implements Dao {

  private Data data;

  public DaoImpl(Data data) {
    this.data = data;
  }

  public void setData(Data data2){
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
  public List<HeartRate> getPatientHeartRateList(String name) {
    for (int i = 0; i < getNumberOfPatients(); i++) {
      if (data.getPatients().get(i).getName().equals(name)) {
        return data.getPatients().get(i).getHeartRateList();
      }
    }
    return null;
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
            setIdWristband(stringTokenizer.nextToken());
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

      private void setIdWristband(String stringIdWristband) {
        idWristband = Integer.parseInt(stringIdWristband);
      }

      private void setHeartbeat(String stringHeartbeat) {
        heartbeat = Integer.parseInt(stringHeartbeat);
      }

      private void setDate(String stringDate) throws ParseException {
        DateFormat timeFormat = new SimpleDateFormat("MM:dd:yyyy:HH:mm:ss");
        date = timeFormat.parse(stringDate);
      }
    });
  }
}