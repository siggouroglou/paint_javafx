package gr.papei.computergraphics.controller.edit;

import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.singleton.Settings;
import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class EditSettingsController implements Initializable {

    @FXML
    private ColorPicker colorFontNode;
    @FXML
    private ColorPicker colorDesignNode;
    @FXML
    private Slider widthDesignNode;
    @FXML
    private ColorPicker colorFillNode;

    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert colorFontNode != null : "fx:id=\"colorFontNode\" was not injected: check your FXML file 'EditSettings.fxml'.";
        assert colorDesignNode != null : "fx:id=\"colorDesignNode\" was not injected: check your FXML file 'EditSettings.fxml'.";
        assert widthDesignNode != null : "fx:id=\"widthDesignNode\" was not injected: check your FXML file 'EditSettings.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'EditSettings.fxml'.";
        assert colorFillNode != null : "fx:id=\"colorFillNode\" was not injected: check your FXML file 'EditSettings.fxml'.";
        
        // Initialize colors from settings.
        colorFontNode.setValue(Color.valueOf(Settings.getInstance().getBackgroundColor()));
        colorDesignNode.setValue(Color.valueOf(Settings.getInstance().getDesignColor()));
        widthDesignNode.setValue(Settings.getInstance().getDesignWidth());
        colorFillNode.setValue(Color.valueOf(Settings.getInstance().getFillColor()));
    }

    @FXML
    void cancelClick(ActionEvent event) {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) throws IOException {
        // Save the font color to settings.
        Color colorFont = colorFontNode.getValue();
        Color colorDesign = colorDesignNode.getValue();
        Color colorFill = colorFillNode.getValue();
        Settings settings = Settings.getInstance();
        settings.setBackgroundColor(colorFont.toString());
        settings.setDesignColor(colorDesign.toString());
        settings.setDesignWidth(widthDesignNode.getValue());
        settings.setFillColor(colorFill.toString());
        settings.save();
        
        // Load the new font to canvas and view.
        CanvasManager.getInstance().changeBackgroundColor(colorFont);
        ShapeProperties.getInstance().colorProperty().set(colorDesign);
        ShapeProperties.getInstance().widthProperty().setValue(widthDesignNode.getValue());
        ShapeProperties.getInstance().fillProperty().set(colorFill);
        
        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
}
