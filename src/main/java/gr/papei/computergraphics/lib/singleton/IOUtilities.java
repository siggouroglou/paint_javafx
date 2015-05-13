package gr.papei.computergraphics.lib.singleton;

import gr.papei.computergraphics.lib.shape.model.Shape;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
                for (Shape shape : ShapeListManager.getInstance().getShapeList()) {
                    bufferedWriter.write(shape.getShapeTitle());
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

        // Open the file for writing.
        boolean isEverythingOk = true;
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            try (BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)) {
                for (Shape shape : ShapeListManager.getInstance().getShapeList()) {
                    bufferedWriter.write(shape.getShapeTitle());
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
            if (!savedFile.isFile()) {
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
        if (!savedFile.isFile()) {
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
