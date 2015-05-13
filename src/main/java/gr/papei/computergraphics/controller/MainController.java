package gr.papei.computergraphics.controller;

import gr.papei.computergraphics.controller.help.AboutController;
import gr.papei.computergraphics.lib.ColorUtilities;
import gr.papei.computergraphics.lib.singleton.CanvasManager;
import gr.papei.computergraphics.lib.singleton.Settings;
import gr.papei.computergraphics.lib.singleton.ShapeProperties;
import gr.papei.computergraphics.lib.singleton.ShapeListManager;
import gr.papei.computergraphics.lib.shape.initiator.LineInitiator;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.Dialogs;

public class MainController implements Initializable {

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
    private ToggleGroup radioGroup1;
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
        assert scrollPane != null : "fx:id=\"scrollPane\" was not injected: check your FXML file 'Main.fxml'.";
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
        shapeProperiesGridPane.visibleProperty().bind(Bindings.isNotNull(radioGroup1.selectedToggleProperty()));
        shapePropertyColor.valueProperty().bindBidirectional(ShapeProperties.getInstance().colorProperty());
        shapePropertyWidth.valueProperty().bindBidirectional(ShapeProperties.getInstance().widthProperty());
        shapePropertyFill.valueProperty().bindBidirectional(ShapeProperties.getInstance().fillProperty());

        // Bind CanvasManager drawingEnableProperty property.
        CanvasManager.getInstance().drawingEnableProperty().bind(Bindings.isNotNull(radioGroup1.selectedToggleProperty()));

        // Bind savadNo and savedYes properties to canvas.
        savedYesLabel.visibleProperty().bind(Bindings.and(CanvasManager.getInstance().canvasInitializedProperty(), IOUtilities.savedProperty()));
        savedNoLabel.visibleProperty().bind(Bindings.and(CanvasManager.getInstance().canvasInitializedProperty(), IOUtilities.savedProperty().isEqualTo(new SimpleBooleanProperty(false))));
    }

    private Stage getStage() {
        return (Stage) scrollPane.getScene().getWindow();
    }

    //<editor-fold defaultstate="collapsed" desc="Palet Bar">
    @FXML
    void shapeLineClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new LineInitiator());
    }

    @FXML
    void shapeCircleClick(ActionEvent event) {

    }

    @FXML
    void shapeTriangleAction(ActionEvent event) {

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

        /// Show it.
        fileNewStage.show();
    }

    @FXML
    void fileSaveClick(ActionEvent event) {
        IOUtilities.savedProperty().set(true);
    }

    @FXML
    void fileSaveAsClick(ActionEvent event) {

    }

    @FXML
    void fileExportClick(ActionEvent event) {
        FileChooser choose = new FileChooser();
        choose.setTitle("Επιλογή αρχείου για εξαγωγή σχημάτων");
        File file = choose.showOpenDialog(getStage());
        if (!file.isFile()) {
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
        if (!IOUtilities.savedProperty().get()) {
            IOUtilities.questionForSave(getStage());
        }

        FileChooser choose = new FileChooser();
        choose.setTitle("Επιλογή αρχείου για εισαγωγή σχημάτων");
        File file = choose.showOpenDialog(getStage());
        if (!file.isFile()) {
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

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Edit Menu">
    @FXML
    void editShapeLineClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new LineInitiator());
    }

    @FXML
    void editShapeCicleClick(ActionEvent event) {

    }

    @FXML
    void editShapeTriangleClick(ActionEvent event) {

    }

    @FXML
    void editShapeSquareClick(ActionEvent event) {

    }

    @FXML
    void editShapeCrookedLineClick(ActionEvent event) {

    }

    @FXML
    void editShapePolygonClick(ActionEvent event) {

    }

    @FXML
    void editColorFontClick(ActionEvent event) {

    }

    @FXML
    void editColorDesignClick(ActionEvent event) {

    }

    @FXML
    void editColorFillClick(ActionEvent event) {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/EditSettings.fxml"));
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
