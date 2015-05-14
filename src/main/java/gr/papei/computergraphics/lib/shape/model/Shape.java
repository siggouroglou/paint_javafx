package gr.papei.computergraphics.lib.shape.model;

import gr.papei.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

/**
 *
 * @author siggouroglou
 */
public interface Shape {
    // Properties.
    public String getClassName();
    public String getImageFilePath();
    public Color getShapeColor();
    public String getShapeTitle();
    
    // Drawing.
    public void draw(PixelWriter pixelWriter);
    public void clear(PixelWriter pixelWriter, Color backgroundColor);
    
    // Context Menu.
    public ShapeListItemEditStrategy getEditStrategy();

    public String exportToJson();
    public void importFixJson();
}
