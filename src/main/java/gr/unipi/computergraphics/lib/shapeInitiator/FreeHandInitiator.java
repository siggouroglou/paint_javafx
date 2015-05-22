package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Shape;
import gr.unipi.computergraphics.model.shape.FreeHand;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class FreeHandInitiator implements ShapeInitiator<Shape> {

    private FreeHand freeHand;

    public FreeHandInitiator() {
        this.freeHand = null;
    }

    @Override
    public void initialize() {
        freeHand = new FreeHand();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Add a new point.
        Point point = new Point(x, y);
        freeHand.getPointList().add(point);
        freeHand.setLineColor(ShapeProperties.getInstance().getColor());

        // Add this shape.
        ShapeListManager.getInstance().add(freeHand);

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Add a new point.
        Point point = new Point(x, y);
        freeHand.getPointList().add(point);

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Add a new point.
        Point point = new Point(x, y);
        freeHand.getPointList().add(point);

        return ShapeInitiatorState.COMPLETED;
    }
}
