package tex61;

import java.util.ArrayList;
import java.util.List;

import static tex61.FormatException.error;

/** An object that receives a sequence of words of text and formats
 *  the words into filled and justified text lines that are sent to a receiver.
 *  @author Andrew Berger
 */
class LineAssembler {

    /** A new, empty line assembler with default settings of all
     *  parameters, sending finished lines to PAGES. */
    LineAssembler(PageAssembler pages) {
        _pages = pages;
        _width = Defaults.TEXT_WIDTH;
        _indentation = Defaults.INDENTATION;
        _parindentation = Defaults.PARAGRAPH_INDENTATION;
        _parskip = Defaults.PARAGRAPH_SKIP;
        _fill = true;
        _justify = true;
        _words = new ArrayList<String>();
        _chars = new ArrayList<Character>();
        _newParagraph = true;
    }

    /** Add TEXT to the word currently being built. */
    void addText(String text) {
        for (char c : text.toCharArray()) {
            _chars.add(c);
        }
    }

    /** Finish the current word, if any, and add to words being accumulated. */
    void finishWord() {
        if (!_chars.isEmpty()) {
            _words.add(arrayListToString(_chars));
            _chars = new ArrayList<Character>();
        }
    }

    /** Add WORD to the formatted text. */
    void addWord(String word) {
        if (_chars.isEmpty()) {
            _words.add(word);
        }
    }

    /** Add LINE to our output, with no preceding paragraph skip.  There must
     *  not be an unfinished line pending. */
    void addLine(String line) {
        _pages.addLine(line);
    }

    /** Set the current indentation to VAL. VAL >= 0. */
    void setIndentation(int val) throws FormatException {
        if (val >= 0) {
            _indentation = val;
        } else {
            throw error("Tried to set indentation to %d < 0", val);
        }
    }

    /** Set the current paragraph indentation to VAL. VAL >= 0. */
    void setParIndentation(int val) throws FormatException {
        if (val + _indentation >= 0) {
            _parindentation = val;
        }  else {
            throw error("Tried to set par indentation disallowed val %d", val);
        }
    }

    /** Set the text width to VAL, where VAL >= 0. */
    void setTextWidth(int val) throws FormatException {
        if (val >= 0) {
            _width = val;
        } else {
            throw error("Tried to set text with to %d < 0", val);
        }
    }

    /** Iff ON, set fill mode. */
    void setFill(boolean on) {
        if (on) {
            _fill = on;
            if (!_nojustify) {
                _justify = on;
            }
        } else {
            _fill = on;
            _justify = on;
        }
    }

    /** Iff ON, set justify mode (which is active only when filling is
     *  also on). */
    void setJustify(boolean on) {
        if (on & _fill) {
            _justify = on;
        } else if (!on) {
            _nojustify = true;
            _justify = on;
        }
    }

    /** Set paragraph skip to VAL.  VAL >= 0. */
    void setParSkip(int val) throws FormatException {
        if (val >= 0) {
            _lastParskip = _parskip;
            _parskip = val;
        } else {
            throw error("Tried to set parskup to %d < 0", val);
        }
    }

    /** Swaps _parskip with _lastParskip. */
    void swapParSkip() {
        int temp = _parskip;
        _parskip = _lastParskip;
        _lastParskip = temp;
    }

    /** Set page height to VAL > 0. */
    void setTextHeight(int val) {
        _pages.setTextHeight(val);
    }

    /** Process the end of the current input line.  No effect if
     *  current line accumulator is empty or in fill mode.  Otherwise,
     *  adds a new complete line to the finished line queue and clears
     *  the line accumulator. */
    void newLine() {
        if (!_fill & _words.size() != 0) {
            simpleEmitLine(_indentation);
        }
    }

    /** If there is a current unfinished paragraph pending, close it
     *  out and start a new one. */
    void endParagraph() {
        finishWord();
        if (_fill) {
            outputLine(true);
        } else {
            newLine();
        }
        _newParagraph = true;
    }

    /** If there is an unfinished line, fill and justify it
     * then send it the output. */
    void finishLine() {
        finishWord();
        this.outputLine(false);
    }

    /** Returns the total number of characters contained in WORDS. */
    private static int wordChars(List<String> words) {
        int runningSum = 0;
        for (int i = 0; i < words.size(); i++) {
            runningSum += words.get(i).length();
        }
        return runningSum;
    }


    /** Resets _words and _chars to empty ArrayLists. */
    private void clear() {
        _words = new ArrayList<String>();
        _chars = new ArrayList<Character>();
    }


    /** Returns a new StringBuilder with INDENT chars of space. */
    private StringBuilder initLine(int indent) {
        StringBuilder line = new StringBuilder(_width);
        for (int i = 0; i < indent; i++) {
            line.append(' ');
        }
        return line;
    }

    /** Returns a string with NUM space characters and nothing else. */
    private static String nblanks(int num) {
        String b = "";
        for (int k = 0; k < num; k++) {
            b = b + " ";
        }
        return b;
    }

    /** Returns true iff _width - chars in WORDS - INDENT >= WORDS.size() - 1.*/
    private boolean filled(List<String> words, int indent) {
        int wchars = wordChars(words);
        int numWords = words.size();
        return (_width - wchars - indent >= numWords - 1);
    }

