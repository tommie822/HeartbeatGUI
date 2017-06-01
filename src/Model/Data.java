package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
  private List<Patient> patients = new ArrayList<Patient>();

  public Data() {
  }

  public Data loadStateFrom(String folderPathForSavedData) {
    try {
      File savedDataFile = new File(folderPathForSavedData);
      FileInputStream fileInputStream = new FileInputStream(savedDataFile);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      Data data = (Data) objectInputStream.readObject();
      objectInputStream.close();
      return data;
    }catch (IOException | ClassNotFoundException e){
      e.printStackTrace();
      return this;
    }
  }

  public void saveStateInto(String folderPath) {
    try {
      saveInto(folderPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveInto(String folderPath) throws IOException {
    File file = new File(folderPath);
    file.createNewFile();
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    objectOutputStream.writeObject(this);
    objectOutputStream.close();
  }

  public List<Patient> getPatients() {
    return patients;
  }
}
