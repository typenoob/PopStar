import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.awt.Font;
import java.sql.*;

//偏移量-73，-70
public class GamePanel {
    private JPanel mainboard;
    private JButton pause;
    private JButton[][] starbt;
    private ImageIcon[] color;
    private ImageIcon[] color_;
    private JLabel scorebar, best, number, target, score, info, bonus;
    protected Stars stars;
    protected Score sc;
    private final static int OFFSETWIDTH = -36;// 宽度补偿参数
    private final static int OFFSETHEIGHT = -12;// 宽度补偿参数
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://101.132.79.142:3306/Ranking?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "password";
    private ResultSet rs;
    private String sql;
    private static Connection conn = null;
    private static Statement stmt = null;

    GamePanel(MainFrame jf) {
        sc = new Score(this);
        stars = new Stars(this);
        scorebar = new JLabel(new ImageIcon("res/image/ps_bar.png"));
        scorebar.setBounds(-10, -70, jf.getWidth(), jf.getHeight() - jf.getWidth() + OFFSETWIDTH);
        pause = new JButton();
        pause.setBounds(419, 89, 25, 32);
        pause.setOpaque(false);
        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PauseLog(jf, GamePanel.this);
            }
        });
        best = new JLabel(getBest(MainFrame.getUser()));
        best.setHorizontalAlignment(JLabel.CENTER);
        best.setForeground(Color.WHITE);
        best.setBounds(196, 28, 189, 20);
        best.setFont(getFont());
        number = new JLabel();
        number.setHorizontalAlignment(JLabel.CENTER);
        number.setForeground(Color.WHITE);
        number.setBounds(108, 57, 25, 20);
        number.setFont(getFont());
        target = new JLabel();
        target.setHorizontalAlignment(JLabel.CENTER);
        target.setForeground(Color.WHITE);
        target.setBounds(275, 56, 155, 20);
        target.setFont(getFont());
        score = new JLabel();
        score.setHorizontalAlignment(JLabel.CENTER);
        score.setBounds(200, 114, 100, 20);
        score.setForeground(Color.WHITE);
        score.setFont(getFont());
        info = new JLabel();
        info.setBounds(160, 140, 140, 20);
        info.setForeground(Color.WHITE);
        info.setFont(getFont());
        bonus = new JLabel();
        bonus.setBounds(160, 170, 140, 20);
        bonus.setForeground(Color.WHITE);
        bonus.setFont(getFont());
        mainboard = new JPanel();
        mainboard.setBounds(0, jf.getHeight() - jf.getWidth() + OFFSETWIDTH, jf.getWidth() + OFFSETHEIGHT,
                jf.getWidth());
        mainboard.setBackground(null);
        mainboard.setOpaque(false);
        color = new ImageIcon[5];
        color_ = new ImageIcon[5];
        for (int i = 0; i < 5; i++) {
            color[i] = pattern(i);
            color_[i] = pattern_(i);
        }
        starbt = new JButton[Stars.HEIGHT + 1][Stars.WIDTH + 1];
        mainboard.setLayout(new GridLayout(Stars.HEIGHT, Stars.WIDTH));
        for (int i = 1; i <= Stars.HEIGHT; i++)
            for (int j = 1; j <= Stars.WIDTH; j++) {
                starbt[i][j] = new JButton();
                starbt[i][j].setRolloverEnabled(false);
                starbt[i][j].addActionListener(new StarsListener(i, j, this));
                mainboard.add(starbt[i][j]);
            }
        Refresh();
        jf.setLayout(null);
        jf.add(scorebar, 0);
        jf.add(pause);
        jf.add(best, 0);
        jf.add(number, 0);
        jf.add(target, 0);
        jf.add(score, 0);
        jf.add(info, 0);
        jf.add(bonus, 0);
        jf.add(mainboard, 0);
    }

    private static Font getFont() {
        try {
            InputStream is = new FileInputStream(new File("res/fonts/Marker Felt.ttf"));
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, 15);
            return font;
        } catch (Exception e) {
            throw new RuntimeException("字体不存在");
        }
    }

    private ImageIcon pattern(int color) {
        switch (color) {
            case Stars.RED:
                return new ImageIcon("res/image/star_red.jpg");
            case Stars.GREEN:
                return new ImageIcon("res/image/star_green.jpg");
            case Stars.BLUE:
                return new ImageIcon("res/image/star_blue.jpg");
            case Stars.YELLOW:
                return new ImageIcon("res/image/star_yellow.jpg");
            case Stars.PURPLE:
                return new ImageIcon("res/image/star_purple.jpg");
            default:
                return new ImageIcon();
        }
    }

    private ImageIcon pattern_(int color) {
        switch (color) {
            case Stars.RED:
                return new ImageIcon("res/image/star_red_.jpg");
            case Stars.GREEN:
                return new ImageIcon("res/image/star_green_.jpg");
            case Stars.BLUE:
                return new ImageIcon("res/image/star_blue_.jpg");
            case Stars.YELLOW:
                return new ImageIcon("res/image/star_yellow_.jpg");
            case Stars.PURPLE:
                return new ImageIcon("res/image/star_purple_.jpg");
            default:
                return new ImageIcon();
        }
    }

    public void enableAll(boolean enable) {
        scorebar.setVisible(enable);
        pause.setVisible(enable);
        best.setVisible(enable);
        number.setVisible(enable);
        target.setVisible(enable);
        score.setVisible(enable);
        info.setVisible(enable);
        bonus.setVisible(enable);
        mainboard.setVisible(enable);
    }

    public void Border(int h, int w) {
        starbt[h][w].setIcon(color_[stars.getStatusAt(h, w)]);
    }

    public void unBorder(int h, int w) {
        starbt[h][w].setIcon(color[stars.getStatusAt(h, w)]);
    }

    public void setInfo() {
        info.setText(sc.getCount() + "  BLOCKS  " + sc.getToadd() + "  POINTS");
    }

    public void Refresh() {
        score.setText(sc.getPresent());
        target.setText(sc.getTarget());
        number.setText(sc.getNumber());
        for (int i = 1; i <= Stars.HEIGHT; i++)
            for (int j = 1; j <= Stars.WIDTH; j++) {
                switch (stars.getStatusAt(i, j)) {
                    case Stars.STOP:
                        starbt[i][j].setVisible(false);
                        break;
                    default:
                        starbt[i][j].setVisible(true);
                        starbt[i][j].setIcon(color[stars.getStatusAt(i, j)]);
                }
            }
    }

    public void nextLevel(int bonus) {
        if (bonus > 0)
            sc.addScore(bonus);
        this.bonus.setText(null);
        Refresh();
        if (sc.Reach()) {
            sc.Update();
            JOptionPane.showMessageDialog(null, "新的目标:" + sc.getTarget(), "恭喜进入下一关！", JOptionPane.WARNING_MESSAGE);
            stars = (new Stars(this));
            Refresh();
        } else {
            Record(MainFrame.getUser(), sc.getPresent()); // 数据存入数据库
            JOptionPane.showMessageDialog(null, "目标未达成，大侠请下次再来！", "游戏结束", JOptionPane.ERROR_MESSAGE);
            enableAll(false);
            MainFrame.Conceal();
            MainFrame.enableAll(true);
        }
    }

    public void Save() {
        enableAll(false);
        MainFrame.enableAll(true);
    }

    public void setBonus(String bonus) {
        this.bonus.setText(bonus);
    }

    public String getBest(String name) {
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "SELECT * FROM rec" + " WHERE name='" + name + "' ORDER BY score desc";
            rs = stmt.executeQuery(sql);
            rs.next();
            String bs = Integer.toString(rs.getInt("score"));
            rs.close();
            stmt.close();
            conn.close();
            return bs;
        } catch (SQLException se) {
            // 数据库中每有记录，返回0
            return "0";
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return null;
    }

    public void Record(String name, String score) {
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            sql = "INSERT INTO rec(name,score,his) VALUES('" + name + "','" + score + "'," + "now()" + ")";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }

}