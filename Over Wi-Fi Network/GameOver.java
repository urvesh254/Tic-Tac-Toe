import java.io.Serializable;

public class GameOver implements Serializable {
    public boolean isWin;
    public Player winPlayer = null;
    public int r1, r2, r3;
    public int c1, c2, c3;

    public GameOver() {
        this.isWin = false;
    }

    public GameOver(int r1, int c1, int r2, int c2, int r3, int c3) {
        this.isWin = true;
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
        this.r3 = r3;
        this.c3 = c3;
    }

}