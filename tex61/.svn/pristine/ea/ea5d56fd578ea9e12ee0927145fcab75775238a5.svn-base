package tex61;

import java.io.PrintWriter;

import java.util.List;
import java.util.ArrayList;

/** Receives (partial) words and commands, performs commands, and
 *  accumulates and formats words into lines of text, which are sent to a
 *  designated PageAssembler.  At any given time, a Controller has a
 *  current word, which may be added to by addText, a current list of
 *  words that are being accumulated into a line of text, and a list of
 *  lines of endnotes.
 *  @author Andrew Berger
 */
class Controller {

    /** A new Controller that sends formatted output to OUT. */
    Controller(PrintWriter out) {
        _pages = new PagePrinter(out);
        _line = new LineAssembler(_pages);
        _endnotes = new ArrayList<String>();
        _enotes = new PageCollector(_endnotes);
        _eline = new LineAssembler(_enotes);
        _eline.setIndentation(Defaults.ENDNOTE_INDENTATION);
        _eline.setParSkip(Defaults.ENDNOTE_PARAGRAPH_SKIP);
        _eline.setParIndentation(Defaults.ENDNOTE_PARAGRAPH_INDENTATION);
        _eline.setTextWidth(Defaults.ENDNOTE_TEXT_WIDTH);
        _refNum = 1;
        setNormalMode();
    }


    /** Add TEXT to the end of the word of formatted text currently
     *  being accumulated. */
    void addText(String text) {
        _currLine.addText(text);
    }

    /** Finish any current word of text and, if present, add to the
     *  list of words for the next line.  Has no effect if no unfinished
     *  word is being accumulated. */
    void endWord() {
        _currLine.finishWord();
    }

    /** Finish any current word of formatted text and process an end-of-line
     *  according to the current formatting parameters. */
    void addNewline() {
        endWord();
        _currLine.newLine();
    }

    /** Finish any current word of formatted text, format and output any
     *  current line of text, and start a new paragraph. */
    void endParagraph() {
        _currLine.endParagraph();
    }

    /** If valid, process TEXT into an endnote, first appending a reference
     *  to it to the line currently being accumulated. */
    void formatEndnote(String text) {
        addText("[" + _refNum + "]");
        setEndnoteMode();
        InputParser endNoteParser = new InputParser(text, this);
        addText("[" + _refNum + "] ");
        _refNum += 1;
        endNoteParser.process();
    }

    /** Set the current text height (number of lines per page) to VAL, if
     *  it is a valid setting.  Ignored when accumulating an endnote. */
    void setTextHeight(int val) throws FormatException {
        if (!_endnoteMode) {
            _currLine.setTextHeight(val);
        }
    }

    /** Set the current text width (width of lines including indentation)
     *  to VAL, if it is a valid setting. */
    void setTextWidth(int val) throws FormatException {
        _currLine.setTextWidth(val);
    }

    /** Set the current text indentation (number of spaces inserted before
     *  each line of formatted text) to VAL, if it is a valid setting. */
    void setIndentation(int val) throws FormatException {
        _currLine.setIndentation(val);
    }

    /** Set the current paragraph indentation (number of spaces inserted before
     *  first line of a paragraph in addition to indentation) to VAL, if it is
     *  a valid setting. */
    void setParIndentation(int val) throws FormatException {
        _currLine.setParIndentation(val);

    }

    /** Set the current paragraph skip (number of blank lines inserted before
     *  a new paragraph, if it is not the first on a page) to VAL, if it is
     *  a valid setting. */
    void setParSkip(int val) throws FormatException {
        _currLine.setParSkip(val);
    }

    /** Swaps _parskip with _lastParskip. */
    void swapParSkip() {
        _currLine.swapParSkip();
    }

    /** Iff ON, begin filling lines of formatted text. */
    void setFill(boolean on) {
        _currLine.setFill(on);
    }

    /** Iff ON, begin justifying lines of formatted text whenever filling is
     *  also on. */
    void setJustify(boolean on) {
        _currLine.setJustify(on);
    }

    /** Finish the current formatted document or endnote (depending on mode).
     *  Formats and outputs all pending text. */
    void close() {
        if (_endnoteMode) {
            _eline.endParagraph();
            setNormalMode();
        } else {
            _line.endParagraph();
            writeEndnotes();
            _line.finishLine();
        }
    }

    /** Start directing all formatted text to the endnote assembler. */
    private void setEndnoteMode() {
        _endnoteMode = true;
        _currLine = _eline;
    }

    /** Return to directing all formatted text to _mainText. */
    private void setNormalMode() {
        _endnoteMode = false;
        _currLine = _line;
    }

    /** Write all accumulated endnotes to _mainText. */
    private void writeEndnotes() {
        for (String line : _endnotes) {
            _line.addLine(line);
        }
    }

    /** True iff we are currently processing an endnote. */
    private boolean _endnoteMode;
    /** Number of next endnote. */
    private int _refNum;
    /** The object holding the lineAssembler. */
    private LineAssembler _line;
    /** The object holding the pageAssembler. */
    private PageAssembler _pages;
    /** The List holding the text of the endnotes. */
    private List<String> _endnotes;
    /** Assembler for endnotes. */
    private PageAssembler _enotes;
    /** Assembler for endnotes. */
    private LineAssembler _eline;
    /** Points to the current LineAssembler. */
    private LineAssembler _currLine;
}
