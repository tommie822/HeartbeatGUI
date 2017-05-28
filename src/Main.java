import Model.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Tom on 19-3-2017.
 * Stalin was here
 */
public class Main extends Application{
    public static void main(String[] args){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    String workingDirectory;
                    //here, we assign the name of the OS, according to Java, to a variable...
                    String OS = (System.getProperty("os.name")).toUpperCase();
                    //to determine what the workingDirectory is.
                    //if it is some version of Windows
                    if (OS.contains("WIN"))
                    {
                        //it is simply the location of the "AppData" folder
                        workingDirectory = System.getenv("AppData");
                    }
                    //Otherwise, we assume Linux or Mac
                    else
                    {
                        //in either case, we would start in the user's home directory
                        workingDirectory = System.getProperty("user.home");
                        //if we are on a Mac, we are not done, we look for "Application Support"
                        workingDirectory += "/Library/Application Support";
                    }
                    File file = new File(workingDirectory+"\\data.txt");
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(Data.getInstance());
                    objectOutputStream.close();
                    System.out.println("Closing");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }));
        launch(args);
    }

    /**Sets a new scene into the mainStage and makes it maximized*/
    public void start(Stage mainStage) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        Pane rootPane = loader.load(getClass().getResourceAsStream("/View/HomePage.fxml"));
        Scene scene = new Scene(rootPane, 1920, 1080);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());

        mainStage.setTitle("Heartbeat");
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Logo.jpg")));
        mainStage.setAlwaysOnTop(false);
        mainStage.show();
    }

    public void close(){

    }
}
