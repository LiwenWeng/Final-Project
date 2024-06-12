import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Collidable {
    private double x;
    private double y;
    private int width;
    private int height;
    private double originalX;
    private double originalY;
    private static Map<String, ArrayList<Integer>> sidesCollided;
    private Background background;
    private Rectangle collidableRect;
    private Rectangle collidableRectTop;
    private Rectangle collidableRectBottom;
    private Rectangle collidableRectLeft;
    private Rectangle collidableRectRight;

    public Collidable(double x, double y, int width, int height, Background background) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.background = background;
        this.width = width;
        this.height = height;

        collidableRect = new Rectangle((int) x, (int) y, width, height);
        collidableRectTop = new Rectangle((int) x + 5, (int) y - 5, width - 10, 5);
        collidableRectBottom = new Rectangle((int) x + 5, (int) y + height, width - 10, 5);
        collidableRectLeft = new Rectangle((int) x - 5, (int) y + 5, 5, height - 10);
        collidableRectRight = new Rectangle((int) x + width, (int) y + 5, 5, height - 10);

        sidesCollided =  new HashMap<>();
        sidesCollided.put("top", new ArrayList<>());
        sidesCollided.put("bottom", new ArrayList<>());
        sidesCollided.put("left", new ArrayList<>());
        sidesCollided.put("right", new ArrayList<>());
    }

    public static Map<String, ArrayList<Integer>> getSidesCollided() {
        return sidesCollided;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Rectangle getCollidableRect() {
        collidableRect.setLocation((int) x, (int) y);
        return collidableRect;
    }

    public Rectangle getCollidableRectLeft() {
        collidableRectLeft.setLocation((int) x - 5, (int) y + 5);
        return collidableRectLeft;
    }

    public Rectangle getCollidableRectRight() {
        collidableRectRight.setLocation((int) x + width, (int) y + 5);
        return collidableRectRight;
    }

    public Rectangle getCollidableRectTop() {
        collidableRectTop.setLocation((int) x + 5, (int) y - 5);
        return collidableRectTop;
    }

    public Rectangle getCollidableRectBottom() {
        collidableRectBottom.setLocation((int) x + 5, (int) y + height);
        return collidableRectBottom;
    }

    public void updatePosition() {
        this.x = originalX + background.getX();
        this.y = originalY + background.getY();
    }
}
