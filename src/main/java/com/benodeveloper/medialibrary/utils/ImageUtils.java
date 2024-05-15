package com.benodeveloper.medialibrary.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public  static String GIF_CONTENT_TYPE = "image/gif";
    public  static String JPG_CONTENT_TYPE = "image/jpeg";
    public  static String PNG_CONTENT_TYPE = "image/png";
    public  static String TIFF_CONTENT_TYPE = "image/tiff";
    public  static String X_TIFF_CONTENT_TYPE = "image/x-tiff";
    public  static String APNG_CONTENT_TYPE = "image/apng";
    public  static String AVIF_CONTENT_TYPE = "image/avif";
    public  static String WEBP_CONTENT_TYPE = "image/webp";

    public static String[] ALLOWED_CONTENT_TYPE = new String[]{
            GIF_CONTENT_TYPE,
            JPG_CONTENT_TYPE,
            PNG_CONTENT_TYPE,
            TIFF_CONTENT_TYPE,
            X_TIFF_CONTENT_TYPE,
            APNG_CONTENT_TYPE,
            AVIF_CONTENT_TYPE,
            WEBP_CONTENT_TYPE,
    };

    public static void ConvertImageToJPG(InputStream inputStream,String filename) throws IOException {
        final BufferedImage image = ImageIO.read(inputStream);
        inputStream.close();

        final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        final FileOutputStream outputStream = new FileOutputStream(filename);
        final boolean canWrite = ImageIO.write(convertedImage,"jpg", outputStream);
        outputStream.close();

        if(!canWrite) throw new IllegalArgumentException("Failed to write image.");

    }
}
