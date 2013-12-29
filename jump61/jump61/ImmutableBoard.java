package jump61;

import java.util.Arrays;
import static jump61.Color.*;

/** A minimal and immutable representation of board.
 *  Designed specifically for undo
 * @author Andrew Berger */
class ImmutableBoard extends Board {

    /** A new ImmutabeBoard that provides a snapshot of BOARD. */
    ImmutableBoard(Board board) {
        isquares = board.getSquares();
        imove = board.numMoves();
        inumOfCol = new int[2];
        inumOfCol[0] = board.numOfColor(RED);
        inumOfCol[1] = board.numOfColor(BLUE);
        isize = board.size();
        _squares = isquares;
        _numOfCol = inumOfCol;
        _hash = Arrays.copyOf(board._hash, 4);
    }

    @Override
    int size() {
        return isize;
    }

    @Override
    int spots(int r, int c) {
        return spots(sqNum(r, c));
    }

    @Override
    int spots(int n) {
        return isquares[n].spots();
    }

    @Override
    Color color(int r, int c) {
        return color(sqNum(r, c));
    }

    @Override
    Color color(int n) {
        return _squares[n].color();
    }

    @Override
    int numMoves() {
        return imove;
    }

    @Override
    public Square[] getSquares() {
        return isquares;
    }

    @Override
    int numOfColor(Color color) {
        if (color == RED) {
            return inumOfCol[0];
        } else if (color == BLUE) {
            return inumOfCol[1];
        } else {
            return size() - (inumOfCol[0] + inumOfCol[1]);
        }
    }

    /** Immutable squares. */
    private final Square[] isquares;
    /** Immutable moves .*/
    private final int imove;
    /** Immutable list of color. */
    private final int[] inumOfCol;
    /** Immutable size .*/
    private final int isize;

}
