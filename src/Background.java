import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Background {
    private Animation animation;
    private double xCoord;
    private double yCoord;
    private final double LEFT_LIMIT;
    private final double RIGHT_LIMIT;
    private final double MOVE_AMT = Constants.SCREEN_HEIGHT * 0.002;

    public Background(String img, int x, int y) {
        this.xCoord = x;
        this.yCoord = y;

        animation = new Animation("background", Animation.loadAnimation(img, 1, 1),200);
        animation.start();
        LEFT_LIMIT = 0;
        RIGHT_LIMIT = Constants.SCREEN_WIDTH - getBackgroundImage().getWidth();
    }

    public int getXCoord() {
        return (int) xCoord;
    }


    public int getYCoord() {
        return (int) yCoord;
    }

    public boolean moveRight(boolean isWithinLeftLimit) {
        if (xCoord + MOVE_AMT <= LEFT_LIMIT && !isWithinLeftLimit) {
            xCoord += MOVE_AMT;
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                collidable.setX(collidable.getX() + MOVE_AMT);
            }
            for (Enemy enemy : GraphicsPanel.getEnemies()) {
                enemy.setX(enemy.getX() + MOVE_AMT);
            }
            return true;
        }
        return false;
    }

    public boolean moveLeft(boolean isWithinRightLimit) {
        if (xCoord - MOVE_AMT >= RIGHT_LIMIT && !isWithinRightLimit) {
            xCoord -= MOVE_AMT;
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                collidable.setX(collidable.getX() - MOVE_AMT);
            }
            for (Enemy enemy : GraphicsPanel.getEnemies()) {
                enemy.setX(enemy.getX() - MOVE_AMT);
            }
            return true;
        }
        return false;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public double getDoubleYCoord() {
        return yCoord;
    }

    public BufferedImage getBackgroundImage() {
        return animation.getActiveFrame();
    }

    public boolean isLeftLimit() {
        return (xCoord >= 0);
    }

    public boolean isRightLimit() {
        return (xCoord <= Constants.SCREEN_WIDTH - getBackgroundImage().getWidth());
    }
}
