package tex61;

import static tex61.FormatException.error;

/** A PageAssembler accepts complete lines of text (minus any
 *  terminating newlines) and turns them into pages, adding form
 *  feeds as needed.  It prepends a form feed (Control-L  or ASCII 12)
 *  to the first line of each page after the first.  By overriding the
 *  'write' method, subtypes can determine what is done with
 *  the finished lines.
 *  @author Andrew Berger
 */
abstract class PageAssembler {

    /** Create a new PageAssembler that sends its output to OUT.
     *  Initially, its text height is unlimited. It prepends a form
     *  feed character to the first line of each page except the first. */
    PageAssembler() {
        _textHeight = Defaults.TEXT_HEIGHT;
        _currHeight = 1;
    }

    /** Add LINE to the current page, starting a new page with it if
     *  the previous page is full. A null LINE indicates a skipped line,
     *  and has no effect at the top of a page. */
    void addLine(String line) {
        if (_currHeight == _textHeight) {
            if (line != null) {
                line = "\f" + line;
                write(line);
                _currHeight = 1;
            }
        } else {
            if (line == null) {
                if (_currHeight > 1) {
                    write("\n");
                    _currHeight += 1;
                }
            } else {
                write(line);
                _currHeight += 1;
            }

        }

    }

    /** Set text height to VAL, where VAL > 0. */
    void setTextHeight(int val) throws FormatException {
        if (val > 0) {
            _textHeight = val;
        } else {
            throw error("TextHeight can't be set to %d <0", val);
        }
    }

    /** Perform final disposition of LINE, as determined by the
     *  concrete subtype. */
    abstract void write(String line);

    /** Int containining the current text height. */
    private int _textHeight;


    /** Int between 0 and _TEXTHEIGHT. */
    private int _currHeight;


}
