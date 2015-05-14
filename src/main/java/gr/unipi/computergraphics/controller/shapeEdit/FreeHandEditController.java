package gr.unipi.computergraphics.controller.shapeEdit;

import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.shape.Point;
import gr.unipi.computergraphics.lib.shape.model.FreeHand;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author siggouroglou
 */
public class FreeHandEditController implements Initializable {

    private FreeHand freeHand;

    @FXML
    private ColorPicker shapePropertyColor;
    @FXML
    private TextArea pointListNode;
    @FXML
    private Label errorLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'FreeHandEdit.fxml'.";
        assert pointListNode != null : "fx:id=\"pointListNode\" was not injected: check your FXML file 'FreeHandEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'FreeHandEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        freeHand = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClick(ActionEvent event) {
        // Validate pointList.
        String[] pointListArray = pointListNode.getText().split("-->");
        List<Point> pointList = new ArrayList<>(pointListArray.length);
        for (String point : pointListArray) {
            String[] pointXY = point.split(",");
            if (pointXY.length != 2) {
                errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε στη λίστα σημείων δεν είναι αποδεκτός.");
                return;
            }

            // Get the coordinates without parenthesis.
            String pointX = pointXY[0].substring(1);
            String pointY = pointXY[1].substring(0, pointXY[1].length() - 1);

            // Cast them to int fields.
            // Validate coordinates.
            int x, y;
            try {
                x = Integer.parseInt(pointX);
                y = Integer.parseInt(pointY);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Κάποιος από τους αριθμούς που θέσατε στη λίστα σημείων δεν είναι αποδεκτός.");
                return;
            }

            // Add the point to the list.
            pointList.add(new Point(x, y));
        }

        // Set this lines attributes.
        freeHand.setPointList(pointList);
        freeHand.setLineColor(shapePropertyColor.getValue());

        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();

        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(freeHand);

        freeHand = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setFreeHand(FreeHand freeHand) {
        this.freeHand = freeHand;

        // Load data to view.
        shapePropertyColor.setValue(freeHand.getLineColor());

        // Initialize the point list node.
        StringBuilder builder = new StringBuilder();
        for (Point point : freeHand.getPointList()) {
            builder.append("(")
                    .append(point.getX()).append(",").append(point.getY())
                    .append(")-->");
        }
        // Remove the lase -->
        String output = builder.toString();
        if (builder.length() > 0) {
            output = builder.substring(0, builder.length() - 3);
        }
        pointListNode.setText(output);
    }
}
