import javax.swing.*;

public class MenuFrame {
    private MenuPanel panel;

    public MenuFrame() {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);

        panel = new MenuPanel(frame);
        frame.add(panel);

        frame.setVisible(true);
    }
}
