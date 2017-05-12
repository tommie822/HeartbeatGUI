package Controller;

import Model.Data;
import Model.DataDaoImpl;
import Model.HeartRate;
import Model.Patient;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HomePageController {
    @FXML
    private ListView<String> listViewNames;
    @FXML
    private LineChart<String, Integer> heartRateLineChart;
    private DataDaoImpl dataDao = new DataDaoImpl();
    /**Opens the extra Editor window*/
    public void editor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Editor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Editor");
            stage.setScene(new Scene(rootPane, 600, 400));
            stage.show();
            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updatePatientIDListView();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**Adds an changelistener to the ListView*/
    public void initialize(){
        listViewNames.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateLineChart(newValue.intValue());
            }
        });
    }

    /**Will set the title of the linechart to the selected person
     * And will import his data into the linechart*/
    private void updateLineChart(int newValue){
        heartRateLineChart.setTitle(dataDao.getPatientName(newValue));
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
        for(HeartRate heartRate : dataDao.getPatientHeartRateList(newValue)) {
            series.getData().add(new XYChart.Data(timeFormat.format(heartRate.getDate()), heartRate.getHeartBeat()));
        }
        heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        heartRateLineChart.getData().setAll(series);
    }

    /**Updates the listview with the linked names to all the ID's*/
    private void updatePatientIDListView(){
        ObservableList<String> patientNamen = FXCollections.observableArrayList();
        for(int index = 0; index < dataDao.getNumberOfPatients(); index++){
            patientNamen.add(dataDao.getPatientName(index));
        }
        listViewNames.getItems().setAll(patientNamen);
    }

    /**Opens a new window where you can select a file to import new data*/
    public void importData(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("importer");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Heartrate files", "*.heartrate"));
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            try {
                Scanner scanner = new Scanner(file);
                String importData = scanner.nextLine();
                StringTokenizer stringTokenizer = new StringTokenizer(importData);
                int devices = Integer.parseInt((String) stringTokenizer.nextElement());
                Data.getInstance().getPatients().clear();
                for (int index = 0; index < devices; index++) {
                    dataDao.addNewPatient(new Patient(index, Integer.toString(index)));
                }
                while (scanner.hasNextLine()) {
                    importData = scanner.nextLine();
                    stringTokenizer = new StringTokenizer(importData);
                    int idWristband = Integer.parseInt((String) stringTokenizer.nextElement());
                    DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                    Date date = null;
                    date = timeFormat.parse((String) stringTokenizer.nextElement());
                    int heartbeat = Integer.parseInt((String) stringTokenizer.nextElement());
                    dataDao.addNewPatientHeartRateData(idWristband,new HeartRate(date, heartbeat));
                }
            }catch (ParseException e){
               e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
            updatePatientIDListView();
        }
    }

}
