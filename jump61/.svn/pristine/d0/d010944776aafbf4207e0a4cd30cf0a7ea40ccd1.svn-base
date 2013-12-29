package jump61;

import static jump61.Color.*;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests of Boards.
 *  @author Andrew Berger
 */
public class BoardTest {

    private static final String NL = System.getProperty("line.separator");

    @Test
    public void testSize() {
        Board B = new MutableBoard(5);
        assertEquals("bad length", 5, B.size());
        ConstantBoard C = new ConstantBoard(B);
        assertEquals("bad length", 5, C.size());
        Board D = new MutableBoard(C);
        assertEquals("bad length", 5, C.size());
    }

    @Test
    public void testSet() {
        Board B = new MutableBoard(5);
        B.set(2, 2, 1, RED);
        B.setMoves(1);
        assertEquals("wrong number of spots", 1, B.spots(2, 2));
        assertEquals("wrong color", RED, B.color(2, 2));
        assertEquals("wrong count", 1, B.numOfColor(RED));
        assertEquals("wrong count", 0, B.numOfColor(BLUE));
        assertEquals("wrong count", 24, B.numOfColor(WHITE));
    }

    @Test
    public void testMove() {
        Board B = new MutableBoard(6);
        B.addSpot(RED, 1, 1);
        checkBoard("#1", B, 1, 1, 1, RED);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#2", B, 1, 1, 1, RED, 2, 1, 1, BLUE);
        B.addSpot(RED, 1, 1);
        checkBoard("#3", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.addSpot(BLUE, 2, 1);
        checkBoard("#4", B, 1, 1, 2, RED, 2, 1, 2, BLUE);
        B.addSpot(RED, 1, 1);
        checkBoard("#5", B, 1, 1, 1, RED, 2, 1, 3, RED, 1, 2, 1, RED);
        B.undo();
        checkBoard("#4U", B, 1, 1, 2, RED, 2, 1, 2, BLUE);
        B.undo();
        checkBoard("#3U", B, 1, 1, 2, RED, 2, 1, 1, BLUE);
        B.undo();
        checkBoard("#2U", B, 1, 1, 1, RED, 2, 1, 1, BLUE);
        B.undo();
        checkBoard("#1U", B, 1, 1, 1, RED);
    }

    @Test
    public void testHash() {
        Board B = new MutableBoard(6);
        int[] moves = new int[] {0, 1, 2, 3, 4};
        int h1, h2;
        Color c = RED;
        for (int move : moves) {
            h1 = B.getHash(move);
            B.addSpot(c, move);
            h2 = B.bhash(0);
            assertEquals("hashes should be the same", h1, h2);
            c = c.opposite();
        }
    }

    @Test
    public void testHashOrientation() {
        Board B = new MutableBoard(6);
        int h1, h2;
        B.addSpot(RED, 0);
        h1 = B.bhash(0);
        Color c = BLUE;
        int[] moves = new int[] {5, 35, 30};
        for (int i = 0; i < 3; i++) {
            B.addSpot(c, moves[i]);
            h2 = B.bhash(i + 1);
            B.undo();
            assertEquals("Rotated traversal should be same", h1, h1);
        }
    }

    @Test
    public void testColor() {
        Board B = new MutableBoard(6);
        B.addSpot(RED, 1);
        assertEquals("Blues turn", BLUE, B.whoseMove());
        B.addSpot(BLUE, 3);
        assertEquals("Reds turn", RED, B.whoseMove());
        B.clear(6);
        assertEquals("Reds turn", RED, B.whoseMove());
        B.setMoves(4);
        assertEquals("Blues turn", BLUE, B.whoseMove());
    }

    @Test
    public void testLegal() {
        Board B = new MutableBoard(4);
        Color c = RED;
        int[] moves = new int[] {0, 2, 4, 3, 5};
        for (int move : moves) {
            B.addSpot(c, move);
            c = c.opposite();
            assertEquals("Square occupied", false, B.isLegal(c, move));
        }
    }

    private void checkBoard(String msg, Board B, Object... contents) {
        for (int k = 0; k < contents.length; k += 4) {
            String M = String.format("%s at %d %d", msg, contents[k],
                                     contents[k + 1]);
            assertEquals(M, (int) contents[k + 2],
                         B.spots((int) contents[k], (int) contents[k + 1]));
            assertEquals(M, contents[k + 3],
                         B.color((int) contents[k], (int) contents[k + 1]));
        }
        int c;
        c = 0;
        for (int i = B.size() * B.size() - 1; i >= 0; i -= 1) {
            assertTrue("bad white square #" + i,
                       (B.color(i) == WHITE) == (B.spots(i) == 0));
            if (B.color(i) != WHITE) {
                c += 1;
            }
        }
        assertEquals("extra squares filled", contents.length / 4, c);
    }

}
