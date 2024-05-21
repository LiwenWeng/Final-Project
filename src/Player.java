public class Player extends Entity {
    private double moveAmount;
    private String name;
    private Background background;

    public Player(Background background) {
        super(100, 10, Constants.SCREEN_WIDTH * 0.5, Constants.SCREEN_HEIGHT * 0.75, true, "idle");
        this.name = "joe";
        moveAmount = Constants.SCREEN_WIDTH * 0.001;
        this.background = background;
        isLeftLimit = false;
        isRightLimit = false;
        setPlayer(this);
    }

    public String getName() {
       return name;
    }

    public void moveRight() {
        if (isLimit()) {
            if (getX() + moveAmount <= Constants.SCREEN_WIDTH - getEntityImage().getWidth()) {
                setX(getX() + moveAmount);
            }
        } else {
            background.moveRight();
        }
    }

    public void moveLeft() {
        if (isLimit()) {
            if (getX() - moveAmount >= 0) {
                setX(getX() - moveAmount);
            }
        } else {
            background.moveLeft();
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

    public boolean isLimit() {
        if (getX() == Constants.SCREEN_WIDTH * 0.5) {
            return false;
        }
        if (background.isLeftLimit() || background.isRightLimit()) {
            return true;
        }
        return false;
    }

    public void swing() {

    }

    public void shoot() {

    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - damage);
    }
}
