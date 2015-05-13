package gr.papei.computergraphics.lib.singleton;

import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiator;
import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiatorState;
import gr.papei.computergraphics.lib.shape.model.Shape;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * A singleton that manages the canvas.
 *
 * @author siggouroglou@gmail.com
 */
public final class CanvasManager {

    private static CanvasManager INSTANCE;

    private final ScrollPane parent;
    private final Label coordinatesLabel;
    private final BooleanProperty drawingEnable;
    private final BooleanProperty canvasInitialized;
    private Canvas canvas;
    private ShapeInitiator shapeInitiator;

    private CanvasManager() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public CanvasManager(ScrollPane parent, Label coordinatesLabel) {
        this.parent = parent;
        this.coordinatesLabel = coordinatesLabel;
        this.drawingEnable = new SimpleBooleanProperty(false);
        this.canvasInitialized = new SimpleBooleanProperty(false);
        this.canvas = null;
        this.shapeInitiator = null;
    }

    public static CanvasManager initInstance(ScrollPane parent, Label coordinatesLabel) {
        if (INSTANCE == null) {
            INSTANCE = new CanvasManager(parent, coordinatesLabel);
        }
        return INSTANCE;
    }

    public static CanvasManager getInstance() {
        return INSTANCE;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public BooleanProperty drawingEnableProperty() {
        return drawingEnable;
    }

    public BooleanProperty canvasInitializedProperty() {
        return canvasInitialized;
    }

    public void resetCanvas(int width, int height) {
        // Initialize canvas.
        canvas = new Canvas(width, height);
        canvas.setCursor(Cursor.CROSSHAIR);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        // Enable canvasInitialized property and set saved property to false.
        canvasInitialized.set(true);
        IOUtilities.savedProperty().set(false);

        // Set background color.
        Color color = Color.valueOf(Settings.getInstance().getBackgroundColor());
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Mouse mouve.
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent mouseEvent) -> {
            // Write the correct coordinates.
            coordinatesLabel.setText("P: " + mouseEvent.getX() + " : " + mouseEvent.getY());
        });

        // Mouse click event.
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            if (drawingEnable.get() && mouseEvent.isPrimaryButtonDown() && shapeInitiator != null) {
                // Clear shapes from canvas.
                clearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventPressed(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                IOUtilities.savedProperty().set(false);
            }
        });

        // Mouse drag event.
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
            // Write the correct coordinates.
            coordinatesLabel.setText("P: " + mouseEvent.getX() + " : " + mouseEvent.getY());

            if (drawingEnable.get() && mouseEvent.isPrimaryButtonDown() && shapeInitiator != null) {
                // Clear shapes from canvas.
                clearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventDragged(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                IOUtilities.savedProperty().set(false);
            }
        });

        // Mouse 
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent mouseEvent) -> {
            if (drawingEnable.get() && shapeInitiator != null) {
                // Clear shapes from canvas.
                clearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventReleased(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                IOUtilities.savedProperty().set(false);
            }
        });

        // Clear parent and add the new created canvas.
        parent.setContent(canvas);
    }

    public void startDrawing(ShapeInitiator shapeInitiator) {
        this.shapeInitiator = shapeInitiator;
        this.shapeInitiator.initialize();
    }

    private void clearAllShapes() {
        if (canvas == null) {
            return;
        }
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        for (Shape shape : ShapeListManager.getInstance().getShapeList()) {
            shape.clear(pixelWriter, Color.valueOf(Settings.getInstance().getBackgroundColor()));
        }
    }

    private void drawAllShapes() {
        if (canvas == null) {
            return;
        }
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        for (Shape shape : ShapeListManager.getInstance().getShapeList()) {
            shape.draw(pixelWriter);
        }
    }

    public void refreshShapes() {
        clearAllShapes();
        drawAllShapes();
    }

    public void refreshCanvas() {
        if (canvas == null) {
            return;
        }

        // Clear everything from vanvas and fill with background color.
        Color color = Color.valueOf(Settings.getInstance().getBackgroundColor());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw all the shapes.
        drawAllShapes();
    }

    public void removeCanvas() {
        // Remove the canvas.
        parent.setContent(null);
        canvas = null;
        canvasInitialized.set(false);
        
        // Reset the ShapeList.
        ShapeListManager.getInstance().clear();
    }

    public void changeBackgroundColor(Color color) {
        refreshCanvas();

        // Change the saved state.
        IOUtilities.savedProperty().set(false);
    }
}
