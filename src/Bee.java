import java.util.Map;

public class Bee extends Enemy {
    public Bee(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 10, x, y, 500, 500, player, background, animations);
    }
}
