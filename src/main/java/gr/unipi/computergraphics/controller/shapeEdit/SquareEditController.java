package gr.unipi.computergraphics.controller.shapeEdit;

import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Square;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class SquareEditController implements Initializable {

    private Square square;

    @FXML
    private ColorPicker shapePropertyColor;
    @FXML
    private Slider shapePropertyWidth;
    @FXML
    private ColorPicker shapePropertyFill;
    @FXML
    private TextField point1XNode;
    @FXML
    private TextField point1YNode;
    @FXML
    private TextField point2XNode;
    @FXML
    private TextField point2YNode;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert shapePropertyWidth != null : "fx:id=\"shapePropertyWidth\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert shapePropertyFill != null : "fx:id=\"shapePropertyFill\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert point1XNode != null : "fx:id=\"point1XNode\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert point1YNode != null : "fx:id=\"point1YNode\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert point2XNode != null : "fx:id=\"point2XNode\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert point2YNode != null : "fx:id=\"point2YNode\" was not injected: check your FXML file 'SquareEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'SquareEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        square = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) {
        // Validate coordinates.
        int x0, y0, x1, y1;
        try {
            x0 = Integer.parseInt(point1XNode.getText());
            y0 = Integer.parseInt(point1YNode.getText());
            x1 = Integer.parseInt(point2XNode.getText());
            y1 = Integer.parseInt(point2YNode.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε δεν είναι αποδεκτός.");
            return;
        }

        // Set this lines attributes.
        square.setFrom(new Point(x0, y0));
        square.setTo(new Point(x1, y1));
        square.setLineColor(shapePropertyColor.getValue());
        square.setFillColor(shapePropertyFill.getValue());
        square.setWidth((int) shapePropertyWidth.getValue());
        
        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();
        
        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(square);
        
        square = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setSquare(Square square) {
        this.square = square;
        
        // Load data to view.
        point1XNode.setText(String.valueOf(square.getFrom().getX()));
        point1YNode.setText(String.valueOf(square.getFrom().getY()));
        point2XNode.setText(String.valueOf(square.getTo().getX()));
        point2YNode.setText(String.valueOf(square.getTo().getY()));
        shapePropertyColor.setValue(square.getLineColor());
        shapePropertyFill.setValue(square.getFillColor());
        shapePropertyWidth.setValue(square.getWidth());
    }
}
