package gr.papei.computergraphics.controller;

import gr.papei.computergraphics.lib.mainView.CanvasManager;
import gr.papei.computergraphics.lib.mainView.Settings;
import gr.papei.computergraphics.lib.mainView.ShapeStackManager;
import gr.papei.computergraphics.lib.shape.initiator.LineInitiator;
import gr.papei.computergraphics.lib.shape.model.Line;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {
    
    @FXML
    private StackPane stackPane;
    @FXML
    private VBox shapeStackContainer;
    @FXML
    private Label footerLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        assert scrolPane != null : "fx:id=\"scrolPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert stackPane != null : "fx:id=\"stackPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert shapeStackContainer != null : "fx:id=\"shapeStackContainer\" was not injected: check your FXML file 'Main.fxml'.";
        assert footerLabel != null : "fx:id=\"loggerLabel\" was not injected: check your FXML file 'Main.fxml'.";
        
        // Initialize ShapeStackManager.
        CanvasManager.initInstance(stackPane);
        ShapeStackManager.initInstance(shapeStackContainer);
        
        // Initialize Stack pane background color.
        Color color = Color.valueOf(Settings.getInstance().getFontColor());
        int red = (int)Math.round(color.getRed() * 255.0);
        int green = (int)Math.round(color.getGreen() * 255.0);
        int blue = (int)Math.round(color.getBlue() * 255.0);
        int opacity = (int)Math.round(color.getOpacity() * 255.0);
        stackPane.setStyle("-fx-background-color: rgb(" + red + "," + green + "," + blue + "," + opacity + ")");
    }

    private Stage getStage() {
        return (Stage) stackPane.getScene().getWindow();
    }

    //<editor-fold defaultstate="collapsed" desc="Palet Bar">
    @FXML
    void colorFontClick(ActionEvent event) {

    }

    @FXML
    void colorDesignClick(ActionEvent event) {

    }

    @FXML
    void colorFillClick(ActionEvent event) {

    }

    @FXML
    void shapeLineClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new LineInitiator(new Line()));
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
        fileNewStage.setResizable(false);

        // Load the view.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/FileNew.fxml"));
        Parent root = (Parent) loader.load();
        fileNewStage.setScene(new Scene(root));
        
        /// Show it.
        fileNewStage.show();
    }

    @FXML
    void fileOpenClick(ActionEvent event) {
    }

    @FXML
    void fileSaveClick(ActionEvent event) {

    }

    @FXML
    void fileSaveAsClick(ActionEvent event) {

    }

    @FXML
    void fileExportClick(ActionEvent event) {

    }

    @FXML
    void fileImportClick(ActionEvent event) {

    }

    @FXML
    void fileQuitClick(ActionEvent event) {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Edit Menu">
    @FXML
    void editShapeLineClick(ActionEvent event) {
        CanvasManager.getInstance().startDrawing(new LineInitiator(new Line()));
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
    void helpAboutClick(ActionEvent event) {

    }
    //</editor-fold>
}
