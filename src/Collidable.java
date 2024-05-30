import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Collidable {
    private double x;
    private double y;
    private BufferedImage image;
    public static final int RIGHT = 1;
    public static final int LEFT = -1;
    public static final int UP = 2;
    public static final int DOWN = -2;

    public Collidable(double x, double y, String img) {
        this.x = x;
        this.y = y;
        try {
            this.image = ImageIO.read(new File(img));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //GraphicsPanel.getCollidables().add(this);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Rectangle collidableRect() {
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        return new Rectangle((int) x, (int) y, imageWidth, imageHeight);
    }
}
