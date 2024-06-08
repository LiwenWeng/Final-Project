import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Animation implements ActionListener {
    private ArrayList<BufferedImage> frames;
    private Timer timer;
    private int currentFrame;
    private boolean active;
    private String name;

    public Animation(String name, ArrayList<BufferedImage> frames, int delay) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        timer = new Timer(delay, this);
        timer.start();
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public void start() {
        active = true;
    }

    public void stop() {
        active = false;
        reset();
    }

    public void reset() {
        currentFrame = 0;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (!active) return;
            currentFrame = (currentFrame + 1) % frames.size();
        }
    }

    public static ArrayList<BufferedImage> loadAnimation(String animationName, double scaleX, double scaleY) {
        ArrayList<BufferedImage> result = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String filename = "src/assets/animations/" + animationName + "/" + animationName + i + ".png";
            try {
                Image image = ImageIO.read((new File(filename)));
                BufferedImage originalImage = ImageIO.read((new File(filename)));
                image = image.getScaledInstance((int) (originalImage.getWidth() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleX), (int) (originalImage.getHeight() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleY), Image.SCALE_DEFAULT);
                result.add(Utils.toBufferedImage(image));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public String toString() {
        return name;
    }
}
