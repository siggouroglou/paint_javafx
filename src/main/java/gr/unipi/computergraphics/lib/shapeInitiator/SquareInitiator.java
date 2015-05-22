package gr.unipi.computergraphics.lib.shapeInitiator;

import gr.unipi.computergraphics.lib.singleton.ShapeProperties;
import gr.unipi.computergraphics.lib.singleton.ShapeListManager;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.model.shape.Square;
import gr.unipi.computergraphics.model.shape.Shape;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class SquareInitiator implements ShapeInitiator<Shape> {

    private Square square;

    public SquareInitiator() {
        this.square = null;
    }

    @Override
    public void initialize() {
        square = new Square();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        
        square.setFrom(new Point(x, y));
        square.setTo(new Point(x, y));
        square.setLineColor(ShapeProperties.getInstance().getColor());
        square.setFillColor(ShapeProperties.getInstance().getFill());
        square.setWidth(ShapeProperties.getInstance().getWidth().intValue());
        
        // This is the first click, so create the line.
        ShapeListManager.getInstance().add(square);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        
        // Set the correct x. (x1 - x = y1 - y  => x=x1-y1+y)
        Point point = square.getTo();
        point.setX(point.getX() - point.getY() + y);
        point.setY(y);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Get coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        
        // Set the correct x. (x1 - x = y1 - y  => x=x1-y1+y)
        Point point = square.getTo();
        point.setX(point.getX() - point.getY() + y);
        point.setY(y);
        
        return ShapeInitiatorState.COMPLETED;
    }

}
