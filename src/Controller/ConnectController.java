package Controller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ConnectController {

  @FXML
  private ListView<String> serialDeviceListView;
  @FXML
  private Button connect;

  public void initialize() {
    initializeSerialDeviceListView();
  }

  private void initializeSerialDeviceListView() {
    SerialPort[] serialPorts = SerialPort.getCommPorts();
    ObservableList<String> tempSerialDeviceList = FXCollections.observableArrayList();
    for (SerialPort serialPort : serialPorts) {
      tempSerialDeviceList.add(serialPort.getDescriptivePortName());
    }
    serialDeviceListView.getItems().setAll(tempSerialDeviceList);
  }

  public void connect() {
    String nameOfSerialDevice = serialDeviceListView.getSelectionModel().getSelectedItem();
    Thread connectionToSerialDevice = new Thread(new ConnectionToSerialDeviceThread(nameOfSerialDevice));
    connectionToSerialDevice.setDaemon(true);
    connectionToSerialDevice.start();
    closeStage();
  }

  private void closeStage() {
    Stage stage = (Stage) connect.getScene().getWindow();
    stage.close();
  }
}
