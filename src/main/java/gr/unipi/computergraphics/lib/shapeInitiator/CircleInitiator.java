package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Circle;
import gr.unipi.computergraphics.model.shape.Shape;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class CircleInitiator implements ShapeInitiator<Shape> {

    private Circle circle;

    public CircleInitiator() {
        this.circle = null;
    }

    @Override
    public void initialize() {
        circle = new Circle();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        circle.setCenter(new Point(x, y));
        circle.setRadius(0);
        circle.setLineColor(ShapeProperties.getInstance().getColor());
        circle.setFillColor(ShapeProperties.getInstance().getFill());

        // This is the first click, so create it.
        ShapeListManager.getInstance().add(circle);

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the center.
        int x1 = circle.getCenter().getX();
        int y1 = circle.getCenter().getY();

        // Get the coordinates.
        int x2 = (int) mouseEvent.getX();
        int y2 = (int) mouseEvent.getY();

        // Get the distance.
        double  xDiff = x1-x2;
        double  xSqr  = Math.pow(xDiff, 2);
	double yDiff = y1-y2;
	double ySqr = Math.pow(yDiff, 2);
        double radius   = Math.sqrt(xSqr + ySqr);
        
        // Set the radius.
        circle.setRadius((int) radius);

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get the center.
        int x1 = circle.getCenter().getX();
        int y1 = circle.getCenter().getY();

        // Get the coordinates.
        int x2 = (int) mouseEvent.getX();
        int y2 = (int) mouseEvent.getY();

        // Get the distance.
        double  xDiff = x1-x2;
        double  xSqr  = Math.pow(xDiff, 2);
	double yDiff = y1-y2;
	double ySqr = Math.pow(yDiff, 2);
        double radius   = Math.sqrt(xSqr + ySqr);
        
        // Set the radius.
        circle.setRadius((int) radius);

        return ShapeInitiatorState.COMPLETED;
    }

}
