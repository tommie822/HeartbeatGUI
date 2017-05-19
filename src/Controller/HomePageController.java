package Controller;

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
    //TODO Realtime button needs to linked with the incoming Connect function
    //TODO The connect button so that it reads all the incoming data and saves it.
    //TODO Make a saving function so that nurses can save data from one specific user or can save the real-time data that has been collected.
    //TODO Duidelijk structuur aanbrengen aan het programma, en vooral in deze classe waar alle functies door elkaar heen staan.
    @FXML
    private ListView<String> listViewNames;
    @FXML
    private LineChart<String, Integer> heartRateLineChart;
    private DataDaoImpl dataDao = new DataDaoImpl();

    /**Adds on initializing of the homepage.fxml a changelistener to the ListViewNames, which will ensure that nurses are able to
     * select patients out of the view.*/
    public void initialize(){
        listViewNames.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() >= 0) {
                    updateLineChart(heartRateLineChart, newValue.intValue());
                }
            }
        });
    }

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

    public void connect(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane connectPane = loader.load(getClass().getResourceAsStream("/View/Connect.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Connect");
            stage.setScene(new Scene(connectPane, 400, 400));
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void help(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Help.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(new Scene(rootPane, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Opens a new window where people can select a .heartrate file to import into the program.
     * Only 1 file can be loaded. It puts the data from the .heartrate file in the list with patients in the Data class*/
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
                dataDao.clearPatients();
                while(scanner.hasNext()){
                    updatePatientData(scanner.nextLine());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
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
                    updatePatientIDListView();
                }
        }catch (ParseException e){
            e.printStackTrace();
        }
        updateLineChart(heartRateLineChart, listViewNames.getSelectionModel().getSelectedIndex());
    }

    /**Will set the title of the linechart to the selected persons ID
     * And will import his data into the linechart*/
    private void updateLineChart(LineChart heartRateLineChart, int newValue){
            XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
            DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
            if(!dataDao.getPatientHeartRateList(newValue).isEmpty()) {
                heartRateLineChart.setTitle(dataDao.getPatientName(newValue));
                for (HeartRate heartRate : dataDao.getPatientHeartRateList(newValue)) {
                    series.getData().add(new XYChart.Data(timeFormat.format(heartRate.getDate()), heartRate.getHeartBeat()));
                }
                heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
                heartRateLineChart.getData().setAll(series);
            }
    }

    /**This function will be called everytime a file gets imported or when the editor view is closed*/
    private void updatePatientIDListView(){
        ObservableList<String> patientNamen = FXCollections.observableArrayList();
        for(int index = 0; index < 50; index++){
            patientNamen.add("");
        }
        for(int index = 0; index < dataDao.getNumberOfPatients(); index++){
            patientNamen.set(dataDao.getPatientID(index), dataDao.getPatientName(dataDao.getPatientID(index)));
        }
        listViewNames.getItems().setAll(patientNamen);
    }

}
