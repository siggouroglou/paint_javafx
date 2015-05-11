package gr.papei.computergraphics.controller;

import gr.papei.computergraphics.lib.mainView.CanvasManager;
import gr.papei.computergraphics.lib.mainView.ShapeStackManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert widthTextField != null : "fx:id=\"widthTextField\" was not injected: check your FXML file 'FileNew.fxml'.";
        assert heightTextField != null : "fx:id=\"heightTextField\" was not injected: check your FXML file 'FileNew.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'FileNew.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createClick(ActionEvent event) {
        // Get width and height as canvas.
        int width = 0;
        try {
            width = Integer.parseInt(widthTextField.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Μη αποδεκτός αριθμός για το πεδίο 'Πλατος (width)'");
            return;
        }
        int height = 0;
        try {
            height = Integer.parseInt(heightTextField.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Μη αποδεκτός αριθμός για το πεδίο 'Ύψος (height)'");
            return;
        }
        
        // Initialize canvas and stack.
        CanvasManager.getInstance().resetCanvas(width, height);
        ShapeStackManager.getInstance().clear();

        // Close this stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

}
