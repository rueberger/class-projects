package tex61;

import java.io.PrintWriter;

/** A PageAssembler that sends lines immediately to a PrintWriter, with
 *  terminating newlines.
 *  @author Andrew Berger
 */
class PagePrinter extends PageAssembler {

    /** A new PagePrinter that sends lines to OUT. */
    PagePrinter(PrintWriter out) {
        _out = out;
    }

    /** Print LINE to my output. */
    @Override
    void write(String line) {
        for (char c: line.toCharArray()) {
            _out.append(c);
        }
        if (!line.equals("\n")) {
            _out.append("\n");
        }
        _out.flush();
    }

    /** The field holding THIS output. */
    private PrintWriter _out;
}
