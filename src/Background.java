import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Background {
    private Animation run;
    private int xCoord;
    private int yCoord;
    private boolean leftLimit;
    private boolean rightLimit;
    private final double MOVE_AMT = Constants.SCREEN_HEIGHT / 1080.0;

    public Background(String image, int x, int y) {
        this.xCoord = 0;
        this.yCoord = 0;
        ArrayList<BufferedImage> run_animation = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String filename = image + i + ".png";
            try {
                run_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        run = new Animation(run_animation,200);
    }

    public int getXCoord() {
        return xCoord;
    }


    public int getYCoord() {
        return yCoord;
    }

    public void moveRight() {
        if (xCoord - MOVE_AMT >= Constants.SCREEN_WIDTH - getBackgroundImage().getWidth()) {
            xCoord -= MOVE_AMT;
            leftLimit = false;
        } else {
            leftLimit = true;
        }
    }

    public void moveLeft() {

        if (xCoord + MOVE_AMT <= 0) {
            xCoord += MOVE_AMT;
            rightLimit = false;
        } else {
            rightLimit = true;
        }
    }
    public BufferedImage getBackgroundImage() {
        return run.getActiveFrame();
    }

    public boolean isLeftLimit() {
        return leftLimit;
    }

    public boolean isRightLimit() {
        return rightLimit;
    }
}
