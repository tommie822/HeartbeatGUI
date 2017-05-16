package Model;

import Controller.RootController;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

/**The @VistaNavigator class is for switching between frames inside the mainstage(HomePage)
 * To use the vistaNavigator correctly you will need to initialize the controller of the rootPane where all the other
 * javafx panes will show in. Afterwards you can use this class to switch between panes by calling
 * the @rootController.setRootAnchorpane function inside the rootpane*/
public class VistaNavigator {
    public static final String HOMEPAGE = "/View/HomePage.fxml";

    private static RootController rootController;

    public static void setRootFrameController(RootController mainController){
        VistaNavigator.rootController = mainController;
    }

    /**Everything that needs to be initialized in the rootpane at the beginning of the start of the program
     * needs to be declared in this function.*/
    public static void setBasic(){
        try{
            rootController.setrootAnchorPane(FXMLLoader.load(VistaNavigator.class.getResource(HOMEPAGE)));
        }catch (IOException e) {
         e.printStackTrace();
        }
    }
}
