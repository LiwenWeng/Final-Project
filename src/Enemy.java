import java.awt.*;
import java.util.Map;

public class Enemy extends Entity {
    private double moveAmount;
    private double originalX;
    private double originalY;
    private Background background;
    private Rectangle attackRangeRect;
    private boolean playerInRange;
    private Player player;
    private double leftLimit;
    private double rightLimit;
    private int direction;

    public Enemy(int health, int damage, double x, double y, int rangeWidth, int rangeHeight, Player player, Background background, Map<String, Animation> animations) {
        super(health, damage, x, y, true, animations);
        this.background = background;
        this.originalX = x;
        this.originalY = y;
        moveAmount = 1;
        attackRangeRect = new Rectangle((int) x, (int) y, rangeWidth, rangeHeight);
        leftLimit = attackRangeRect.x;
        rightLimit = attackRangeRect.x + attackRangeRect.getWidth();
        direction = 1;
        playerInRange = false;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPlayerInRange() {
        return playerInRange;
    }

    public void setPlayerInRange(boolean playerInRange) {
        this.playerInRange = playerInRange;
    }

    public Rectangle getAttackRangeRect() {
        return attackRangeRect;
    }

    public void moveLeft() {
        if (Collidable.getSidesCollided().get("right").contains(getId())) {
            direction = 1;
            return;
        }

        if (getX() - moveAmount >= 0) {
            originalX -= moveAmount;
            playAnimation("run", true);
        }
    }

    public void moveRight() {
        if (Collidable.getSidesCollided().get("left").contains(getId())) {
            direction = -1;
            return;
        }

        if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
            originalX += moveAmount;
            playAnimation("run", true);
        }
    }

    public void checkForPlayer() {
        Point center = Utils.getCenterPos(entityRect());
        attackRangeRect.setLocation((int) (center.x - attackRangeRect.getWidth()/2), (int) (center.y - attackRangeRect.getHeight()/2));

        if (attackRangeRect.intersects(player.entityRect())) {
            playerInRange = true;
            attack();
        } else {
            playerInRange = false;
        }
    }

    public void targetPlayer() {
        if (!playerInRange) return;

        if (player.entityRect().getX() + player.entityRect().getWidth() < Utils.getCenterPos(getAttackHitbox(), "x")) {
            moveLeft();
            faceRight();
        }
        if (player.entityRect().getX() > Utils.getCenterPos(getAttackHitbox(), "x")) {
            moveRight();
            faceLeft();
        }
        if (player.getY() + player.getHeight() < getAttackHitbox().y + getAttackHitbox().getHeight()/2) {
            jump();
        }
    }

    public void jump() {
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(Constants.SCREEN_HEIGHT * 0.006);
    }

    public void simulateGravity() {
        if (!Collidable.getSidesCollided().get("top").contains(getId())) setGrounded(false);
        if (isGrounded()) return;

        setGravity(getGravity() - Constants.SCREEN_HEIGHT * 0.0005);
        originalY -= getGravity();
        if (Collidable.getSidesCollided().get("top").contains(getId())) {
            setAirCollided(false);
            setGrounded(true);
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                if (collidable.getCollidableRectTop().intersects(entityRect()) && getGravity() < 0) {
                    if (isDead()) {

                    } else {
                        originalY = originalY + getGravity() - ((entityRect().getY() + entityRect().getHeight()) - collidable.getCollidableRect().y);
                    }
                }
            }
            setGravity(0);
        }
    }

    public void defaultMovement() {
        if (playerInRange) return;

        if (direction > 0) {
            moveRight();
            faceLeft();
            if (getX() + moveAmount > rightLimit) {
                direction = -1;
            }
        } else {
            moveLeft();
            faceRight();
            if (getX() - moveAmount < leftLimit) {
                direction = 1;
            }
        }
    }

    public void attack() {
        if (getAttackHitbox().intersects(player.entityRect())) {
            if (!isAttackDebounce()) {
                //playAnimation("attack", false);
                player.takeDamage(getDamage());
                setAttackDebounce(true);
                Utils.delay(getAttackCD(), (t) -> {
                    setAttackDebounce(false);
                }, 1);

                setHitboxActive(true);
                Utils.delay(1000, (t) -> {
                    setHitboxActive(false);
                }, 1);
            }
        }
    }

    public void updatePosition() {
        setX(originalX + background.getX());
        setY(originalY + background.getY());
    }

    public void start() {
        super.start();
        simulateGravity();
        updatePosition();
        checkForPlayer();
        targetPlayer();
        defaultMovement();
    }
}
