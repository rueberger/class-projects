
package jump61;

import java.util.LinkedList;
import java.util.Arrays;

import static jump61.Color.*;

/** A Jump61 board state.
 *  @author Andrew Berger
 */
class MutableBoard extends Board {

    /** An N x N board in initial configuration. */
    MutableBoard(int N) {
        _N = N;
        initBoard(N);
        _moves = 1;
        _numOfCol = new int[2];
        _previousBoards = new LinkedList<ImmutableBoard>();
    }

    /** A board whose initial contents are copied from BOARD0. Clears the
     *  undo history. */
    MutableBoard(Board board0) {
        copy(board0);
    }

    @Override
    void clear(int N) {
        _N = N;
        _moves = 1;
        initBoard(N);
        _numOfCol = new int[2];
    }

    @Override
    void copy(Board board) {
        _N = board.size();
        _moves = board.numMoves();
        _squares = board.getSquares();
        _numOfCol = board.getNumOfCol();
        _hash = board._hash;
    }

    @Override
    MutableBoard internalCopy() {
        MutableBoard clone = new MutableBoard(this);
        LinkedList<ImmutableBoard> copy =
            new LinkedList<ImmutableBoard>(_previousBoards);

        clone.setUndoStack(copy);
        return clone;
    }

    @Override
    int size() {
        return _N;
    }

    @Override
    int spots(int r, int c) {
        return spots(sqNum(r, c));
    }

    @Override
    int spots(int n) {
        return _squares[n].spots();
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
        return _moves;
    }

    @Override
    int numOfColor(Color color) {
        if (color == RED) {
            return _numOfCol[0];
        } else if (color == BLUE) {
            return _numOfCol[1];
        } else {
            return (size() * size()) - (_numOfCol[0] + _numOfCol[1]);
        }
    }

    @Override
    void addSpot(Color player, int r, int c) {
        addSpot(player, sqNum(r, c));
    }

    @Override
    void addSpot(Color player, int n) {
        if (!isLegal(player, n)) {
            throw new IllegalArgumentException();
        }
        _previousBoards.addFirst(new ImmutableBoard(this));
        _moves += 1;
        set(n, spots(n) + 1, player);
        jump(n);
    }

    @Override
    void set(int r, int c, int num, Color player) {
        set(sqNum(r, c), num, player);
    }



    @Override
    void set(int n, int num, Color player) {
        Color curr = _squares[n].color();
        if (num == 0) {
            player = WHITE;
        }
        updateCount(curr, -spots(n));
        updateCount(player, num);
        _squares[n].setSpots(num);
        _squares[n].setColor(player);
    }

    @Override
    void setMoves(int num) {
        assert num > 0;
        _moves = num;
    }

    @Override
    void undo() {
        Board last = _previousBoards.pop();
        _N = last.size();
        _moves =  last.numMoves();
        _squares = last.getSquares();
        _numOfCol[0] = last.numOfColor(RED);
        _numOfCol[1] = last.numOfColor(BLUE);
        _hash = Arrays.copyOf(last._hash, 4);
    }


    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        LinkedList<Integer> fullSquares = new LinkedList<Integer>();
        if (full(S)) {
            fullSquares.add(S);
        }
    spread:
        while (fullSquares.peek() != null) {
            int currSquare = fullSquares.remove();
            if (full(currSquare)) {
                Color spreading = color(currSquare);
                set(currSquare, 1, spreading);
                int r = row(currSquare);
                int c = col(currSquare);
                int[][] offsets =
                    new int[][] {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
                for (int[] offset : offsets) {
                    int mr = r + offset[0];
                    int mc = c + offset[1];
                    if (exists(mr, mc)) {
                        int mn = sqNum(mr, mc);
                        set(mn, spots(mn) + 1, spreading);
                        if (full(mn)) {
                            fullSquares.add(mn);
                        }
                        if (getWinner() != null) {
                            break spread;
                        }
                    }
                }
            }
        }
    }

    /** Creates and populates _squares[N]. */
    private void initBoard(int N) {
        _squares = new Square[N * N];
        for (int i = 0; i < N * N; i++) {
            _squares[i] = new Square(neighbors(i));
        }
    }


    /** Sets my undoStack to STACK.*/
    public void setUndoStack(LinkedList<ImmutableBoard> stack) {
        _previousBoards = stack;
    }

    @Override
    public int getHash(int move) {
        addSpot(whoseMove(), move);
        int h = bhash(0);
        undo();
        return h;
    }

    /** Updates _numOfCol depending on PLAYER and INCR. */
    private void updateCount(Color player, int incr) {
        if (player == RED) {
            _numOfCol[0] += incr;
        } else if (player == BLUE) {
            _numOfCol[1] += incr;
        } else {
            return;
        }
    }

    /** Total combined number of moves by both sides. */
    protected int _moves;
    /** Convenience variable: size of board (squares along one edge). */
    private int _N;
    /** Holds the undo stack. */
    private LinkedList<ImmutableBoard> _previousBoards;

}
