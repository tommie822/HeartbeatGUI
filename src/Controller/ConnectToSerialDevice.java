package Controller;

import Model.*;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ConnectToSerialDevice implements Runnable {
    private String selectedSerialPortName;
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();
    private static int threadCounter = 0;

    public ConnectToSerialDevice(String selectedSerialPortName){
        this.selectedSerialPortName = selectedSerialPortName;
    }

    @Override
    public void run() {
        threadCounter++;
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for(SerialPort serialPort : serialPorts){
            String serialPortName = serialPort.getDescriptivePortName();
            if(serialPortName.equals(selectedSerialPortName)){
                initializeSerialPort(serialPort);
                dataDao.clearPatients();
                collectIncomingData(serialPort);
                serialPort.closePort();
                System.out.println("Close thread");
                break;
            }
        }
        threadCounter--;
    }

    private void initializeSerialPort(SerialPort serialPort){
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
    }

    private void collectIncomingData(SerialPort serialPort){
        while (threadCounter == 1) {
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
            if(threadCounter == 1) {
                dataDao.addNewPatientHeartRateData(string);
                System.out.println("String: " + string);
            }
        }
    }
}
