package gr.papei.computergraphics.lib.mainView;

import gr.papei.computergraphics.lib.shape.model.Shape;
import java.util.Stack;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Singleton that manages the shape stack view.
 *
 * @author siggouroglou@gmail.com
 */
public final class ShapeStackManager {

    private static ShapeStackManager INSTANCE;
    private VBox shapeStackContainer;
    private Stack<Shape> shapeStack;

    private ShapeStackManager() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    private ShapeStackManager(VBox shapeStackContainer) {
        this.shapeStackContainer = shapeStackContainer;
        this.shapeStack = new Stack<>();
    }

    public static ShapeStackManager initInstance(VBox shapeStackContainer) {
        if (INSTANCE == null) {
            INSTANCE = new ShapeStackManager(shapeStackContainer);
        }
        return INSTANCE;
    }

    public static ShapeStackManager getInstance() {
        return INSTANCE;
    }

    public void clear() {
        // Clear the stack.
        shapeStack.clear();
        
        // Clear the view.
        shapeStackContainer.getChildren().clear();
    }

    public void add(Shape shape) {
        // Add to the stack.
        shapeStack.push(shape);
        
        // Add to view.
        HBox hBox = shape.getView();
        shapeStackContainer.getChildren().add(hBox);
    }

    public Iterable<Shape> getStack() {
        return shapeStack;
    }
}
