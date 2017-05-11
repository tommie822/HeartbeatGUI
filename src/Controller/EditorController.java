package Controller;

import Model.Data;
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

    /**Initializes the editors listView with the amount of ID devices
     * and adds an listener to the listviewer*/
    public void initialize(){
        ObservableList<String> patientIDs = FXCollections.observableArrayList();
        for(int index = 0; index < Data.getInstance().getPatients().size(); index++){
            patientIDs.add(Integer.toString(index));
        }
        listViewID.getItems().addAll(patientIDs);
        listViewID.getSelectionModel().select("0");
        nameInput.setText(Data.getInstance().getPatients().get(0).getName());
        listViewID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nameInput.setText(Data.getInstance().getPatients().get(Integer.parseInt(newValue)).getName());
            }
        });
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
