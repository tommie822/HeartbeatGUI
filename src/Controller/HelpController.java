package Controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;


public class HelpController {
    @FXML
    WebView helpWebView;
    //TODO HTML file needs a normal path so that it can be included in the software and not as a given html file.
    File file = new File("C:\\Users\\tom\\IdeaProjects\\HeartbeatGUI\\src\\Resources\\helpEditor.html");
    public void initialize(){
        WebEngine webEngine = helpWebView.getEngine();
        try {
            webEngine.load(file.toURI().toURL().toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }
}
