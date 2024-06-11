import java.awt.*;
import java.util.Map;

public class Snail extends Enemy {
    public Snail(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 5, x, y, 300, 300, player, background, animations);
    }

    @Override
    public void reconcileHitbox() {
        if (isFacingRight()) {
            entityRect().setLocation((int) (getX() + getWidth() * 0.25), (int) (getY() + getHeight() * 0.4));
            getAttackHitbox().setLocation((int) (getX() - entityRect().getWidth() * 0.375), (int) getY());
        } else {
            entityRect().setLocation((int) (getX() - getWidth() * 0.25), (int) (getY() + getHeight() * 0.4));
            getAttackHitbox().setLocation((int) (getX() + entityRect().getWidth() * 1.375), (int) getY());
        }
    }

    @Override
    public void defaultMovement() {

    }

    @Override
    public void jump() {

    }

    public void checkForPlayer() {
        Point center = Utils.getCenterPos(entityRect());
        getAttackRangeRect().setLocation((int) (center.x - getAttackRangeRect().getWidth()/2), (int) (center.y - getAttackRangeRect().getHeight()/2));

        if (getAttackRangeRect().intersects(getPlayer().entityRect())) {
            setPlayerInRange(true);
            if (getAnimations().get("hide").isReverse()) getAnimations().get("hide").reverse();
            playAnimation("hide", false);
            attack();
        } else {
            setPlayerInRange(false);
            if (!getAnimations().get("hide").isReverse()) getAnimations().get("hide").reverse();
            playAnimation("hide", "idle");
        }
    }
}
