package Controller;

import Model.AbstractCrudDao;
import Model.DataDaoImpl;
import Model.HeartRate;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HomePageController implements AbstractCrudDao.NewPatientListener,
    AbstractCrudDao.NewDataListener {

  @FXML
  public LineChart<String, Integer> heartRateLineChart;
  //TODO Make a saving function so that nurses can saveButton data from one specific user or can saveButton the real-time data that has been collected.
  //TODO Warnings implementeren met geluid en popup.
  @FXML
  private ListView<String> listViewNames;
  @FXML
  private ToggleButton realTimeButton;

  private DataDaoImpl dataDao = DataDaoImpl.getInstance();

  @FXML
  private void initialize() {
    dataDao.addNewDataListener(this);
    dataDao.addNewPatientListener(this);
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
    XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
    DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
    int selectedIdWristband = listViewNames.getSelectionModel().getSelectedIndex();
    if (!dataDao.getPatientHeartRateList(selectedIdWristband).isEmpty()) {
      heartRateLineChart.setTitle(dataDao.getPatientName(selectedIdWristband));
      for (HeartRate heartRate : dataDao.getPatientHeartRateList(selectedIdWristband)) {
        series.getData().add(new XYChart.Data<>(timeFormat.format(heartRate.getDate()),
            heartRate.getHeartBeat()));
      }
      heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
      heartRateLineChart.getData().setAll(series);
    }
  }

  public void updateLineChart() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        XYChart.Series<String, Integer> newSeries = new XYChart.Series<String, Integer>();
        DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
        int selectedWristbandID = listViewNames.getSelectionModel().getSelectedIndex();
        if (dataDao.patientHeartRateListIsNotEmpty(selectedWristbandID) && realTimeButton
            .isSelected()) {
          for (HeartRate heartRate : dataDao.getPatientHeartRateList(selectedWristbandID)) {
            newSeries.getData().add(new XYChart.Data<>(timeFormat.format(heartRate.getDate()),
                heartRate.getHeartBeat()));
          }
          heartRateLineChart.getData().add(newSeries);
        }
      }
    });
  }

  public void updatePatientListView() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        ObservableList<String> patientNames = FXCollections.observableArrayList();
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
}