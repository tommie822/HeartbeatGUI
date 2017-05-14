package Controller;

import Model.ConnectionHandler;
import com.fazecast.jSerialComm.SerialPort;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


/**
 * Created by tom on 14-5-2017.
 */
public class ConnectController {
    @FXML
    private ListView<String> comListView;
    public void initialize(){
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        ObservableList<String> list = FXCollections.observableArrayList();
        for(SerialPort serialPort : serialPorts){
            list.add(serialPort.getDescriptivePortName());
        }
        comListView.getItems().setAll(list);

        comListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                SerialPort[] serialPorts1 = SerialPort.getCommPorts();
                for(SerialPort serialPort: serialPorts1){
                    if(serialPort.getDescriptivePortName().equals(newValue)){
                        //TODO ervoor zorgen dat er maar een thread kan zijn. Anders gaan er twee threads waarde invoeren in de data model wat niet goed is.
                        Thread connectionhandler = new ConnectionHandler(serialPort);
                        connectionhandler.setDaemon(false);
                        connectionhandler.start();
                    }
                }
            }
        });
    }
}
