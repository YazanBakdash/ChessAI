import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Board extends JPanel{
    public static boolean visible;
    public static Piece sourcePiece;
    public static Piece[][] pieces;
    public static Tile[][] tiles;
    public static int moves;
    public static char turn;
    public static Map<Character, Coordinates> kings;
    public static Map<Character, Set<Coordinates>> pieceLocations;
    public static int score;
    public static Set<Coordinates> kingThreats;
    public static Set<Coordinates> onlyPossibilities;
    public static char winner;
    public static Map<String, ArrayList<Action>> openingBook;
    private String FEN;
    public Board(boolean visible, String FEN) {
        Board.visible = visible;
        turn = 'b';
        kings = new HashMap<>();
        pieceLocations = new HashMap<>();
        pieceLocations.put('w', new HashSet<>());
        pieceLocations.put('b', new HashSet<>());
        score = 0;
        winner = 'n';
        kingThreats = new HashSet<>();
        onlyPossibilities = new HashSet<>();
        this.FEN = FEN;
        openingBook = new HashMap<>();
        readMoves();
        if (visible) {
            setBounds(0, 0, 720, 720);
            setLayout(new GridLayout(8, 8, 0, 0));
            setBackground(Main.bgColor);
            tiles = new Tile[8][8];
        }
        pieces = new Piece[8][8];
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                pieces[j][i] = new Piece('e', j, i);
                if (visible) {
                    if ((i + j) % 2 == 1) {
                        tiles[j][i] = new Tile(j, i, new Color(235, 236, 208));
                    } else {
                        tiles[j][i] = new Tile(j, i, new Color(115, 149, 82));
                    }
                    add(tiles[j][i]);
                }
            }
        }
        establishBoard();

    }

    private void establishBoard() {
        if (FEN == null) {
            //white pawns
            for (int i = 0; i < 8; i++) {
                pieces[i][1] = new Pawn('w', i, 1);
            }
            //white knights
            pieces[1][0] = new Knight('w', 1, 0);
            pieces[6][0] = new Knight('w', 6, 0);
            //white bishops
            pieces[2][0] = new Bishop('w', 2, 0);
            pieces[5][0] = new Bishop('w', 5, 0);
            //white rooks
            pieces[0][0] = new Rook('w', 0, 0, false);
            pieces[7][0] = new Rook('w', 7, 0, false);
            //white king and queen
            pieces[4][0] = new King('w', 4, 0);
            pieces[3][0] = new Queen('w', 3, 0);

            //black pawns
            for (int i = 0; i < 8; i++) {
                pieces[i][6] = new Pawn('b', i, 6);
            }
            //black knights
            pieces[1][7] = new Knight('b', 1, 7);
            pieces[6][7] = new Knight('b', 6, 7);
            //black bishops
            pieces[2][7] = new Bishop('b', 2, 7);
            pieces[5][7] = new Bishop('b', 5, 7);
            //black rooks
            pieces[0][7] = new Rook('b', 0, 7, false);
            pieces[7][7] = new Rook('b', 7, 7, false);
            //black king and queen
            pieces[4][7] = new King('b', 4, 7);
            pieces[3][7] = new Queen('b', 3, 7);
        } else {
            readFEN();
        }

        // add the pieces to their respective tile
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].add(pieces[i][j]);
            }
        }
        revalidate();

        // place all the pieces' locations in a set
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char pieceColor = pieces[i][j].color;
                if (pieceColor != 'e') {
                    pieceLocations.get(pieceColor).add(new Coordinates(i, j));
                }
            }
        }
        updateMoves();
        turn = Piece.enemyColor(turn);

    }

    public void readFEN() {
        int row = 7;
        int col = 0;
        for (int i = 0; i < FEN.length(); i++) {
            char letter = FEN.charAt(i);
            if (letter == ' ') {
                turn = Piece.enemyColor(FEN.charAt(i + 1));
                i += 3;
                for (int j = 0; i + j < FEN.length() && FEN.charAt(i + j) != '-' && FEN.charAt(i + j) != ' '; j++) {
                    letter = FEN.charAt(i + j);
                    if (letter == 'K') {
                        pieces[7][0].setHasMoved(false);
                    } else if (letter == 'Q') {
                        pieces[0][0].setHasMoved(false);
                    } else if (letter == 'k') {
                        pieces[7][7].setHasMoved(false);
                    } else if (letter == 'q') {
                        pieces[0][7].setHasMoved(false);
                    }
                }
                break;
            }
            if (Character.isDigit(letter)) {
                col += letter - '0';
            } else if (letter == '/') {
                row --;
                col = 0;
            } else {
                if (letter == 'K') {
                    pieces[col][row] = new King('w', col, row);
                } else if (letter == 'k') {
                    pieces[col][row] = new King('b', col, row);
                } else if (letter == 'Q') {
                    pieces[col][row] = new Queen('w', col, row);
                } else if (letter == 'q') {
                    pieces[col][row] = new Queen('b', col, row);
                } else if (letter == 'R') {
                    pieces[col][row] = new Rook('w', col, row, true);
                } else if (letter == 'r') {
                    pieces[col][row] = new Rook('b', col, row, true);
                } else if (letter == 'B') {
                    pieces[col][row] = new Bishop('w', col, row);
                } else if (letter == 'b') {
                    pieces[col][row] = new Bishop('b', col, row);
                } else if (letter == 'N') {
                    pieces[col][row] = new Knight('w', col, row);
                } else if (letter == 'n') {
                    pieces[col][row] = new Knight('b', col, row);
                } else if (letter == 'P') {
                    pieces[col][row] = new Pawn('w', col, row);
                } else if (letter == 'p') {
                    pieces[col][row] = new Pawn('b', col, row);
                }
                score += pieces[col][row].value;
                col++;
            }
        }
    }

    public void readMoves() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/moves.txt"))) {
            String line;
            String pos = null;
            while ((line = br.readLine()) != null) {
                if (line.substring(0, 2).equals("FE")) {
                    line = line.substring(4);
                    pos = line;
                    openingBook.put(pos, new ArrayList<>());
                } else {
                    Coordinates before = new Coordinates(line.charAt(0) - 97, line.charAt(1) - 49);
                    Coordinates after = new Coordinates(line.charAt(2) - 97, line.charAt(3) - 49);
                    int freq = Integer.parseInt(line.substring(5));
                    openingBook.get(pos).add(new Action(freq, before, after));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String createFEN() {
        String FEN = "";
        for (int row = 7; row >= 0; row--) {
            int gap = 0;
            for (int col = 0; col < 8; col++) {
                char letter = pieces[col][row].getType();
                if (letter == 'e') {
                    gap++;
                } else {
                    if (gap != 0) {
                        FEN += gap;
                        gap = 0;
                    }
                    if (pieces[col][row].getColor() == 'w') {
                        letter = Character.toUpperCase(letter);
                    }
                    FEN += letter;
                }
            }
            if (gap != 0) {
                FEN += gap;
            }
            if (row != 0) {
                FEN += "/";
            }
        }
        FEN += " " + turn + " ";

        // white castle
        if (!pieces[kings.get('w').x][kings.get('w').y].getHasMoved()) {
            if (!pieces[7][0].getHasMoved()) {
                FEN += 'K';
            } if (!pieces[0][0].getHasMoved()) {
                FEN += 'Q';
            }
        }
        // black castle
        if (!pieces[kings.get('b').x][kings.get('w').y].getHasMoved()) {
            if (!pieces[7][7].getHasMoved()) {
                FEN += 'k';
            } if (!pieces[0][7].getHasMoved()) {
                FEN += 'q';
            }
        }

        if (FEN.charAt(FEN.length() - 2) == 'w' || FEN.charAt(FEN.length() - 2) == 'b') {
            FEN += '-';
        }
        FEN += " -";

        return FEN;
    }

    public static void updateMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieces[i][j].validMoves.clear();
            }
        }
        // find the valid moves of the color that just moved to look for any king threats
        for (Coordinates coords: pieceLocations.get(turn)) {
            pieces[coords.x][coords.y].findValidMoves();
        }

        char enemyColor = Piece.enemyColor(turn);
        boolean checkmate = true;
        if (kingThreats.size() > 1) {
            Coordinates king = kings.get(enemyColor);
            pieces[king.x][king.y].findValidMoves();
            if (!pieces[king.x][king.y].validMoves.isEmpty()) {
                checkmate = false;
            }
        } else {
            for (Coordinates coords: pieceLocations.get(enemyColor)) {
                if (!pieces[coords.x][coords.y].findValidMoves().isEmpty()) {
                    checkmate = false;
                }
            }
        }
        if (checkmate && kingThreats.isEmpty()) {
            winner = 'd';
            if (visible) {
                new PopUp("Draw");
            }
        } else if (checkmate) {
            winner = turn;
            if (visible) {
                new PopUp("Checkmate");
            }
        } else if (!kingThreats.isEmpty() && visible) {
            System.out.println("CHECK");
        }
        kingThreats.clear();
        onlyPossibilities.clear();
    }


    public static void changeTurns() {
        Board.updateMoves();
        // change the player's turn
        if (Board.turn == 'w') {
            Board.turn = 'b';
            if (visible) {
                Main.wClock.stop();
                Main.bClock.start();
            }
        } else {
            Board.turn = 'w';
            if (visible) {
                Main.bClock.stop();
                Main.wClock.start();
            }
        }
        if (visible) {
            Main.wName.updateScore(Board.score);
            Main.bName.updateScore(Board.score);
        }
        Board.moves++;

    }
}
