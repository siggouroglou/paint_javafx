package gr.unipi.computergraphics.lib.shape.model;

import com.google.gson.Gson;
import gr.unipi.computergraphics.controller.shapeEdit.PolygonEditController;
import gr.unipi.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.unipi.computergraphics.lib.shape.Point;
import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class Polygon implements Shape {
    public static final boolean lineColorEnable = true;
    public static final boolean widthEnable = true;
    public static final boolean fillColorEnable = true;

    private Point point1;
    private Point point2;
    private Point point3;
    private List<Point> pointList;
    private Color lineColor;
    private Color fillColor;
    private int width;

    public Polygon() {
        this.point1 = null;
        this.point2 = null;
        this.point3 = null;
        this.pointList = new LinkedList<>();
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

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
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
        // Check if this polygon is completed.
        if (!pointList.isEmpty() && pointList.get(pointList.size() - 1).equals(point1)) {
            // Fill the shape. (http://stackoverflow.com/questions/11716268/point-in-polygon-algorithm)
            List<Point> polygonPointList = new ArrayList<>(3 + pointList.size());
            polygonPointList.add(point1);
            polygonPointList.add(point2);
            polygonPointList.add(point3);
            polygonPointList.addAll(pointList);

            Canvas canvas = CanvasManager.getInstance().getCanvas();
            for (int x = 0; x < canvas.getWidth(); x++) {
                for (int y = 0; y < canvas.getHeight(); y++) {
                    if (isPointInPolygon(new Point(x, y), polygonPointList)) {
                        pixelWriter.setColor(x, y, fillColor);
                    }
                }
            }
        }

        // Only one line is designed yet.
        if (point3 == null) {
            drawLine(pixelWriter, point1, point2, lineColor);
        }

        // Only two lines are designed yet.
        if (point3 != null && pointList.isEmpty()) {
            drawLine(pixelWriter, point1, point2, lineColor);
            drawLine(pixelWriter, point2, point3, lineColor);
        }

        // More lines are designed.
        if (!pointList.isEmpty()) {
            drawLine(pixelWriter, point1, point2, lineColor);
            drawLine(pixelWriter, point2, point3, lineColor);

            drawLine(pixelWriter, point3, pointList.get(0), lineColor);
            for (int i = 1; i < pointList.size(); i++) {
                drawLine(pixelWriter, pointList.get(i - 1), pointList.get(i), lineColor);
            }
        }
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        // Check if this polygon is completed.
        if (!pointList.isEmpty() && pointList.get(pointList.size() - 1).equals(point1)) {
            // Fill the shape. (http://stackoverflow.com/questions/11716268/point-in-polygon-algorithm)
            List<Point> polygonPointList = new ArrayList<>(3 + pointList.size());
            polygonPointList.add(point1);
            polygonPointList.add(point2);
            polygonPointList.add(point3);
            polygonPointList.addAll(pointList);

            Canvas canvas = CanvasManager.getInstance().getCanvas();
            for (int x = 0; x < canvas.getWidth(); x++) {
                for (int y = 0; y < canvas.getHeight(); y++) {
                    if (isPointInPolygon(new Point(x, y), polygonPointList)) {
                        pixelWriter.setColor(x, y, backgroundColor);
                    }
                }
            }
        }

        // Only one line is designed yet.
        if (point3 == null) {
            drawLine(pixelWriter, point1, point2, backgroundColor);
        }

        // Only two lines are designed yet.
        if (point3 != null && pointList.isEmpty()) {
            drawLine(pixelWriter, point1, point2, backgroundColor);
            drawLine(pixelWriter, point2, point3, backgroundColor);
        }

        // More lines are designed.
        if (!pointList.isEmpty()) {
            drawLine(pixelWriter, point1, point2, backgroundColor);
            drawLine(pixelWriter, point2, point3, backgroundColor);

            drawLine(pixelWriter, point3, pointList.get(0), backgroundColor);
            for (int i = 1; i < pointList.size(); i++) {
                drawLine(pixelWriter, pointList.get(i - 1), pointList.get(i), backgroundColor);
            }
        }
    }

    private boolean isPointInPolygon(Point point, List<Point> polygonPointList) {
        int i, j, nvert = polygonPointList.size();
        boolean c = false;

        for (i = 0, j = nvert - 1; i < nvert; j = i++) {
            Point polygonPointI = polygonPointList.get(i);
            Point polygonPointJ = polygonPointList.get(j);

            if (((polygonPointI.getY() >= point.getY()) != (polygonPointJ.getY() >= point.getY()))
                    && (point.getX() <= (polygonPointJ.getX() - polygonPointI.getX()) * (point.getY() - polygonPointI.getY()) / (polygonPointJ.getY() - polygonPointI.getY()) + polygonPointI.getX())) {
                c = !c;
            }

//            if (((points[i].y >= point.y) != (points[j].y >= point.y))
//                    && (point.x <= (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
//                c = !c;
//            }
        }

        return c;
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
        final Polygon polygon = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Πολυγώνου");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/PolygonEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference point2 this line.
            PolygonEditController controller = (PolygonEditController) loader.getController();
            controller.setPolygon(polygon);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getClassName() {
        return "Polygon";
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapePolygon.png";
    }

    @Override
    public Color getShapeColor() {
        return this.fillColor;
    }

    @Override
    public String getShapeTitle() {
        return "Πολύγωνο";
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
