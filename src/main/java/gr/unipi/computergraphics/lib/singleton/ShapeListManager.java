package gr.unipi.computergraphics.lib.singleton;

import gr.unipi.computergraphics.lib.mainView.ShapeListItem;
import gr.unipi.computergraphics.model.shape.Shape;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Singleton that manages the shape list view.
 *
 * @author siggouroglou@gmail.com
 */
public final class ShapeListManager {

    private static ShapeListManager INSTANCE;
    private VBox shapeListContainer;
    private List<Shape> shapeList;

    private ShapeListManager() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    private ShapeListManager(VBox shapeListContainer) {
        this.shapeListContainer = shapeListContainer;
        this.shapeList = new ArrayList<>();
    }

    public static ShapeListManager initInstance(VBox shapeListContainer) {
        if (INSTANCE == null) {
            INSTANCE = new ShapeListManager(shapeListContainer);
        }
        return INSTANCE;
    }

    public static ShapeListManager getInstance() {
        return INSTANCE;
    }

    public void clear() {
        // Clear the list.
        shapeList.clear();

        // Clear the view.
        shapeListContainer.getChildren().clear();
    }

    public void add(Shape shape) {
        // Add to the list.
        shapeList.add(shape);

        // Add a new ShapeListItem into the view.
        ShapeListItem item = new ShapeListItem(shape);
        shapeListContainer.getChildren().add(item.getHBox());

        // Change the saved state. Create shape.
        IOManager.getInstance().savedProperty().set(false);
    }

    public void remove(Shape shape, HBox hbox) {
        // Remove from the list.
        shapeList.remove(shape);

        // Remove from view.
        shapeListContainer.getChildren().remove(hbox);
        
        // Redraw the canvas.
        CanvasManager.getInstance().refreshCanvas();

        // Change the saved state. Delete shape.
        IOManager.getInstance().savedProperty().set(false);
    }

    public void reDrawShape(Shape shape) {
        int index = shapeList.indexOf(shape);
        if (index < 0 || index >= shapeList.size()) {
            return;
        }

        // Remove the old hbox.
        shapeListContainer.getChildren().remove(index);

        // Add the new one.
        ShapeListItem item = new ShapeListItem(shape);
        HBox hBoxNew = item.getHBox();
        shapeListContainer.getChildren().add(index, hBoxNew);

        // Change the saved state. Edit shape.
        IOManager.getInstance().savedProperty().set(false);
    }

    public List<Shape> getShapeList() {
        return shapeList;
    }

    public void transerSourceUpToTarget(int sourceIndex, int targetIndex) {
        // Find the source node and shape.
        Node sourceNode = shapeListContainer.getChildren().get(sourceIndex);
        Shape sourceShape = shapeList.get(sourceIndex);

        // Remove the old source from hbox and shapeList.
        shapeListContainer.getChildren().remove(sourceIndex);
        shapeList.remove(sourceIndex);
        
        // The lists have size +1 now. Check if the sourceIndex is changed to +1.
        targetIndex = sourceIndex < targetIndex ? (targetIndex - 1) : targetIndex;

        // Add the source above of target.
        shapeListContainer.getChildren().add(targetIndex, sourceNode);
        shapeList.add(targetIndex, sourceShape);

        // Change the saved state. Edit shape.
        IOManager.getInstance().savedProperty().set(false);
    }

    public int indexOfHBox(HBox hBox) {
        if(hBox == null) {
            throw new IllegalArgumentException("Null argument");
        }
        
        return shapeListContainer.getChildren().indexOf(hBox);
    }

    void addAllShapes(List<Shape> shapeList) {
        if(shapeList == null) {
            throw new IllegalArgumentException("Shapelist must not be null");
        }
        
        // Append them to the current list them all.
        shapeList.stream().forEach((shape) -> {
            add(shape);
        });
    }
}
