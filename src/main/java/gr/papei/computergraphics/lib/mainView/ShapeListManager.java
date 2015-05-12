package gr.papei.computergraphics.lib.mainView;

import gr.papei.computergraphics.lib.shape.model.Shape;
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
    }

    public void remove(Shape shape, HBox hbox) {
        // Remove from the list.
        shapeList.remove(shape);

        // Remove from view.
        shapeListContainer.getChildren().remove(hbox);
    }

    public void reDraw(Shape shape) {
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
    }

    public Iterable<Shape> getShapeList() {
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
    }

    public int indexOfHBox(HBox hBox) {
        if(hBox == null) {
            throw new IllegalArgumentException("Null argument");
        }
        
        return shapeListContainer.getChildren().indexOf(hBox);
    }
}
