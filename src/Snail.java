import java.awt.*;
import java.util.HashMap;
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

    public void checkForPlayer() {
        Point center = Utils.getCenterPos(entityRect());
        getAttackRangeRect().setLocation((int) (center.x - getAttackRangeRect().getWidth() / 2), (int) (center.y - getAttackRangeRect().getHeight() / 2));

        if (getAttackRangeRect().intersects(getPlayer().entityRect())) {
            setPlayerInRange(true);
            if (!getAnimations().get("hide").isReverse()) getAnimations().get("hide").reverse();
            playAnimation("hide", false);
        } else {
            setPlayerInRange(false);
            if (getAnimations().get("hide").isReverse()) getAnimations().get("hide").reverse();
            playAnimation("hide", "idle");
        }
    }

    public static Map<String, Animation> loadAnimations() {
        Map<String, Animation> snailAnimations = new HashMap<>();
        snailAnimations.put("idle", new Animation("idle", Animation.loadAnimation("snail/", "idle", 2, 2),200));
        snailAnimations.put("attack", new Animation("attack", Animation.loadAnimation("snail/", "attack", 2, 2),200));
        snailAnimations.put("run", new Animation("run", Animation.loadAnimation("snail/", "run", 2, 2),200));
        snailAnimations.put("hit", new Animation("hit", Animation.loadAnimation("snail/", "hit", 2, 2),100));
        snailAnimations.put("hide", new Animation("hide", Animation.loadAnimation("snail/", "hide", 2, 2),200));
        return snailAnimations;
    }

}
