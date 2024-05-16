import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {


    public GraphicsPanel(String name) { }

    @Override
    public void paintComponent(Graphics g) { }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void actionPerformed(ActionEvent e) { }
}
