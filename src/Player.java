import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity implements ActionListener{
    private double moveAmount;
    private String name;
    private Background background;
    private boolean isWithinScreenRight;
    private boolean isWithinScreenLeft;
    private ArrayList<Integer> attackedEnemyIds;
    private boolean canDoubleJump;
    private boolean doubleJumped;
    private boolean isDashing;
    private boolean canDash;
    private Timer dashTimer;
    private Timer dashAnimationTimer;
    private boolean dashRight;
    private boolean dashLeft;
    private double dashPosition;

    public Player(Background background, Map<String, Animation> animations) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.75, true, animations);
        this.name = "joe";

        moveAmount = Constants.SCREEN_HEIGHT * 0.004;
        this.background = background;

        isWithinScreenRight = false;
        isWithinScreenLeft = true;

        attackedEnemyIds = new ArrayList<>();

        canDoubleJump = false;
        doubleJumped = true;

        isDashing = false;
        canDash = true;
        dashTimer = new Timer(20, this);
        dashAnimationTimer = new Timer(50, this);
        dashRight = false;
        dashLeft = false;
        dashTimer.start();
        dashTimer.addActionListener(this);
        dashAnimationTimer.start();
        dashAnimationTimer.addActionListener(this);
        dashPosition = 0;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getAttackedEnemyIds() {
        return attackedEnemyIds;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public void moveRight() {
        if (Collidable.getSidesCollided().get("left").contains(getId()) || (isHitboxActive() && isGrounded()) || isDashing) return;
        if (!background.moveLeft(isWithinScreenLeft)) {
            if (getX() + moveAmount < Constants.SCREEN_WIDTH - entityRect().getWidth()) {
                setX(getX() + moveAmount);
                isWithinScreenRight = true;
                if (getX() + moveAmount > Constants.SCREEN_WIDTH * 0.5) {
                    isWithinScreenLeft = false;
                }
            }
        }
    }

    public void moveLeft() {
        if (Collidable.getSidesCollided().get("right").contains(getId()) || (isHitboxActive() && isGrounded()) || isDashing) return;
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
    public void dashRight() {
        if (canDash) {
            isDashing = true;
            canDash = false;
            dashRight = true;
            dashPosition = background.getX();
            setGravity(0);
            Utils.delay(300, (t) -> {
                isDashing = false;
                dashRight = false;
            }, 1);
            Utils.delay(4000, (t) -> {
                canDash = true;
            }, 1);
        }
    }
    public void dashLeft() {
        if (canDash) {
            isDashing = true;
            canDash = false;
            dashLeft = true;
            dashPosition = background.getX();
            setGravity(0);
            Utils.delay(300, (t) -> {
                isDashing = false;
                dashLeft = false;
            }, 1);
            Utils.delay(4000, (t) -> {
                canDash = true;
            }, 1);
        }
    }

    public void jump() {
        if (isDashing) return;
        if (canDoubleJump && !Collidable.getSidesCollided().get("bottom").contains(getId())) {
            setGravity(Constants.SCREEN_HEIGHT * 0.009);
            canDoubleJump = false;
            doubleJumped = true;
            getAnimations().get("jump").reset(true);
        }
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(Constants.SCREEN_HEIGHT * 0.009);
        canDoubleJump = false;
        playAnimation("jump", false);
    }

    public void simulateGravity() {
        if (!Collidable.getSidesCollided().get("top").contains(getId())) {
            setGrounded(false);
        }

        if (isGrounded() || isDashing) return;

        setGravity(getGravity() - Constants.SCREEN_HEIGHT * 0.0003 );
        if (!doubleJumped) {
            doubleJumped = true;
            Utils.delay(200,(t) -> {
                canDoubleJump = true;
            } , 1);
        }

        background.setY(background.getY() + getGravity());
        if (Collidable.getSidesCollided().get("top").contains(getId())) {
            setAirCollided(false);
            setGrounded(true);
            doubleJumped = false;
            canDoubleJump = false;
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                if (collidable.getCollidableRectTop().intersects(entityRect()) && getGravity() < 0) {
                    if (isDead()) {
                        background.setY(background.getY() + -getGravity() + ((entityRect().getY() + entityRect().getHeight()) - collidable.getY()));
                    } else {
                        background.setY(background.getY() + -getGravity() + ((entityRect().getY() + entityRect().getHeight()) - collidable.getCollidableRectTop().y));
                    }
                }
            }
            setGravity(0);
        }
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            enemy.setY(enemy.getY() + getGravity());
        }
    }

    public void hitboxDetection() {
        if (!isHitboxActive()) return;
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            if (getAttackHitbox().intersects(enemy.entityRect()) && !attackedEnemyIds.contains(enemy.getId())) {
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

    @Override
    public void faceRight() {
        if (isDashing) return;
        super.faceRight();
    }

    @Override
    public void faceLeft() {
        if (isDashing) return;
        super.faceLeft();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (e.getSource() == dashTimer) {
                if (dashRight) {
                    if (Collidable.getSidesCollided().get("left").contains(getId()) || isHitboxActive()) return;
                    if (!background.dashLeft(isWithinScreenLeft)) {
                        if (getX() + moveAmount * 5.0 < Constants.SCREEN_WIDTH - entityRect().getWidth()) {
                            setX(getX() + moveAmount * 5.0);
                            isWithinScreenRight = true;
                            if (getX() + moveAmount > Constants.SCREEN_WIDTH * 0.5) {
                                isWithinScreenLeft = false;
                            }
                        }
                    }
                }
                else if (dashLeft) {
                    if (Collidable.getSidesCollided().get("right").contains(getId()) || isHitboxActive()) return;
                    if (!background.dashRight(isWithinScreenRight)) {
                        if (getX() - moveAmount * 5.0 > 0) {
                            setX(getX() - moveAmount * 5.0);
                            isWithinScreenLeft = true;
                            if (getX() - moveAmount < Constants.SCREEN_WIDTH * 0.5) {
                                isWithinScreenRight = false;
                            }
                        }
                    }
                }
            }
            if (e.getSource() == dashAnimationTimer) {
                if (isDashing) {
                    if (background.canDashLeft() && background.canDashRight()) {
                        GraphicsPanel.getDashImages().add(new DashImage(getEntityImage(), (int) (getDrawX() + (background.getX() - dashPosition)), (int) (getY()), (int) getWidth(), (int) getHeight(), true));
                    } else {
                        GraphicsPanel.getDashImages().add(new DashImage(getEntityImage(), (int) getDrawX(), (int) getY(), (int) getWidth(), (int) getHeight(), false));
                    }
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        simulateGravity();
        hitboxDetection();
    }
}