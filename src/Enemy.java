import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private boolean seesPlayer;

    public Enemy(int health, int damage, double x, double y, boolean facingRight) {
        super(health, damage, x, y, facingRight);
    }
}
