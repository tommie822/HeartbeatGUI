package Controller;

import Model.AbstractCrudDao;
import Model.DataDaoImpl;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionToSerialDevice implements Runnable, AbstractCrudDao.DataClearedListener {

  private static volatile int threadCounter = 0;
  private String selectedSerialPortName;
  private DataDaoImpl dataDao = DataDaoImpl.getInstance();
  private boolean stopThreadWhenImport = false;
  private SerialPort connectedSerialPort;

  ConnectionToSerialDevice(String selectedSerialPortName) {
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
    DataDaoImpl.getInstance().addDataClearedListener(this);
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
    String string = "";
    try {
      string = processIncomingDataToStrings();
      if (threadCounter == 1 && !stopThreadWhenImport) {
        dataDao.addNewPatientHeartRateData(string);
        System.out.println("String: " + string);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String processIncomingDataToStrings() throws IOException {
    System.out.println("incomingDataProcessing");
    String string = "";
    char c = (char) connectedSerialPort.getInputStream().read();
    while (c != '!') {
      string = string + (c);
      c = (char) connectedSerialPort.getInputStream().read();
    }
    System.out.println(string);
    return string;
  }

  @Override
  public void stopConnectionThread() {
    stopThreadWhenImport = true;
  }
}
