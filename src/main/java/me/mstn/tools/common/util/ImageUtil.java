package me.mstn.tools.common.util;

import me.mstn.tools.MasstonToolsPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage loadImage(String fileName) {
        BufferedImage img = null;
        String pluginPath = MasstonToolsPlugin.PATH.getAbsolutePath() + "/";

        try {
            File file = new File(pluginPath + fileName);
            img = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while downloading the file \"" + fileName + "\".", e);
        }

        return img;
    }

}
