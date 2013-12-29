package jump61;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;

import static jump61.Color.*;

/** An automated Player.
 *  @author Andrew Berger
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
        _ltable = new HashMap<Integer, int[]>();
        _sign = 1;
        if (color == BLUE) {
            _sign = -1;
        }
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        Board reference = new ImmutableBoard(board);
        int[] moves = generateMoves(board);
        int[] scores = new int[moves.length];
        boolean compute = true;
        int depth = 1;
        int max = LOW_SCORE;
        int move = 0;
        long start = System.currentTimeMillis();
    search:
        while (compute) {
            for (int i = 0; i < moves.length; i++) {
                board.addSpot(getColor(), moves[i]);
                scores[i] = _sign * negamax(BOT, TOP, depth,
                                            getColor().opposite(), board);
                board.undo();
                if (timeout(start)
                    || depth > (THRESHOLD - board.size())  / board.size()) {
                    break search;
                } else if (scores[i] == HIGH_SCORE) {
                    compute = false;
                }
            }
            depth += 1;
        }
        for (int n = 0; n < moves.length; n++) {
            if (scores[n] > max
                && board.isLegal(reference.whoseMove(), moves[n])) {
                max = scores[n];
                move = moves[n];
            }
        }
        game.makeMove(move);
        announceMove(getColor(), board.row(move),
                     board.col(move));
    }


    /** Alpha-beta search with ALPHA and BETA to DEPTH.
     *  Starts with color P and board B
     *  Some move ordering will help
     *  Returns the score found so far */
    private int negamax(int alpha, int beta, int depth, Color p, Board b) {
        int max = LOW_SCORE;
        int[] previous = _ltable.get(Integer.valueOf(b.bhash(0)));
        if (previous != null) {
            if (previous[1] > depth) {
                max = previous[1];
                alpha = Math.max(max, alpha);
                if (alpha >= beta) {
                    return max;
                }
            }
        }
        if (depth == 0 || b.getWinner() != null) {
            if (b.numMoves() > 10) {
                return quiesce(alpha, beta, p, b);
            }
            return staticEval(p, b);
        }
        int[] moves = generateMoves(b);
        if (depth > 5) {
            moves = sortMoves(b, moves);
        }
        for (int move : moves) {
            b.addSpot(p, move);
            int prosp = -negamax(-beta, -alpha, depth - 1, p.opposite(), b) - 1;
            b.undo();
            if (prosp > HIGH_SCORE / 2) {
                prosp -= 1;
            }
            max = Math.max(max, prosp);
            alpha = Math.max(alpha, prosp);
            if (alpha >= beta) {
                break;
            }
        }
        addToTable(b, max, depth);
        return max;
    }

    /** Returns a list of integers for B.
     * The squares where we can add a dot
     * Sorts them with the hash table.
     * Prioritizes jumps*/
    private int[] generateMoves(Board b) {
        ArrayList<Integer> moves = new ArrayList<Integer>();
        ArrayList<Integer> jumps = new ArrayList<Integer>();
        Color p = b.whoseMove();
        for (int n = 0; n < b.size() * b.size(); n++) {
            if (b.isLegal(p, n)) {
                if (b.full(n)) {
                    jumps.add(n);
                } else {
                    moves.add(n);
                }
            }
        }
        int[] out = toPrimitiveArray(jumps, moves);
        return out;
    }


    /** Sorts MOVES from B by entries in the hash table.
     *  Returns the same list, but in a better order. */
    private int[] sortMoves(Board b, int[] moves) {
        int[][] scoredMoves = new int[moves.length][2];
        for (int m = 0; m < moves.length; m++) {
            int hash = b.getHash(moves[m]);
            int[] hit = _ltable.get(hash);
            if (hit != null) {
                scoredMoves[m][0] = hit[0];
            } else {
                scoredMoves[m][0] = 0;
            }
            scoredMoves[m][1] = moves[m];
        }
        Arrays.sort(scoredMoves, new Comparator<int[]>() {
                @Override
                public int compare(final int[] e1, final int[] e2) {
                    final int score1 = e1[0];
                    final int score2 = e2[0];
                    return score2 - score1;
                }
            });
        int[] result = new int[moves.length];
        for (int k = 0; k < moves.length; k++) {
            result[k] = scoredMoves[k][1];
        }
        return result;
    }


    /** Returns a list of legal moves from state B.*/
    private int[] generateJumps(Board b) {
        ArrayList<Integer> jumps = new ArrayList<Integer>();
        Color p = b.whoseMove();
        for (int n = 0; n < b.size() * b.size(); n++) {
            if (b.isLegal(p, n) & b.spots(n) == b.neighbors(n)) {
                jumps.add(n);
            }
        }
        return toPrimitiveArray(jumps, new ArrayList<Integer>());
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Color p, Board b) {
        int score = 0;
        if (b.getWinner() != null) {
            if (b.getWinner() == p) {
                score = HIGH_SCORE;
            } else {
                score = LOW_SCORE;
            }
        } else {
            score = b.numOfColor(p) - b.numOfColor(p.opposite());
        }
        if (p == RED) {
            return score;
        }
        return score * -1;
    }

    /** Like staticEval, but better.
     *  Returns a score given ALPHA BETA P and B
     *  Checks to make sure the opponent doesn't have
     *  a devasting response. */
    private int quiesce(int alpha, int beta, Color p, Board b) {
        int myEval = staticEval(p, b);
        if (myEval >= beta) {
            return beta;
        } else if (myEval > alpha) {
            alpha = myEval;
        }
        int[] jumpMoves = generateJumps(b);
        for (int move : jumpMoves) {
            b.addSpot(p, move);
            int score = -quiesce(-beta, -alpha, p.opposite(), b);
            b.undo();
            if (score >= beta) {
                return beta;
            } else if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

    /** Adds the four hashes from B to our table.
     *  That is, if they don't already have a deeper
     *  entry in the table.
     *  Entry added of the form {SCORE, DEPTH}*/
    private void addToTable(Board b, int score, int depth) {
        for (int o = 0; o < 4; o++) {
            int hashVal = b.bhash(o);
            if (_ltable.get(Integer.valueOf(hashVal)) == null
                || _ltable.get(Integer.valueOf(hashVal))[1] < depth) {
                _ltable.put(Integer.valueOf(hashVal), new int[] {score, depth});
            }
        }
    }

    /** Utility function because java lacks this wtf.
     *  Takes an ARR1 and ARR2 ayList and returns an int[]
     *  Puts ARR1 before ARR2*/
    private int[] toPrimitiveArray(ArrayList<Integer> arr1,
                                   ArrayList<Integer> arr2) {
        int[] prim = new int[arr1.size() + arr2.size()];
        for (int i = 0; i < arr1.size(); i++) {
            prim[i] = arr1.get(i);
        }
        for (int j = 0; j < arr2.size(); j++) {
            prim[j + arr1.size()] = arr2.get(j);
        }
        return prim;
    }

    @Override
    Board getBoard() {
        return getGame().getMutableBoard();
    }

    /** Print announcement.
     * Format: PLAYER "(Red|Blue) moves R C."*/
    private void announceMove(Color player, int r, int c) {
        String col = "";
        if (player == RED) {
            col = "Red";
        } else if (player == BLUE) {
            col = "Blue";
        }
        System.out.printf("%s moves %d %d.%n", col, r, c);
    }

    /** Returns true iff START - currentTime > 10s . */
    private boolean timeout(long start) {
        long elapsed = System.currentTimeMillis() - start;
        return elapsed > SEC;
    }


    /** A (very large) hashMap.
     *  Holds the last score of hashed positions
     *  In addition to the depth it was searched at */
    private Map<Integer, int[]> _ltable;

    /** Static eval changes based on color. */
    private int _sign;

    /** For min max purposes. Be careful of mod arith. */
    private static final int HIGH_SCORE = 1000000000;
    /** For min max purposes. Be careful of mod arith. */
    private static final int LOW_SCORE = -1000000000;
    /** Bigger than the other one still not close to max. */
    private static final int TOP = 2000000000;
    /** Smaller than the other still not close to min. */
    private static final int BOT = -2000000000;
    /** One second in miliseconds. */
    private static final int SEC = 10000;
    /** THe proper time for counting down, provisionally 5. */
    private static final int THRESHOLD = 20;
}
