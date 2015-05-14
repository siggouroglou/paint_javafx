package gr.unipi.computergraphics.lib.shape.model;

import com.google.gson.Gson;
import gr.unipi.computergraphics.controller.shapeEdit.RectEditController;
import gr.unipi.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.unipi.computergraphics.lib.shape.Point;
import gr.unipi.computergraphics.lib.singleton.CanvasManager;
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
public class Rect implements Shape {

    private Point from;
    private Point to;
    private Color lineColor;
    private Color fillColor;
    private int width;

    public Rect() {
        this.from = null;
        this.to = null;
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

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void draw(PixelWriter pixelWriter) {
        // Get the other two points.
        Point point12 = new Point(to.getX(), from.getY());
        Point point21 = new Point(from.getX(), to.getY());

        // Fill the square.
        int difference = from.getY() - to.getY();
        boolean isGoingUp = difference < 0;
        for (int y = (isGoingUp ? from.getY() : to.getY()); y < (isGoingUp ? to.getY() : from.getY()); y++) {
            drawLine(pixelWriter, new Point(from.getX(), y), new Point(to.getX(), y), fillColor);
        }
        
        // Draw the square lines.
        drawLine(pixelWriter, from, point12, lineColor);
        drawLine(pixelWriter, point12, to, lineColor);
        drawLine(pixelWriter, to, point21, lineColor);
        drawLine(pixelWriter, point21, from, lineColor);
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        // Get the other two points.
        Point point12 = new Point(to.getX(), from.getY());
        Point point21 = new Point(from.getX(), to.getY());

        // Fill the square.
        int difference = from.getY() - to.getY();
        boolean isGoingUp = difference < 0;
        for (int y = (isGoingUp ? from.getY() : to.getY()); y < (isGoingUp ? to.getY() : from.getY()); y++) {
            drawLine(pixelWriter, new Point(from.getX(), y), new Point(to.getX(), y), backgroundColor);
        }

        // Draw the square lines.
        drawLine(pixelWriter, from, point12, backgroundColor);
        drawLine(pixelWriter, point12, to, backgroundColor);
        drawLine(pixelWriter, to, point21, backgroundColor);
        drawLine(pixelWriter, point21, from, backgroundColor);
    }

    public void drawLine(PixelWriter pixelWriter, Point p1, Point p2, Color color) {
        // Set the correct points.
        int x0 = p1.getX();
        int y0 = p1.getY();
        int x1 = p2.getX();
        int y1 = p2.getY();
        float wd = (float) width;

        // Run the Bresenham algorithm. (http://members.chello.at/~easyfilter/bresenham.html)
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int err = dx - dy, e2, x2, y2;
        float ed = (float) (dx + dy == 0 ? 1 : Math.sqrt((float) dx * dx + (float) dy * dy));

        for (wd = (wd + 1) / 2;;) {
            pixelWriter.setColor(x0, y0, color);
            e2 = err;
            x2 = x0;
            if (2 * e2 >= -dx) {
                for (e2 += dy, y2 = y0; e2 < ed * wd && (y1 != y2 || dx > dy); e2 += dx) {
                    y2 += sy;
                    pixelWriter.setColor(x0, y2, color);
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
                    pixelWriter.setColor(x2, y0, color);
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
        final Rect rect = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Ορθογωνίου");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/RectEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference to this line.
            RectEditController controller = (RectEditController) loader.getController();
            controller.setRect(rect);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getClassName() {
        return "Rect";
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapeRect.png";
    }

    @Override
    public Color getShapeColor() {
        return this.fillColor;
    }

    @Override
    public String getShapeTitle() {
        return "Ορθογώνιο";
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

