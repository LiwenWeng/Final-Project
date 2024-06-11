import java.awt.*;

public class Enemy extends Entity {
    private double moveAmount;
    private double originalX;
    private double originalY;
    private Background background;
    private Rectangle attackRangeRect;
    private boolean playerInRange;
    private Player player;

    public Enemy(int health, int damage, double x, double y, int rangeWidth, int rangeHeight, Player player, Background background) {
        super(health, damage, x, y, true, 1, 1);
        this.background = background;
        this.originalX = x;
        this.originalY = y;
        moveAmount = 0.5;
        attackRangeRect = new Rectangle((int) x, (int) y, rangeWidth, rangeHeight);
        playerInRange = false;
        this.player = player;
    }

    public Rectangle getAttackRangeRect() {
        return attackRangeRect;
    }

    public void moveLeft() {
        if (getX() - moveAmount >= 0) {
            originalX -= moveAmount;
        }
    }

    public void moveRight() {
        if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
            originalX += moveAmount;
        }
    }

    public void checkForPlayer() {
        Point center = Utils.getCenterPos(entityRect());
        attackRangeRect.setLocation((int) (center.x - attackRangeRect.getWidth()/2), (int) (center.y - attackRangeRect.getHeight()/2));

        if (attackRangeRect.intersects(player.entityRect())) {
            playerInRange = true;
        } else {
            playerInRange = false;
        }
    }

    public void targetPlayer() {
        if (!playerInRange) return;

        if (player.entityRect().getX() + player.entityRect().getWidth() < Utils.getCenterPos(getAttackHitbox(), "x")) {
            moveLeft();
            faceLeft();
        }
        if (player.entityRect().getX() > Utils.getCenterPos(getAttackHitbox(), "x")) {
            moveRight();
            faceRight();
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

        setGravity(getGravity() - Constants.SCREEN_HEIGHT * 0.0002);
        originalY -= getGravity();
        if (Collidable.getSidesCollided().get("top").contains(getId())) {
            setAirCollided(false);
            setGrounded(true);
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                if (collidable.collidableRectTop().intersects(entityRect()) && getGravity() < 0) {
                    if (isDead()) {

                    } else {
                        originalY = originalY + getGravity() - ((entityRect().getY() + entityRect().getHeight()) - collidable.collidableRect().y);
                    }
                }
            }
            setGravity(0);
        }
    }

    public void defaultMovement() {

    }

    public void attack() {
        if (getAttackHitbox().intersects(player.entityRect())) {
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
