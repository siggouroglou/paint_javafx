package gr.papei.computergraphics.lib;

import javafx.scene.paint.Color;

/**
 *
 * @author siggouroglou@gmail.com
 */
public class ColorUtilities {

    public static String colorToWeb(Color color) {
        // Get color properties.
        int red = (int)Math.round(color.getRed() * 255.0);
        int green = (int)Math.round(color.getGreen() * 255.0);
        int blue = (int)Math.round(color.getBlue() * 255.0);
        int opacity = (int)Math.round(color.getOpacity() * 255.0);
        
        // Return color in web form.
        return "rgb(" + red + "," + green + "," + blue + "," + opacity + ")";
    }
}
