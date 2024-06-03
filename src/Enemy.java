import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private static int currentId = 0;
    private boolean lineOfSight;
    private double moveAmount;
    private int id;

    public Enemy(int health, int damage, double x, double y, boolean facingRight) {
        super(health, damage, x, y, facingRight, 1, 1);
        lineOfSight = false;
        moveAmount = 0.0002;
        id = currentId;
        currentId++;
    }

    public int getId() {
        return id;
    }

    public boolean hasLineOfSight() {
        return lineOfSight;
    }

    public void setLineOfSight(boolean lineOfSight) {
        this.lineOfSight = lineOfSight;
    }

    public void moveLeft() {
        if (getX() - moveAmount >= 0) {
            setX(getX() - moveAmount);
        }
    }

    public void moveRight() {
        if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
            setX(getX() + moveAmount);
        }
    }

    private void attack(Player player) {
        if (player.getX() < getX()) {
            moveLeft();
        }
        if (player.getX() > getX()) {
            moveRight();
        }
        if (entityRect().intersects(player.entityRect())) {
            player.takeDamage(getDamage());
            Utils.wait(1000);
        }
    }
}
