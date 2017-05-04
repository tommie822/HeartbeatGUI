package Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Tom on 19-3-2017.
 */
public class RootController {
    @FXML
    AnchorPane rootAnchorPane;

    public void setrootAnchorPane(Node node){
        rootAnchorPane.getChildren().setAll(node);
    }
}
