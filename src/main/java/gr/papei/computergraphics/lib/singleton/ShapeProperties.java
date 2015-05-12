package gr.papei.computergraphics.lib.singleton;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * A singleton that is binding to the main scene shape properties color, width and fill.
 * 
 * @author siggouroglou@gmail.com
 */
public final class ShapeProperties {

    private static ShapeProperties INSTANCE;

    private ObjectProperty<Color> color;
    private DoubleProperty width;
    private ObjectProperty<Color> fill;

    private ShapeProperties() {
        this.color = new SimpleObjectProperty<>(Color.valueOf(Settings.getInstance().getBackgroundColor()));
        this.width = new SimpleDoubleProperty(1D);
        this.fill = new SimpleObjectProperty<>(Color.valueOf(Settings.getInstance().getFillColor()));
    }

    public static ShapeProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShapeProperties();
        }
        return INSTANCE;
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public Double getWidth() {
        return width.get();
    }

    public void setWidth(Double width) {
        this.width.set(width);
    }

    public ObjectProperty<Color> fillProperty() {
        return fill;
    }

    public Color getFill() {
        return fill.get();
    }

    public void setFill(Color fill) {
        this.fill.set(fill);
    }
}
