package trip;

import graph.Weighted;

/** The ELabel for trip.
 *  @author Andrew Berger */
public class Route implements Weighted {
    /** A route of NAME DIRECTION and weight D.*/
    Route(String name, String direction, double d) {
        _weight = d;
        _name = name;
        _dir = direction;
    }

    @Override
    public double weight() {
        return _weight;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns my direction. */
    String direction() {
        return _dir;
    }

    /** Returns the direction I travel starting at v0.*/
    String compassReverse() {
        switch (_dir) {
        case "NS":
            return "south";
        case "SN":
            return "north";
        case "EW":
            return "west";
        case "WE":
            return "east";
        default:
            return "";
        }
    }

    /** Returns the direction I travel starting at v1. */
    String compass() {
        switch(_dir) {
        case "NS":
            return "north";
        case "SN":
            return "south";
        case "EW":
            return "east";
        case "WE":
            return "west";
        default:
            return "";
        }
    }

    /** The weight. */
    private double _weight;
    /** The name of this route. */
    private String _name;
    /** One of the four allowed directions. */
    private String _dir;
}
