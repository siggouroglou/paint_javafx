package gr.papei.computergraphics.controller;

import gr.papei.computergraphics.lib.mainView.CanvasManager;
import gr.papei.computergraphics.lib.mainView.Settings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class EditSettingsController implements Initializable {

    @FXML
    private ColorPicker colorFontPicker;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @xparam rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert colorFontPicker != null : "fx:id=\"colorFontPicker\" was not injected: check your FXML file 'EditSettings.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'EditSettings.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) throws IOException {
        // Save the font color to settings.
        Color color = colorFontPicker.getValue();
        Settings settings = Settings.getInstance();
        settings.setFontColor(color.toString());
        settings.save();
        
        // Load the new font to canvas.
        CanvasManager.getInstance().changeBackgroundColor(color);
        
        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
}
