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
    private TextField nameInput;
    @FXML
    private ListView<String> listViewID;
    private DataDao dataDao = DataDaoImpl.getInstance();
    /**Initializes the editors listView with the amount of ID devices
     * and adds an listener to the listviewer*/
    public void initialize(){
        ObservableList<String> patientIDs = FXCollections.observableArrayList();
        if(dataDao.getAllPatients() != null) {
            for(int index = 0; index < 50; index++){
                patientIDs.add("");
            }
            for(int index = 0; index < dataDao.getNumberOfPatients(); index++){
                patientIDs.set(dataDao.getPatientID(index), Integer.toString(dataDao.getPatientID(index)));
            }
            listViewID.getItems().addAll(patientIDs);
            listViewID.getSelectionModel().select("0");
            nameInput.setText(dataDao.getPatientName(0));
            listViewID.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    nameInput.setText(dataDao.getPatientName(newValue.intValue()));
                }
            });
        }
    }

    /**If a name is given for an ID, it gets saved into the IDLinker so that each ID is linked to a name*/
    public void linkID(){
        dataDao.setPatientName(listViewID.getSelectionModel().getSelectedIndex(),nameInput.getText());
    }
}
