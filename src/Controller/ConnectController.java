package Controller;

import Model.*;
import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectController{
    private static boolean connected = false;
    @FXML
    private ListView<String> comListView;
    @FXML
    private Button connect;
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();
    public void initialize(){
        connected = false;
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        ObservableList<String> list = FXCollections.observableArrayList();
        for(SerialPort serialPort : serialPorts){
            list.add(serialPort.getDescriptivePortName());
        }
        comListView.getItems().setAll(list);
    }

    /**
     * This function is executed after the connect button is pressed
     * */
    public void connect(){
        String nameOfSerialDevice = comListView.getSelectionModel().getSelectedItem();
        Thread connectionToSerialDevice = new Thread(new ConnectToSerialDevice(nameOfSerialDevice));
        connectionToSerialDevice.setDaemon(true);
        connectionToSerialDevice.start();
        closeStage();
    }

    private void closeStage(){
        Stage stage = (Stage) connect.getScene().getWindow();
        stage.close();
    }
}
