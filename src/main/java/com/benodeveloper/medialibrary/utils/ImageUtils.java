package com.benodeveloper.medialibrary.utils;

import com.benodeveloper.medialibrary.exceptions.UnreadableFileException;
import com.benodeveloper.medialibrary.exceptions.WriteFileFailedException;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        final BufferedImage image = readImageFromPath(path);
        final BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        if (!writeImageToPath(convertedImage, regenerateImagePath(path, "", ".jpg"), "jpg"))
            throw new UnreadableFileException("Failed to write converted image.");
    }

    public static Path resizeImage(Path path, int width, int height, String format) {
        final BufferedImage image = readImageFromPath(path);
        int[] dimensions = adjustResizeImageDimensions(image, width, height);
        Path dist = regenerateImagePath(path, "-scaled-" + dimensions[0] + "X" + dimensions[1], format);
        if (dist.toFile().exists()) return dist;
        return resizeImage(image, dist, dimensions[0], dimensions[1], format);
    }

    public static Path resizeImage(BufferedImage image, Path dist, int width, int height, String format) {
        final Image scaledInstance = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage copy = new BufferedImage(scaledInstance.getWidth(null), scaledInstance.getHeight(null), BufferedImage.TYPE_INT_RGB);
        copy.createGraphics().drawImage(scaledInstance, 0, 0, null);
        if (!writeImageToPath(copy, dist, format)) {

            throw new UnreadableFileException("Failed to write scaled image.");
        }
        return dist;
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
        BufferedImage image = readImageFromPath(path);
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
        Path dist = regenerateImagePath(path, width + "X" + height, ".jpg");
        cropImage(image, dist.toString(), width, height, xAndY[0], xAndY[1]);
    }

    /**
     * @param path   the file path.
     * @param width  corp width.
     * @param height crop height.
     * @throws IOException
     */
    public static void cropImage(Path path, int width, int height) {
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
    public static void cropImage(Path path, int width, int height, int startX, int startY) {
        final BufferedImage image = readImageFromPath(path);
        Path dist = regenerateImagePath(path, width + "X" + height, ".jpg");
        cropImage(image, dist.toString(), width, height, startX, startY);
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
    public static void cropImage(BufferedImage image, String dist, int width, int height, int startX, int startY) {
        // create a copy of the image with the same width and height
        // NOTE: we could use getSubimage, but getSubimage acts as a pointer which points at a subsection of
        // the original image That means when we edit or crop the subimage, the edits will also happen to
        // the original image, so we need to create a copy from the original and redraw it as a new image.
        final BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        // draw the image inside the copy
        Graphics graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        // then crop it and save it as jpg file
        if (!writeImageToPath(copy.getSubimage(startX, startY, width, height), Paths.get(dist), "jpg"))
            throw new WriteFileFailedException("Failed to write corp image.");
    }

    public static Path regenerateImagePath(Path path, String suffix) {
        String extension = FilenameUtils.getExtension(path.toString());
        return regenerateImagePath(path, suffix, extension);
    }

    public static Path regenerateImagePath(Path path, String suffix, String extension) {
        return Paths.get(FilenameUtils.removeExtension(path.toString()).concat(suffix + "." + extension));
    }

    public static BufferedImage readImageFromPath(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new UnreadableFileException(e);
        }
    }

    public static boolean writeImageToPath(BufferedImage image, Path path, String format) {
        try {
            final FileOutputStream outputStream = new FileOutputStream(path.toFile());
            boolean canWrite = ImageIO.write(image, format, outputStream);
            outputStream.close();
            return canWrite;
        } catch (IOException e) {
            throw new UnreadableFileException(e);
        }
    }

    public static int[] adjustResizeImageDimensions(BufferedImage image, int width, int height) {
        width = (width == 0) ? image.getWidth() : width;
        height = (height == 0) ? image.getHeight() : height;

        if (width < 0 || height < 0) {
            if (width < 0 && height > 0) {
                width = (height == image.getHeight()) ? image.getWidth() : (height * image.getWidth()) / image.getHeight();
            } else if (height < 0 && width > 0) {
                height = (width == image.getWidth()) ? image.getHeight() : (width * image.getHeight()) / image.getWidth();
            } else {
                width = image.getWidth();
                height = image.getHeight();
            }
        }
        return new int[]{width, height};
    }
}
