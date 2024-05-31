import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private Background background;
    private Player player;
    private boolean[] pressedKeys;
    private Timer timer;
    private static ArrayList<Collidable> collidables = new ArrayList<>();

    public GraphicsPanel() {
        background = new Background("tempbackground", 0, 0);
        player = new Player(background);
        pressedKeys = new boolean[128];
        timer = new Timer(10, this);

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
        timer.addActionListener(this);

        collidables.add(new Collidable(400, 700, "src/assets/animations/jump/jump1.png")); //test
    }

    public static ArrayList<Collidable> getCollidables() {
        return collidables;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getBackgroundImage(), background.getXCoord(), background.getYCoord(), null);
        g.drawImage(player.getEntityImage(), (int) player.getDrawX(), (int) player.getY(), player.getWidth(), player.getHeight(), null);
        g.drawRect((int) player.entityRect().getX(), (int) player.entityRect().getY(), (int) player.entityRect().getWidth(), (int) player.entityRect().getHeight());

        for (Collidable collidable : collidables) {
            g.drawImage(collidable.getImage(), (int) collidable.getX(), (int) collidable.getY(), collidable.getWidth(), collidable.getHeight(), null);
            g.drawRect((int) collidable.collidableRect().getX(), (int) collidable.collidableRect().getY(), (int) collidable.collidableRect().getWidth(), (int) collidable.collidableRect().getHeight());
        }

        player.collided();
        player.simulateGravity();
        player.getCurrentPlayingAnim().start();
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (e.getSource() == timer) {
                if (pressedKeys[68]) {
                    player.faceRight();
                    player.moveRight();
                }
                if (pressedKeys[65]) {
                    player.faceLeft();
                    player.moveLeft();
                }
                if (pressedKeys[32] || pressedKeys[87]) {
                    player.jump();
                }

                if (pressedKeys[32] || pressedKeys[87]) {
                    player.playAnimation("jump");
                } else if (pressedKeys[68] || pressedKeys[65]) {
                    player.playAnimation("run");
                } else {
                    player.playAnimation("idle");
                }
            }
            
        }
 
    }
}
