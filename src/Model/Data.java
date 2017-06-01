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
  private static Data instance = null;
  private List<Patient> patients = new ArrayList<Patient>();

  protected Data() {
  }

  static Data getInstance() {
    if (instance == null) {
      try {
        String ApplicationDataFolderPath = DataPath.getLocalApplicationDataFolderPath();
        instance = getSavedDataFrom(ApplicationDataFolderPath);
      } catch (ClassNotFoundException | IOException e) {
        //If there isn't any saved data, an exception will be throwed so then a fresh Data object will be made.
        instance = new Data();
        e.printStackTrace();
      }
    }
    return instance;
  }

  public static Data getSavedDataFrom(String folderPathForSavedData)
      throws ClassNotFoundException, IOException {
    File savedDataFile = new File(folderPathForSavedData);
    FileInputStream fileInputStream = new FileInputStream(savedDataFile);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    Data savedData = (Data) objectInputStream.readObject();
    objectInputStream.close();
    return savedData;
  }

  public static void saveStateOfDataInstance() {
    try {
      String dataFolderPath = DataPath.getLocalApplicationDataFolderPath();
      saveInstanceInto(dataFolderPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void saveInstanceInto(String folderPath) throws IOException {
    File file = new File(folderPath);
    file.createNewFile();
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    objectOutputStream.writeObject(Data.getInstance());
    objectOutputStream.close();
  }

  List<Patient> getPatients() {
    return patients;
  }
}
