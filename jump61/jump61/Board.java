package jump61;


import java.util.Formatter;

import static jump61.Color.*;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered 0 - size()-1, in
 *  row 2 numbered size() - 2*size() - 1, etc.
 *  @author Andrew Berger
 */
abstract class Board {

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        unsupported("clear");
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {
        unsupported("copy");
    }

    /** Returns a copy of me.
     *  Needed so the AI can search without
     *  destroying the board state */
    MutableBoard internalCopy() {
        unsupported("internalCopy");
        return null;
    }

    /**Makes MOVE, returns the new hash, undos the move.
     * Common operation in the search algorithm
     * Makes the move for the current player*/
    public int getHash(int move) {
        unsupported("getHash");
        return 0;
    }

    /** Return the number of rows and of columns of THIS. */
    abstract int size();

    /** Returns the number of spots in the square at row R, column C,
     *  1 <= R, C <= size (). */
    abstract int spots(int r, int c);

    /** Returns the number of spots in square #N. */
    abstract int spots(int n);

    /** Returns the color of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    abstract Color color(int n);

    /** Returns the color of the square at row R, column C,
     *  1 <= R, C <= size(). */
    abstract Color color(int r, int c);

    /** Returns the total number of moves made (red makes the odd moves,
     *  blue the even ones). */
    abstract int numMoves();



    /** Returns an int representing a hashed board state.
     *  Ideally rotationally symmetric but that's crazy
     *  So there is an ORIENTATION param 0-3
     *  Does some hax with modular arithmetic.*/
    public final int bhash(int orientation) {
        int incr;
        int hash = 0;
        int pos = 0;
        final int n = size() * size() - 1;
        switch (orientation) {
        case 0:
            incr = 1;
            break;
        case 1:
            incr = size();
            break;
        case 2:
            incr = -1;
            break;
        case 3:
            incr = -size();
            break;
        default:
            incr = 1;
        }
        for (int curr = 0; curr < n; curr++) {
            int col = 1;
            if (color(pos) == RED) {
                col = 2;
            } else if (color(pos) == BLUE) {
                col = 6;
            }
            hash = (int) ((hash +  ((spots(pos) + col)
                     * Math.pow(LCD, n - curr - 1))) % HASH_SPACE);
            pos = (pos + incr) % n;
            pos = (pos < 0) ? pos + n : pos;
        }
        _hash[orientation] = hash;
        return hash;
    }



    /** Returns the Color of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Color whoseMove() {
        if (numMoves() % 2 == 1) {
            return RED;
        } else {
            return BLUE;
        }
    }

    /** Useful for player selection in game.
     *  Returns 0 or 1 */
    int selectMove() {
        return (numMoves() + 1) % 2;
    }


    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        int r = 1;
        while (n >= size()) {
            n -= size();
            r += 1;
        }
        return r;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        n += 1;
        while (n > size()) {
            n -= size();
        }
        return n;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (r - 1) * size() + c - 1;
    }


    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Color player, int r, int c) {
        if (isLegal(player)) {
            if (color(r, c) != player.opposite()) {
                return true;
            }
            return false;
        }
        return false;
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Color player, int n) {
        return isLegal(player, row(n), col(n));
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Color player) {
        if (player != whoseMove()) {
            return false;
        }
        return true;
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Color getWinner() {
        if (numOfColor(RED) == 0 && numOfColor(BLUE) >= size() * size()) {
            return BLUE;
        } else if (numOfColor(BLUE) == 0
                   && numOfColor(RED) >= size() * size()) {
            return RED;
        }
        return null;
    }

    /** Return the number of squares of given COLOR. */
    abstract int numOfColor(Color color);

    /** Returns the current color counts. */
    public int[] getNumOfCol() {
        return _numOfCol;
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Color player, int r, int c) {
        unsupported("addSpot");
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Color player, int n) {
        unsupported("addSpot");
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Clear the undo
     *  history. */
    void set(int r, int c, int num, Color player) {
        unsupported("set");
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white).  Clear the undo history. */
    void set(int n, int num, Color player) {
        unsupported("set");
    }

    /** Set the current number of moves to N.  Clear the undo history. */
    void setMoves(int n) {
        unsupported("setMoves");
    }

    /** Undo the effects one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {
        unsupported("undo");
    }

    /** Returns true iff the N square is full. */
    boolean full(int n) {
        return _squares[n].isFull();
    }

    /** Returns true iff the R C square is full. */
    boolean full(int r, int c) {
        return full(sqNum(r, c));
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format(getFullDump());
        return out.toString();
    }

    /** Returns dump of ROW. */
    private String getRowDump(int row) {
        StringBuilder rd = new StringBuilder();
        rd.append("    ");
        for (int col = 1; col <= size(); col++) {
            if (color(row, col) == WHITE) {
                rd.append("--");
            } else {
                rd.append(spots(row, col));
                if (color(row, col) == RED) {
                    rd.append('r');
                } else {
                    rd.append('b');
                }
            }
            if (col != size()) {
                rd.append(' ');
            } else {
                rd.append("%n");
            }
        }
        return rd.toString();
    }

    /** Returns full dump. */
    private String getFullDump() {
        StringBuilder fd = new StringBuilder();
        fd.append("===%n");
        for (int row = 1; row <= size(); row++) {
            fd.append(getRowDump(row));
        }
        fd.append("===");
        return fd.toString();
    }

    /** Returns an external rendition of me, suitable for
     *  human-readable textual display.  This is distinct from the dumped
     *  representation (returned by toString). */
    public String toDisplayString() {
        StringBuilder out = new StringBuilder(toString());
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int n = 4;
        if (r == 1 || r == size()) {
            n -= 1;
        }
        if (c == 1 || c == size()) {
            n -= 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    /** Indicate fatal error: OP is unsupported operation. */
    private void unsupported(String op) {
        String msg = String.format("'%s' operation not supported", op);

        throw new UnsupportedOperationException(msg);
    }



    /** Returns the board representation. */
    public Square[] getSquares() {
        Square[] copy = new Square[_squares.length];
        for (int s = 0; s < _squares.length; s++) {
            copy[s] = _squares[s].clone();
        }
        return copy;
    }

    /** The length of an end of line on this system. */
    private static final int NL_LENGTH =
        System.getProperty("line.separator").length();

    /** Array containing four hashed representations.
     *  One for each rotation of the board, which are
     *  also valid board states */
    protected int[] _hash = new int[4];

    /** The master board representation. */
    protected Square[] _squares;

    /** First element is for Red, second for Blue. */
    protected int[] _numOfCol;

    /** The group size of our hash. */
    private static final int HASH_SPACE = (int) Math.pow(2, 30);

    /** Wraps around nicely when hashing. */
    private static final int LCD = 31;

}