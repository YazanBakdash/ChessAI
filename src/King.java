import javax.swing.*;
import java.util.*;
public class King extends Piece {
    boolean hasMoved;
    // constructors
    public King(char c, int l, int n){
        super(c, l, n);
        Board.kings.put(color, new Coordinates(l, n));
        type = 'k';
        hasMoved = false;
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    @Override
    public void setHasMoved(boolean moved) {
        hasMoved = moved;
    }
    @Override
    public boolean getHasMoved() {
        return hasMoved;
    }

    // custom methods
    @Override
    public Set<Coordinates> specificValidMoves() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (letterIndex + i <= 7 && letterIndex + i >= 0 && numberIndex + j <= 7 && numberIndex + j >= 0 && !(i == 0 && j == 0) && isSafe(letterIndex + i, numberIndex + j, color)) {
                    Coordinates coords = new Coordinates(letterIndex + i, numberIndex + j);
                    validMoves.add(coords);
                }
            }
        }
        // king side castle
        if (!hasMoved && Board.pieces[7][numberIndex].getType() == 'r' && isSafe(letterIndex, numberIndex, color) && Board.pieces[5][numberIndex].getColor() == 'e' && isSafe(5, numberIndex, color) && Board.pieces[6][numberIndex].getColor() == 'e' && isSafe(6, numberIndex, color) && !Board.pieces[7][numberIndex].getHasMoved()) {
            Coordinates coords = new Coordinates(6, numberIndex);
            validMoves.add(coords);
        }
        // queen side castle
        if (!hasMoved && Board.pieces[0][numberIndex].getType() == 'r' && isSafe(letterIndex, numberIndex, color) && Board.pieces[1][numberIndex].getColor() == 'e' && isSafe(1, numberIndex, color) && Board.pieces[2][numberIndex].getColor() == 'e' && isSafe(2, numberIndex, color) && Board.pieces[3][numberIndex].getColor() == 'e' && isSafe(3, numberIndex, color) && !Board.pieces[0][numberIndex].getHasMoved()) {
            Coordinates coords = new Coordinates(2, numberIndex);
            validMoves.add(coords);
        }
        return validMoves;
    }
    @Override
    public void move(int newX, int newY) {
        // kingside castle
        if (!hasMoved && newX == 6 && letterIndex == 4) {
            Board.pieces[7][numberIndex].move(5, numberIndex);
        }
        // queenside castle
        if (!hasMoved && newX == 2 && letterIndex == 4) {
            Board.pieces[0][numberIndex].move(3, numberIndex);
        }
        super.move(newX, newY);
        Coordinates coords = new Coordinates(newX, newY);
        Board.kings.put(color, coords);
        hasMoved = true;
    }
}
