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
        moveAmount = 0.5;
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

    public void targetPlayer(Player player) {
        if (player.getX() < getX()) {
            moveLeft();
        }
        if (player.getX() > getX()) {
            moveRight();
        }
    }

    public void attack(Player player) {
        if (getHitbox().intersects(player.entityRect())) {
            if (!isAttackDebounce()) {
                player.takeDamage(getDamage());
                System.out.println("player health: " + player.getHealth());
                setAttackDebounce(true);
                Utils.delay(getAttackCD(), (t) -> {
                    setAttackDebounce(false);
                }, 1);
                System.out.println("enemy attack on cd");

                setHitboxActive(true);
                Utils.delay(1000, (t) -> {
                    setHitboxActive(false);
                }, 1);
            }
        }
    }
}
