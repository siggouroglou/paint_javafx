package gr.papei.computergraphics.controller;

import gr.papei.computergraphics.controller.help.AboutController;
import gr.papei.computergraphics.lib.shape.initiator.CircleInitiator;
import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.singleton.Settings;
import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.shape.initiator.LineInitiator;
import gr.papei.computergraphics.lib.shape.initiator.TriangleInitiator;
import gr.papei.computergraphics.lib.singleton.IOUtilities;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Dialogs;

public class MainController implements Initializable {

    private Stage stage;
    
    @FXML
    private MenuItem fileSaveAsMenu;
    @FXML
    private MenuItem fileExportMenu;
    @FXML
    private MenuItem fileImportMenu;
    @FXML
    private MenuItem fileSaveMenu;
    @FXML
    private MenuItem fileCloseMenu;
    @FXML
    private Menu editDesignMenu;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox shapeListContainer;
    @FXML
    private Label coordinatesLabel;
    @FXML
    private Label footerLabel;
    @FXML
    private Label savedNoLabel;
    @FXML
    private Label savedYesLabel;

    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private GridPane shapeProperiesGridPane;
    @FXML
    private ColorPicker shapePropertyColor;
    @FXML
    private Slider shapePropertyWidth;
    @FXML
    private ColorPicker shapePropertyFill;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert fileSaveAsMenu != null : "fx:id=\"fileSaveAsMenu\" was not injected: check your FXML file 'Main.fxml'.";
        assert fileExportMenu != null : "fx:id=\"fileExportMenu\" was not injected: check your FXML file 'Main.fxml'.";
        assert fileImportMenu != null : "fx:id=\"fileImportMenu\" was not injected: check your FXML file 'Main.fxml'.";
        assert fileSaveMenu != null : "fx:id=\"fileSaveMenu\" was not injected: check your FXML file 'Main.fxml'.";
        assert fileCloseMenu != null : "fx:id=\"fileCloseMenu\" was not injected: check your FXML file 'Main.fxml'.";
        assert editDesignMenu != null : "fx:id=\"editDesignMenu\" was not injected: check your FXML file 'Main.fxml'.";
        
        assert shapeListContainer != null : "fx:id=\"shapeStackContainer\" was not injected: check your FXML file 'Main.fxml'.";
        assert coordinatesLabel != null : "fx:id=\"coordinatesLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert footerLabel != null : "fx:id=\"footerLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert savedNoLabel != null : "fx:id=\"savedNoLabel\" was not injected: check your FXML file 'Main.fxml'.";
        assert savedYesLabel != null : "fx:id=\"savedYesLabel\" was not injected: check your FXML file 'Main.fxml'.";

        assert shapeProperiesGridPane != null : "fx:id=\"shapeProperiesGridPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert shapePropertyColor != null : "fx:id=\"shapePropertyColor\" was not injected: check your FXML file 'Main.fxml'.";
        assert shapePropertyWidth != null : "fx:id=\"shapePropertyWidth\" was not injected: check your FXML file 'Main.fxml'.";
        assert shapePropertyFill != null : "fx:id=\"shapePropertyFill\" was not injected: check your FXML file 'Main.fxml'.";

        // Initialize CanvasManager and ShapeListManager.
        CanvasManager.initInstance(scrollPane, coordinatesLabel);
        ShapeListManager.initInstance(shapeListContainer);

        // Load ShapeProperties from settings.
        ShapeProperties.getInstance().colorProperty().set(Color.valueOf(Settings.getInstance().getDesignColor()));
        ShapeProperties.getInstance().widthProperty().set(Settings.getInstance().getDesignWidth());

        // Bind shape properties to shape toogle buttons.
        shapeProperiesGridPane.visibleProperty().bind(Bindings.isNotNull(toggleGroup.selectedToggleProperty())
                                            .and(CanvasManager.getInstance().canvasInitializedProperty()));
        shapePropertyColor.valueProperty().bindBidirectional(ShapeProperties.getInstance().colorProperty());
        shapePropertyWidth.valueProperty().bindBidirectional(ShapeProperties.getInstance().widthProperty());
        shapePropertyFill.valueProperty().bindBidirectional(ShapeProperties.getInstance().fillProperty());

