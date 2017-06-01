import Model.DaoImpl;
import Model.Data;
import Model.DataPath;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

  private Scene homePage;
  private Data data = new Data();

  public static void main(String[] args) {
    launch(args);
  }

  public void start(Stage mainStage) throws IOException {
    DataPath.data = data.loadStateFrom(DataPath.getLocalApplicationDataFolderPath());
    DataPath.dao = new DaoImpl(DataPath.data);
    setHomePage();
    addStyleToHomePage();
    initializeStage(mainStage);
    mainStage.show();
  }

  private void setHomePage() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    Pane homePageFXML = loader.load(getClass().getResourceAsStream("/View/HomePage.fxml"));
    homePage = new Scene(homePageFXML, 1920, 1080);
  }

  private void addStyleToHomePage() {
    homePage.getStylesheets().add(getClass().getClassLoader().getResource("Layout.css").toString());
  }

  private void initializeStage(Stage mainStage) {
    mainStage.setTitle("Heartbeat");
    mainStage.setScene(homePage);
    mainStage.setMaximized(true);
    mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Logo.png")));
    mainStage.setAlwaysOnTop(false);
  }

  public void stop() {
    DataPath.data.saveStateInto(DataPath.getLocalApplicationDataFolderPath());
    System.out.println("The system closed successfully");
  }
}
