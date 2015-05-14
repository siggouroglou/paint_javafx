package gr.papei.computergraphics.lib.shape.initiator;

import gr.papei.computergraphics.lib.shape.Point;
import gr.papei.computergraphics.lib.shape.model.Shape;
import gr.papei.computergraphics.lib.shape.model.Rect;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class RectInitiator implements ShapeInitiator<Shape> {

    private Rect rect;

    public RectInitiator() {
        this.rect = null;
    }

    @Override
    public void initialize() {
        rect = new Rect();
    }

    @Override
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        
        rect.setFrom(new Point(x, y));
        rect.setTo(new Point(x, y));
        rect.setLineColor(ShapeProperties.getInstance().getColor());
        rect.setFillColor(ShapeProperties.getInstance().getFill());
        rect.setWidth(ShapeProperties.getInstance().getWidth().intValue());
        
        // This is the first click, so create the line.
        ShapeListManager.getInstance().add(rect);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        Point point = rect.getTo();
        point.setX(x);
        point.setY(y);
        
        return ShapeInitiatorState.DRAWING;
    }

    @Override
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent) {
        // Set line coordinates.
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        Point point = rect.getTo();
        point.setX(x);
        point.setY(y);
        
        return ShapeInitiatorState.COMPLETED;
    }

}
