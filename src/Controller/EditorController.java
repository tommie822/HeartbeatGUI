package Controller;

import Model.Dao;
import Model.DataPath;
import Model.Patient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class EditorController {

  @FXML
  private TextField nameInputField, maximumHeartRate, minimumHeartRate;
  @FXML
  private ToggleButton enableWarnings;
  @FXML
  private ListView<String> listViewID;
  private Dao dataDao = DataPath.dao;

  /**
   * Initializes the editors patientListView with the amount of ID devices
   * and adds an listener to the listviewer
   */
  public void initialize() {
    initializeListViewID();
    initializeNameToNameInputField(1);
    addListenerToListViewID();
    minimumHeartRate.setText(Integer.toString(dataDao.getPatient(listViewID.getSelectionModel().getSelectedIndex()).getMinumumHeartrate()));
    maximumHeartRate.setText(Integer.toString(dataDao.getPatient(listViewID.getSelectionModel().getSelectedIndex()).getMaximumHeartrate()));
    minimumHeartRate.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
          minimumHeartRate.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
    maximumHeartRate.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
          maximumHeartRate.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
    if(dataDao.getPatient(1).warningEnabled){
      enableWarnings.setSelected(true);
      enableWarnings.setText("Disable warning");
    }else {
      enableWarnings.setSelected(false);
      enableWarnings.setText("Enable warning");
    }
  }

  private void initializeListViewID() {
    ObservableList<String> patientIDs = FXCollections.observableArrayList();
    for (int index = 0; index < 50; index++) {
      patientIDs.add("");
    }
    for (int index = 0; index < dataDao.getNumberOfPatients(); index++) {
      patientIDs.set(dataDao.getPatientID(index), Integer.toString(dataDao.getPatientID(index)));
    }
    listViewID.getItems().addAll(patientIDs);
    listViewID.getSelectionModel().select("1");
  }

  private void initializeNameToNameInputField(int idWristband) {
    nameInputField.setText(dataDao.getPatientName(idWristband));
  }

  private void addListenerToListViewID() {
    listViewID.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observable, Number oldValue,
              Number newValue) {
            initializeNameToNameInputField(newValue.intValue());
            minimumHeartRate.setText(Integer.toString(dataDao.getPatient(newValue.intValue()).getMinumumHeartrate()));
            maximumHeartRate.setText(Integer.toString(dataDao.getPatient(newValue.intValue()).getMaximumHeartrate()));
            if(dataDao.getPatient(newValue.intValue()).warningEnabled){
              enableWarnings.setSelected(true);
              enableWarnings.setText("Disable warning");
            }else {
              enableWarnings.setSelected(false);
              enableWarnings.setText("Enable warning");
            }
          }
        });
  }

  /**
   * If a name is given for an ID, it gets saved into the IDLinker so that each ID is linked to a
   * name
   */
  public void linkID() {
    dataDao.setPatientName(listViewID.getSelectionModel().getSelectedIndex(),
        nameInputField.getText());
  }

  public void saveMinimum() {
    int patientID = listViewID.getSelectionModel().getSelectedIndex();
    Patient patient = dataDao.getPatient(patientID);
    patient.setMinumumHeartrate(Integer.parseInt(minimumHeartRate.getText()));
  }

  public void saveMaximum() {
    int patientID = listViewID.getSelectionModel().getSelectedIndex();
    Patient patient = dataDao.getPatient(patientID);
    patient.setMaximumHeartrate(Integer.parseInt(maximumHeartRate.getText()));
  }

  public void enableWarning(){
    int patientID = listViewID.getSelectionModel().getSelectedIndex();
    Patient patient = dataDao.getPatient(patientID);
    if(enableWarnings.isSelected()) {
      patient.warningEnabled = true;
      enableWarnings.setText("Disable warning");
    }else {
      patient.warningEnabled = false;
      enableWarnings.setText("Enable warning");
    }
  }
}
