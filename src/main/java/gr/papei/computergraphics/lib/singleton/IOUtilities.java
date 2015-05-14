package gr.papei.computergraphics.lib.singleton;

import com.google.gson.Gson;
import gr.papei.computergraphics.lib.shape.model.Shape;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * A utilities class that manages the saved state and the io of this
 * application.
 *
 * @author siggouroglou@gmail.com
 */
public final class IOUtilities {

    private static final BooleanProperty saved = new SimpleBooleanProperty(false);
    private static File savedFile;

    public static BooleanProperty savedProperty() {
        return saved;
    }

    public static boolean exportCanvas(File file) {
        // Check the argument.
        if (file == null || !file.isFile()) {
            throw new IllegalArgumentException("File must be not null and must be existing.");
        }

        // Open the file for writing.
        boolean isEverythingOk = true;
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                // Export background.
                bufferedWriter.write(Settings.getInstance().getBackgroundColor());
                bufferedWriter.write("\n");

                // Export shapes.
                for (Shape shape : ShapeListManager.getInstance().getShapeList()) {
                    bufferedWriter.write(shape.getClassName());
                    bufferedWriter.write("=");
                    bufferedWriter.write(shape.exportToJson());
                    bufferedWriter.write("\n");
                }
            }
        } catch (Exception ex) {
            isEverythingOk = false;
            ex.printStackTrace();
        }

        return isEverythingOk;
    }

    public static boolean importShapes(File file) {
        // Check the argument.
        if (file == null || !file.isFile()) {
            throw new IllegalArgumentException("File must be not null and must be existing.");
        }

        // Usuful objects.
        String backgroundColor = "";
        List<Shape> shapeList = new LinkedList<>();

        // Open the file for reading.
        boolean isEverythingOk = true;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            InputStreamReader inputStreamWriter = new InputStreamReader(inputStream, "UTF-8");
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamWriter)) {
                // First line contains the background color.
                backgroundColor = bufferedReader.readLine();

                // Other lines contains the shapes.
                String shapeLine;
                while ((shapeLine = bufferedReader.readLine()) != null) {
                    String[] lineArray = shapeLine.split("=");
                    if (lineArray.length < 2) {
                        continue;
                    }

                    // Get the class type and the json data.
                    Class<?> clazz = Class.forName("gr.papei.computergraphics.lib.shape.model." + lineArray[0]);
                    Gson gson = new Gson();
                    int indexNo = shapeLine.indexOf("=");
                    String json = shapeLine.substring(indexNo + 1);
                    
                    // Load data to it.
                    Object shape = gson.fromJson(json, clazz);
                    
                    // Add this object that is a shape to the shape list.
                    shapeList.add((Shape) shape);
                }
            }
        } catch (Exception ex) {
            isEverythingOk = false;
            ex.printStackTrace();
        }
        
        // Update the canvas background.
        // I will not update them, i changed my mind.
        
        // Update the Shapes and canvas.
        ShapeListManager.getInstance().addAllShapes(shapeList);
        CanvasManager.getInstance().refreshShapes();

        return isEverythingOk;
    }

    public static void questionForSave(Stage stage) {
        Action response = Dialogs.create()
                .owner(stage)
                .title("Αποθήκευση")
                .masthead("Αποθήκευση αλλαγών?")
                .message("Πιέστε YES αν θα θέλατε να αποθηκεύσετε τις αλλαγές που έχετε κάνει.")
                .showConfirm();

        // User click to ok button.
        if (response == Dialog.Actions.YES) {
            save(stage);
        }
    }

    public static void save(Stage stage) {
        // Has user selected a file.
        if (savedFile == null) {
            // Create the allowed extensions list.
            FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("PNG Image", "*.png");
            FileChooser.ExtensionFilter extFilterJpg = new FileChooser.ExtensionFilter("JPG Image", "*.jpg");

            // Select the file to save to.
            FileChooser choose = new FileChooser();
            choose.setTitle("Επιλογή αρχείου για αποθήκευση εικόνας");
            choose.getExtensionFilters().addAll(extFilterPng, extFilterJpg);
            savedFile = choose.showOpenDialog(stage);
            if (savedFile == null || !savedFile.isFile()) {
                try {
                    FileUtils.touch(savedFile);
                } catch (IOException ex) {
                    Dialogs.create()
                            .owner(stage)
                            .title("Πρόβλημα")
                            .masthead("Το αρχείο δεν βρέθηκε!")
                            .message("Το αρχείο που επιλέξατε δεν υπάρχει και δεν μπορεί να δημιουργηθεί. Παρακαλώ επαναλάβετε τη διαδικασία επιλογής του.")
                            .showWarning();
                    return;
                }
            }
        }

        String ext = FilenameUtils.getExtension(savedFile.getName());

        WritableImage writableImage = CanvasManager.getInstance().getCanvas().snapshot(null, null);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, ext.toLowerCase(), savedFile);
        } catch (IOException ignorred) {
        }
    }

    public static void saveAs(Stage stage) {
        // Create the allowed extensions list.
        FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("PNG Image", "*.png");
        FileChooser.ExtensionFilter extFilterJpg = new FileChooser.ExtensionFilter("JPG Image", "*.jpg");

        // Select the file to save to.
        FileChooser choose = new FileChooser();
        choose.setTitle("Επιλογή αρχείου για αποθήκευση εικόνας");
        choose.getExtensionFilters().addAll(extFilterPng, extFilterJpg);
        savedFile = choose.showOpenDialog(stage);
        if (savedFile == null || !savedFile.isFile()) {
            try {
                FileUtils.touch(savedFile);
            } catch (IOException ex) {
                Dialogs.create()
                        .owner(stage)
                        .title("Πρόβλημα")
                        .masthead("Το αρχείο δεν βρέθηκε!")
                        .message("Το αρχείο που επιλέξατε δεν υπάρχει και δεν μπορεί να δημιουργηθεί. Παρακαλώ επαναλάβετε τη διαδικασία επιλογής του.")
                        .showWarning();
                return;
            }
        }

        String ext = FilenameUtils.getExtension(savedFile.getName());

        WritableImage writableImage = CanvasManager.getInstance().getCanvas().snapshot(null, null);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bufferedImage, ext.toLowerCase(), savedFile);
        } catch (IOException ignorred) {
        }
    }

}
