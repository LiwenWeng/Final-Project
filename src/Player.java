import java.util.ArrayList;
import java.util.Arrays;

public class Player extends Entity {
    private double moveAmount;
    private String name;
    private Background background;
    private boolean isWithinScreenRight;
    private boolean isWithinScreenLeft;
    private ArrayList<Integer> attackedEnemyIds;
    private boolean canDoubleJump;
    private boolean doubleJumped;

    public Player(Background background) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.5, true, 2, 2);
        this.name = "joe";
        moveAmount = Constants.SCREEN_HEIGHT * 0.002;
        this.background = background;
        isWithinScreenRight = false;
        isWithinScreenLeft = false;
        attackedEnemyIds = new ArrayList<>();
        canDoubleJump = false;
        doubleJumped = true;
    }

    public String getName() {
       return name;
    }

    public ArrayList<Integer> getAttackedEnemyIds() {
        return attackedEnemyIds;
    }

    public void moveRight() {
        if (Collidable.getSidesCollided()[3] || (isHitboxActive() && isGrounded())) return;
        if (!background.moveLeft(isWithinScreenLeft)) {
            if (getX() + moveAmount < Constants.SCREEN_WIDTH - getWidth()) {
                setX(getX() + moveAmount);
                isWithinScreenRight = true;
                if (getX() + moveAmount > Constants.SCREEN_WIDTH * 0.5) {
                    isWithinScreenLeft = false;
                }
            }
        }
    }

    public void moveLeft() {
        if (Collidable.getSidesCollided()[2] || (isHitboxActive() && isGrounded())) return;
        if (!background.moveRight(isWithinScreenRight)) {
            if (getX() - moveAmount > 0) {
                setX(getX() - moveAmount);
                isWithinScreenLeft = true;
                if (getX() - moveAmount < Constants.SCREEN_WIDTH * 0.5) {
                    isWithinScreenRight = false;
                }
            }
        }
    }

    public void jump() {
        if (canDoubleJump && !Collidable.getSidesCollided()[0]) {
            setGravity(Constants.SCREEN_HEIGHT * 0.006);
            canDoubleJump = false;
            doubleJumped = true;
        }
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(Constants.SCREEN_HEIGHT * 0.006);
        canDoubleJump = false;
        playAnimation("jump");
    }

    public void simulateGravity() {
        if (!Collidable.getSidesCollided()[1]) {
            setGrounded(false);
        }
        if (isGrounded()) return;
        setGravity(getGravity() - Constants.SCREEN_HEIGHT * 0.0002 );
        if (!doubleJumped) {
            Utils.delay(200,(t) -> {
                canDoubleJump = true;
                doubleJumped = true;
            } , 1);
        }
        background.setY(background.getY() + getGravity());
        if (Collidable.getSidesCollided()[1]) {
            setAirCollided(false);
            setGrounded(true);
            doubleJumped = false;


            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                if (collidable.collidableRectTop().intersects(entityRect()) && getGravity() < 0) {
                    background.setY(background.getY() + -getGravity() + ((getY() + getHeight()) - collidable.getY()));
                }
            }
        }
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            enemy.setY(enemy.getY() + getGravity());
        }
    }

    @Override
    public void hitboxDetection() {
        if (!isHitboxActive()) return;
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            if (getHitbox().intersects(enemy.entityRect()) && !attackedEnemyIds.contains(enemy.getId())) {
                if (enemy.isDead()) continue;
                enemy.takeDamage(getDamage());
                if (enemy.isDead()) continue;
                attackedEnemyIds.add(enemy.getId());
            }
        }
    }

    @Override
    public void attack() {
        if (!isAttackDebounce()) {
            setAttackDebounce(true);
            Utils.delay(getAttackCD(), (t) -> {
                setAttackDebounce(false);
            }, 1);

            setHitboxActive(true);
            Utils.delay(500, (t) -> {
                setHitboxActive(false);
                attackedEnemyIds.clear();
            }, 1);
        }
    }
}
