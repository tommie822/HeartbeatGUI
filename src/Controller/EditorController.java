package Controller;

import Model.Data;
import Model.DataDao;
import Model.DataDaoImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by Tom on 20-3-2017.
 */
public class EditorController {
    @FXML
    TextField nameInput;
    @FXML
    ListView<String> listViewID;
    DataDao dataDao = new DataDaoImpl();
    /**Initializes the editors listView with the amount of ID devices
     * and adds an listener to the listviewer*/
    public void initialize(){
        ObservableList<String> patientIDs = FXCollections.observableArrayList();
        if(dataDao.getAllPatients() != null) {
            for(int index = 0; index < 50; index++){
                patientIDs.add("");
            }
            for(int index = 0; index < dataDao.getNumberOfPatients(); index++){
                patientIDs.set(dataDao.getPatientID(index), dataDao.getPatientName(dataDao.getPatientID(index)));
            }
            listViewID.getItems().addAll(patientIDs);
            listViewID.getSelectionModel().select("0");
            nameInput.setText(dataDao.getPatientName(0));
            listViewID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(!newValue.equals("")) {
                        nameInput.setText(dataDao.getPatientName(Integer.parseInt(newValue)));
                    }
                }
            });
        }
    }

    /**If a name is given for an ID, it gets saved into the IDLinker so that each ID is linked to a name*/
    public void linkID(){
        Data data = Data.getInstance();
        for(int index = 0; index < Data.getInstance().getPatients().size(); index++){
            if(data.getPatients().get(index).getIdWristband() == Integer.parseInt(listViewID.getSelectionModel().getSelectedItem())){
                data.getPatients().get(index).setName(nameInput.getText());
            }
        }
    }
}
