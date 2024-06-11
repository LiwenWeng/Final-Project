import java.util.Map;

public class Snail extends Enemy {
    public Snail(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 10, x, y, 200, 200, player, background, animations);
    }

    @Override
    public void reconcileHitbox() {
        if (isFacingRight()) {
            entityRect().setLocation((int) (getX() + getWidth() * 0.25), (int) (getY() + getHeight() * 0.4));
            getAttackHitbox().setLocation((int) (getX() + entityRect().getWidth() * 1.375), (int) getY());
        } else {
            entityRect().setLocation((int) (getX() - getWidth() * 0.25), (int) (getY() + getHeight() * 0.4));
            getAttackHitbox().setLocation((int) (getX() - entityRect().getWidth() * 0.375), (int) getY());
        }
    }

    @Override
    public void defaultMovement() {

    }
}
