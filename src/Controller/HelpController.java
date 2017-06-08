package Controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class HelpController {

  @FXML
  private WebView helpWebView;

  public void initialize() {
    WebEngine webEngine = helpWebView.getEngine();
    webEngine.load(getClass().getClassLoader().getResource("helpEditor.html").toString());
  }
}
