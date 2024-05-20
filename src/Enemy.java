import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private boolean seesPlayer;

    public Enemy(int health, int damage, double x, double y, boolean facingRight) {
        super(health, damage, x, y, facingRight);
    }

    public boolean seesPlayer() {
        return seesPlayer;
    }

    public void setSeesPlayer(boolean seesPlayer) {
        this.seesPlayer = seesPlayer;
    }

    private void attack() {

    }

//    public BufferedImage getPlayerImage() {
//        return run.getActiveFrame();
//    }
//
//    //These functions are newly added to let the player turn left and right
//    //These functions when combined with the updated getxCoord()
//    //Allow the player to turn without needing separate images for left and right
//    public int getHeight() {
//        return getPlayerImage().getHeight();
//    }
//
//    public int getWidth() {
//        if (isFacingRight()) {
//            return getPlayerImage().getWidth();
//        } else {
//            return getPlayerImage().getWidth() * -1;
//        }
//    }
//
//    // we use a "bounding Rectangle" for detecting collision
//    public Rectangle playerRect() {
//        int imageHeight = getPlayerImage().getHeight();
//        int imageWidth = getPlayerImage().getWidth();
//        return new Rectangle((int) getX(), (int) getY(), imageWidth, imageHeight);
//    }
}
