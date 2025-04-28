import javax.swing.*;
import java.util.*;
public class Knight extends Piece {
    // constructors
    public Knight(char c, int l, int n){
        super(c, l, n);
        type = 'n';
        value = 3;
        if (color == 'b') {
            value *= -1;
        }
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    // custom methods
    @Override
    public Set<Coordinates> specificValidMoves() {
        // up left
        if (letterIndex - 1 >= 0 && numberIndex + 2 <= 7) {
            Coordinates coords = new Coordinates(letterIndex - 1, numberIndex + 2);
            validMoves.add(coords);
        }
        // up right
        if (letterIndex + 1 <= 7 && numberIndex + 2 <= 7) {
            Coordinates coords = new Coordinates(letterIndex + 1, numberIndex + 2);
            validMoves.add(coords);
        }
        // right up
        if (letterIndex + 2 <= 7 && numberIndex + 1 <= 7) {
            Coordinates coords = new Coordinates(letterIndex + 2, numberIndex + 1);
            validMoves.add(coords);
        }
        // right down
        if (letterIndex + 2 <= 7 && numberIndex - 1 >= 0) {
            Coordinates coords = new Coordinates(letterIndex + 2, numberIndex - 1);
            validMoves.add(coords);
        }
        // down right
        if (letterIndex + 1 <= 7 && numberIndex - 2 >= 0) {
            Coordinates coords = new Coordinates(letterIndex + 1, numberIndex - 2);
            validMoves.add(coords);
        }
        // down left
        if (letterIndex - 1 >= 0 && numberIndex - 2 >= 0) {
            Coordinates coords = new Coordinates(letterIndex - 1, numberIndex - 2);
            validMoves.add(coords);
        }
        // left down
        if (letterIndex - 2 >= 0 && numberIndex - 1 >= 0) {
            Coordinates coords = new Coordinates(letterIndex - 2, numberIndex - 1);
            validMoves.add(coords);
        }
        // left up 
        if (letterIndex - 2 >= 0 && numberIndex + 1 <= 7) {
            Coordinates coords = new Coordinates(letterIndex - 2, numberIndex + 1);
            validMoves.add(coords);
        }

        return validMoves;
    }
}
