import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Entity {
    private int health;
    private int damage;
    private double x;
    private double y;
    private boolean facingRight;
    private Animation idle;
    private Animation jump;
    private Animation currentPlayingAnim;
    private boolean isGrounded;
    private double gravity;
    private static Player player;

    public Entity(int health, int damage, double x, double y, boolean facingRight, double scalex, double scaley) {
        this.health = health;
        this.damage = damage;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        isGrounded = false;
        gravity = 0;

        idle = new Animation(loadAnimation("idle", scalex, scaley),200);
        jump = new Animation(loadAnimation("jump", scalex, scaley), 500);
        currentPlayingAnim = idle;

        idle.start();
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDrawX() {
        return player.getX() + (player.isFacingRight() ? 0 : player.getEntityImage().getWidth());
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public double getGravity() {
        return gravity;
    }

    public BufferedImage getEntityImage() {
        return currentPlayingAnim.getActiveFrame();
    }

    public int getHeight() {
        return getEntityImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getEntityImage().getWidth() * -1;
        } else {
            return getEntityImage().getWidth();
        }
    }

    public Animation getIdle() {
        return idle;
    }

    public Animation getJump() {
        return jump;
    }

    public Animation getCurrentPlayingAnim() {
        return currentPlayingAnim;
    }

    public void setCurrentPlayingAnim(Animation anim) {
        currentPlayingAnim = anim;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }

    public boolean collided() {
        for (Collidable collidable : GraphicsPanel.getCollidables()) {
            if (entityRect().intersects(collidable.getX(), collidable.getY(), collidable.getWidth(), collidable.getHeight())) {
                return true;
            }
        }
        return false;
    }

    public Rectangle entityRect() {
        int imageHeight = getEntityImage().getHeight();
        int imageWidth = getEntityImage().getWidth();
        return new Rectangle((int) x, (int) y, imageWidth, imageHeight);
    }

    public void setPlayer(Player p) {
        player = p;
    }

    private ArrayList<BufferedImage> loadAnimation(String animationName, double scalex, double scaley) {
        ArrayList<BufferedImage> result = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String filename = "src/assets/animations/" + animationName + "/" + animationName + i + ".png";
            try {
                BufferedImage before = ImageIO.read(new File(filename));
                int w = before.getWidth();
                int h = before.getHeight();
                BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale((Constants.SCREEN_HEIGHT / 1080.0) * scalex, (Constants.SCREEN_HEIGHT / 1080.0) * scaley);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                after = scaleOp.filter(before, after);
                result.add(after);
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
}
