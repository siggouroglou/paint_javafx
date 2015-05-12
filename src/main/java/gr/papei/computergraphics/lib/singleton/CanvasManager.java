package gr.papei.computergraphics.lib.singleton;

import gr.papei.computergraphics.lib.ColorUtilities;
import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiator;
import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiatorState;
import gr.papei.computergraphics.lib.shape.model.Shape;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * A singleton that manages the canvas.
 *
 * @author siggouroglou@gmail.com
 */
public final class CanvasManager {

    private static CanvasManager INSTANCE;

    private final StackPane parent;
    private final Label coordinatesLabel;
    private final BooleanProperty drawingEnable;
    private final BooleanProperty saved;
    private Canvas canvas;
    private ShapeInitiator shapeInitiator;

    private CanvasManager() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public CanvasManager(StackPane parent, Label coordinatesLabel) {
        this.parent = parent;
        this.coordinatesLabel = coordinatesLabel;
        this.drawingEnable = new SimpleBooleanProperty(false);
        this.saved = new SimpleBooleanProperty(false);
        this.canvas = null;
        this.shapeInitiator = null;
    }

    public static CanvasManager initInstance(StackPane parent, Label coordinatesLabel) {
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

    public BooleanProperty savedProperty() {
        return saved;
    }

    public void resetCanvas(int width, int height) {
        canvas = new Canvas(width, height);
        canvas.setCursor(Cursor.CROSSHAIR);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        // Mouse mouve.
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent mouseEvent) -> {
            // Write the correct coordinates.
            coordinatesLabel.setText("P: " + mouseEvent.getX() + " : " + mouseEvent.getY());
        });

        // Mouse click event.
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            if (drawingEnable.get() && mouseEvent.isPrimaryButtonDown() && shapeInitiator != null) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventPressed(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                saved.set(false);
            }
        });

        // Mouse drag event.
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
            // Write the correct coordinates.
            coordinatesLabel.setText("P: " + mouseEvent.getX() + " : " + mouseEvent.getY());

            if (drawingEnable.get() && mouseEvent.isPrimaryButtonDown() && shapeInitiator != null) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventDragged(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                saved.set(false);
            }
        });

        // Mouse 
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent mouseEvent) -> {
            if (drawingEnable.get() && shapeInitiator != null) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventReleased(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    shapeInitiator.initialize();
                }

                // Draw shapes to canvas.
                drawAllShapes();

                // Change the saved state.
                saved.set(false);
            }
        });

        // Clear parent and add the new created canvas.
        parent.getChildren().clear();
        parent.getChildren().add(canvas);
    }

    public void startDrawing(ShapeInitiator shapeInitiator) {
        this.shapeInitiator = shapeInitiator;
        this.shapeInitiator.initialize();
    }

    private void crearAllShapes() {
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
        crearAllShapes();
        drawAllShapes();
    }

    public void refreshCanvas() {
        // Clear everything from vanvas
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawAllShapes();
    }

    public void changeBackgroundColor(Color color) {
        refreshCanvas();

        // Draw the new background.
        canvas.getParent().setStyle("-fx-background-color: " + ColorUtilities.colorToWeb(color));

        // Change the saved state.
        saved.set(false);
    }
}
