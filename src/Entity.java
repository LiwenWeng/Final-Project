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
    private Animation run;
    private Animation currentPlayingAnim;
    private boolean isGrounded;
    private double gravity;
    private boolean airCollided;
    private boolean hitboxActive;
    private Rectangle hitbox;
    private boolean attackDebounce;
    private int attackCD;

    public Entity(int health, int damage, double x, double y, boolean facingRight, double scalex, double scaley) {
        this.health = health;
        this.damage = damage;
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        isGrounded = false;
        gravity = 0;
        airCollided = false;
        hitboxActive = false;
        attackDebounce = false;
        attackCD = 700;

        idle = new Animation("idle", Animation.loadAnimation("idle", scalex, scaley),200);
        jump = new Animation("jump", Animation.loadAnimation("jump", scalex, scaley), 100);
        run = new Animation("run", Animation.loadAnimation("run", scalex, scaley), 50);
        currentPlayingAnim = idle;

        hitbox = new Rectangle((int) (x + getWidth() * 0.75), (int) y, (int) (getWidth() * 0.67), getHeight());
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
        return getX() + (isFacingRight() ? 0 : getEntityImage().getWidth());
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
            return getEntityImage().getWidth();
        } else {
            return getEntityImage().getWidth() * -1;
        }
    }

    public Animation getIdle() {
        return idle;
    }

    public Animation getJump() {
        return jump;
    }
    public Animation getRun() {
        return run;
    }

    public Animation getCurrentPlayingAnim() {
        return currentPlayingAnim;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isHitboxActive() {
        return hitboxActive;
    }

    public boolean isAttackDebounce() {
        return attackDebounce;
    }

    public int getAttackCD() {
        return attackCD;
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

    public void setAirCollided(boolean airCollided) {
        this.airCollided = airCollided;
    }

    public void setAttackDebounce(boolean attackDebounce) {
        this.attackDebounce = attackDebounce;
    }

    public void setHitboxActive(boolean hitboxActive) {
        this.hitboxActive = hitboxActive;
    }

    public void reconcileHitbox() {
        if (facingRight) {
            hitbox.setLocation((int) (x + entityRect().getWidth() * 0.75), (int) y);
        } else {
            hitbox.setLocation((int) (x - entityRect().getWidth() * 0.375), (int) y);
        }
    }

    public void hitboxDetection() {
        if (!hitboxActive) return;
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            if (hitbox.intersects(enemy.entityRect())) {
                System.out.println(enemy.getHealth());
            }
        }
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public int collided() {
        for (Collidable collidable : GraphicsPanel.getCollidables()) {
            if (entityRect().intersects(collidable.collidableRect())) {
                //if (airCollided) return ;
                if (entityRect().intersects(collidable.collidableRectBottom())) {
                    if (!airCollided) {
                        gravity = 0;
                        airCollided = true;
    
                    }
                    return Collidable.DOWN;
                }
                if (entityRect().intersects(collidable.collidableRectTop())) {
                    isGrounded = true;
                    return Collidable.UP;
                }
                if (entityRect().intersects(collidable.collidableRectRight())) {
                    return Collidable.RIGHT;
                }
                if (entityRect().intersects(collidable.collidableRectLeft())) {
                    return Collidable.LEFT;
                }
            }
        }
        return 5;
    }

    public Rectangle entityRect() {
        int imageHeight = getEntityImage().getHeight();
        int imageWidth = getEntityImage().getWidth();

        return new Rectangle((int) x, (int) y, imageWidth, imageHeight);
    }

    public void playAnimation(String animationName) {
        if (currentPlayingAnim.toString().equals(animationName)) return;
        switch (animationName) {
            case "idle" -> {
                idle.start();
                currentPlayingAnim.stop();
                currentPlayingAnim = idle;
            }
            case "jump" -> {
                jump.start();
                currentPlayingAnim.stop();
                currentPlayingAnim = jump;
            }
            case "run" -> {
                run.start();
                currentPlayingAnim.stop();
                currentPlayingAnim = run;
            }
        }
    }

    public void attack() {
        if (!attackDebounce) {
            attackDebounce = true;
            Utils.delay(1000, (t) -> {
                hitboxActive = false;
            }, 1);

            hitboxActive = true;
            Utils.delay(attackCD, (t) -> {
                attackDebounce = false;
            }, 1);
        }
    }

    public Rectangle toggleHitbox() {
        return new Rectangle((int) x + getWidth(), (int) y, getWidth(), getHeight());
    }
}
