package gr.papei.computergraphics.lib.shape.initiator;

import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author siggouroglou
 */
public interface ShapeInitiator<T> {
    
    public void initialize();
    public ShapeInitiatorState eventPressed(PixelWriter pixelWriter, MouseEvent mouseEvent);
    public ShapeInitiatorState eventDragged(PixelWriter pixelWriter, MouseEvent mouseEvent);
    public ShapeInitiatorState eventReleased(PixelWriter pixelWriter, MouseEvent mouseEvent);
}
