import Controller.RootController;
import Model.VistaNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Tom on 19-3-2017.
 * Stalin was here
 */
public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    /**Sets a new scene into the mainStage and makes it maximized*/
    public void start(Stage mainStage) throws IOException{
        Scene scene = new Scene(loadRootPane(), 1920, 1080);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
        mainStage.setTitle("Heartbeat");
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Logo.jpg")));
        mainStage.setAlwaysOnTop(false);
        mainStage.show();
    }

    /**Gets the fxml file of the rootPane. It also sets the rootcontroller in the vista navigator
     * So that there is one class to coöridinate between all the different panes that can be inserted into the rootPane.
     * It also calls the starting situation of the GUI by @VistaNavigator.setBasic()
     * Afterwards it returns the rootPane so that it can be shown onto the mainstage*/
    private Pane loadRootPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane rootPane = loader.load(getClass().getResourceAsStream("/View/Root.fxml"));

        RootController rootFrameController = loader.getController();

        VistaNavigator.setRootFrameController(rootFrameController);
        VistaNavigator.setBasic();

        return rootPane;
    }
}
