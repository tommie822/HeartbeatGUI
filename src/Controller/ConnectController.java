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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class ConnectController {
    @FXML
    private ListView<String> comListView;
    @FXML
    private Button connect;
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();
    public void initialize(){
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        ObservableList<String> list = FXCollections.observableArrayList();
        for(SerialPort serialPort : serialPorts){
            list.add(serialPort.getDescriptivePortName());
        }
        comListView.getItems().setAll(list);
    }

    public void connect(){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SerialPort[] serialPorts = SerialPort.getCommPorts();
                    for(SerialPort serialPort : serialPorts){
                        System.out.println(serialPort.getDescriptivePortName());
                        if(serialPort.getDescriptivePortName().equals(comListView.getSelectionModel().getSelectedItem())){
                            System.out.println(serialPort.getSystemPortName());
                            if(Data.getInstance().getConnectedPort() != serialPort) {
                                serialPort.openPort();
                                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
                                System.out.println("setPort");
                                Data.getInstance().setConnectedPort(serialPort);
                                dataDao.clearPatients();
                                while (Data.getInstance().getConnectedPort() == serialPort && !Data.getInstance().isImport1()) {
                                    String string = "";
                                    try {
                                        char c = (char) serialPort.getInputStream().read();
                                        while (c != '!') {
                                            string = string + (c);
                                            c = (char) serialPort.getInputStream().read();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(!Data.getInstance().isImport1()) {
                                        dataDao.addNewPatientHeartRateData(string);
                                        System.out.println("String: " + string);
                                    }
                                }
                                serialPort.closePort();
                                Data.getInstance().setImport1(false);
                                Data.getInstance().setConnectedPort(null);
                                break;
                            }
                        }
                    }
                }
            });
        thread.setDaemon(true);
        thread.start();
        Stage stage = (Stage) connect.getScene().getWindow();
        stage.close();
    }
}
