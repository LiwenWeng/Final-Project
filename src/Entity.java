import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity {
    private int health;
    private int damage;
    private double x;
    private double y;
    private boolean facingRight;

    public Entity(int health, int damage, double x, double y, boolean facingRight) {
        this.health = health;
        this.damage = damage;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
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

    public boolean isFacingRight() {
        return facingRight;
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

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
