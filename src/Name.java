import javax.swing.*;
import java.awt.*;

public class Name extends JLabel {
    public String title;
    public char color;
    public Name(String title, char color) {
        this.title = title;
        this.color = color;
        setText(title);
        setForeground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        setFont(new Font("Arial", Font.BOLD, 20));
    }
    public void updateScore(int score) {
        if (score == 0 || (color == 'w' && score < 0) || (color == 'b' && score > 0)) {
            setText(title);
        } else if (score > 0) {
            setText(title + " (+" + score + ")");
        } else {
            setText(title + " (+" + score * -1 + ")");
        }
    }
}
