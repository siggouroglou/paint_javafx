package gr.papei.computergraphics.lib.shape.initiator;

import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.shape.Point;
import gr.papei.computergraphics.lib.shape.model.Triangle;
import gr.papei.computergraphics.lib.shape.model.Shape;
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
        triangle.setLineColor(ShapeProperties.getInstance().getColor());
        triangle.setFillColor(ShapeProperties.getInstance().getFill());
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
