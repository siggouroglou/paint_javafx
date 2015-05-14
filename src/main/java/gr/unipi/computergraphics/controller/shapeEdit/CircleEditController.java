package gr.unipi.computergraphics.controller.shapeEdit;

import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.shape.Point;
import gr.unipi.computergraphics.lib.shape.model.Circle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class CircleEditController implements Initializable {

    private Circle circle;

    @FXML
    private ColorPicker shapePropertyColor;
    @FXML
    private ColorPicker shapePropertyFill;
    @FXML
    private TextField centerYNode;
    @FXML
    private TextField centerXNode;
    @FXML
    private TextField radiusNode;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'CircleEdit.fxml'.";
        assert shapePropertyFill != null : "fx:id=\"shapePropertyFill\" was not injected: check your FXML file 'CircleEdit.fxml'.";
        assert centerYNode != null : "fx:id=\"centerYNode\" was not injected: check your FXML file 'CircleEdit.fxml'.";
        assert centerXNode != null : "fx:id=\"centerXNode\" was not injected: check your FXML file 'CircleEdit.fxml'.";
        assert radiusNode != null : "fx:id=\"radiusNode\" was not injected: check your FXML file 'CircleEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'CircleEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        circle = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) {
        // Validate coordinates.
        int x0;
        int y0;
        int radius;
        try {
            x0 = Integer.parseInt(centerXNode.getText());
            y0 = Integer.parseInt(centerYNode.getText());
            radius = Integer.parseInt(radiusNode.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε δεν είναι αποδεκτός.");
            return;
        }

        // Set this lines attributes.
        circle.setCenter(new Point(x0, y0));
        circle.setRadius(radius);
        circle.setLineColor(shapePropertyColor.getValue());
        circle.setFillColor(shapePropertyFill.getValue());
        
        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();
        
        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(circle);
        
        circle = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
        
        // Load data to view.
        centerXNode.setText(String.valueOf(circle.getCenter().getX()));
        centerYNode.setText(String.valueOf(circle.getCenter().getY()));
        radiusNode.setText(String.valueOf(circle.getRadius()));
        shapePropertyColor.setValue(circle.getLineColor());
        shapePropertyFill.setValue(circle.getFillColor());
    }
}
