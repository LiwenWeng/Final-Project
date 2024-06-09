import java.awt.image.BufferedImage;

public class DashImage {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage image;
    private float alpha;

    DashImage(BufferedImage image, int x, int y, int width, int height, boolean middle) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = width;
        this.height = height;
        if (middle) {
            if (GraphicsPanel.getDashImages().size() * 0.1f > 1) {
                alpha = 0f;
            } else {
                alpha = 1f - GraphicsPanel.getDashImages().size() * 0.1f;
            }
        } else {
            this.alpha = 0.8f;
        }
        Utils.startThread(() -> {
            for (int i = 0; i < 10; i++) {
                if (alpha - 0.1 >= 0) {
                    alpha -= 0.1f;
                } else {
                    alpha = 0f;
                }
                Utils.wait(50);
            }
        });
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    
}
