package gr.papei.computergraphics.lib.shape.model;

import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author siggouroglou
 */
public interface Shape {
    public void draw(PixelWriter pixelWriter);
    public void clear(PixelWriter pixelWriter, Color backgroundColor);
    public HBox getView();
}
