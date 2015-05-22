package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Shape;
import gr.unipi.computergraphics.model.shape.Polygon;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class PolygonInitiator implements ShapeInitiator<Shape> {

    private Polygon polygon;

    public PolygonInitiator() {
        this.polygon = null;
    }

    @Override
    public void initialize() {
        polygon = new Polygon();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // For the other lines.
        if (polygon.getPoint3() != null && polygon.getPoint2() != null && polygon.getPoint1() != null) {
            polygon.getPointList().add(new Point(x, y));
        }

        // For the second line.
        if (polygon.getPoint3() == null && polygon.getPoint2() != null && polygon.getPoint1() != null) {
            polygon.setPoint3(new Point(x, y));
        }

        // For  the first line.
        if (polygon.getPoint1() == null && polygon.getPoint2() == null) {
            polygon.setPoint1(new Point(x, y));
            polygon.setPoint2(new Point(x, y));
            polygon.setLineColor(ShapeProperties.getInstance().getColor());
            polygon.setFillColor(ShapeProperties.getInstance().getFill());

            // This is the first click, so create it.
            ShapeListManager.getInstance().add(polygon);
        }

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // For  the first line.
        if (polygon.getPoint3() == null) {
            Point point2 = polygon.getPoint2();
            point2.setX(x);
            point2.setY(y);
        }

        // For the second line.
        if (polygon.getPoint3() != null && polygon.getPointList().isEmpty()) {
            Point point3 = polygon.getPoint3();
            point3.setX(x);
            point3.setY(y);
        }

        // For the other lines.
        if (!polygon.getPointList().isEmpty()) {
            // Get the last added point.
            int size = polygon.getPointList().size();
            Point point = polygon.getPointList().get(size - 1);
            point.setX(x);
            point.setY(y);
        }

        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        // For  the first line.
        if (polygon.getPoint3() == null) {
            Point point2 = polygon.getPoint2();
            point2.setX(x);
            point2.setY(y);
        }

        // For the second line.
        if (polygon.getPoint3() != null && polygon.getPointList().isEmpty()) {
            Point point3 = polygon.getPoint3();
            point3.setX(x);
            point3.setY(y);
        }

        // For the other lines.
        if (!polygon.getPointList().isEmpty()) {
            // Get the last added point.
            int size = polygon.getPointList().size();
            Point point = polygon.getPointList().get(size - 1);
            point.setX(x);
            point.setY(y);

            // Check if this point is close enought(~5px distance) to the first point.
            double distance = getEuclideanDistance(polygon.getPoint1(), point);
            if (distance <= 10) {
                point.setX(polygon.getPoint1().getX());
                point.setY(polygon.getPoint1().getY());
                return ShapeInitiatorState.COMPLETED;
            }
        }

        return ShapeInitiatorState.DRAWING;
    }

    private double getEuclideanDistance(Point point1, Point point2) {
        double x_2 = Math.pow(point2.getX() - point1.getX(), 2);
        double y_2 = Math.pow(point2.getY() - point1.getY(), 2);
        return Math.sqrt(x_2 - y_2);
    }
}
