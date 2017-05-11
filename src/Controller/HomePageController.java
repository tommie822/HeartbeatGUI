package Controller;

import Model.Data;
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
    BorderPane borderPane;
    @FXML
    ToggleButton realTimeButton;
    @FXML
    ListView<String> listViewNames;
    @FXML
    LineChart<String, Integer> heartRateLineChart;

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
        listViewNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateLineChart(newValue);
            }
        });
    }

    private void updateLineChart(String newValue){
        heartRateLineChart.setTitle(newValue);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        DateFormat timeFormat = new SimpleDateFormat("MMM/dd HH:mm:ss");
        for(int index = 0; index < Data.getInstance().getPatients().size() ; index++){
            if(Data.getInstance().getPatients().get(index).getName().equals(newValue)){
                for(HeartRate heartRate : Data.getInstance().getPatients().get(index).getHeartRateList()){
                    series.getData().add(new XYChart.Data(timeFormat.format(heartRate.getDate()), heartRate.getHeartBeat()));
            }
                //realTimeButton.setText(Integer.toString(Data.getInstance().getPatients().get(index).getIdWristband()));
            }
        }
        heartRateLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        heartRateLineChart.getData().setAll(series);
    }

    /**Updates the listview with the linked names to all the ID's*/
    public void updatePatientIDListView(){
        ObservableList<String> patientNamen = FXCollections.observableArrayList();
        for(int index = 0; index < Data.getInstance().getPatients().size(); index++){
            patientNamen.add(Data.getInstance().getPatients().get(index).getName());
        }
        listViewNames.getItems().setAll(patientNamen);
    }

    /**Opens a new window where you can select a file to import new data*/
    public void importData(){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("importer");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(stage);
        try {
            Data.getInstance().getPatients().clear();
            Scanner scanner = new Scanner(file);
            String importData = scanner.nextLine();
            System.out.println(importData);
            StringTokenizer stringTokenizer = new StringTokenizer(importData);
            int devices = Integer.parseInt((String)stringTokenizer.nextElement());
            for(int index = 0; index < devices; index++) {
                Data.getInstance().getPatients().add(new Patient(index, Integer.toString(index)));
            }
            while(scanner.hasNextLine()){
                importData = scanner.nextLine();
                stringTokenizer = new StringTokenizer(importData);
                int idWristband = Integer.parseInt((String)stringTokenizer.nextElement());
                DateFormat timeFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
                Date date = null;
                try {
                    date = timeFormat.parse((String) stringTokenizer.nextElement());
                }catch (ParseException e){
                    e.printStackTrace();
                }
                int heartbeat = Integer.parseInt((String)stringTokenizer.nextElement());
                for(int index = 0; index < devices; index++){
                    if(Data.getInstance().getPatients().get(index).getIdWristband() == idWristband){
                        Data.getInstance().getPatients().get(index).getHeartRateList().add(new HeartRate(date,heartbeat));
                        System.out.println(index + "\t"+date +"\t"+ heartbeat);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        updatePatientIDListView();
    }

}
