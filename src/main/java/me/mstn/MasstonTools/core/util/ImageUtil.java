package me.mstn.MasstonTools.core.util;

import me.mstn.MasstonTools.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage loadImage(String fileName) {
        BufferedImage img = null;
        String pluginPath = Main.PATH.getAbsolutePath() + "/";

        try {
            File file = new File(pluginPath + fileName);
            img = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить изображение \"" + fileName + "\" или при его открытии возникла ошибка.", e);
        }

        return img;
    }

}
