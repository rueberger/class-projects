package tex61;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/** Unit tests of LineAssemblers
 *  DEAR READER: please forgive my paltry tests
 *  I am of the opinion that test driven development is
 * impossible when the spec is so purposely confusingly
 * @author Andrew Berger
 */
public class LineAssemblerTest {

    private static final String NL = System.getProperty("line.separator");

    private String makeTestWords(String word, int n) {
        for (int i = 0; i < n; i++) {
            testWords += word + " ";
        }
        return "";
    }

    private String[] makeFilledWords(String word, int n) {
        return new String[1];
    }

    @Test
    public void addLineTest() {
        String test = "add me to unused";
        testLine.addLine(test);
        assertEquals("Problem", test, unused.get(0));
    }


    private List<String> unused = new ArrayList<String>();
    private PageAssembler testPage = new PageCollector(unused);
    private LineAssembler testLine = new LineAssembler(testPage);
    private int textWidth;
    private String testWords;
}
