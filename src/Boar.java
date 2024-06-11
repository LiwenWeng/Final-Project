import java.util.Map;

public class Boar extends Enemy {
    public Boar(double x, double y, Player player, Background background, Map<String, Animation> animations) {
        super(100, 10, x, y, 500, 500, player, background, animations);
    }
}
