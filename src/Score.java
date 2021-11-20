import java.lang.Math;

public class Score {
    private int target, best, present, toadd, number;
    private GamePanel gp;

    Score(GamePanel gp) {
        this.gp = gp;
        target = 1000;
        present = 0;
        best = 0;
        number = 1;
    }

    public void Calculate() {
        toadd = (int) (5 * Math.pow(gp.stars.getCount(), 2));
    }

    public void addScore() {
        present += toadd;
    }

    public void addScore(int toadd) {
        present += toadd;
    }

    public void Update() {
        if (target == 1000)
            target += 2000;
        else
            target += 3000;
        number += 1;
    }

    public String getTarget() {
        return Integer.toString(target);
    }

    public String getPresent() {
        return Integer.toString(present);
    }

    public String getBest() {
        return Integer.toString(best);
    }

    public String getToadd() {
        return Integer.toString(toadd);
    }

    public String getNumber() {
        return Integer.toString(number);
    }

    public String getCount() {
        return Integer.toString(gp.stars.getCount());
    }

    public boolean Reach() {
        return present >= target;
    }
}