import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    private double moveAmount;
    private String name;
    private Background background;

    public Player(Background background) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.75, true, "idle");
        this.name = "joe";
        moveAmount = Constants.SCREEN_HEIGHT * 0.002;
        this.background = background;
    }

   public String getName() {
       return name;
   }

   public void moveRight() {
       background.moveRight();
    //    if (background.isLeftLimit()) {
    //        if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
    //             setX(getX() + moveAmount);
    //        }
    //    }
   }

    public void moveLeft() {
        background.moveLeft();
    //    if (background.isRightLimit()) {
    //        if (getX() - moveAmount >= 0) {
    //            setX((getX() - moveAmount));
    //        }
    //    }
   }

   public void jump() {
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(3.5);
   }

   public void simulateGravity() {
        if (isGrounded()) return;
        setGravity(getGravity() - 0.05);
        setY(getY() - getGravity());
        if (getY() >= Constants.SCREEN_HEIGHT * 0.75) {
            setGrounded(true);
        }
   }
}
