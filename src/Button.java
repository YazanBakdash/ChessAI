import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {
    public Button() {
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setText("Close Game");
        setForeground(Color.white);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBackground(new Color(115, 149, 82));
        addActionListener(e -> {
            System.exit(0); // Close the entire program
        });
    }
}
