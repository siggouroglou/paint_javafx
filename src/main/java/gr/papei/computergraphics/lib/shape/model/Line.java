package gr.papei.computergraphics.lib.shape.model;

import gr.papei.computergraphics.controller.shapeEdit.LineEditController;
import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.papei.computergraphics.lib.shape.Point;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public ShapeListItemEditStrategy getEditStrategy() {
        final Line line = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Γραμμής");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/LineEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference to this line.
            LineEditController controller = (LineEditController) loader.getController();
            controller.setLine(line);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapeLine.png";
    }

    @Override
    public Color getShapeColor() {
        return this.lineColor;
    }

    @Override
    public String getShapeTitle() {
        return "Γραμμή";
    }
}
