package gr.unipi.computergraphics.model.shape;

import com.google.gson.Gson;
import gr.unipi.computergraphics.controller.shapeEdit.FreeHandEditController;
import gr.unipi.computergraphics.lib.mainView.ShapeListItemEditStrategy;
import gr.unipi.computergraphics.model.Point;
import gr.unipi.computergraphics.lib.singleton.CanvasManager;
import java.util.LinkedList;
import java.util.List;
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
public class FreeHand implements Shape {
    public static final boolean lineColorEnable = true;
    public static final boolean widthEnable = false;
    public static final boolean fillColorEnable = false;

    private List<Point> pointList;
    private Color lineColor;

    public FreeHand() {
        this.pointList = new LinkedList<>();
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

    @Override
    public void draw(PixelWriter pixelWriter) {
        if(pointList.size() > 1) {
            for(int i = 1; i < pointList.size(); i++) {
                drawLine(pixelWriter, pointList.get(i - 1).getX(), pointList.get(i - 1).getY(), pointList.get(i).getX(), pointList.get(i).getY(), lineColor);
            }
        }
    }

    @Override
    public void clear(PixelWriter pixelWriter, Color backgroundColor) {
        if(pointList.size() > 1) {
            for(int i = 1; i < pointList.size(); i++) {
                drawLine(pixelWriter, pointList.get(i - 1).getX(), pointList.get(i - 1).getY(), pointList.get(i).getX(), pointList.get(i).getY(), backgroundColor);
            }
        }
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
        final FreeHand freeHand = this;
        return () -> {
            // Stages and owners.
            Stage currentStage = (Stage) CanvasManager.getInstance().getCanvas().getScene().getWindow();
            Stage lineEditStage = new Stage();
            lineEditStage.initModality(Modality.WINDOW_MODAL);
            lineEditStage.initOwner(currentStage);
            lineEditStage.setTitle("Επεξεργασία Ελεύθερης Γραμμής");
            lineEditStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
            lineEditStage.setResizable(false);

            // Load the view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/shapeEdit/FreeHandEdit.fxml"));
            Parent root = (Parent) loader.load();
            lineEditStage.setScene(new Scene(root));

            // Set the contollerreference point2 this line.
            FreeHandEditController controller = (FreeHandEditController) loader.getController();
            controller.setFreeHand(freeHand);

            /// Show it.
            lineEditStage.show();
        };
    }

    @Override
    public String getClassName() {
        return "FreeHand";
    }

    @Override
    public String getImageFilePath() {
        return "/files/images/shapeFreeHand.png";
    }

    @Override
    public Color getShapeColor() {
        return this.lineColor;
    }

    @Override
    public String getShapeTitle() {
        return "Ελεύθερη Γραμμή";
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
    }
}
