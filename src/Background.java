import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Background {
    private Animation run;
    private double xCoord;
    private double yCoord;
    private boolean leftLimit;
    private boolean rightLimit;
    private final double MOVE_AMT = Constants.SCREEN_HEIGHT * 0.002;

    public Background(String img, int x, int y) {
        this.xCoord = 0;
        this.yCoord = 0;
        ArrayList<BufferedImage> run_animation = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String filename = "src/assets/animations/" + img + "/" + img + i + ".png";
            try {
                run_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        run = new Animation(run_animation,200);
        leftLimit = false;
        rightLimit = true;
    }

    public int getXCoord() {
        return (int) xCoord;
    }


    public int getYCoord() {
        return (int) yCoord;
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= Constants.SCREEN_WIDTH - getBackgroundImage().getWidth()) {
            xCoord -= MOVE_AMT;
        }
    }

    public void moveRight() {
        if (xCoord + MOVE_AMT <= 0) {
            xCoord += MOVE_AMT;
        }
    }
    public BufferedImage getBackgroundImage() {
        return run.getActiveFrame();
    }

    public boolean isLeftLimit() {
        return (xCoord >= 0);
    }

    public boolean isRightLimit() {
        return (xCoord <= Constants.SCREEN_WIDTH - getBackgroundImage().getWidth());
    }
}
