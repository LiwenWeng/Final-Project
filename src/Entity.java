import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    private int health;
    private int damage;
    private double x;
    private double y;
    private boolean facingRight;
    private Animation idle;
    private Animation jump;
    private Animation run;
    private Animation attack;
    private Animation dash;
    private Animation deadAnim;
    private Map<String, Animation> animations;
    private Animation currentPlayingAnim;
    private boolean isGrounded;
    private double gravity;
    private boolean airCollided;
    private boolean hitboxActive;
    private Rectangle attackHitbox; // attack range
    private Rectangle hitbox;
    private boolean attackDebounce; // allows attack
    private boolean bottomCollided;
    private boolean topCollided;
    private boolean rightCollided;
    private boolean leftCollided;
    private int attackCD;
    private boolean dead;

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
        dead = false;

        idle = new Animation("idle", Animation.loadAnimation("idle", scalex, scaley),200);
        jump = new Animation("jump", Animation.loadAnimation("jump", scalex, scaley), 100);
        run = new Animation("run", Animation.loadAnimation("run", scalex, scaley), 100);
        attack = new Animation("attack", Animation.loadAnimation("attack", scalex, scaley), 100);
        dash = new Animation("dash", Animation.loadAnimation("dash", scalex, scaley), 100);
        deadAnim = new Animation("dead", Animation.loadAnimation("dead", scalex, scaley), 100);
        currentPlayingAnim = idle;

        animations = new HashMap<>();
        animations.put("idle", idle);
        animations.put("jump", jump);
        animations.put("run", run);
        animations.put("attack", attack);
        animations.put("dash", dash);
        animations.put("dead", deadAnim);

        hitbox = new Rectangle((int) (x + getWidth() * 0.25), (int) (y + getHeight() * 0.15), (int) (getWidth() * 0.55), (int) (getHeight() * 0.65));
        attackHitbox = new Rectangle((int) (x + getWidth() * 0.75), (int) y, (int) (getWidth() * 0.67), getHeight());
        bottomCollided = false;
        topCollided = false;
        rightCollided = false;
        leftCollided = false;

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

    public boolean isDead() {
        return dead;
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

    public Rectangle getAttackHitbox() {
        return attackHitbox;
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

    public void takeDamage(int damage) {
        if (dash.isActive()) return;

        health -= damage;
        if (health <= 0) {
            dead = true;
            playAnimation("dead", false);
        };
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

    public void start() {
        currentPlayingAnim.start();
        reconcileHitbox();
        hitboxDetection();
        collided();
    }

    public void reconcileHitbox() { // attackHitbox moves with enemy
        if (isDead()) {
            if (facingRight) {
                hitbox.setLocation((int) (x + getWidth() * 0.25), (int) (y - getHeight() * 0.075));
            } else {
                hitbox.setLocation((int) (x - getWidth() * 0.25), (int) (y - getHeight() * 0.075));
            }
        } else {
            if (facingRight) {
                hitbox.setLocation((int) (x + getWidth() * 0.25), (int) (y + getHeight() * 0.15));
                attackHitbox.setLocation((int) (x + hitbox.getWidth() * 1.375), (int) y);
            } else {
                hitbox.setLocation((int) (x - getWidth() * 0.25), (int) (y + getHeight() * 0.15));
                attackHitbox.setLocation((int) (x - hitbox.getWidth() * 0.375), (int) y);
            }
        }
    }

    public void hitboxDetection() {
        if (!hitboxActive) return;
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            if (attackHitbox.intersects(enemy.entityRect())) {
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

    public void collided() {
        for (Collidable collidable : GraphicsPanel.getCollidables()) {
            if (entityRect().intersects(collidable.collidableRectBottom())) {
                if (!airCollided) {
                    gravity = 0;
                    airCollided = true;
                }
                bottomCollided = true;
                Collidable.getSidesCollided()[0] = true;
            }
            if (entityRect().intersects(collidable.collidableRectTop())) {
                topCollided = true;
                Collidable.getSidesCollided()[1] = true;
            }
            if (entityRect().intersects(collidable.collidableRectRight())) {
                rightCollided = true;
                Collidable.getSidesCollided()[2] = true;
            }
            if (entityRect().intersects(collidable.collidableRectLeft())) {
                leftCollided = true;
                Collidable.getSidesCollided()[3] = true;
            }
        }
        if (bottomCollided) {
            bottomCollided = false;
        } else {
            Collidable.getSidesCollided()[0] = false;
            airCollided = false;
        }
        if (topCollided) {
            topCollided = false;
        } else {
            Collidable.getSidesCollided()[1] = false;
        }
        if (rightCollided) {
            rightCollided = false;
        } else {
            Collidable.getSidesCollided()[2] = false;
        }
        if (leftCollided) {
            leftCollided = false;
        } else {
            Collidable.getSidesCollided()[3] = false;
        }
    }


    public Rectangle entityRect() {
        return hitbox;
    }

    public void playAnimation(String animationName, boolean loop) {
        if (currentPlayingAnim.toString().equals(animationName)) {
            if (!loop && currentPlayingAnim.isLooped() > 0) {
                currentPlayingAnim.stop(!currentPlayingAnim.toString().equals("dead"));
            }
            return;
        };
        currentPlayingAnim.stop(true);
        currentPlayingAnim = animations.get(animationName).start();
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
