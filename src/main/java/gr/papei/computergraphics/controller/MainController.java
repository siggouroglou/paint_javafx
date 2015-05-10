package gr.papei.computergraphics.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable {

    @FXML
    private ScrollPane scrolPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert scrolPane != null : "fx:id=\"scrolPane\" was not injected: check your FXML file 'Main.fxml'.";
    }

    private Stage getStage() {
        return (Stage) scrolPane.getScene().getWindow();
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

        // Load the view.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/FileNew.fxml"));
        Parent root = (Parent) loader.load();
        fileNewStage.setScene(new Scene(root));

//        LogInController controller = (LogInController)loader.getController();        
//        controller.setStage(primaryStage);
        
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
    void editSettingsClick(ActionEvent event) {

    }
    //</editor-fold>

    @FXML
    void helpAboutClick(ActionEvent event) {

    }
    //</editor-fold>
}
