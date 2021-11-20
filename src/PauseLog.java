import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class PauseLog extends JDialog {
    private JButton save, ret;
    private JLabel tip;

    PauseLog(JFrame jf, GamePanel gp) {
        super(jf, true);
        save = new JButton("save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gp.Save();
                PauseLog.this.dispose();
            }
        });
        ret = new JButton("ret");
        ret.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PauseLog.this.dispose();
            }
        });
        tip = new JLabel("PAUSED", JLabel.CENTER);
        this.setUndecorated(true);
        this.setLayout(new BorderLayout(3, 3));
        this.add(tip, BorderLayout.NORTH);
        this.add(save, BorderLayout.WEST);
        this.add(ret, BorderLayout.EAST);
        this.setSize(150, 100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

}
