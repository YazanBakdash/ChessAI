public class Action {
    public Coordinates before;
    public Coordinates after;
    public int score;
    public Piece takenPiece;
    public Piece piece;
    public boolean hasMoved;
    public int depth;
    public int freq;
    public Action childAction = null;

    public Action(Coordinates b, Coordinates a, int d) {
        before = b;
        after = a;
        depth = d;
        takenPiece = Board.pieces[after.x][after.y];
        piece = Board.pieces[before.x][before.y];
        hasMoved = piece.getHasMoved();
    }

    public Action(int f, Coordinates b, Coordinates a) {
        freq = f;
        before = b;
        after = a;
    }

    public Action(int s, int d) {
        before = null;
        after = null;
        score = s;
        depth = d;
    }

    @Override
    public String toString() {
        if (before == null) {
            return Integer.toString(score);
        }
        return "{" + before.x + ", " + before.y + "} to {" + after.x + ", " + after.y + "}";
    }

    public void assign(Action action) {
        this.score = action.score;
        this.depth = action.depth;
        this.childAction = action;
    }
}
