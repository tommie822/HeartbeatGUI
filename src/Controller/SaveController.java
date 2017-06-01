package Controller;

import Model.DataDaoImpl;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

/**
 * Created by tom on 31-5-2017.
 */
public class SaveController {

  @FXML
  private ListView<CheckBox> patientListView;

  private DataDaoImpl dataDao = DataDaoImpl.getInstance();


  @FXML
  private void initialize() {

    ObservableList<CheckBox> patientNameCheckBoxes = FXCollections.observableArrayList();
    int totalPatients = dataDao.getNumberOfPatients();
    for(int idWristband = 0; idWristband < totalPatients; idWristband++){
      String patientName = dataDao.getPatientName(idWristband);
      CheckBox checkBox = new CheckBox(patientName);
      patientNameCheckBoxes.add(checkBox);
    }
    patientListView.getItems().setAll(patientNameCheckBoxes);
  }

  @FXML
  private void save() {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.SAVE);
        dialog.setVisible(true);
        for(File file : dialog.getFiles()){
          try {
            file.createNewFile();
          }catch (IOException e){
            e.printStackTrace();
          }
          System.out.println(file.getAbsolutePath());
        }
      }
    });
    thread.setDaemon(true);
    thread.start();
  }
}
