import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background {
    private Animation animation;
    private double x;
    private double y;
    private double originalX;
    private double originalY;
    private final double LEFT_LIMIT;
    private final double RIGHT_LIMIT;
    private final double MOVE_AMT = Constants.SCREEN_HEIGHT * 0.004;

    public Background(String img, int x, int y, double scalex, double scaley) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        animation = new Animation("background", Animation.loadAnimation("", img, scalex, scaley),200);
        animation.start();
        LEFT_LIMIT = 0;
        RIGHT_LIMIT = Constants.SCREEN_WIDTH - getBackgroundImage().getWidth();
    }

    public boolean moveRight(boolean isWithinLeftLimit) {
        if (x + MOVE_AMT <= LEFT_LIMIT && !isWithinLeftLimit) {
            x += MOVE_AMT;
            return true;
        }
        return false;
    }
    public boolean dashRight(boolean isWithinLeftLimit) {
        if (x + MOVE_AMT * 5.0 <= LEFT_LIMIT && !isWithinLeftLimit) {
            x += MOVE_AMT * 5.0;
            return true;
        }
        return false;
    }

    public boolean moveLeft(boolean isWithinRightLimit) {
        if (x - MOVE_AMT >= RIGHT_LIMIT && !isWithinRightLimit) {
            x -= MOVE_AMT;
            return true;
        }
        return false;
    }
    public boolean dashLeft(boolean isWithinRightLimit) {
        if (x - MOVE_AMT * 5.0 >= RIGHT_LIMIT && !isWithinRightLimit) {
            x -= MOVE_AMT * 5.0;
            return true;
        }
        return false;
    }
    
    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public BufferedImage getBackgroundImage() {
        return animation.getActiveFrame();
    }

    public boolean isLeftLimit() {
        return (x >= 0);
    }

    public boolean isRightLimit() {
        return (x <= Constants.SCREEN_WIDTH - getBackgroundImage().getWidth());
    }

    public double getOriginalX() {
        return originalX;
    }

    public double getOriginalY() {
        return originalY;
    }

    public boolean canDashRight() {
        if (x + MOVE_AMT * 5.0 <= LEFT_LIMIT) {
            return true;
        }
        return false;
    }
    public boolean canDashLeft() {
        if (x - MOVE_AMT * 5.0 >= RIGHT_LIMIT) {
            return true;
        }
        return false;
    }

    public int getWidth() {
        return getBackgroundImage().getWidth();
    }

    public int getHeight() {
        return getBackgroundImage().getHeight();
    }

}
