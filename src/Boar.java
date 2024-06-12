import java.util.HashMap;
import java.util.Map;

public class Boar extends Enemy {
    public Boar(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 10, x, y, 500, 500, player, background, animations);
    }

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
    public void jump() {

    }

    public static Map<String, Animation> loadAnimations() {
         Map<String, Animation> boarAnimations = new HashMap<>();
         boarAnimations.put("idle", new Animation("idle", Animation.loadAnimation("boar/", "idle", 2, 2),200));
         boarAnimations.put("attack", new Animation("attack", Animation.loadAnimation("boar/", "attack", 2, 2),200));
         boarAnimations.put("run", new Animation("run", Animation.loadAnimation("boar/", "run", 2, 2),200));
         boarAnimations.put("hit", new Animation("hit", Animation.loadAnimation("boar/", "hit", 2, 2),200));
        return boarAnimations;
    }
}
