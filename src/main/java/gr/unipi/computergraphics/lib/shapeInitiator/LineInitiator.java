package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Line;
import gr.unipi.computergraphics.model.shape.Shape;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class LineInitiator implements ShapeInitiator<Shape> {
    private Line line;

    public LineInitiator() {
        this.line = null;
    }

    @Override
    public void initialize() {
        line = new Line();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        
        line.setFrom(new Point(x, y));
        line.setTo(new Point(x, y));
        line.setLineColor(ShapeProperties.getInstance().getColor());
        line.setWidth(ShapeProperties.getInstance().getWidth());
        
        // This is the first click, so create the line.
        ShapeListManager.getInstance().add(line);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        Point point = line.getTo();
        point.setX(x);
        point.setY(y);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        Point point = line.getTo();
        point.setX(x);
        point.setY(y);
        
        return ShapeInitiatorState.COMPLETED;
    }

}
