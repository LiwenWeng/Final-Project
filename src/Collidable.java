import javax.imageio.ImageIO;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.module.ResolutionException;

public class Collidable {
    private double x;
    private double y;
    private BufferedImage image;
    public static final int RIGHT = 0;
    public static final int LEFT = 180;
    public static final int UP = 90;
    public static final int DOWN = 270;

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
    public Rectangle collidableRectLeft() {
        return new Rectangle((int) x - 5, (int) y, 5, image.getHeight());
    }
    public Rectangle collidableRectRight() {
        return new Rectangle((int) x + image.getWidth(), (int) y, 5, image.getHeight());
    }
    public Rectangle collidableRectTop() {
        return new Rectangle((int) x + 5, (int) y + 5, image.getWidth() - 10, 5);
    }
    public Rectangle collidableRectBottom() {
        return new Rectangle((int) x + 5, (int) y + image.getHeight(), image.getWidth() - 10, 5);
    }

}