        // Bind CanvasManager drawingEnableProperty property.
        CanvasManager.getInstance().drawingEnableProperty().bind(Bindings.isNotNull(toggleGroup.selectedToggleProperty()));

        // Bind savadNo and savedYes properties to canvas.
        savedYesLabel.visibleProperty().bind(Bindings.and(CanvasManager.getInstance().canvasInitializedProperty(), IOUtilities.savedProperty()));
        savedNoLabel.visibleProperty().bind(Bindings.and(CanvasManager.getInstance().canvasInitializedProperty(), IOUtilities.savedProperty().isEqualTo(new SimpleBooleanProperty(false))));

        // Toggle buttons enable property bind to canvas initialization property.
        for(Toggle toggle : toggleGroup.getToggles()) {
            ToggleButton button = (ToggleButton) toggle;
            button.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        }
        
        // Disable menu items when canvas is not initialized.
        fileSaveAsMenu.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        fileExportMenu.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        fileImportMenu.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        fileSaveMenu.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        fileCloseMenu.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        for(MenuItem menuItem : editDesignMenu.getItems()) {
            menuItem.disableProperty().bind(CanvasManager.getInstance().canvasInitializedProperty().isEqualTo(new SimpleBooleanProperty(false)));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        // Register the event once.
        if (getStage().getOnCloseRequest() == null) {
            // Event when close window is clicked.
            getStage().setOnCloseRequest((WindowEvent we) -> {
                if (!IOUtilities.savedProperty().get() && CanvasManager.getInstance().canvasInitializedProperty().get()) {
                    IOUtilities.questionForSave(getStage());
                }
            });
        }
    }

    private Stage getStage() {
        return this.stage;
    }

