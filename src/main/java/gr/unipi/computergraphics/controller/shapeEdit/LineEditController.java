package gr.unipi.computergraphics.controller.shapeEdit;

import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Line;
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
public class LineEditController implements Initializable {

    private Line line;

    @FXML
    private ColorPicker shapePropertyColor;
    @FXML
    private Slider shapePropertyWidth;
    @FXML
    private TextField toXNode;
    @FXML
    private TextField fromYNode;
    @FXML
    private TextField toYNode;
    @FXML
    private TextField fromXNode;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert shapePropertyWidth != null : "fx:id=\"shapePropertyWidth\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert toXNode != null : "fx:id=\"toXNode\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert fromYNode != null : "fx:id=\"fromYNode\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert toYNode != null : "fx:id=\"toYNode\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert fromXNode != null : "fx:id=\"fromXNode\" was not injected: check your FXML file 'LineEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'LineEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        line = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) {
        // Validate coordinates.
        int x0 = 0;
        int y0 = 0;
        int x1 = 0;
        int y1 = 0;
        try {
            x0 = Integer.parseInt(fromXNode.getText());
            y0 = Integer.parseInt(fromYNode.getText());
            x1 = Integer.parseInt(toXNode.getText());
            y1 = Integer.parseInt(toYNode.getText());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε δεν είναι αποδεκτός.");
            return;
        }

        // Set this lines attributes.
        line.setFrom(new Point(x0, y0));
        line.setTo(new Point(x1, y1));
        line.setLineColor(shapePropertyColor.getValue());
        line.setWidth(shapePropertyWidth.getValue());
        
        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();
        
        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(line);
        
        line = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setLine(Line line) {
        this.line = line;
        
        // Load data to view.
        fromXNode.setText(String.valueOf(line.getFrom().getX()));
        fromYNode.setText(String.valueOf(line.getFrom().getY()));
        toXNode.setText(String.valueOf(line.getTo().getX()));
        toYNode.setText(String.valueOf(line.getTo().getY()));
        shapePropertyColor.setValue(line.getLineColor());
        shapePropertyWidth.setValue(line.getWidth());
    }
}
