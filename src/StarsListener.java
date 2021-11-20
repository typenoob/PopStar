import java.awt.event.*;

public class StarsListener implements ActionListener {
    private int h, w;
    public static boolean restrict = false;
    private GamePanel gp;

    StarsListener(int h, int w, GamePanel gp) {
        this.h = h;
        this.w = w;
        this.gp = gp;
    }

    public void actionPerformed(ActionEvent e) {
        if (restrict)
            return;
        if (gp.stars.getStatusAt(h, w) >= Stars.READY) {
            gp.stars.Pop(h, w);
            gp.stars.Fall();
            gp.stars.Checkout();
        } else {
            gp.stars.Release();
            gp.stars.Mark(h, w);
            gp.stars.Prepare();
        }
        // stars.printTrace();
    }
}
