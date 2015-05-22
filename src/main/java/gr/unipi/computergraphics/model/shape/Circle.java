package gr.unipi.computergraphics.model.shape;

import com.google.gson.Gson;
import gr.unipi.computergraphics.controller.shapeEdit.CircleEditController;
import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.unipi.computergraphics.model.Point;
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
public final class Circle implements Shape {
    public static final boolean lineColorEnable = true;
    public static final boolean widthEnable = false;
    public static final boolean fillColorEnable = true;

    private Point center;
    private int radius;
    private Color lineColor;
    private Color fillColor;

    public Circle() {
        this.center = new Point();
        this.radius = 0;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void draw(PixelWriter pixelWriter) {
        // Draw the line of the circle and fill it.
        // Set the correct points.
        int xm = this.center.getX();
        int ym = this.center.getY();
        int r = this.radius;

        // Run the Bresenham algorithm.
        // (http://members.chello.at/~easyfilter/bresenham.html)
        // (http://stackoverflow.com/questions/1201200/fast-algorithm-for-drawing-filled-circles)
        
        // Draw the circle fill.
        int x = -r;
        int y = 0;
        int err = 2 - 2 * r;
        do {
            // Draw the circle fill by drawing lines per quadrant.
            drawLine(pixelWriter, xm + x, ym - y, xm - x, ym - y, this.fillColor);//1-2
            drawLine(pixelWriter, xm - x, ym + y, xm + x, ym + y, this.fillColor);//3-4

            r = err;
            if (r <= y) {
                err += ++y * 2 + 1;
            }
            if (r > x || err > y) {
                err += ++x * 2 + 1;
            }
        } while (x < 0);

        // Draw the circle line.
        xm = this.center.getX();
        ym = this.center.getY();
        r = this.radius;
        x = -r;
        y = 0;
        err = 2 - 2 * r;
        do {
            // One call per quadrant per pixel.
            pixelWriter.setColor(xm + y, ym + x, this.lineColor);//1ο
            pixelWriter.setColor(xm + x, ym - y, this.lineColor);//2ο
            pixelWriter.setColor(xm - y, ym - x, this.lineColor);//3ο
            pixelWriter.setColor(xm - x, ym + y, this.lineColor);//4ο

            r = err;
            if (r <= y) {
                err += ++y * 2 + 1;
            }
            if (r > x || err > y) {
                err += ++x * 2 + 1;
            }
        } while (x < 0);
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        // Draw the line of the circle and fill it.
        // Set the correct points.
        int xm = this.center.getX();
        int ym = this.center.getY();
        int r = this.radius;

        // Run the Bresenham algorithm.
        // (http://members.chello.at/~easyfilter/bresenham.html)
        // (http://stackoverflow.com/questions/1201200/fast-algorithm-for-drawing-filled-circles)
        
        // Draw the circle fill.
        int x = -r;
        int y = 0;
        int err = 2 - 2 * r;
        do {
            // Draw the circle fill by drawing lines per quadrant.
            drawLine(pixelWriter, xm + x, ym - y, xm - x, ym - y, backgroundColor);//1-2
            drawLine(pixelWriter, xm - x, ym + y, xm + x, ym + y, backgroundColor);//3-4

            r = err;
            if (r <= y) {
                err += ++y * 2 + 1;
            }
            if (r > x || err > y) {
                err += ++x * 2 + 1;
            }
        } while (x < 0);

        // Draw the circle line.
        xm = this.center.getX();
        ym = this.center.getY();
        r = this.radius;
        x = -r;
        y = 0;
        err = 2 - 2 * r;
        do {
            // One call per quadrant per pixel.
            pixelWriter.setColor(xm + y, ym + x, backgroundColor);//1ο
            pixelWriter.setColor(xm + x, ym - y, backgroundColor);//2ο
            pixelWriter.setColor(xm - y, ym - x, backgroundColor);//3ο
            pixelWriter.setColor(xm - x, ym + y, backgroundColor);//4ο

            r = err;
            if (r <= y) {
                err += ++y * 2 + 1;
            }
            if (r > x || err > y) {
                err += ++x * 2 + 1;
            }
        } while (x < 0);
    }

    public void drawLine(PixelWriter pixelWriter, int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx + dy, e2; /* error value e_xy */

        for (;;) {  /* loop */

            pixelWriter.setColor(x0, y0, color);
            if (x0 == x1 && y0 == y1) {
                break;
            }
            e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            } /* e_xy+e_x > 0 */

            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            } /* e_xy+e_y < 0 */

        }
    }

    @Override
    public ShapeListItemEditStrategy getEditStrategy() {
        final Circle circle = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Κύκλου");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/CircleEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference to this line.
            CircleEditController controller = (CircleEditController) loader.getController();
            controller.setCircle(circle);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getClassName() {
        return "Circle";
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapeCircle.png";
    }

    @Override
    public Color getShapeColor() {
        return this.fillColor;
    }

    @Override
    public String getShapeTitle() {
        return "Κύκλος";
    }

    @Override
    public String exportToJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

    @Override
    public void importFixJson() {
        Color color = this.lineColor;
        this.lineColor = Color.valueOf(color.toString());
        
        color = this.fillColor;
        this.fillColor = Color.valueOf(color.toString());
    }
}
