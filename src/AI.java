import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AI {
    public static final int maxDepth = 4;
    private static final int infinity = 99999;
    public static void move() {
        new Thread(() -> {
            Action move = bookMove();
            if (move == null) {
                Board.visible = false;
                if (Board.turn == 'w') {
                    move = maxValue(maxDepth, -infinity, infinity, false);
                } else {
                    move = minValue(maxDepth, -infinity, infinity, false);
                }
                Board.visible = true;
            }
            Board.pieces[move.before.x][move.before.y].move(move.after.x, move.after.y);
            Board.changeTurns();
            System.out.println("Predicted eval: " + move.score);
            printMovePath(move);
        }).start();
    }

    public static void printMovePath(Action move) {
        Action node = move;
        while (node.childAction != null) {
            System.out.println(node);
            node = node.childAction;
        }
    }

    private static Action maxValue(int depth, int alpha, int beta, boolean onlyCaptures) {
        if (onlyCaptures) {
            Action standPat = new Action(eval(), 0);
            if (standPat.score >= beta) {
                return standPat;
            }
            if (alpha < standPat.score) {
                alpha = standPat.score;
            }
        }
        if (Board.winner == 'b') {
            return new Action(-9999, depth);
        } else if (Board.winner == 'd') {
            return new Action(0, depth);
        } if (depth == 0) {
            // if it reached a depth of 0 while doing onlyCaptures
            if (onlyCaptures) {
                return new Action(eval(), depth);
            }
            // continue search with only captures as possible moves
            onlyCaptures = true;
            depth = 3;
        }
        Action max = new Action(-9999, -1);
        ArrayList<Action> actions = orderMoves(actions(onlyCaptures));
        if (actions.isEmpty()) {
            return new Action(eval(), depth);
        }
        for (Action action: actions) {
            Board.pieces[action.before.x][action.before.y].move(action.after.x, action.after.y);
            Board.changeTurns();
            action.assign(minValue(depth - 1, alpha, beta, onlyCaptures));
            undoMove(action);
            if (action.score > max.score || (action.score == max.score && action.depth > max.depth)) {
                max = action;
            }
            // alpha beta pruning
            if (action.score > alpha) {
                alpha = action.score;
            }
            if (beta <= alpha) {
                break;
            }
        }
        return max;
    }
    private static Action minValue(int depth, int alpha, int beta, boolean onlyCaptures) {
        if (onlyCaptures) {
            Action standPat = new Action(eval(), 0);
            if (standPat.score >= beta) {
                return standPat;
            }
            if (alpha < standPat.score) {
                alpha = standPat.score;
            }
        }
        if (Board.winner == 'w') {
            return new Action(9999, depth);
        } else if (Board.winner == 'd') {
            return new Action(0, depth);
        } else if (depth == 0) {
            // if it reached a depth of 0 while doing onlyCaptures
            if (onlyCaptures) {
                return new Action(eval(), depth);
            }
            // continue search with only captures as possible moves
            onlyCaptures = true;
            depth = 3;
        }
        Action min = new Action(99999999, -1);
        ArrayList<Action> actions = orderMoves(actions(onlyCaptures));
        if (actions.isEmpty()) {
            return new Action(eval(), depth);
        }
        for (Action action: actions) {
            Board.pieces[action.before.x][action.before.y].move(action.after.x, action.after.y);
            Board.changeTurns();
            action.assign(maxValue(depth - 1, alpha, beta, onlyCaptures));
            undoMove(action);
            if (action.score < min.score || (action.score == min.score && action.depth > min.depth)) {
                min = action;
            }
            // alpha beta pruning
            if (action.score < beta) {
                beta = action.score;
            }
            if (beta <= alpha) {
                break;
            }
        }
        return min;
    }


    private static int eval() {
        int eval = Board.score * 100;

        eval += earlyGameEval('w');
        eval -= earlyGameEval('b');
        eval += endGameEval('w');
        eval -= endGameEval('b');
        
        
        return eval;
    }

    private static int earlyGameEval(char color) {
        int eval = 0;

        // reward position where king is close to edge / far from center
        Coordinates king = Board.kings.get(color);
        int xDistFromCenter = king.x - 4;
        if (xDistFromCenter < 0) {
            xDistFromCenter = 3 - king.x;
        }
        // incentivize king to go one away from the edge instead of on the edge
        if (xDistFromCenter == 3) {
            xDistFromCenter = 1;
        }
        int yDistFromCenter = king.y - 4;
        if (yDistFromCenter < 0) {
            yDistFromCenter = 3 - king.y;
        }
        yDistFromCenter *= 2;

        // incentivize castling
        if ((king.x == 1 && Board.pieces[0][king.y].getType() == 'e') || (king.x == 6 && Board.pieces[7][king.y].getType() == 'e')) {
            eval += 5;
        }

        eval += xDistFromCenter + yDistFromCenter;

        float materialFactor = (float) (-Math.exp(0.15 * ((16.0f - Board.pieceLocations.get(color).size()) - 14.251)) + 1.119);
        int earlyGameWeight = 10;

        eval = (int) (eval * materialFactor * earlyGameWeight);

        return eval;
    }
    private static int endGameEval(char color) {
        int eval = 0;

        // reward position where enemy king is close to edge / far from center
        Coordinates enemyKing = Board.kings.get(Piece.enemyColor(color));
        int xDistFromCenter = enemyKing.x - 4;
        if (xDistFromCenter < 0) {
            xDistFromCenter = 3 - enemyKing.x;
        }
        int yDistFromCenter = enemyKing.y - 4;
        if (yDistFromCenter < 0) {
            yDistFromCenter = 3 - enemyKing.y;
        }
        eval += xDistFromCenter + yDistFromCenter;

        // reward position where king is closer to enemy king
        Coordinates king = Board.kings.get(color);
        int xKingGap = Math.abs(king.x - enemyKing.x);
        int yKingGap = Math.abs(king.y - enemyKing.y);
        eval += 14 - (xKingGap + yKingGap);

        float materialFactor = 1 - Board.pieceLocations.get(Piece.enemyColor(color)).size()/16.0f;
        int endGameWeight = 10;

        eval = (int) (eval * materialFactor * endGameWeight);

        return eval;

    }

    private static void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static int score() {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                score += Board.pieces[i][j].value;
            }
        }
        return score;
    }

    private static ArrayList<Action> actions(boolean onlyCaptures) {
        ArrayList<Action> actions = new ArrayList<>();
        for (Coordinates coords: Board.pieceLocations.get(Board.turn)) {
            for (Coordinates validMove: Board.pieces[coords.x][coords.y].validMoves) {
                if (!onlyCaptures || Board.pieces[validMove.x][validMove.y].color != 'e') {
                    actions.add(new Action(coords, validMove, -1));
                }
            }
        }
        return actions;
    }
    private static ArrayList<Action> orderMoves(ArrayList<Action> actions) {
        actions.sort((a, b) -> {
            int scoreA = Math.abs(Board.pieces[a.after.x][a.after.y].value) - Math.abs(Board.pieces[a.before.x][a.before.y].value);
            int scoreB = Math.abs(Board.pieces[b.after.x][b.after.y].value) - Math.abs(Board.pieces[b.before.x][b.before.y].value);
            return Integer.compare(scoreB, scoreA); // Higher scores first
        });
        return actions;
    }

    public static void undoMove(Action action) {
        Board.pieces[action.after.x][action.after.y].move(action.before.x, action.before.y);
        action.piece.setLetterIndex(action.before.x);
        action.piece.setNumberIndex(action.before.y);
        Piece.replace(action.takenPiece, action.after.x, action.after.y);
        Board.score += action.takenPiece.value;

        char pieceType = action.piece.type;
        if (pieceType == 'k' && action.after.x == 6 && action.before.x == 4) { // kingside castle
            Board.pieces[5][action.after.y].move(7, action.after.y);
            Board.pieces[7][action.after.y].setHasMoved(false);
        } else if (pieceType == 'k' && action.after.x == 2 && action.before.x == 4) { // queenside castle
            Board.pieces[3][action.after.y].move(0, action.after.y);
            Board.pieces[0][action.after.y].setHasMoved(false);
        }
        if (pieceType == 'k' || pieceType == 'r') {
            Board.pieces[action.before.x][action.before.y].setHasMoved(action.hasMoved);
        } if (pieceType == 'p' && (action.after.y == 0 || action.after.y == 7)) { // undo queen promotion
            Piece.replace(action.piece, action.before.x, action.before.y);
            Board.score += action.piece.value;
        } if (pieceType == 'p' && action.before.x != action.after.x && action.takenPiece.getType() == 'e') { //enPassant
            Piece.replace(new Pawn(Piece.enemyColor(action.piece.color), action.after.x, action.before.y), action.after.x, action.before.y);
            Board.pieces[action.after.x][action.before.y].setEnPassant(Board.moves - 2);
            Board.score += Board.pieces[action.after.x][action.before.y].value;
        } if (pieceType == 'p') {
            Board.pieces[action.before.x][action.before.y].setEnPassant(-1);
        }

        Board.moves --;
        Board.changeTurns();
        Board.moves --;
        Board.winner = 'n';
    }

    public static Action bookMove() {
        ArrayList<Action> moves = Board.openingBook.getOrDefault(Board.createFEN(), null);
        if (moves == null) {
            return null;
        }
        int totalFreq = 0;
        for (Action move: moves) {
            totalFreq += move.freq;
        }
        Random random = new Random();
        int randomInt = random.nextInt(totalFreq);

        totalFreq = 0;
        for (Action move: moves) {
            totalFreq += move.freq;
            if (randomInt <= totalFreq) {
                return move;
            }
        }
        return null;
    }

    public static void displayLocations() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board.tiles[i][j].setBackground(Board.tiles[i][j].color);
            }
        }
        for (Coordinates location: Board.pieceLocations.get('w')) {
            if ((location.x + location.y) % 2 == 0) {
                Board.tiles[location.x][location.y].setBackground(new Color(185, 206, 235));
            } else {
                Board.tiles[location.x][location.y].setBackground(new Color(95, 139, 132));
            }
        }
        for (Coordinates location: Board.pieceLocations.get('b')) {
            if ((location.x + location.y) % 2 == 0) {
                Board.tiles[location.x][location.y].setBackground(new Color(185, 206, 235));
            } else {
                Board.tiles[location.x][location.y].setBackground(new Color(95, 139, 132));
            }
        }
    }
}
