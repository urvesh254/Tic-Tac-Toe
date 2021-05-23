import java.io.Serializable;

public class GameRunning implements Serializable {
	private static final long serialVersionUID = 1L;
	public int row;
	public int col;

	public GameRunning(int row, int col) {
		this.row = row;
		this.col = col;
	}
}
