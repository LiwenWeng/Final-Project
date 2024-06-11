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
            setX(getX() - moveAmount);
        }
    }

    public void moveRight() {
        if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
            setX(getX() + moveAmount);
        }
    }

    public void checkForPlayer() {
        Point center = Utils.getCenterPos(entityRect());
        attackRangeRect.setLocation((int) (center.x - attackRangeRect.getWidth()/2), (int) (center.y - attackRangeRect.getHeight()/2));

        if (attackRangeRect.intersects(player.entityRect())) {
            playerInRange = true;
            System.out.println("player in range");
        } else {
            playerInRange = false;
            System.out.println("player not in range");
        }
    }

    public void targetPlayer() {
        if (!playerInRange) return;

        if (player.getX() < getX()) {
            moveLeft();
        }
        if (player.getX() > getX()) {
            moveRight();
        }
    }

    public void jump() {

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
