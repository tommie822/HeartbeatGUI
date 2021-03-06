package Controller;

import Model.AbstractCrudDao;
import Model.AbstractCrudDao.CriticalStateListener;
import Model.DaoImpl;
import Model.DataPath;
import Model.HeartRate;
import java.awt.Toolkit;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import javafx.scene.Node;


import java.util.Calendar;

public class HomePageController implements AbstractCrudDao.NewPatientListener,
    AbstractCrudDao.NewDataListener, CriticalStateListener {

  @FXML
  public LineChart<String, Integer> heartRateLineChart;
  //TODO scaling of linechart en data kunnen selecteren
  //TODO warning trigger different for each patient
  @FXML
  private ListView<String> listViewNames;
  @FXML
  private ToggleButton realTimeButton;
  @FXML
  private NumberAxis yAxis;
  @FXML
  private CategoryAxis xAxis;
  @FXML
  private LocalDateTimeTextField fromDateTime, tillDateTime;

  private DaoImpl dataDao = DataPath.dao;

  @FXML
  private void initialize() {
    dataDao.addNewDataListener(this);
    dataDao.addNewPatientListener(this);
    dataDao.addCriticalStateListener(this);
    updatePatientListView();
    listViewNames.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observable, Number oldValue,
              Number newValue) {
            if (newValue.intValue() >= 0 && !oldValue.equals(newValue)) {
              showLineChart();
            }
          }
        });
  }

  @FXML
  private void connect() {
    Stage connect = createStage("Connect", true);
    Pane pane = loadFxmlIntoPane("View/Connect.fxml");
    Scene scene = createScene(pane, 600, 400);
    connect.setScene(scene);
    connect.setOnHiding(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        realTimeButton.setSelected(true);
      }
    });
    connect.show();
  }

  @FXML
  private void importer() {
    HeartRateFileImporter.open();
  }

  @FXML
  private void editor() {
    Stage editor = createStage("Editor", true);
    Pane pane = loadFxmlIntoPane("View/Editor.fxml");
    Scene scene = createScene(pane, 600, 400);
    editor.setScene(scene);
    editor.show();
  }

  @FXML
  private void save() {
    Stage save = createStage("Save", true);
    Pane pane = loadFxmlIntoPane("View/Save.fxml");
    Scene scene = createScene(pane, 400, 600);
    save.setScene(scene);
    save.show();
  }

  @FXML
  private void help() {
    Stage help = createStage("Help", true);
    Pane pane = loadFxmlIntoPane("View/Help.fxml");
    Scene scene = createScene(pane, 600, 400);
    help.setScene(scene);
    help.show();
  }

  private Stage createStage(String title, boolean alwaysOnTop) {
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.setAlwaysOnTop(alwaysOnTop);
    return stage;
  }

  private Pane loadFxmlIntoPane(String relativeFxmlFilePath) {
    Pane fxmlPane;
    try {
      FXMLLoader loader = new FXMLLoader();
      fxmlPane = loader.load(getClass().getResourceAsStream("/" + relativeFxmlFilePath));
    } catch (IOException e) {
      e.printStackTrace();
      fxmlPane = new Pane();
    }
    return fxmlPane;
  }

  private Scene createScene(Pane scenePane, int width, int height) {
    Scene scene = new Scene(scenePane, 600, 400);
    scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
    return scene;
  }

  @FXML
  private void realTime() {
    if (realTimeButton.isSelected()) {
      showLineChart();
      realTimeButton.setText("Disable Realtime");
    } else {
      realTimeButton.setText("Enable Realtime");
    }
  }

  @FXML
  private void showLineChart() {
    String till = tillDateTime.getText();
    String from = fromDateTime.getText();
    DateFormat timeFormat2 = new SimpleDateFormat("dd-MMMMM-yyyy HH:mm:ss");
    Date tillDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2000);
    Date fromDate = cal.getTime();
    try {
        if(!till.equals("")) {
            tillDate = timeFormat2.parse(till);
            System.out.println(tillDate);
        }
        if(!from.equals("")) {
            fromDate = timeFormat2.parse(from);
            System.out.println(fromDate);
        }
    }catch (Exception e){
      e.printStackTrace();
    }
    XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
    DateFormat timeFormat = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
    int selectedIdWristband = listViewNames.getSelectionModel().getSelectedIndex();
    if (!dataDao.getPatientHeartRateList(selectedIdWristband).isEmpty()) {
      heartRateLineChart.setTitle(dataDao.getPatientName(selectedIdWristband));
      for (HeartRate heartRate : dataDao.getPatientHeartRateList(selectedIdWristband)) {
        Date dateOfHeartrate = heartRate.getDate();
        System.out.println(dateOfHeartrate);
        if(dateOfHeartrate.after(fromDate) && dateOfHeartrate.before(tillDate)) {
          series.getData().add(new XYChart.Data<>(timeFormat.format(heartRate.getDate()),
              heartRate.getHeartBeat()));
        }
      }
      heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
      yAxis.setAutoRanging(true);
      heartRateLineChart.getData().setAll(series);
    }
  }

  public void updateLineChart() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
          String till = tillDateTime.getText();
          String from = fromDateTime.getText();
          DateFormat timeFormat2 = new SimpleDateFormat("dd-MMMMM-yyyy HH:mm:ss");
          Date tillDate = new Date();
          Calendar cal = Calendar.getInstance();
          cal.set(Calendar.YEAR, 2000);
          Date fromDate = cal.getTime();
          try {
              if(!till.equals("")) {
                  tillDate = timeFormat2.parse(till);
                  System.out.println(tillDate);
              }
              if(!from.equals("")) {
                  fromDate = timeFormat2.parse(from);
                  System.out.println(fromDate);
              }
          }catch (Exception e){
              e.printStackTrace();
          }
        XYChart.Series<String, Integer> newSeries = new XYChart.Series<String, Integer>();
        DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
        int selectedWristbandID = listViewNames.getSelectionModel().getSelectedIndex();
        if (dataDao.patientHeartRateListIsNotEmpty(selectedWristbandID) && realTimeButton
            .isSelected()) {
          for (HeartRate heartRate : dataDao.getPatientHeartRateList(selectedWristbandID)) {
              Date dateOfHeartrate = heartRate.getDate();
              if(dateOfHeartrate.after(fromDate) && dateOfHeartrate.before(tillDate)) {
                  newSeries.getData().add(new XYChart.Data<>(timeFormat.format(heartRate.getDate()),
                          heartRate.getHeartBeat()));
              }
          }
          yAxis.setAutoRanging(true);
          heartRateLineChart.getData().add(newSeries);
        }
      }
    });
  }

  public void updatePatientListView() {
    ObservableList<String> patientNames = FXCollections.observableArrayList();
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        for (int index = 0; index < 50; index++) {
          patientNames.add("");
        }
        for (int index = 0; index < dataDao.getNumberOfPatients(); index++) {
          int patientId = dataDao.getPatientID(index);
          String patientName = dataDao.getPatientName(patientId);
          patientNames.set(patientId, patientName);
        }
        int previousSelectedCell = listViewNames.getSelectionModel().getSelectedIndex();
        listViewNames.getItems().setAll(patientNames);
        listViewNames.getSelectionModel().select(previousSelectedCell);
      }
    });
  }

  @Override
  public void showWarning(int idWristband, String patientName) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.WARNING, "Patient "+patientName+" with wristband: "+idWristband+" has probably some problems", ButtonType.CLOSE);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        alert.setTitle("Warning");
        alert.showAndWait();
      }
    });
  }
}