package gr.papei.computergraphics.controller.shapeEdit;

import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.shape.Point;
import gr.papei.computergraphics.lib.shape.model.Polygon;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class PolygonEditController implements Initializable {

    private Polygon polygon;

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
    private TextField point3XNode;
    @FXML
    private TextField point3YNode;
    @FXML
    private TextField pointListNode;
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
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert shapePropertyWidth != null : "fx:id=\"shapePropertyWidth\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert shapePropertyFill != null : "fx:id=\"shapePropertyFill\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point1XNode != null : "fx:id=\"point1XNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point1YNode != null : "fx:id=\"point1YNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point2XNode != null : "fx:id=\"point2XNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point2YNode != null : "fx:id=\"point2YNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point3XNode != null : "fx:id=\"point3XNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert point3YNode != null : "fx:id=\"point3YNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert pointListNode != null : "fx:id=\"pointListNode\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
        assert errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'PolygonEdit.fxml'.";
    }

    @FXML
    void cancelClick(ActionEvent event) {
        polygon = null; //remove reference.

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
        polygon.setPoint1(new Point(x0, y0));
        polygon.setPoint2(new Point(x1, y1));
        polygon.setPoint3(new Point(x2, y2));
        polygon.setPointList(pointList);
        polygon.setLineColor(shapePropertyColor.getValue());
        polygon.setFillColor(shapePropertyFill.getValue());
        polygon.setWidth((int) shapePropertyWidth.getValue());

        // Redraw canvas cause background color may has change.
        CanvasManager.getInstance().refreshCanvas();

        // Redraw the shapeListItem, in case of a color change.
        ShapeListManager.getInstance().reDrawShape(polygon);

        polygon = null; //remove reference.

        // Close the stage.
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;

        // Load data to view.
        point1XNode.setText(String.valueOf(polygon.getPoint1().getX()));
        point1YNode.setText(String.valueOf(polygon.getPoint1().getY()));
        point2XNode.setText(String.valueOf(polygon.getPoint2().getX()));
        point2YNode.setText(String.valueOf(polygon.getPoint2().getY()));
        point3XNode.setText(String.valueOf(polygon.getPoint3().getX()));
        point3YNode.setText(String.valueOf(polygon.getPoint3().getY()));
        shapePropertyColor.setValue(polygon.getLineColor());
        shapePropertyFill.setValue(polygon.getFillColor());
        shapePropertyWidth.setValue(polygon.getWidth());

        // Initialize the point list node.
        StringBuilder builder = new StringBuilder();
        for (Point point : polygon.getPointList()) {
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
