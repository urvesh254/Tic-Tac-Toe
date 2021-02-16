import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class Button extends JButton {
    public static final Color WIN_COLOR = new Color(66, 217, 20);
    int row, col;

    public Button(String text, int row, int col) {
        super(text);
        this.row = row;
        this.col = col;
        setFont(new Font("MV Boli", Font.BOLD, 50));
    }

    static boolean equals(Button b1, Button b2, Button b3) {
        return b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    @Override
    public String toString() {
        return getText();
    }
}
