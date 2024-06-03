import java.util.ArrayList;
import java.util.Arrays;

public class Player extends Entity {
    private double moveAmount;
    private String name;
    private Background background;
    private boolean isWithinScreenRight;
    private boolean isWithinScreenLeft;
    private ArrayList<Integer> attackedEnemyIds;

    public Player(Background background) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.75, true, 0.4, 0.4);
        this.name = "joe";
        moveAmount = Constants.SCREEN_HEIGHT * 0.002;
        this.background = background;
        isWithinScreenRight = false;
        isWithinScreenLeft = false;
        attackedEnemyIds = new ArrayList<>();
    }

    public String getName() {
       return name;
    }

    public ArrayList<Integer> getAttackedEnemyIds() {
        return attackedEnemyIds;
    }

    public void moveRight() {
        if (collided() == Collidable.LEFT) return;
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
        if (collided() == Collidable.RIGHT) return;
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
        getJump().start();
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(5);
        playAnimation("jump");
    }

    public void simulateGravity() {
        if (isGrounded()) return;
        setGravity(getGravity() - Constants.SCREEN_HEIGHT / 12800.0 );
        background.setYCoord(background.getDoubleYCoord() + getGravity());
        if (background.getDoubleYCoord() <= 0) {
            setAirCollided(false);
            setGrounded(true);
            playAnimation("idle");
        }

        for (Collidable collidable : GraphicsPanel.getCollidables()) { //move collidables with background
            collidable.setY(collidable.getY() + getGravity());
        }
    }

    @Override
    public void hitboxDetection() {
        if (!isHitboxActive()) return;
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            if (getHitbox().intersects(enemy.entityRect()) && !attackedEnemyIds.contains(enemy.getId())) {
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
            Utils.delay(1000, (t) -> {
                setHitboxActive(false);
                attackedEnemyIds.clear();
            }, 1);
        }
    }

    public void shoot() {

    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - damage);
    }
}
