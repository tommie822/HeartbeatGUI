package Controller;

import Model.*;
import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class ConnectController {
    @FXML
    private ListView<String> comListView;
    private DataDaoImpl dataDao = new DataDaoImpl();
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
                            serialPort.openPort();
                            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
                            if(!Data.getInstance().getConnectedPort().equals(serialPort)) {
                                Data.getInstance().setConnectedPort(serialPort);
                                while (Data.getInstance().getConnectedPort().equals(serialPort)) {
                                    String string = "";
                                    try {
                                        char c = (char) serialPort.getInputStream().read();
                                        while (c != 0) {
                                            string = string + (c);
                                            c = (char) serialPort.getInputStream().read();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    updatePatientData(string);
                                    System.out.println("String: " + string);
                                }
                            }
                        }
                    }
                }
            });
            thread.start();
    }

    private void updatePatientData(String importData){
        try {
            StringTokenizer stringTokenizer;
            /**Will import all the data from the file and will put it in the correct patient*/
            boolean hasPatient = false;
            stringTokenizer = new StringTokenizer(importData);
            int idWristband = Integer.parseInt((String) stringTokenizer.nextElement());
            for(Patient patient : dataDao.getAllPatients()){
                if(patient.getIdWristband() == idWristband){
                    DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                    Date date = null;
                    date = timeFormat.parse((String) stringTokenizer.nextElement());
                    int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                    dataDao.addNewPatientHeartRateData(idWristband,new HeartRate(date, heartbeat));
                    hasPatient = true;
                    break;
                }
            }
            if(!hasPatient) {
                dataDao.addNewPatient(new Patient(idWristband, Integer.toString(idWristband)));
                DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                Date date = null;
                date = timeFormat.parse((String) stringTokenizer.nextElement());
                int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                dataDao.addNewPatientHeartRateData(idWristband,new HeartRate(date, heartbeat));
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}
