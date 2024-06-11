import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private Background background;
    private Player player;
    private boolean[] pressedKeys;
    private Timer timer;
    private static ArrayList<Collidable> collidables = new ArrayList<>();
    private static ArrayList<Enemy> enemies = new ArrayList<>();
    private static ArrayList<DashImage> dashImages = new ArrayList<>();
    private boolean dashed;
    private boolean tapRight;
    private boolean tapRightAgain;
    private boolean tapLeft;
    private boolean tapLeftAgain;
    private ArrayList<UI> UIList;
    private int orginalHealth;
    private boolean dead;
    private boolean deadGravity;

    public GraphicsPanel() {
        Map<String, Animation> playerAnimations = new HashMap<>();
        playerAnimations.put("idle", new Animation("idle", Animation.loadAnimation("player/", "idle", 2, 2),200));
        playerAnimations.put("jump", new Animation("jump", Animation.loadAnimation("player/", "jump", 2, 2),100));
        playerAnimations.put("run", new Animation("run", Animation.loadAnimation("player/", "run", 2, 2),100));
        playerAnimations.put("attack", new Animation("attack", Animation.loadAnimation("player/", "attack", 2, 2),100));
        playerAnimations.put("dash", new Animation("dash", Animation.loadAnimation("player/", "dash", 2, 2),100));
        playerAnimations.put("dead", new Animation("dead", Animation.loadAnimation("player/", "dead", 2, 2),100));

        background = new Background("levelbackground", 0, -1080, 1, 1);
        player = new Player(background, playerAnimations);
        pressedKeys = new boolean[128];
        timer = new Timer(20, this);
        dashed = false;
        tapRight = false;
        tapLeft = false;
        tapRightAgain = false;
        tapLeftAgain = false;
        dead = false;
        deadGravity = false;
        UIList = new ArrayList<>();
        UIList.add(new UI("yellowrhombus", 50, 50, 4, 4));
        UIList.add(new UI("healthbar", 155, 90, 4, 4));
        UIList.add(new UI("healthbaroutline", 151, 86, 4, 4));
        orginalHealth = player.getHealth();

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
        timer.addActionListener(this);

        collidables.add(new Collidable(0, 875 - background.getOriginalY(), 1400, 200, background));
        collidables.add(new Collidable(1380, 730 - background.getOriginalY(), 50, 200, background));
        collidables.add(new Collidable(1230, 700 - background.getOriginalY(), 210, 30, background));
        collidables.add(new Collidable(1070, 640 - background.getOriginalY(), 145, 30, background));
        collidables.add(new Collidable(735, 540 - background.getOriginalY(), 240, 40, background));
        collidables.add(new Collidable(1060, 310 - background.getOriginalY(), 250, 30, background));
        collidables.add(new Collidable(1310, 410 - background.getOriginalY(), 610, 310, background));

        // Map<String, Animation> boarAnimations = new HashMap<>();
        // boarAnimations.put("idle", new Animation("idle", Animation.loadAnimation("boar/", "idle", 2, 2),200));
        // boarAnimations.put("attack", new Animation("attack", Animation.loadAnimation("boar/", "attack", 2, 2),200));
        // boarAnimations.put("run", new Animation("run", Animation.loadAnimation("boar/", "run", 2, 2),200));
        // boarAnimations.put("hit", new Animation("hit", Animation.loadAnimation("boar/", "hit", 2, 2),200));
        // enemies.add(new Boar(400, 700, player, background, boarAnimations));

        // Map<String, Animation> beeAnimations = new HashMap<>();
        // beeAnimations.put("idle", new Animation("idle", Animation.loadAnimation("bee/", "idle", 2, 2),200));
        // beeAnimations.put("attack", new Animation("attack", Animation.loadAnimation("bee/", "attack", 2, 2),200));
        // beeAnimations.put("run", new Animation("run", Animation.loadAnimation("bee/", "run", 2, 2),200));
        // beeAnimations.put("hit", new Animation("hit", Animation.loadAnimation("bee/", "hit", 2, 2),200));
        // enemies.add(new Bee(800, 800, player, background, beeAnimations));

        enemies.add(new Snail(2540, -780, player, background, Snail.loadAnimations()));
        enemies.add(new Snail(1100, 2300, player, background, Snail.loadAnimations()));
//        for (int i = 0; i < 1; i++) {
//            enemies.add(new Snail(500 + i * 400, -1000, player, background, Snail.loadAnimations()));
//        }
    }

    public static ArrayList<Collidable> getCollidables() {
        return collidables;
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public static ArrayList<DashImage> getDashImages() {
        return dashImages;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getBackgroundImage(), (int) background.getX(), (int) background.getY(), null);
        g.drawImage(player.getEntityImage(), (int) player.getDrawX(), (int) player.getY(), player.getWidth(), player.getHeight(), null);
        g.drawRect((int) player.entityRect().getX(), (int) player.entityRect().getY(), (int) player.entityRect().getWidth(), (int) player.entityRect().getHeight());
        g.drawRect((int) player.getAttackHitbox().getX(), (int) player.getAttackHitbox().getY(), (int) player.getAttackHitbox().getWidth(), (int) player.getAttackHitbox().getHeight());

        for (Collidable collidable : collidables) {
            collidable.updatePosition();
            g.drawRect((int) collidable.collidableRect().getX(), (int) collidable.collidableRect().getY(), (int) collidable.collidableRect().getWidth(), (int) collidable.collidableRect().getHeight());
        }

        for (Enemy enemy : enemies) {
            if (enemy.isDead()) enemies.remove(enemy);
            g.drawImage(enemy.getEntityImage(), (int) enemy.getDrawX(), (int) enemy.getY(), enemy.getWidth(), enemy.getHeight(), null);
            g.drawRect((int) enemy.entityRect().getX(), (int) enemy.entityRect().getY(), (int) enemy.entityRect().getWidth(), (int) enemy.entityRect().getHeight());
            g.drawRect((int) enemy.getAttackHitbox().getX(), (int) enemy.getAttackHitbox().getY(), (int) enemy.getAttackHitbox().getWidth(), (int) enemy.getAttackHitbox().getHeight());
            g.drawRect((int) enemy.getAttackRangeRect().getX(), (int) enemy.getAttackRangeRect().getY(), (int) enemy.getAttackRangeRect().getWidth(), (int) enemy.getAttackRangeRect().getHeight());
            enemy.start();
        }
        for (UI ui : UIList) {
            if (ui.getName().equals("healthbar")) {
                g.drawImage(ui.getImage(), ui.getx(), ui.gety(), player.getHealth() >= 0 ? (int) (ui.getWidth() - ((orginalHealth - player.getHealth()) * (ui.getWidth() / (double) orginalHealth))) : 0, ui.getHeight(), this);
            } else {
                g.drawImage(ui.getImage(), ui.getx(), ui.gety(), ui.getWidth(), ui.getHeight(), this);
            }
        }

        for (DashImage dashImage : dashImages) {
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, dashImage.getAlpha());
            ((Graphics2D) g).setComposite(ac);
            g.drawImage(dashImage.getImage(), dashImage.getX(), dashImage.getY(), dashImage.getWidth(), dashImage.getHeight(), this);
        }

        if (player.isDead()) {
            Utils.delay(3000, (t) -> {
                dead = true;
            }, 1);
        }
        if (dead) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 10000, 10000);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 50));
            g.drawString("Game Over", 100, 100);
        }

        if (enemies.isEmpty()) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 10000, 10000);
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier New", Font.BOLD, 50));
            g.drawString("VICTORY", 100, 100);
        }
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        try {
           pressedKeys[key] = true;
        }
        catch (ArrayIndexOutOfBoundsException f) {

        }
        if (player.isDead()) return;
        if (tapRightAgain && key == 68) {
            player.faceRight();
            player.dashRight();
        }
        if (key == 68) {
            tapRight = true;
            Utils.delay(100, (t) -> {
                tapRight = false;
            }, 1);
        }
        if (tapLeftAgain && key == 65) {
            player.faceLeft();
            player.dashLeft();
        }
        if (key == 65) {
            tapLeft = true;
            Utils.delay(100, (t) -> {
                tapLeft = false;
            }, 1);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        try {
            pressedKeys[key] = false;
         }
         catch (ArrayIndexOutOfBoundsException f) {

         }
        if (tapRight && key == 68) {
            tapRightAgain = true;
            Utils.delay(100, (t) -> {
                tapRightAgain = false;
            }, 1);
        }
        if (tapLeft && key == 65) {
            tapLeftAgain = true;
            Utils.delay(100, (t) -> {
                tapLeftAgain = false;
            }, 1);
        }
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) {
        player.attack();
        System.out.println((e.getPoint().getX() - background.getX()) + " " + (e.getPoint().getY() - (background.getY() + 1680)));
        player.takeDamage(20);
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (e.getSource() == timer) {
                if (player.isDead()) {
                    Utils.delay(1000, (t) -> {
                        deadGravity = true;
                    }, 1);;
                }
                if (deadGravity) return;
                player.start();
                if (background.getY() <= background.getOriginalY() - 10) {
                    player.takeDamage(10);
                }

                if (player.isDead()) return;
                if (pressedKeys[68]) {
                    player.faceRight();
                    player.moveRight();
                }

                if (pressedKeys[65]) {
                    player.faceLeft();
                    player.moveLeft();
                }

                if (pressedKeys[32]) {
                    player.jump();
                }
                if (player.isDashing()) {
                    player.playAnimation("dash", false);
                } else if (player.isHitboxActive()) {
                    player.playAnimation("attack", true);
                } else if (!player.isGrounded() || pressedKeys[32]) {
                    player.playAnimation("jump", true);
                } else if (pressedKeys[68] || pressedKeys[65]) {
                    player.playAnimation("run", true);
                } else {
                    player.playAnimation("idle", true);
                }
                if (player.isDashing()) {
                    dashed = true;
                }
                if (dashed) {
                    Utils.delay(1000, (t) -> {
                        dashImages = new ArrayList<>();
                    }, 1);
                    dashed = false;
                }
            }
        }
 
    }
}
