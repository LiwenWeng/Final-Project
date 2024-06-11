import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Button {
    private int x;
    private int y;
    private BufferedImage image;

    public Button(String img, int x, int y) {
        this.x = x;
        this.y = y;
        try {
            this.image = ImageIO.read(new File("src/assets/" + img + ".png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle buttonRect() {
        int imageHeight = getImage().getHeight();
        int imageWidth = getImage().getWidth();
        Rectangle rect = new Rectangle(x, y, imageWidth, imageHeight);
        return rect;
    }
}