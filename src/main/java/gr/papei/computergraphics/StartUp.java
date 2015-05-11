package gr.papei.computergraphics;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class StartUp extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("ΠΑΝΕΠΙΣΤΗΜΙΟ ΠΕΙΡΑΙΑ - ΤΜΗΜΑ ΠΛΗΡΟΦΟΡΙΚΗΣ - ΓΡΑΦΙΚΑ ΜΕ ΥΠΟΛΟΓΙΣΤΕΣ 2015");
        primaryStage.getIcons().add(new Image("/files/images/unipi_logo.jpg"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frames/Main.fxml"));
        
        Parent root = (Parent)loader.load();
        primaryStage.setScene(new Scene(root));
        
//        LogInController controller = (LogInController)loader.getController();        
//        controller.setStage(primaryStage);
        
        primaryStage.show();
    }

}
