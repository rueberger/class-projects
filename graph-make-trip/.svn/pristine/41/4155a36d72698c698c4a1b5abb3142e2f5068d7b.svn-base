package trip;

import graph.Weightable;

/** The VLabel for trip.
 *  @author Andrew Berger*/
public class Place implements Weightable {
    /** A new place of NAME at X, Y.*/
    Place(String name, float x, float y) {
        _name = name;
        _x = x;
        _y = y;
    }

    @Override
    public double weight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    /** Returns my current name.*/
    public String name() {
        return _name;
    }

    /** Returns the coords I'm at.*/
    public float[] coords() {
        return new float[] {_x, _y};
    }

    /** The weight.*/
    private double _weight;
    /** The name of this label.*/
    private String _name;
    /** The x coordinate. */
    private float _x;
    /** The y coordinate. */
    private float _y;
}
