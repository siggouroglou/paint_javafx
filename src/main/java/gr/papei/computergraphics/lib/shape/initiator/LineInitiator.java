package gr.papei.computergraphics.lib.shape.initiator;

import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.shape.Point;
import gr.papei.computergraphics.lib.shape.model.Line;
import gr.papei.computergraphics.lib.shape.model.Shape;
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
        this.line = new Line();
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
