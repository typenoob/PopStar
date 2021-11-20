import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.BoxLayout;

public class MainFrame extends JFrame {
    private static JLabel background, user;
    private static JPanel menu;
    private static JButton start, proceed, rank;
    private static JTextArea name;
    private static GamePanel gp;
    private static final long serialVersionUID = 1L;

    MainFrame() {
        this.setTitle("PopStar");
        this.setSize(480, 800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        user = new JLabel("USER");
        user.setBounds(20, 20, 50, 20);
        user.setForeground(Color.WHITE);
        name = new JTextArea("unknown");
        name.setBounds(80, 20, 80, 20);
        menu = new JPanel();
        menu.setBounds(198, 0, 100, 120);
        menu.setBackground(null);
        menu.setOpaque(false);
        background = new JLabel(new ImageIcon("res/image/bg.png"));
        background.setBounds(0, 0, this.getWidth(), this.getHeight());
        start = new JButton("  START  ");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableAll(false);
                gp = new GamePanel(MainFrame.this);
                proceed.setVisible(true);
            }
        });
        proceed = new JButton("COTINUE");
        proceed.setVisible(false);
        proceed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableAll(false);
                gp.enableAll(true);
            }
        });
        rank = new JButton("HIGHLIST");
        rank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ScoreBoard(MainFrame.this);
            }
        });
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.add(start);
        menu.add(Box.createVerticalStrut(10));
        menu.add(proceed);
        menu.add(Box.createVerticalStrut(10));
        menu.add(rank);
        this.setLayout(null);
        this.add(menu);
        this.add(background);
        this.add(user, 0);
        this.add(name, 0);
        this.setVisible(true);
    }

    public static void enableAll(boolean enable) {
        menu.setVisible(enable);
        name.setVisible(enable);
        user.setVisible(enable);
    }

    public static void Conceal() {
        proceed.setVisible(false);
    }

    public static String getUser() {
        return name.getText();
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
