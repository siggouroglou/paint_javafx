package gr.papei.computergraphics.lib.shape.model;

import gr.papei.computergraphics.lib.ColorUtilities;
import gr.papei.computergraphics.lib.mainView.CanvasManager;
import gr.papei.computergraphics.lib.mainView.ShapeStackManager;
import gr.papei.computergraphics.lib.shape.Point;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 *
 * @author siggouroglou@gmail.com
 */
public final class Line implements Shape {

    private Point from;
    private Point to;
    private Color lineColor;
    private double width;

    public Line() {
        this.from = new Point();
        this.to = new Point();
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public void draw(PixelWriter pixelWriter) {
        // Set the correct points.
        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();
        float wd = (float) width;

        // Run the Bresenham algorithm. (http://members.chello.at/~easyfilter/bresenham.html)
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx - dy, e2, x2, y2;
        float ed = (float) (dx + dy == 0 ? 1 : Math.sqrt((float) dx * dx + (float) dy * dy));

        for (wd = (wd + 1) / 2;;) {
            pixelWriter.setColor(x0, y0, this.lineColor);
            e2 = err;
            x2 = x0;
            if (2 * e2 >= -dx) {
                for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx) {
                    y2 += sy;
                    pixelWriter.setColor(x0, y2, this.lineColor);
                }
                if (x0 == x1) {
                    break;
                }
                e2 = err;
                err -= dy;
                x0 += sx;
            }
            if (2 * e2 <= dy) {
                for (e2 = dx - e2; e2 < ed * wd && (x1 != x2 || dx < dy); e2 += dy) {
                    x2 += sx;
                    pixelWriter.setColor(x2, y0, this.lineColor);
                }
                if (y0 == y1) {
                    break;
                }
                err += dx;
                y0 += sy;
            }
        }
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        // Set the correct points.
        int x0 = from.getX();
        int y0 = from.getY();
        int x1 = to.getX();
        int y1 = to.getY();
        float wd = (float) width;

        // Run the Bresenham algorithm. (http://members.chello.at/~easyfilter/bresenham.html)
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx - dy, e2, x2, y2;
        float ed = (float) (dx + dy == 0 ? 1 : Math.sqrt((float) dx * dx + (float) dy * dy));

        for (wd = (wd + 1) / 2;;) {
            pixelWriter.setColor(x0, y0, backgroundColor);
            e2 = err;
            x2 = x0;
            if (2 * e2 >= -dx) {
                for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx) {
                    y2 += sy;
                    pixelWriter.setColor(x0, y2, backgroundColor);
                }
                if (x0 == x1) {
                    break;
                }
                e2 = err;
                err -= dy;
                x0 += sx;
            }
            if (2 * e2 <= dy) {
                for (e2 = dx - e2; e2 < ed * wd && (x1 != x2 || dx < dy); e2 += dy) {
                    x2 += sx;
                    pixelWriter.setColor(x2, y0, backgroundColor);
                }
                if (y0 == y1) {
                    break;
                }
                err += dx;
                y0 += sy;
            }
        }
    }

    @Override
    public HBox getView() {
        final Line line = this;
        final HBox container = new HBox(10D);
        container.alignmentProperty().set(Pos.CENTER);
        container.getStyleClass().add("shapeStackItem");

        // Add the image.
        Image image = new Image(getClass().getResourceAsStream("/files/images/shapeLine.png"));
        ImageView imageView = new ImageView(image);
        container.getChildren().add(imageView);

        // Add the text.
        Label label = new Label("Γραμμή");
        label.setStyle("-fx-text-fill: " + ColorUtilities.colorToWeb(this.lineColor));
        container.getChildren().add(label);

        // Create the context menu.
        final ContextMenu contextMenu = new ContextMenu();

        // Menu Edit.
        MenuItem menuItem1 = new MenuItem("Επεξεργασία");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            }
        });
        contextMenu.getItems().add(menuItem1);

        // Menu Delete.
        MenuItem menuItem2 = new MenuItem("Διαγραφή");
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ShapeStackManager.getInstance().remove(line, container);
                CanvasManager.getInstance().refreshCanvas();
            }
        });
        contextMenu.getItems().add(menuItem2);

        container.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(container, Side.BOTTOM, 0, 0);
                }
            }
        });

        return container;
    }
}
