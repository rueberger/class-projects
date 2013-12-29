package make;

/** The VLabel class for make.
 *  @author Andrew Berger*/
public class Target {

    /** A new Target NAME  executing RULE.
     *  Starts at time 1.*/
    Target(String name, String rule) {
        _name = name;
        _rule = rule;
    }

    /** Returns our commands and sets CURRTIME. */
    public String execute(int currTime) {
        setTime(currTime);
        exists();
        return _rule;
    }

    /** Set change date to TIME.*/
    public void setTime(int time) {
        _lastChanged = time;
        if (time > _youngestAncestor) {
            _youngestAncestor = time;
        }
    }

    /** Sets the change date of the youngest ancestor to TIME. */
    public void setAncestor(int time) {
        _youngestAncestor = time;
    }

    /** Returns my change date. */
    public int changed() {
        return _lastChanged;
    }

    /** Returns the change date of my youngest ancestor. */
    public int youngest() {
        return _youngestAncestor;
    }

    /** Returns my name. */
    public String name() {
        return _name;
    }


    /** Sets that I exist.*/
    public void exists() {
        _exists = true;
    }

    /** Returns true iff I have been built.*/
    public boolean isExtant() {
        return _exists;
    }

    /** My name.*/
    private String _name;
    /** Last time I was modified.*/
    private int _lastChanged;
    /** The rule I execute.*/
    private String _rule;
    /** True if I have been built.*/
    private boolean _exists;
    /** The system time of the youngest ancestor. */
    private int _youngestAncestor;

}
