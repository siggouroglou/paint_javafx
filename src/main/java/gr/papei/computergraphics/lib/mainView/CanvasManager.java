package gr.papei.computergraphics.lib.mainView;

import gr.papei.computergraphics.lib.canvas.CanvasState;
import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiator;
import gr.papei.computergraphics.lib.shape.initiator.ShapeInitiatorState;
import gr.papei.computergraphics.lib.shape.model.Shape;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    private StackPane parent;
    private CanvasState state;
    private Canvas canvas;
    private ShapeInitiator shapeInitiator;

    private CanvasManager() {
        throw new UnsupportedOperationException("Empty constructor is not supported.");
    }

    public CanvasManager(StackPane parent) {
        this.parent = parent;
        this.state = CanvasState.NO_DRAWING;
        this.canvas = null;
        this.shapeInitiator = null;
    }

    public static CanvasManager initInstance(StackPane parent) {
        if (INSTANCE == null) {
            INSTANCE = new CanvasManager(parent);
        }
        return INSTANCE;
    }

    public static CanvasManager getInstance() {
        return INSTANCE;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void resetCanvas(int width, int height) {
        canvas = new Canvas(width, height);
        canvas.setCursor(Cursor.CROSSHAIR);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        // Mouse click event.
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            if (state == CanvasState.UNDER_DRAWING) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventPressed(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    state = CanvasState.NO_DRAWING;
                    shapeInitiator = null;
                }

                // Draw shapes to canvas.
                drawAllShapes();
            }
        });

        // Mouse drag event.
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
            if (state == CanvasState.UNDER_DRAWING) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventDragged(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    state = CanvasState.NO_DRAWING;
                    shapeInitiator = null;
                }

                // Draw shapes to canvas.
                drawAllShapes();
            }
        });

        // Mouse 
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent mouseEvent) -> {
            if (state == CanvasState.UNDER_DRAWING) {
                // Clear shapes from canvas.
                crearAllShapes();

                // Draw current shape.
                ShapeInitiatorState shapeInitiatorState = shapeInitiator.eventReleased(pixelWriter, mouseEvent);
                if (shapeInitiatorState == ShapeInitiatorState.COMPLETED) {
                    state = CanvasState.NO_DRAWING;
                    shapeInitiator = null;
                }

                // Draw shapes to canvas.
                drawAllShapes();
            }
        });
        
        // Clear parent and add the new created canvas.
        parent.getChildren().clear();
        parent.getChildren().add(canvas);
    }

    public void startDrawing(ShapeInitiator shapeInitiator) {
        this.shapeInitiator = shapeInitiator;
        this.state = CanvasState.UNDER_DRAWING;
    }

    private void crearAllShapes() {
        if (canvas == null) {
            return;
        }
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        for (Shape shape : ShapeStackManager.getInstance().getStack()) {
            shape.clear(pixelWriter, Color.valueOf(Settings.getInstance().getFontColor()));
        }
    }

    private void drawAllShapes() {
        if (canvas == null) {
            return;
        }
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = gc.getPixelWriter();

        for (Shape shape : ShapeStackManager.getInstance().getStack()) {
            shape.draw(pixelWriter);
        }
    }

    public void refreshShapes() {
        crearAllShapes();
        drawAllShapes();
    }

    public void changeBackgroundColor(Color color) {
        // Get the new background colors.
        int red = (int) Math.round(color.getRed() * 255.0);
        int green = (int) Math.round(color.getGreen() * 255.0);
        int blue = (int) Math.round(color.getBlue() * 255.0);
        int opacity = (int) Math.round(color.getOpacity() * 255.0);

        // Clear everything from vanvas
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw the new background.
        canvas.getParent().setStyle("-fx-background-color: rgb(" + red + "," + green + "," + blue + "," + opacity + ")");

        // Draw shapes.
        drawAllShapes();
    }
}
