import java.util.Map;

public class Snail extends Enemy {
    public Snail(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 10, x, y, 100, 100, player, background, animations);
    }
}
