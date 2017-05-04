package Controller;

import Model.Data;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class HomePageController {
    @FXML
    BorderPane borderPane;
    @FXML
    ToggleButton realTimeButton;
    @FXML
    ListView<String> listViewNames;

    /**Opens the extra Editor window*/
    public void editor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Editor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Editor");
            stage.setScene(new Scene(rootPane, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**Adds an changelistener to the ListView*/
    public void initialize(){
        listViewNames.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                for(Map.Entry<Integer, String> entry : Data.getIDLinker().entrySet()){
                    if(entry.getValue().equals(newValue)){
                        realTimeButton.setText(Integer.toString(entry.getKey()));
                    }
                }
            }
        });
    }

    /**Updates the listview with the linked names to all the ID's*/
    public void updatePatientToIDView(){
        ObservableList<String> patientNamen = FXCollections.observableArrayList();
        for(int index = 1; index <= Data.getIDLinker().size(); index++){
            patientNamen.add(Data.getIDLinker().get(index));
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
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(data);
            Data data1 = new Data(Integer.parseInt(stringTokenizer.nextToken()));
            System.out.println(stringTokenizer.nextToken());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
