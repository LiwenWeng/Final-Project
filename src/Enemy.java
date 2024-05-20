import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private boolean lineOfSight;

    public Enemy(int health, int damage, double x, double y, boolean facingRight) {
        super(health, damage, x, y, facingRight, "");
        lineOfSight = false;
    }

    public boolean hasLineOfSight() {
        return lineOfSight;
    }

    public void setLineOfSight(boolean lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    private void attack() {

    }
}
