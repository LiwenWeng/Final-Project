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
}
