package gr.unipi.computergraphics.model.shape;

import com.google.gson.Gson;
import gr.unipi.computergraphics.controller.shapeEdit.TriangleEditController;
import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import gr.unipi.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.unipi.computergraphics.model.Point;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author siggouroglou@gmail.com
 */
public final class Triangle implements Shape {
    public static final boolean lineColorEnable = true;
    public static final boolean widthEnable = false;
    public static final boolean fillColorEnable = true;

    private Point point1;
    private Point point2;
    private Point point3;
    private Color lineColor;
    private Color fillColor;

    public Triangle() {
        this.point1 = null;
        this.point2 = null;
        this.point3 = null;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public Point getPoint3() {
        return point3;
    }

    public void setPoint3(Point point3) {
        this.point3 = point3;
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
        // Check if this triangle includes at least one line.
        if (point1 == null || point2 == null) {
            return;
        }

        // Check if this triangle includes the final point.
        if (point3 == null) {
            // Draw the base line.
            drawLine(pixelWriter, point1.getX(), point1.getY(), point2.getX(), point2.getY(), lineColor);
            return;
        }

        // Fill. (http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle)
        Canvas canvas = CanvasManager.getInstance().getCanvas();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                if (isPointInTriangle(new Point(x, y))) {
                    pixelWriter.setColor(x, y, fillColor);
                }
            }
        }

        // Draw the lines to the trird point.
        drawLine(pixelWriter, point1.getX(), point1.getY(), point2.getX(), point2.getY(), lineColor); // repeat this draw cause it may be overriden from fill.
        drawLine(pixelWriter, point1.getX(), point1.getY(), point3.getX(), point3.getY(), lineColor);
        drawLine(pixelWriter, point2.getX(), point2.getY(), point3.getX(), point3.getY(), lineColor);
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        // Check if this triangle includes at least one line.
        if (point1 == null || point2 == null) {
            return;
        }

        // Check if this triangle includes the final point.
        if (point3 == null) {
            // Draw the base line.
            drawLine(pixelWriter, point1.getX(), point1.getY(), point2.getX(), point2.getY(), backgroundColor);
            return;
        }

        // Fill. (http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle)
        Canvas canvas = CanvasManager.getInstance().getCanvas();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                if (isPointInTriangle(new Point(x, y))) {
                    pixelWriter.setColor(x, y, backgroundColor);
                }
            }
        }

        // Draw the lines to the trird point.
        drawLine(pixelWriter, point1.getX(), point1.getY(), point3.getX(), point3.getY(), backgroundColor);
        drawLine(pixelWriter, point2.getX(), point2.getY(), point3.getX(), point3.getY(), backgroundColor);
    }

    private boolean isPointInTriangle(Point pt) {
        // Found in http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle
        boolean b1, b2, b3;

        b1 = sign(pt, point1, point2) < 0.0f;
        b2 = sign(pt, point2, point3) < 0.0f;
        b3 = sign(pt, point3, point1) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }

    private float sign(Point pointA, Point pointB, Point pointC) {
        // Found in http://stackoverflow.com/questions/2049582/how-to-determine-a-point-in-a-triangle
        return (pointA.getX() - pointC.getX()) * (pointB.getY() - pointC.getY()) - (pointB.getX() - pointC.getX()) * (pointA.getY() - pointC.getY());
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
        final Triangle triangle = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Τριγώνου");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/TriangleEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference to this line.
            TriangleEditController controller = (TriangleEditController) loader.getController();
            controller.setTriangle(triangle);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getClassName() {
        return "Triangle";
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapeTriangle.png";
    }

    @Override
    public Color getShapeColor() {
        return this.fillColor;
    }

    @Override
    public String getShapeTitle() {
        return "Τρίγωνο";
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
