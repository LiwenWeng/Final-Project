import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        if (Collidable.getSidesCollided()[3] || (isHitboxActive() && isGrounded()) || isDashing) return;
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
        if (Collidable.getSidesCollided()[2] || (isHitboxActive() && isGrounded()) || isDashing) return;
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
            Utils.delay(400, (t) -> {
                isDashing = false;
                dashRight = false;
            }, 1);
            Utils.delay(2000, (t) -> {
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
            Utils.delay(400, (t) -> {
                isDashing = false;
                dashLeft = false;
            }, 1);
            Utils.delay(2000, (t) -> {
                canDash = true;
            }, 1);
        }
    }

    public void jump() {
        if (isDashing) return;
        if (canDoubleJump && !Collidable.getSidesCollided()[0]) {
            setGravity(Constants.SCREEN_HEIGHT * 0.006);
            canDoubleJump = false;
            doubleJumped = true;
            getJump().reset();
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
        if (isGrounded() || isDashing) return;
        setGravity(getGravity() - Constants.SCREEN_HEIGHT * 0.0002 );
        if (!doubleJumped) {
            doubleJumped = true;
            Utils.delay(200,(t) -> {
                canDoubleJump = true;
            } , 1);
        }
        background.setY(background.getY() + getGravity());
        if (Collidable.getSidesCollided()[1]) {
            setAirCollided(false);
            setGrounded(true);
            doubleJumped = false;
            for (Collidable collidable : GraphicsPanel.getCollidables()) {
                if (collidable.collidableRectTop().intersects(entityRect()) && getGravity() < 0) {
                    background.setY(background.getY() + -getGravity() + ((entityRect().getY() + entityRect().getHeight()) - collidable.getY()));
                }
            }
            setGravity(0);
        }
        for (Enemy enemy : GraphicsPanel.getEnemies()) {
            enemy.setY(enemy.getY() + getGravity());
        }
    }

    @Override
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
                    if (Collidable.getSidesCollided()[3] || isHitboxActive()) return;
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
                    if (Collidable.getSidesCollided()[2] || isHitboxActive()) return;
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
}
