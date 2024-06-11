import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel implements ActionListener, MouseListener {
    Button startButton;
    Background background;
    Frame enclosingFrame;
    BufferedImage img1;
    BufferedImage img2;
    BufferedImage title;
    double moveAmount;
    public MenuPanel(JFrame frame) {
        enclosingFrame = frame;
        startButton = new Button("buttons/play", Constants.SCREEN_WIDTH / 2 - 150, Constants.SCREEN_HEIGHT / 2);
        background = new Background("menu", 0, 0, 1, 1);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
        moveAmount = 0;

        try {
            img1 = ImageIO.read(new File("src/assets/animations/menu/menu1.png"));
            img2 = ImageIO.read(new File("src/assets/animations/menu/menu2.png"));
            title = ImageIO.read(new File("src/assets/title.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img1, (int) moveAmount, 0, this);
        g.drawImage(img2, (int) moveAmount + 1920, 0, this);
        g.drawImage(img1, (int) moveAmount + 1920 * 2, 0, this);
        moveAmount -= 1;
        if (moveAmount <= -1920 * 2) {
            moveAmount = 0;
        }
        g.drawImage(title, Constants.SCREEN_WIDTH / 2 - 720, Constants.SCREEN_HEIGHT / 2 - 360, null);
        g.drawImage(startButton.getImage(), startButton.getx(), startButton.gety(), null);
    }

    public void actionPerformed(ActionEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (startButton.buttonRect().contains(e.getPoint())) {
                MainFrame f = new MainFrame();
                enclosingFrame.setVisible(false);
                enclosingFrame.removeAll();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
