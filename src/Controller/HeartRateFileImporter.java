package Controller;

import Model.DataDaoImpl;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HeartRateFileImporter {

  private static DataDaoImpl dataDao = DataDaoImpl.getInstance();

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
        .add(new FileChooser.ExtensionFilter("Heartrate files", "*.heartrate"));
    return fileChooser.showOpenDialog(stage);
  }

  private static void importHeartrateData(File heartrateFile) {
    try {
      Scanner scanner = new Scanner(heartrateFile);
      dataDao.clearPatients();
      while (scanner.hasNext()) {
        dataDao.addNewPatientHeartRateData(scanner.nextLine());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