    //<editor-fold defaultstate="collapsed" desc="Palet Bar">
    @FXML
    void shapeLineClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new LineInitiator());
    }

    @FXML
    void shapeCircleClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new CircleInitiator());
    }

    @FXML
    void shapeTriangleAction(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new TriangleInitiator());
    }

    @FXML
    void shapeSquareClick(ActionEvent event) {

    }

    @FXML
    void shapeCrookedLineClick(ActionEvent event) {

    }

    @FXML
    void shapePolygonClick(ActionEvent event) {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Menu Bar">
    //<editor-fold defaultstate="collapsed" desc="File Menu">
    @FXML
    void fileNewClick(ActionEvent event) throws IOException {
        // Check if there is unsaved changes.
        if (!IOUtilities.savedProperty().get() && CanvasManager.getInstance().canvasInitializedProperty().get()) {
            IOUtilities.questionForSave(getStage());
        }

        // Stages and owners.
        Stage currentStage = getStage();
        Stage fileNewStage = new Stage();
        fileNewStage.initModality(Modality.WINDOW_MODAL);
        fileNewStage.initOwner(currentStage);
        fileNewStage.setTitle("Δημιουργία Νεου Καμβα");
        fileNewStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
        fileNewStage.setResizable(false);

        // Load the view.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/file/FileNew.fxml"));
        Parent root = (Parent) loader.load();
        fileNewStage.setScene(new Scene(root));

        // Show it.
        fileNewStage.show();
    }

    @FXML
    void fileSaveClick(ActionEvent event) {
        if (!IOUtilities.savedProperty().get() && CanvasManager.getInstance().canvasInitializedProperty().get()) {
            IOUtilities.save(getStage());
            IOUtilities.savedProperty().set(true);
        }
    }

    @FXML
    void fileSaveAsClick(ActionEvent event) {
        if (CanvasManager.getInstance().canvasInitializedProperty().get()) {
            IOUtilities.saveAs(getStage());
            IOUtilities.savedProperty().set(true);
        }
    }

    @FXML
    void fileCloseClick(ActionEvent event) {
        if (CanvasManager.getInstance().canvasInitializedProperty().get()) {
            if (!IOUtilities.savedProperty().get()) {
                IOUtilities.questionForSave(getStage());
            }
            CanvasManager.getInstance().removeCanvas();
        }
    }

    @FXML
    void fileExportClick(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Επιλογή αρχείου για εξαγωγή σχημάτων");
        File file = choose.showOpenDialog(getStage());
        if (file == null || !file.isFile()) {
            try {
                FileUtils.touch(file);
            } catch (IOException ex) {
                Dialogs.create()
                        .owner(getStage())
                        .title("Πρόβλημα")
                        .masthead("Το αρχείο δεν βρέθηκε!")
                        .message("Το αρχείο που επιλέξατε δεν υπάρχει και δεν μπορεί να δημιουργηθεί. Παρακαλώ επαναλάβετε τη διαδικασία επιλογής του.")
                        .showWarning();
                return;
            }
        }

        IOUtilities.exportCanvas(file);
    }

    @FXML
    void fileImportClick(ActionEvent event) {
        // Check if the current canvas is saved.
        if (!IOUtilities.savedProperty().get() && CanvasManager.getInstance().canvasInitializedProperty().get()) {
            IOUtilities.questionForSave(getStage());
        }

        FileChooser choose = new FileChooser();
        choose.setTitle("Επιλογή αρχείου για εισαγωγή σχημάτων");
        File file = choose.showOpenDialog(getStage());
        if (file == null || !file.isFile()) {
            Dialogs.create()
                    .owner(getStage())
                    .title("Πρόβλημα")
                    .masthead("Το αρχείο δεν βρέθηκε!")
                    .message("Το αρχείο που επιλέξατε δεν υπάρχει. Παρακαλώ επαναλάβετε τη διαδικασία επιλογής του.")
                    .showWarning();
        }

        IOUtilities.importShapes(file);
    }

    @FXML
    void fileQuitClick(ActionEvent event) {
        // The event is not working when i close the stage directly. Thus i implement this source twice.
        if (!IOUtilities.savedProperty().get() && CanvasManager.getInstance().canvasInitializedProperty().get()) {
            IOUtilities.questionForSave(getStage());
        }

        // Close the stage.
        getStage().close();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Edit Menu">
    @FXML
    void editShapeLineClick(ActionEvent event) {
        shapeLineClick(null);
    }

    @FXML
    void editShapeCicleClick(ActionEvent event) {
        shapeCircleClick(null);
    }

    @FXML
    void editShapeTriangleClick(ActionEvent event) {
        shapeTriangleAction(null);
    }

    @FXML
    void editShapeSquareClick(ActionEvent event) {
        shapeSquareClick(null);
    }

    @FXML
    void editShapeCrookedLineClick(ActionEvent event) {
        shapeCrookedLineClick(null);
    }

    @FXML
    void editShapePolygonClick(ActionEvent event) {
        shapePolygonClick(null);
    }

    @FXML
    void editSettingsClick(ActionEvent event) throws IOException {
        // Stages and owners.
        Stage currentStage = getStage();
        Stage editSettingsStage = new Stage();
        editSettingsStage.initModality(Modality.WINDOW_MODAL);
        editSettingsStage.initOwner(currentStage);
        editSettingsStage.setTitle("Ρυθμίσεις");
        editSettingsStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
        editSettingsStage.setResizable(false);

        // Load the view.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/edit/EditSettings.fxml"));
        Parent root = (Parent) loader.load();
        editSettingsStage.setScene(new Scene(root));

        /// Show it.
        editSettingsStage.show();
    }
    //</editor-fold>

    @FXML
    void helpAboutClick(ActionEvent event) throws IOException {
        Stage aboutStage = new Stage();
        aboutStage.initModality(Modality.WINDOW_MODAL);
        aboutStage.initStyle(StageStyle.UNDECORATED);
        aboutStage.initOwner(getStage());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/help/About.fxml"));
        Parent root = (Parent) loader.load();
        aboutStage.setScene(new Scene(root));

        AboutController controller = (AboutController) loader.getController();
        controller.setStage(aboutStage);

        aboutStage.show();
    }
    //</editor-fold>
}
