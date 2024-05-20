import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    private double moveAmount;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private String name;
    private Animation run;
    private boolean isGrounded;
    private double gravity;


    public Player() {
        super(100, 10, 5, 900, true);
        this.name = "joe";
        xCoord = Constants.SCREEN_WIDTH * 0.5;
        yCoord = Constants.SCREEN_HEIGHT * 0.75;
        moveAmount = Constants.SCREEN_WIDTH * 0.001;
        isGrounded = true;
        gravity = 3.5;

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

   public int getxCoord() {
       if (facingRight) {
           return (int) xCoord;
       } else {
           return (int) (xCoord + (getPlayerImage().getWidth()));
       }
   }

   public int getyCoord() {
       return (int) yCoord;
   }

   public String getName() {
       return name;
   }

   public void faceRight() {
       facingRight = true;
   }

   public void faceLeft() {
       facingRight = false;
   }

   public void setGrounded(boolean grounded) { isGrounded = grounded; }

   public void moveRight() {
       if (xCoord + moveAmount <= Constants.SCREEN_WIDTH - getPlayerImage().getWidth()) {
           xCoord += moveAmount;
       }
   }

   public void moveLeft() {
       if (xCoord - moveAmount >= 0) {
           xCoord -= moveAmount;
       }
   }

   public void jump() {
        if (!isGrounded) return;
        isGrounded = false;
        gravity = 3.5;
   }

   public void simulateGravity() {
        if (isGrounded) return;
        gravity -= 0.1;
        yCoord -= gravity;
        if (yCoord >= Constants.SCREEN_HEIGHT * 0.75) {
            isGrounded = true;
        }
   }

   public void turn() {
       if (facingRight) {
           faceLeft();
       } else {
           faceRight();
       }
   }

   public BufferedImage getPlayerImage() {
       return run.getActiveFrame();
   }

   //These functions are newly added to let the player turn left and right
   //These functions when combined with the updated getxCoord()
   //Allow the player to turn without needing separate images for left and right
   public int getHeight() {
       return getPlayerImage().getHeight();
   }

   public int getWidth() {
       if (facingRight) {
           return getPlayerImage().getWidth();
       } else {
           return getPlayerImage().getWidth() * -1;
       }
   }

   // we use a "bounding Rectangle" for detecting collision
   public Rectangle playerRect() {
       int imageHeight = getPlayerImage().getHeight();
       int imageWidth = getPlayerImage().getWidth();
       return new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
    }
}
