package Controller;

import Model.DataDao;
import Model.DataDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class EditorController {

  @FXML
  private TextField nameInputField;
  @FXML
  private ListView<String> listViewID;
  private DataDao dataDao = DataDaoImpl.getInstance();

  /**
   * Initializes the editors patientListView with the amount of ID devices
   * and adds an listener to the listviewer
   */
  public void initialize() {
    initializeListViewID();
    initializeNameToNameInputField(0);
    addListenerToListViewID();
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
    listViewID.getSelectionModel().select("0");
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
}
