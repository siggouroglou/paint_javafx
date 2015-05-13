package gr.papei.computergraphics.lib.mainView;

import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.ColorUtilities;
import gr.papei.computergraphics.lib.shape.model.Shape;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

/**
 *
 * An immutable class that wraps an HBox.
 *
 * @author siggouroglou@gmail.com
 */
public class ShapeListItem {

    private final HBox hBox;
    private final Shape shape;

    public ShapeListItem(Shape shape) {
        // Create the hbox.
        this.hBox = new HBox(10D);
        this.shape = shape;
        hBox.alignmentProperty().set(Pos.CENTER_LEFT);
        hBox.getStyleClass().add("shapeListItem");

        // Create the nodes inseide the hbox.
        createNodes();

        // Create the context menu.
        createContextMenu();

        // Create the drag and drop functionality.
        createDragAndDrop();
    }

    private void createNodes() {
        // Add the image.
        Image image = new Image(getClass().getResourceAsStream(shape.getImageFilePath()));
        ImageView imageView = new ImageView(image);
        hBox.getChildren().add(imageView);

        // Add the text.
        Label label = new Label(shape.getShapeTitle());
        label.setStyle("-fx-text-fill: " + ColorUtilities.colorToWeb(shape.getShapeColor()));
        hBox.getChildren().add(label);
    }

    private void createContextMenu() {
        // Create the context menu.
        final ContextMenu contextMenu = new ContextMenu();

        // Menu Edit.
        MenuItem menuItem1 = new MenuItem("Επεξεργασία");
        menuItem1.setOnAction((ActionEvent e) -> {
            try {
                shape.getEditStrategy().createContextEdit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        contextMenu.getItems().add(menuItem1);

        // Menu Delete.
        MenuItem menuItem2 = new MenuItem("Διαγραφή");
        menuItem2.setOnAction((ActionEvent e) -> {
            ShapeListManager.getInstance().remove(shape, hBox);
        });
        contextMenu.getItems().add(menuItem2);

        // Open context menu on secondary mouse button click.
        hBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(hBox, Side.BOTTOM, 0, 0);
            }
        });
    }

    public HBox getHBox() {
        return this.hBox;
    }

    private void createDragAndDrop() {
        // On drag detect. This hbox start the dragging.
        hBox.setOnDragDetected((MouseEvent event) -> {
            // Allow any transfer mode.
            Dragboard db = hBox.startDragAndDrop(TransferMode.ANY);

            // Put a string on dragboard.
            ClipboardContent content = new ClipboardContent();
            content.putString("not-in-use-string");
            db.setContent(content);

            event.consume();
        });

        // On drag over. This hbox receives a drag that happened to another.
        hBox.setOnDragOver((DragEvent event) -> {
            // Accept it only if it is not dragged from the same node and if it has a string data.
            if (event.getGestureSource() != hBox && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        // Drag enter the hbox. This hbox receives a drag that happened to another.
        hBox.setOnDragEntered((DragEvent event) -> {
            // Show to the user that it is an actual gesture target.
            if (event.getGestureSource() != hBox && event.getDragboard().hasString()) {
                hBox.setStyle("-fx-border-color: #A0A0A0;");
                hBox.setStyle("-fx-border-width:  5 5 5 5;");
            }

            event.consume();
        });

        // Drag exit the hbox. This hbox receives a drag that happened to another.
        hBox.setOnDragExited((DragEvent event) -> {
            // Show to the user that it is an actual gesture target.
            if (event.getGestureSource() != hBox && event.getDragboard().hasString()) {
                hBox.setStyle("-fx-border-color: #D7D7D7;");
                hBox.setStyle("-fx-border-width:  1 1 1 1;");
            }

            event.consume();
        });

        // Drag drop inside the hbox. This hbox receives a drag that happened to another.
        hBox.setOnDragDropped((DragEvent event) -> {
            // Get the source hbox.
            HBox sourceHBox = (HBox) event.getGestureSource();
            if (sourceHBox != null) {
                int sourceIndex = ShapeListManager.getInstance().indexOfHBox(sourceHBox);
                int targetIndex = ShapeListManager.getInstance().indexOfHBox(hBox);

                // Make the transfering.
                ShapeListManager.getInstance().transerSourceUpToTarget(sourceIndex, targetIndex);

                // Refresh shapes.
                CanvasManager.getInstance().refreshShapes();

                event.setDropCompleted(true);
            }
        });

//        // Drag completed. This hbox drag completed.
//        hbox.setOnDragDone((DragEvent event) -> {
//            // The drag-and-drop gesture ended.
//            System.out.println("onDragDone");
//            
//            event.consume();
//        });
    }
}