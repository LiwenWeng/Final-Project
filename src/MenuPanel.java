import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel implements ActionListener, MouseListener {
    Button startButton;
    Background background;
    Frame enclosingFrame;
    public MenuPanel(JFrame frame) {
        enclosingFrame = frame;
        startButton = new Button("buttons/playbutton", Constants.SCREEN_WIDTH / 2 - 100, Constants.SCREEN_HEIGHT / 2);
        background = new Background("tempmenu", 0, 0);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background.getBackgroundImage(), 0, 0, this);
        g.drawImage(startButton.getImage(), startButton.getxCoord(), startButton.getyCoord(), null);
    }

    public void actionPerformed(ActionEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (startButton.buttonRect().contains(e.getPoint())) {
                MainFrame f = new MainFrame();
                enclosingFrame.setVisible(false);
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
