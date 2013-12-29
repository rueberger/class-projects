package jump61;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of AI
 *  This is way harder than I thought it would be
 *  @author Andrew Berger
 */
public class AITest {

    @Test
    public void testMoveGen() {
        Writer output = new OutputStreamWriter(System.out);
        Game game = new Game(new InputStreamReader(System.in),
                             output, output,
                             new OutputStreamWriter(System.err));
        Color c = RED;
        Player comp = new AI(game, c);
        int[] spots = new int[] {0, 4, 8, 12, 16, 20, 24};
        for (int i = 0; i < 0; i++) {
            int[] allowed = makeMoves(Arrays.copyOfRange(spots, 0, i), 6);
        }
        assertEquals("This test is broken", true, true);
    }

    /** Return list of moves minus MISSING.
     *  Valid for board of SIZE. */
    private int[] makeMoves(int[] missing, int size) {
        int[] res = new int[(size * size) - missing.length];
        int count = 0;
        Set<Integer> disallowed = new HashSet<Integer>();
        for (int move : missing) {
            disallowed.add(move);
        }
        for (int n = 0; n < size * size; n++) {
            if (!disallowed.contains(n)) {
                res[count] = n;
                count += 1;
            }
        }
        return res;
    }

    private boolean hasMove(int[] moves, int move) {
        for (int m : moves) {
            if (m == move) {
                return true;
            }
        }
        return false;
    }

}
