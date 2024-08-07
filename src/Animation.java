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
    private int looped;
    private String name;
    private boolean reverse;
    private double gravity;

    public Animation(String name, ArrayList<BufferedImage> frames, int delay) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        timer = new Timer(delay, this);
        timer.start();
        active = false;
        looped = 0;
        reverse = false;
    }

    public boolean isActive() {
        return active;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public int isLooped() {
        return looped;
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public boolean isReverse() {
        return reverse;
    }

    public void reverse() {
        reverse = !reverse;
        if (reverse) {
            currentFrame = frames.size() - 1;
        } else {
            currentFrame = 0;
        }

    }

    public Animation start() {
        active = true;
        return this;
    }

    public void stop(boolean resetFrames, boolean resetLoop) {
        active = false;
        if (resetFrames)
            reset(resetLoop);
    }

    public void reset(boolean resetLoop) {
        currentFrame = reverse ? frames.size() - 1 : 0;
        if (resetLoop)
            looped = 0;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (!active)
                return;
            if (name.equals("jump")) {
                if (gravity > 9) {
                    currentFrame = 1;
                } else if (gravity > 8) {
                    currentFrame = 2;
                } else if (gravity > 6) {
                    currentFrame = 3;
                } else if (gravity > 4) {
                    currentFrame = 4;
                } else if (gravity > 1) {
                    currentFrame = 5;
                } else if (gravity > -1) {
                    currentFrame = 6;
                } else if (gravity > -2) {
                    currentFrame = 7;
                } else if (gravity > -4) {
                    currentFrame = 8;
                } else if (gravity > -6) {
                    currentFrame = 9;
                } else if (gravity > -8) {
                    currentFrame = 10;
                } else if (gravity > -10) {
                    currentFrame = 11;
                } else if (gravity > -12) {
                    currentFrame = 12;
                }

            } else {
                if (!reverse) {
                    currentFrame = (currentFrame + 1) % frames.size();
                    looped += (currentFrame == frames.size() - 1) ? 1 : 0;
                } else {
                    currentFrame--;
                    if (currentFrame < 0)
                        currentFrame = frames.size() - 1;
                    looped += (currentFrame == 0) ? 1 : 0;
                }
            }
        }
    }

    public static ArrayList<BufferedImage> loadAnimation(String type, String animationName, double scaleX,
            double scaleY) {
        ArrayList<BufferedImage> result = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            String filename = "src/assets/animations/" + type + animationName + "/" + animationName + i + ".png";
            try {
                Image image = ImageIO.read((new File(filename)));
                BufferedImage originalImage = ImageIO.read((new File(filename)));
                image = image.getScaledInstance(
                        (int) (originalImage.getWidth() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleX),
                        (int) (originalImage.getHeight() * (Constants.SCREEN_HEIGHT / 1080.0) * scaleY),
                        Image.SCALE_DEFAULT);
                result.add(Utils.toBufferedImage(image));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public String toString() {
        return name;
    }
}
