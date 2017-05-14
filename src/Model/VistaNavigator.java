package Model;

import Controller.RootController;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

/**
 * Created by Tom on 7-3-2017.
 */
public class VistaNavigator {
    public static final String HOMEPAGE = "/View/HomePage.fxml";

    private static RootController rootController;

    public static void setRootFrameController(RootController mainController){
        VistaNavigator.rootController = mainController;
    }

    /**Sets the basis panes for the GUI to start with*/
    public static void setBasic(){
        try{
            rootController.setrootAnchorPane(FXMLLoader.load(VistaNavigator.class.getResource(HOMEPAGE)));
        }catch (IOException e) {
         e.printStackTrace();
        }
    }
}
