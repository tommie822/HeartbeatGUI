package Controller;

import Model.*;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomePageController implements AbstractCrudDao.NewPatientListener, AbstractCrudDao.NewDataListener {
    //TODO Make a saving function so that nurses can save data from one specific user or can save the real-time data that has been collected.
    //TODO Javadoc code toevoegen
    //TODO Permanente save lijst voor alle namen gelinkt aan wristbands. Zodat je niet steeds opnieuw hoeft te doen wanneer programma opnieuw aangaat.
    //TODO Warnings implementeren met geluid en popup.

    @FXML
    private ListView<String> listViewNames;
    @FXML
    public LineChart<String, Integer> heartRateLineChart;
    @FXML
    private ToggleButton realTimeButton;
    private DataDaoImpl dataDao = DataDaoImpl.getInstance();

    /**Adds on initializing of the homepage.fxml a changelistener to the ListViewNames, which will ensure that nurses are able to
     * select patients out of the view.*/
    @FXML
    private void initialize(){
        dataDao.addNewDataListener(this);
        dataDao.addNewPatientListener(this);
        ObservableList<String> patientNamen = FXCollections.observableArrayList();
        for(int index = 0; index < 50; index++){
            patientNamen.add("");
        }
        listViewNames.getItems().setAll(patientNamen);
        listViewNames.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() >= 0 && !oldValue.equals(newValue)) {
                    showLineChart();
                }
            }
        });
        updateListView();
    }

    /**Opens the extra Editor window*/
    @FXML
    private void editor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Editor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Editor");
            Scene scene = new Scene(rootPane, 600, 400);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateListView();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void connect(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane connectPane = loader.load(getClass().getResourceAsStream("/View/Connect.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Connect");
            Scene scene = new Scene(connectPane, 400, 400);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateListView();
                            realTimeButton.setSelected(true);
                        }
                    });
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void help(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Help.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Help");
            Scene scene = new Scene(rootPane, 600, 400);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Opens a new window where people can select a .heartrate file to import into the program.
     * Only 1 file can be loaded. It puts the data from the .heartrate file in the list with patients in the Data class*/
    @FXML
    private void importData(){
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("importer");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Heartrate files", "*.heartrate"));
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            try {
                Scanner scanner = new Scanner(file);
                dataDao.clearPatients();
                while(scanner.hasNext()){
                    dataDao.addNewPatientHeartRateData(scanner.nextLine());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void realTime(){
        if(realTimeButton.isSelected()) {
            realTimeButton.setText("Disable Realtime");
        }else {
            showLineChart();
            realTimeButton.setText("Enable Realtime");
        }
    }


    @FXML
    private void showLineChart(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
                DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
                int ind = listViewNames.getSelectionModel().getSelectedIndex();
                if(!dataDao.getPatientHeartRateList(ind).isEmpty()) {
                    heartRateLineChart.setTitle(dataDao.getPatientName(ind));
                    for (HeartRate heartRate : dataDao.getPatientHeartRateList(ind)) {
                        series.getData().add(new XYChart.Data(timeFormat.format(heartRate.getDate()), heartRate.getHeartBeat()));
                    }
                    heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
                    heartRateLineChart.getData().setAll(series);
                }
            }
        });
    }

    /**Will set the title of the linechart to the selected persons ID
     * And will import his data into the linechart*/
    public void updateLineChart(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
                DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
                int ind = listViewNames.getSelectionModel().getSelectedIndex();
                if(!dataDao.getPatientHeartRateList(ind).isEmpty() && realTimeButton.isSelected()) {
                    for (HeartRate heartRate : dataDao.getPatientHeartRateList(ind)) {
                        series.getData().add(new XYChart.Data(timeFormat.format(heartRate.getDate()), heartRate.getHeartBeat()));
                    }
                    heartRateLineChart.getData().add(series);
                }
            }
        });
    }

    /**This function will be called everytime a file gets imported or when the editor view is closed*/
    public void updateListView(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ObservableList<String> patientNamen = FXCollections.observableArrayList();
                for(int index = 0; index < 50; index++){
                    patientNamen.add("");
                }
                for(int index = 0; index < dataDao.getNumberOfPatients(); index++){
                    patientNamen.set(dataDao.getPatientID(index), dataDao.getPatientName(dataDao.getPatientID(index)));
                }
                int index = listViewNames.getSelectionModel().getSelectedIndex();
                listViewNames.getItems().setAll(patientNamen);
                listViewNames.getSelectionModel().select(index);
            }
        });
    }
}
