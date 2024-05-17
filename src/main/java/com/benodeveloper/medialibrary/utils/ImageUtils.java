package com.benodeveloper.medialibrary.utils;

import com.benodeveloper.medialibrary.exceptions.UnreadableFileException;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

public class ImageUtils {

    public static final String GIF_CONTENT_TYPE = "image/gif";
    public static final String JPG_CONTENT_TYPE = "image/jpeg";
    public static final String PNG_CONTENT_TYPE = "image/png";
    public static final String TIFF_CONTENT_TYPE = "image/tiff";
    public static final String X_TIFF_CONTENT_TYPE = "image/x-tiff";
    public static final String APNG_CONTENT_TYPE = "image/apng";
    public static final String AVIF_CONTENT_TYPE = "image/avif";
    public static final String WEBP_CONTENT_TYPE = "image/webp";
    public static final int CROP_POSITION_LEFT_TOP = -4;
    public static final int CROP_POSITION_LEFT_MIDDLE = -3;
    public static final int CROP_POSITION_LEFT_BOTTOM = -2;
    public static final int CROP_POSITION_CENTER_TOP = -1;
    public static final int CROP_POSITION_CENTER = 0;
    public static final int CROP_POSITION_CENTER_BOTTOM = 1;
    public static final int CROP_POSITION_RIGHT_TOP = 2;
    public static final int CROP_POSITION_RIGHT_MIDDLE = 3;
    public static final int CROP_POSITION_RIGHT_BOTTOM = 4;

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

    /**
     * Convert an image to jpg.
     *
     * @param path
     * @throws IOException
     */
    public static void ConvertImageToJPG(Path path) throws RuntimeException {


        boolean canWrite = false;
        try {
             InputStream inputStream = new FileInputStream(path.toFile());
            final BufferedImage image;
            image = ImageIO.read(inputStream);
            inputStream.close();
            String filename = FilenameUtils.removeExtension(path.toString()).concat(".jpg");
            final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            final FileOutputStream outputStream = new FileOutputStream(filename);
            canWrite = ImageIO.write(convertedImage, "jpg", outputStream);
            outputStream.close();

        } catch (IOException e) {
            throw new UnreadableFileException(e);
        }

        if (!canWrite) throw new UnreadableFileException("Failed to write converted image.");
    }

    /**
     * Crop a given image in a given path.
     *
     * @param path     the file path.
     * @param width    corp width.
     * @param height   crop height.
     * @param position position to start the crop.
     * @throws IOException
     * @see #CROP_POSITION_LEFT_TOP
     * @see #CROP_POSITION_LEFT_MIDDLE
     * @see #CROP_POSITION_LEFT_BOTTOM
     * @see #CROP_POSITION_CENTER_TOP
     * @see #CROP_POSITION_CENTER
     * @see #CROP_POSITION_CENTER_BOTTOM
     * @see #CROP_POSITION_RIGHT_TOP
     * @see #CROP_POSITION_RIGHT_MIDDLE
     * @see #CROP_POSITION_RIGHT_BOTTOM
     */
    public static void cropImage(Path path, int width, int height, int position) throws IOException {
        File file = path.toFile();
        BufferedImage image = ImageIO.read(file);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (height > imageHeight || width > imageWidth) {
            throw new InterruptedIOException("Cropping to these dimensions on this image is not possible");
        }
        int middle = ((imageHeight / 2) - (height / 2));
        int center = ((imageWidth / 2) - (width / 2));
        int left = 0;
        int top = 0;
        int bottom = imageHeight - height;
        int right = imageWidth - width;

        int[] xAndY = switch (position) {
            case CROP_POSITION_LEFT_TOP -> new int[]{left, top};
            case CROP_POSITION_LEFT_MIDDLE -> new int[]{left, middle};
            case CROP_POSITION_LEFT_BOTTOM -> new int[]{left, bottom};
            case CROP_POSITION_CENTER_TOP -> new int[]{center, top};
            case CROP_POSITION_CENTER -> new int[]{center, middle};
            case CROP_POSITION_CENTER_BOTTOM -> new int[]{center, bottom};
            case CROP_POSITION_RIGHT_TOP -> new int[]{right, top};
            case CROP_POSITION_RIGHT_MIDDLE -> new int[]{right, middle};
            case CROP_POSITION_RIGHT_BOTTOM -> new int[]{right, bottom};
            default -> new int[]{left, top};
        };
        String dist = FilenameUtils.removeExtension(path.toString()).concat("-" + width + "X" + height + ".jpg");
        cropImage(image, dist, width, height, xAndY[0], xAndY[1]);
    }

    /**
     * @param path   the file path.
     * @param width  corp width.
     * @param height crop height.
     * @throws IOException
     */
    public static void cropImage(Path path, int width, int height) throws IOException {
        cropImage(path, width, height, 0, 0);
    }

    /**
     * Crop an image in a given path with a given width/height
     * with given x/y axes to start cropping with.
     *
     * @param path   the file path.
     * @param width  corp width.
     * @param height crop height.
     * @param startX start cropping x axes.
     * @param startY start cropping in y axes.
     * @throws IOException
     */
    public static void cropImage(Path path, int width, int height, int startX, int startY) throws IOException {
        final BufferedImage image = ImageIO.read(path.toFile());
        String dist = FilenameUtils.removeExtension(path.toString()).concat("-" + width + "X" + height + ".jpg");
        cropImage(image, dist, width, height, startX, startY);
    }

    /**
     * Corp a given image as BufferedImage with a given dimensions Width and Height
     * plus x/y axes to start cropping with and save it as a given dist file
     *
     * @param image  Given image as BufferedImage
     * @param dist   dist file
     * @param width  corp width.
     * @param height crop height.
     * @param startX start cropping x axes.
     * @param startY start cropping in y axes.
     * @throws IOException
     */
    public static void cropImage(BufferedImage image, String dist, int width, int height, int startX, int startY) throws IOException {
        // create a copy of the image with the same width and height
        // NOTE: we could use getSubimage, but getSubimage acts as a pointer which points at a subsection of
        // the original image That means when we edit or crop the subimage, the edits will also happen to
        // the original image, so we need to create a copy from the original and redraw it as a new image.
        final BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        // draw the image inside the copy
        Graphics graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        final FileOutputStream outputStream = new FileOutputStream(dist);
        // then crop it and save it as jpg file
        final boolean canWrite = ImageIO.write(copy.getSubimage(startX, startY, width, height), "jpg", outputStream);
        outputStream.close();
        if (!canWrite) throw new IllegalArgumentException("Failed to write corp image.");
    }
}
