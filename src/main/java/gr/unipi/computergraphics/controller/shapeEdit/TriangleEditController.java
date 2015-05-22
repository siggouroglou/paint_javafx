package gr.unipi.computergraphics.controller.shapeEdit;

import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Triangle;
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
public class TriangleEditController implements Initializable {

    private Triangle triangle;

    @FXML
    private ColorPicker shapePropertyColor;
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
    private TextField point3XNode;
    @FXML
    private TextField point3YNode;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert shapePropertyFill != null : "fx:id=\"shapePropertyFill\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point1XNode != null : "fx:id=\"point1XNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point1YNode != null : "fx:id=\"point1YNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point2XNode != null : "fx:id=\"point2XNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point2YNode != null : "fx:id=\"point2YNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point3XNode != null : "fx:id=\"point3XNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert point3YNode != null : "fx:id=\"point3YNode\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'TriangleEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        triangle = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) {
        // Validate coordinates.
        int x0, y0, x1, y1, x2, y2;
        try {
            x0 = Integer.parseInt(point1XNode.getText());
            y0 = Integer.parseInt(point1YNode.getText());
            x1 = Integer.parseInt(point2XNode.getText());
            y1 = Integer.parseInt(point2YNode.getText());
            x2 = Integer.parseInt(point3XNode.getText());
            y2 = Integer.parseInt(point3YNode.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε δεν είναι αποδεκτός.");
            return;
        }

        // Set this lines attributes.
        triangle.setPoint1(new Point(x0, y0));
        triangle.setPoint2(new Point(x1, y1));
        triangle.setPoint3(new Point(x2, y2));
        triangle.setLineColor(shapePropertyColor.getValue());
        triangle.setFillColor(shapePropertyFill.getValue());
        
        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();
        
        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(triangle);
        
        triangle = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
        
        // Load data to view.
        point1XNode.setText(String.valueOf(triangle.getPoint1().getX()));
        point1YNode.setText(String.valueOf(triangle.getPoint1().getY()));
        point2XNode.setText(String.valueOf(triangle.getPoint2().getX()));
        point2YNode.setText(String.valueOf(triangle.getPoint2().getY()));
        point3XNode.setText(String.valueOf(triangle.getPoint3().getX()));
        point3YNode.setText(String.valueOf(triangle.getPoint3().getY()));
        shapePropertyColor.setValue(triangle.getLineColor());
        shapePropertyFill.setValue(triangle.getFillColor());
    }
}
