import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UI {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage image;
    private String name;

    public UI(String name, int x, int y, double scaleX, double scaleY) {
        this.x = x;
        this.y = y;
        this.name = name;
        String filename = "src/assets/" + name + ".png";
        try {
            Image image = ImageIO.read((new File(filename)));
            BufferedImage originalImage = ImageIO.read((new File(filename)));
            image = image.getScaledInstance((int) (originalImage.getWidth() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleX), (int) (originalImage.getHeight() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleY), Image.SCALE_DEFAULT);
            this.image = Utils.toBufferedImage(image);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
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
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public String getName() {
        return name;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}