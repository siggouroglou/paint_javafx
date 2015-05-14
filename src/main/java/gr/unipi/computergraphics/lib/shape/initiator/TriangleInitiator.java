package gr.unipi.computergraphics.lib.shape.initiator;

import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.shape.Point;
import gr.unipi.computergraphics.lib.shape.model.Triangle;
import gr.unipi.computergraphics.lib.shape.model.Shape;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class TriangleInitiator implements ShapeInitiator<Shape> {

    private Triangle triangle;

    public TriangleInitiator() {
        this.triangle = null;
    }

    @Override
    public void initialize() {
        triangle = new Triangle();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Run only for the first two points draw.
        if (triangle.getPoint1() == null && triangle.getPoint2() == null) {
            triangle.setPoint1(new Point(x, y));
            triangle.setPoint2(new Point(x, y));
            triangle.setLineColor(ShapeProperties.getInstance().getColor());
            triangle.setFillColor(ShapeProperties.getInstance().getFill());

            // This is the first click, so create it.
            ShapeListManager.getInstance().add(triangle);
        } else {
            triangle.setPoint3(new Point(x, y));
        }

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Run only for the first two points draw.
        if (triangle.getPoint3() == null) {
            triangle.setPoint2(new Point(x, y));
        }

        // In case of the third click move the third point.
        if (triangle.getPoint3() != null) {
            triangle.setPoint3(new Point(x, y));
        }

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // Initialize the two points and when a third occured, complete the shape.
        if (triangle.getPoint3() == null) {
            triangle.setPoint2(new Point(x, y));
        } else {
            triangle.setPoint3(new Point(x, y));
            return ShapeInitiatorState.COMPLETED;
        }

        return ShapeInitiatorState.DRAWING;
    }

}
