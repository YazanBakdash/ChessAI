import javax.swing.*;
import java.util.*;
public class Queen extends Piece{
    // constructors
    public Queen(char c, int l, int n){
        super(c, l, n);
        type = 'q';
        value = 9;
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
        Piece bishop = new Bishop(color, letterIndex, numberIndex);
        validMoves.addAll(bishop.findValidMoves());
        Piece rook = new Rook(color, letterIndex, numberIndex, true);
        validMoves.addAll(rook.findValidMoves());
        return validMoves;
    }
}
