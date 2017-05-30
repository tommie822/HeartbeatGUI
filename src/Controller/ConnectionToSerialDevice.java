package Controller;

import Model.*;
import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ConnectionToSerialDevice implements Runnable, AbstractCrudDao.DataClearedListener{
    private String selectedSerialPortName;
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();
    private static volatile int threadCounter = 0;
    private boolean stopThreadWhenImport = false;

    ConnectionToSerialDevice(String selectedSerialPortName){
        this.selectedSerialPortName = selectedSerialPortName;
    }

    @Override
    public void run() {
        System.out.println("Threads: "+ threadCounter);
        threadCounter++;
        System.out.println("Threads: "+ threadCounter);
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for(SerialPort serialPort : serialPorts){
            String serialPortName = serialPort.getDescriptivePortName();
            if(serialPortName.equals(selectedSerialPortName)){
                dataDao.clearPatients();
                waitUntilThisThreadIsOnlyThread();
                initializeSerialPort(serialPort);
                collectIncomingData(serialPort);
                serialPort.closePort();
                System.out.println("Close thread");
                break;
            }
        }
        threadCounter--;
        System.out.println("Threads: "+ threadCounter);
    }

    private void waitUntilThisThreadIsOnlyThread(){
        try {
            while (threadCounter != 1) {
                Thread.sleep(200);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void initializeSerialPort(SerialPort serialPort){
        serialPort.openPort();
        //serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        DataDaoImpl.getInstance().addDataClearedListener(this);
    }

    private void collectIncomingData(SerialPort serialPort){
        while (threadCounter == 1 && !stopThreadWhenImport) {
            String string = "";
            try {
                string = processIncomingDataToStrings(serialPort);
                if(threadCounter == 1 && !stopThreadWhenImport){
                    dataDao.addNewPatientHeartRateData(string);
                    System.out.println("String: " + string);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private String processIncomingDataToStrings(SerialPort serialPort) throws IOException{
        //TODO timeout implementeren
        System.out.println("incomingDataProcessing");
        String string = "";
        char c = (char) serialPort.getInputStream().read();
        while (c != '!') {
            string = string + (c);
            c = (char) serialPort.getInputStream().read();
        }
        System.out.println(string);
        return string;
    }

    @Override
    public void stopConnectionThread() {
        stopThreadWhenImport = true;
    }
}
