import javax.swing.*;
import java.util.*;

public class Piece extends JLabel {
    protected char type;
    protected char color;
    public int letterIndex;
    public int numberIndex;
    public char pinned; //'v': vertical, 'h': horizontal, 'd': descending diagonal, 'a': ascending diagonal, 'n': no pin
    public Set<Coordinates> validMoves;
    public int value;

    public Piece(char c, int x, int y) {
        color = c;
        letterIndex = x;
        numberIndex = y;
        type = 'e';
        pinned = 'n';
        value = 0;
        validMoves = new HashSet<>();

        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);

        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
    }

    // setters and getters
    public char getColor() {
        return color;
    }
    public char getType() {
        return type;
    }
    public int getLetterIndex() {
        return letterIndex;
    }
    public int getNumberIndex() {
        return numberIndex;
    }
    public Set<Coordinates> getValidMoves() {
        return validMoves;
    }
    public void setLetterIndex(int i) {
        letterIndex = i;
    }
    public void setNumberIndex(int i) {
        numberIndex = i;
    }
    // to be overridden in pawn class
    public void setEnPassant(int n) {
        return;
    }
    // to be overridden in rook and king class
    public void setHasMoved(boolean moved) {
        return;
    }
    // to be overridden in rook class
    public boolean getHasMoved() {
        return true;
    }
    // to be overridden in pawn class
    public int getEnPassant() {
        return -1;
    }

    public void setType(char newType) {
        ImageIcon image = new ImageIcon("src/resources/pieces/" + color + type + ".png");
        this.setIcon(image);
        type = newType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return letterIndex == piece.getLetterIndex() && numberIndex == piece.getNumberIndex() && type == piece.getType() && color == piece.getColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(letterIndex, numberIndex);
    }

    @Override
    public String toString() {
        return "" + color + type + " [" + letterIndex + ", " + numberIndex + "]";
    }

    public void nonspecificValid() {
        // if it's check
        if (color != Board.turn && !Board.onlyPossibilities.isEmpty() && type != 'k') {
            validMoves.removeIf(validMove -> !Board.onlyPossibilities.contains(validMove));
        }

        Iterator<Coordinates> iterator = validMoves.iterator();
        while (iterator.hasNext()) {
            Coordinates validMove = iterator.next();
            if (Board.pieces[validMove.x][validMove.y].getColor() == this.color && Board.turn != color) {
                iterator.remove();
                continue;
            }
            if (pinned == 'v' && validMove.x != letterIndex) {
                iterator.remove();
            }
            else if (pinned == 'h' && validMove.y != numberIndex) {
                iterator.remove();
            }
            else if (pinned == 'a' && validMove.y - numberIndex != validMove.x - letterIndex) {
                iterator.remove();
            }
            else if (pinned == 'd' && validMove.y - numberIndex != letterIndex - validMove.x) {
                iterator.remove();
            }
        }
        pinned = 'n';
    }

    public Set<Coordinates> findValidMoves() {
        validMoves = specificValidMoves();
        nonspecificValid();
        if (Board.turn == color) {
            Coordinates enemyKing = Board.kings.get(enemyColor(color));
            for (Coordinates validMove : validMoves) {
                if (validMove.equals(enemyKing)) {
                    Board.kingThreats.add(new Coordinates(letterIndex, numberIndex));
                    if (type == 'n' || type == 'p') {
                        Board.onlyPossibilities.add(new Coordinates(letterIndex, numberIndex));
                    }
                }
            }
        }

        return validMoves;
    }

    public void printValidMoves() {
        System.out.print("" + color + type + " valid moves: ");
        for (Coordinates validMove: validMoves) {
            System.out.print("[" + validMove.x + ", " + validMove.y + "]");
        }
        System.out.println();
    }

    // to be overridden in subclasses
    public Set<Coordinates> specificValidMoves() {
        return validMoves;
    }

    public static char enemyColor(char c) {
        if (c == 'w') {
            return 'b';
        }
        return 'w';
    }

    public boolean isSafe(int x, int y, char color) {
        Coordinates coords = new Coordinates(x, y);
        char enemy = enemyColor(color);

        for (Coordinates pieceCoords: Board.pieceLocations.get(enemy)) {
            if (Board.pieces[pieceCoords.x][pieceCoords.y].validMoves.contains(coords) && !(Board.pieces[pieceCoords.x][pieceCoords.y].getType() == 'p' && x == pieceCoords.x)) {
                return false;
            };
        }

        // check for diagonal pawns
        // up left
        if (color == 'w' && x > 0 && y < 7 && Board.pieces[x - 1][y + 1].getType() == 'p' && Board.pieces[x - 1][y + 1].getColor() == enemy) {
            return false;
        }
        // up right
        if (color == 'w' && x < 7 && y < 7 && Board.pieces[x + 1][y + 1].getType() == 'p' && Board.pieces[x + 1][y + 1].getColor() == enemy) {
            return false;
        }
        // down left
        if (color == 'b' && x > 0 && y > 0 && Board.pieces[x - 1][y - 1].getType() == 'p' && Board.pieces[x - 1][y - 1].getColor() == enemy) {
            return false;
        }
        // down right
        if (color == 'b' && x < 7 && y > 0 && Board.pieces[x + 1][y - 1].getType() == 'p' && Board.pieces[x + 1][y - 1].getColor() == enemy) {
            return false;
        }
        return true;
    }

    // replaces the piece at tiles[x][y] with new piece
    public static void replace(Piece piece, int x, int y) {
        if (Board.visible) {
            Board.tiles[x][y].removeAll();
        }
        if (Board.pieces[x][y].getType() != 'e' && piece.getType() != 'e') {
            Board.score -= Board.pieces[x][y].value;
            Board.pieceLocations.get(Board.pieces[x][y].color).remove(new Coordinates(x, y));
        }
        if (piece.getType() != 'e') {
            Board.pieceLocations.get(piece.color).remove(new Coordinates(piece.letterIndex, piece.numberIndex));
            Board.pieceLocations.get(piece.color).add(new Coordinates(x, y));
        }
        Board.pieces[x][y] = piece;
        if (Board.visible) {
            Board.tiles[x][y].add(piece);
            Board.tiles[x][y].repaint();
            Board.tiles[x][y].revalidate();
            Main.board.repaint();
        }
    }

    public void move(int newX, int newY) {
        // make this current tile empty
        replace(new Piece('e', letterIndex, numberIndex), letterIndex, numberIndex);

        // replace the destination piece with this piece
        replace(this, newX, newY);

        this.setLetterIndex(newX);
        this.setNumberIndex(newY);
    }
}
