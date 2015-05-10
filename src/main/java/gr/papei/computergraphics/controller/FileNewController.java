package gr.papei.computergraphics.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class FileNewController implements Initializable {

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert widthTextField != null : "fx:id=\"widthTextField\" was not injected: check your FXML file 'FileNew.fxml'.";
        assert heightTextField != null : "fx:id=\"heightTextField\" was not injected: check your FXML file 'FileNew.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        Stage stage = (Stage)widthTextField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createClick(ActionEvent event) {
        
    }

}
