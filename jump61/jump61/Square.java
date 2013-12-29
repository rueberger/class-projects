package jump61;

import static jump61.Color.*;

/** Represents the state of a single jump61 square.
 * Keeps track of number dots, color, and
 * occupation number
 * @author Andrew Berger */
public class Square {

    /** The default constructor.
     * Makes an empty white square with OCCUPATION*/
    Square(int occupation) {
        _color = WHITE;
        _occ = occupation;
        _spots = 0;
    }

    /** Returns a copy of me. */
    public Square clone() {
        Square c = new Square(_occ);
        c.setColor(_color);
        c.setSpots(_spots);
        return c;
    }

    /** Returns true iff there are more dots than room. */
    public boolean isFull() {
        return _spots > _occ;
    }

    /** Set my color to color PLAYER. */
    public void setColor(Color player) {
        _color = player;
    }

    /** Set my num of spots to NUM. */
    public void setSpots(int num) {
        _spots = num;
    }

    /** Returns my number of spots. */
    public int spots() {
        return _spots;
    }

    /** Returns my color. */
    public Color color() {
        return  _color;
    }

    /** Field holding color of THIS square. */
    private Color _color;

    /** Field holding dots on THIS square. */
    private int _spots;

    /** Field holding occupation num of THIS square. */
    private int _occ;


}
