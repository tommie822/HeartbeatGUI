package Controller;

import Model.*;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class ConnectionToSerialDeviceThread implements Runnable, AbstractCrudDao.DataClearedListener {

  private static volatile int threadCounter = 0;
  private String selectedSerialPortName;
  private DaoImpl dataDao =  DataPath.dao;
  private boolean stopThreadWhenImport = false;
  private SerialPort connectedSerialPort;

  ConnectionToSerialDeviceThread(String selectedSerialPortName) {
    this.selectedSerialPortName = selectedSerialPortName;
  }

  @Override
  public void run() {
    System.out.println("Threads: " + threadCounter);
    threadCounter++;
    System.out.println("Threads: " + threadCounter);
    SerialPort[] serialPorts = SerialPort.getCommPorts();
    for (SerialPort serialPort : serialPorts) {
      String serialPortName = serialPort.getDescriptivePortName();
      if (serialPortName.equals(selectedSerialPortName)) {
        dataDao.clearPatients();
        waitUntilThisThreadIsOnlyThread();
        initializeSerialPort(serialPort);
        collectIncomingData();
        serialPort.closePort();
        System.out.println("Close thread");
        break;
      }
    }
    threadCounter--;
    System.out.println("Threads: " + threadCounter);
  }

  private void waitUntilThisThreadIsOnlyThread() {
    try {
      while (threadCounter != 1) {
        Thread.sleep(200);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void initializeSerialPort(SerialPort serialPort) {
    serialPort.openPort();
    //serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
    DataPath.dao.addDataClearedListener(this);
    connectedSerialPort = serialPort;
  }

  private void collectIncomingData() {
    final Runnable stuffToDo = new Thread() {
      @Override
      public void run() {
        addDataToPatient();
      }
    };
    while (threadCounter == 1 && !stopThreadWhenImport) {
      final ExecutorService executor = Executors.newSingleThreadExecutor();
      final Future future = executor.submit(stuffToDo);
      executor.shutdown();
      try {
        future.get(10, TimeUnit.SECONDS);
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
  }

  private void addDataToPatient() {
    String string;
    try {
      string = processIncomingDataToStrings();
      if (threadCounter == 1 && !stopThreadWhenImport) {
        dataDao.addNewPatientHeartRateDataConnect(string);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String processIncomingDataToStrings() throws IOException {
    String string = "";
    char c = (char) connectedSerialPort.getInputStream().read();
    while (c != '!') {
      string = string + (c);
      c = (char) connectedSerialPort.getInputStream().read();
    }
    return string;
  }

  @Override
  public void stopConnectionThread() {
    stopThreadWhenImport = true;
  }
}
