package gr.papei.computergraphics.lib.singleton;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Singleton class that stores settings synced with a file.
 *
 * @author siggouroglou@gmail.com
 */
public class Settings {

    private final static Logger logger = Logger.getLogger(Settings.class);
    private final static String DEFAULT_LOCATION = "./settings.json";
    private static Settings INSTANCE;

    private String backgroundColor;
    private String designColor;
    private double designWidth;
    private String fillColor;

    private Settings() throws IOException {
    }

    public static Settings getInstance() {
        if (INSTANCE == null) {
            try {
                // Read json from file.
                byte[] encoded = Files.readAllBytes(Paths.get(DEFAULT_LOCATION));
                String content = new String(encoded, StandardCharsets.UTF_8);
                
                // Load json to instance.
                Gson gson = new Gson();
                INSTANCE = gson.fromJson(content, Settings.class);
                logger.log(Level.INFO, "Settings instance loaded successfully.");
            } catch (IOException ex) {
                logger.log(Level.ERROR, "An error occurred with loading the instance.", ex);
            }
        }
        return INSTANCE;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getDesignColor() {
        return designColor;
    }

    public void setDesignColor(String designColor) {
        this.designColor = designColor;
    }

    public double getDesignWidth() {
        return designWidth;
    }

    public void setDesignWidth(double designWidth) {
        this.designWidth = designWidth;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public void save() throws IOException {
        // Convert instance to json.
        Gson gson = new Gson();
        String content = gson.toJson(INSTANCE);
        Files.write(Paths.get(DEFAULT_LOCATION), content.getBytes(), StandardOpenOption.CREATE);
    }
}
