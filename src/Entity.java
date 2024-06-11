import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    private static int currentId = 0;
    private int id;
    private int health;
    private int damage;
    private double x;
    private double y;
    private boolean facingRight;
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

    public Entity(int health, int damage, double x, double y, boolean facingRight, Map<String, Animation> animations) {
        id = currentId;
        currentId++;

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

        this.animations = animations;
        currentPlayingAnim = animations.get("idle");

        hitbox = new Rectangle((int) (x + getWidth() * 0.25), (int) (y + getHeight() * 0.15), (int) (getWidth() * 0.55), (int) (getHeight() * 0.65));
        attackHitbox = new Rectangle((int) (x + getWidth() * 0.75), (int) y, (int) (getWidth() * 0.67), getHeight());
        bottomCollided = false;
        topCollided = false;
        rightCollided = false;
        leftCollided = false;

    }
    public int getId() {
        return id;
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

    public Map<String, Animation> getAnimations() {
        return animations;
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
        if (animations.get("dash") != null && animations.get("dash").isActive()) return;

        health -= damage;
        if (health <= 0) {
            if (animations.get("dead") != null) {
                playAnimation("dead", false);
            };
            dead = true;
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
        collided();
    }

    public void reconcileHitbox() {
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
                if (!Collidable.getSidesCollided().get("bottom").contains(id)) {
                    Collidable.getSidesCollided().get("bottom").add(id);
                }
            }
            if (entityRect().intersects(collidable.collidableRectTop())) {
                topCollided = true;
                if (!Collidable.getSidesCollided().get("top").contains(id)) {
                    Collidable.getSidesCollided().get("top").add(id);                    
                }
            }
            if (entityRect().intersects(collidable.collidableRectRight())) {
                rightCollided = true;
                if (!Collidable.getSidesCollided().get("right").contains(id)) {
                    Collidable.getSidesCollided().get("right").add(id);                    
                }
            }
            if (entityRect().intersects(collidable.collidableRectLeft())) {
                leftCollided = true;
                if (!Collidable.getSidesCollided().get("left").contains(id)) {
                    Collidable.getSidesCollided().get("left").add(id);
                }
            }
        }
        if (bottomCollided) {
            bottomCollided = false;
        } else {
            Collidable.getSidesCollided().get("bottom").remove((Integer) id);
            airCollided = false;
        }
        if (topCollided) {
            topCollided = false;
        } else {
            Collidable.getSidesCollided().get("top").remove((Integer) id);
        }
        if (rightCollided) {
            rightCollided = false;
        } else {
            Collidable.getSidesCollided().get("right").remove((Integer) id);
        }
        if (leftCollided) {
            leftCollided = false;
        } else {
            Collidable.getSidesCollided().get("left").remove((Integer) id);
        }
    }


    public Rectangle entityRect() {
        return hitbox;
    }

    public void playAnimation(String animationName, boolean loop) {
        if (currentPlayingAnim.toString().equals(animationName)) {
            if (!loop && currentPlayingAnim.isLooped() > 0) {
                currentPlayingAnim.stop(true);
            }
            return;
        };
        currentPlayingAnim.stop(true);
        currentPlayingAnim = animations.get(animationName).start();
    }

    public void playAnimation(String animationName, String nextAnimationName) {
        if (currentPlayingAnim.toString().equals(animationName)) {
            if (currentPlayingAnim.isLooped() > 0) {
                currentPlayingAnim.stop(true);
                animations.get(nextAnimationName).start();
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
