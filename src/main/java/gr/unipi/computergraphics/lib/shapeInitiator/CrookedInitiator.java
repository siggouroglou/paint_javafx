package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Shape;
import gr.unipi.computergraphics.model.shape.Crooked;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class CrookedInitiator implements ShapeInitiator<Shape> {

    private Crooked crooked;

    public CrookedInitiator() {
        this.crooked = null;
    }

    @Override
    public void initialize() {
        crooked = new Crooked();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Run only for the first two points draw.
        if (crooked.getPointList().isEmpty()) {
            Point point1 = new Point(x, y);
            Point point2 = new Point(x, y);
            crooked.getPointList().add(point1);
            crooked.getPointList().add(point2);
            crooked.setLineColor(ShapeProperties.getInstance().getColor());

            // This is the first click, so create it.
            ShapeListManager.getInstance().add(crooked);
        } else {
            // Add a new point.
            Point point = new Point(x, y);
            crooked.getPointList().add(point);
        }

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Modify the last added point.
        Point point = crooked.getPointList().get(crooked.getPointList().size() - 1);
        point.setX(x);
        point.setY(y);

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Modify the last added point.
        Point point = crooked.getPointList().get(crooked.getPointList().size() - 1);
        point.setX(x);
        point.setY(y);

        return ShapeInitiatorState.DRAWING; // Never goes to completed state.
    }
}
