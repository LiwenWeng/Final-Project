import java.awt.*;

public class Enemy extends Entity {
    private static int currentId = 0;
    private double moveAmount;
    private int id;
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
        id = currentId;
        currentId++;
        attackRangeRect = new Rectangle((int) x, (int) y, rangeWidth, rangeHeight);
        playerInRange = false;
        this.player = player;
    }

    public int getId() {
        return id;
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
        attackRangeRect.setLocation(entityRect().getLocation());

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
        updatePosition();
        targetPlayer();
        defaultMovement();
    }
}
