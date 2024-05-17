import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    //private final double MOVE_AMT = 0.6;
    private String name;
    private Animation run;

    public Player(String name) {
        super(100, 10, 50, 435, true);
        this.name = name;

        //The code below is used to programatically create an ArrayList of BufferedImages to use for an Animation object
        //By creating all the BufferedImages beforehand, we don't have to worry about lagging trying to read image files during gameplay
        ArrayList<BufferedImage> run_animation = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String filename = "src/idle/idle" + i + ".png";
            try {
                run_animation.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        run = new Animation(run_animation,200);

    }

    //This function is changed from the previous version to let the player turn left and right
    //This version of the function, when combined with getWidth() and getHeight()
    //Allow the player to turn without needing separate images for left and right
//    public int getxCoord() {
//        if (facingRight) {
//            return (int) xCoord;
//        } else {
//            return (int) (xCoord + (getPlayerImage().getWidth()));
//        }
//    }
//
//    public int getyCoord() {
//        return (int) yCoord;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void faceRight() {
//        facingRight = true;
//    }
//
//    public void faceLeft() {
//        facingRight = false;
//    }
//
//    public void moveRight() {
//        if (xCoord + MOVE_AMT <= 920) {
//            xCoord += MOVE_AMT;
//        }
//    }
//
//    public void moveLeft() {
//        if (xCoord - MOVE_AMT >= 0) {
//            xCoord -= MOVE_AMT;
//        }
//    }
//
//    public void moveUp() {
//        if (yCoord - MOVE_AMT >= 0) {
//            yCoord -= MOVE_AMT;
//        }
//    }
//
//    public void moveDown() {
//        if (yCoord + MOVE_AMT <= 435) {
//            yCoord += MOVE_AMT;
//        }
//    }
//
//    public void turn() {
//        if (facingRight) {
//            faceLeft();
//        } else {
//            faceRight();
//        }
//    }
//
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
//        if (facingRight) {
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
//        return new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
//    }
}
