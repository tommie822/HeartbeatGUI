package Controller;

import Model.DaoImpl;
import Model.Data;
import Model.DataPath;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HeartRateFileImporter {

  private static DaoImpl dataDao = DataPath.dao;

  public static void open() {
    File heartrateFile = openFileGetter();
    if (heartrateFile.exists()) {
      importHeartrateData(heartrateFile);
    }
  }

  private static File openFileGetter() {
    Stage stage = new Stage();
    stage.setAlwaysOnTop(true);
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("importer");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Heartrate files", "*.txt", "*.heart"));
    return fileChooser.showOpenDialog(stage);
  }

  private static void importHeartrateData(File heartrateFile) {
    try {
      if(heartrateFile.getPath().toLowerCase().contains(".txt")) {
        Scanner scanner = new Scanner(heartrateFile);
        dataDao.clearPatients();
        while (scanner.hasNext()) {
          dataDao.addNewPatientHeartRateData(scanner.nextLine());
        }
      }else if(heartrateFile.getPath().contains(".heart")){
        Data data = new Data();
        dataDao.clearPatients();
        data = data.loadStateFrom(heartrateFile.getAbsolutePath());
        dataDao.setData(data);
        DataPath.data = data;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