    /** Returns _width - chars in _words - INDENT.
     *  Always refers to blanks available for current _words*/
    private int blanks(int indent) {
        return (_width - wordChars(_words) - indent);
    }

    /** Transfer contents of _words to _pages, adding INDENT characters of
     *  indentation, and a total of SPACES spaces between words, evenly
     *  distributed.  Assumes _words is not empty.  Clears _words and _chars. */
    private void emitLine(int indent, int spaces) {
        StringBuilder line = this.initLine(indent);
        int numWords = _words.size();
        int partitions = numWords - 1;
        if (numWords == 1) {
            line.append(_words.get(0));
            addLine(line.toString());
        } else if (spaces >= 3 * (numWords - 1)) {
            for (int n = 0; n < numWords; n++) {
                line.append(_words.get(n));
                if (n != numWords - 1) {
                    line.append(nblanks(3));
                } else {
                    addLine(line.toString());
                }
            }
        } else {
            int[] spacing = distributeSpaces(spaces, numWords);
            for (int n = 0; n < numWords; n++) {
                line.append(_words.get(n));
                if (n != numWords - 1) {
                    line.append(nblanks(spacing[n]));
                } else {
                    addLine(line.toString());
                }
            }
        }
        this.clear();
    }

    /** Transfer contents of _words to _pages, adding INDENT chars of
     * indentation and one space between each word.
     * Only called for lastLine of each paragraph and non filled output */
    private void simpleEmitLine(int indent) {
        int parindent = indent + _parindentation;
        StringBuilder line = initLine(_newParagraph ? parindent : indent);
        int numWords = _words.size();
        if (_newParagraph) {
            for (int j = 0; j < _parskip; j++) {
                this.addLine(null);
            }
            _newParagraph = false;
        }
        for (int n = 0; n < numWords; n++) {
            line.append(_words.get(n));
            if (n != numWords - 1) {
                line.append(nblanks(1));
            } else {
                addLine(line.toString());
            }
        }
        this.clear();
    }

    /** If the line accumulator is non-empty, justify its current
     *  contents, if needed, add a new complete line to _pages,
     *  and clear the line accumulator. LASTLINE indicates the last line
     *  of a paragraph. */
    private void outputLine(boolean lastLine) {
        int indent = this._indentation;
        indent = _newParagraph ? indent + _parindentation : indent;
        int numWords = _words.size();
        if (numWords != 0) {
            if (!filled(_words, indent)) {
                List<String> extraWords;
                List<String> filledWords;
                boolean adjusted = false;
                for (int i = numWords - 1; i > 0; i--) {
                    filledWords = _words.subList(0, i);
                    extraWords = _words.subList(i, numWords);
                    if (filled(filledWords, indent)) {
                        _words = filledWords;
                        finishLine();
                        _words = extraWords;
                        outputLine(lastLine);
                        break;
                    } else if (i == 1) {
                        _words = filledWords;
                        emitLine(indent, 0);
                        _words = extraWords;
                        outputLine(lastLine);
                    }
                }
            } else {
                if (_newParagraph) {
                    for (int j = 0; j < _parskip; j++) {
                        this.addLine(null);
                    }
                    _newParagraph = !_newParagraph;
                }
                if (lastLine | !_justify) {
                    this.simpleEmitLine(indent);
                } else {
                    this.emitLine(indent, blanks(indent));
                }
            }
        }
    }

    /** Utility class for making common conversion.
     * @return the string rep of A */
    private String arrayListToString(List<Character> a) {
        StringBuilder rep = new StringBuilder(a.size());
        for (int i = 0; i < a.size(); i++) {
            rep.append(a.get(i));
        }
        return rep.toString();
    }

    /** Returns an array of ints describing the proper spacing.
     * Given ISPACES and IWORDS */
    private int[] distributeSpaces(int ispaces, int iwords) {
        double spaces = (double) ispaces;
        double words = (double) iwords;
        double ratio = spaces / (words - 1);
        int[] spacing = new int[iwords - 1];
        int runningSpaces = 0;
        for (int k = 1; k < iwords; k++) {
            int spacesSoFar = ((int) ((ratio * k) + .5));
            int spacesHere = spacesSoFar - runningSpaces;
            spacing[k - 1] = spacesHere;
            runningSpaces += spacesHere;
        }
        return spacing;
    }

    /** Wrapper class for testing distributeSpaces.
     * @return the result of calling distspace(SPACES, WORDS). */
    public int[] testSpaces(int spaces, int words) {
        return distributeSpaces(spaces, words);
    }

    /** Destination given in constructor for formatted lines. */
    private final PageAssembler _pages;
    /** The width of the current line. */
    private int _width;

    /** The current value of indentation. */
    private int _indentation;

    /** The curent par indentation. */
    private int _parindentation;

    /** True iff fill mode is on. */
    private boolean _fill;

    /** True iff justify mode is on. */
    private boolean _justify;

    /** The current value of par skip. */
    private int _parskip;

    /** The last value of parskip. */
    private int _lastParskip;

    /** Stores the current words in the line. */
    private List<String> _words;

    /** Stores the chars being accumulated to add as the next word. */
    private List<Character> _chars;

    /**A bool true iff endParagraph was called after the last line. */
    private boolean _newParagraph;

    /** True iff /nojustify has been called. */
    private boolean _nojustify = false;

}
