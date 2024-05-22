public class Player extends Entity {
    private double moveAmount;
    private String name;
    private Background background;
    private boolean isLeftLimit;
    private boolean isRightLimit;

    public Player(Background background) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.75, true, "idle");
        this.name = "joe";
        moveAmount = Constants.SCREEN_HEIGHT * 0.002;
        this.background = background;
        setPlayer(this);
        isLeftLimit = false;
        isRightLimit = false;

    }

    public String getName() {
       return name;
    }

    public void moveRight() {
        if (isLeftLimit || isRightLimit) {
            if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
                setX(getX() + moveAmount);
            }
        } else {
            background.moveLeft();
        }
    }

    public void moveLeft() {
        if (isLeftLimit || isRightLimit) {
            if (getX() - moveAmount >= 0) {
                setX(getX() - moveAmount);
            }
        } else {
            background.moveRight();
        }
    }

    public void jump() {
        if (!isGrounded()) return;
        setGrounded(false);
        setGravity(3.5);
    }

    public void simulateGravity() {
        if (isGrounded()) return;
        setGravity(getGravity() - 0.05);
        setY(getY() - getGravity());
        if (getY() >= Constants.SCREEN_HEIGHT * 0.75) {
            setGrounded(true);
        }
    }

    public void isLimit() {
        if (background.isLeftLimit()) {
            isLeftLimit = true;
        } else if (getDrawX() >= Constants.SCREEN_WIDTH / 2 - 20) {
            isLeftLimit = false;
        }
        if (background.isRightLimit()) {
            isRightLimit = true;
        } else if (getDrawX() <= Constants.SCREEN_WIDTH / 2 - 20) {
            isRightLimit = false;
        }
    }

    public void swing() {

    }

    public void shoot() {

    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - damage);
    }
}
