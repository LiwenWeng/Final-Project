import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class Utils {
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static <T> void delay(int ms, Consumer<T> callback, T value) {
        startThread(() -> {
            wait(ms);
            callback.accept(value);
        });
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static Point getCenterPos(Rectangle rectangle) {
        double x = rectangle.x + rectangle.getWidth()/2.0;
        double y = rectangle.y + rectangle.getHeight()/2.0;
        return new Point((int) x, (int) y);
    }

    public static double getCenterPos(Rectangle rectangle, String xOrY) {
        if (xOrY.equalsIgnoreCase("x")) {
            return getCenterPos(rectangle).x;
        } else {
            return getCenterPos(rectangle).y;
        }
    }
}
