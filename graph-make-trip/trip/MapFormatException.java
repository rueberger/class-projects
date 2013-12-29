package trip;

/** An exception in our map data.
 *  @author Andrew Berger*/
class MapFormatException extends RuntimeException {

    /** A mapFormatException with no message. */
    MapFormatException() {
    }

    /** MapFormatException with MSG. */
    MapFormatException(String msg) {
        super(msg);
    }

}
