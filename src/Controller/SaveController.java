package Controller;

import Model.DaoImpl;
import Model.Data;
import Model.DataPath;
import Model.Patient;
import java.awt.FileDialog;
import java.awt.Frame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Created by tom on 31-5-2017.
 */
public class SaveController {

  @FXML
  private ListView<CheckBox> patientListView;
  @FXML
  private Button save;

  private DaoImpl dataDao = DataPath.dao;


  @FXML
  private void initialize() {

    ObservableList<CheckBox> patientNameCheckBoxes = FXCollections.observableArrayList();
    for(int idWristband = 0; idWristband < 50; idWristband++){
      String patientName = dataDao.getPatientName(idWristband);
      CheckBox checkBox = new CheckBox(patientName);
      patientNameCheckBoxes.add(checkBox);
    }
    patientListView.getItems().setAll(patientNameCheckBoxes);
  }
  private String path;
  @FXML
  private void save() {
    Stage stage = (Stage) save.getScene().getWindow();
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setAlwaysOnTop(true);
        dialog.setMode(FileDialog.SAVE);
        dialog.setVisible(true);
        String fileName = dialog.getFile();
        String directoryName = dialog.getDirectory();
        path = directoryName + fileName + ".heart";
        System.out.println(path);
      }
    });
    stage.hide();
    thread.setDaemon(true);
    thread.start();
    try {
      thread.join();
    }catch (InterruptedException e){
      e.printStackTrace();
    }
    if(!path.contains("nullnull")) {
      Data data = new Data();
      int index = 0;
      int index2 = 0;
      for (CheckBox checkBox : patientListView.getItems()) {
        if (checkBox.isSelected()) {
          int id = index;
          String patientName = dataDao.getPatientName(index2);
          Patient patient = new Patient(id, patientName);
          patient.getHeartRateList().addAll(dataDao.getPatientHeartRateList(index2));
          data.getPatients().add(patient);
          index++;
        }
        index2++;
      }
      data.saveStateInto(path);
    }


    stage.close();
  }
}
