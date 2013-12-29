package make;

import graph.Traversal;
import graph.Graph;

import java.io.PrintWriter;


/** A new builder.
 *  @author Andrew Berger*/
public class Builder extends Traversal<Target, String> {
    /** A new Builder at SYSTEMTIME with CYCLER.
     *  Sends output to OUT.*/
    Builder(int systemTime, CycleCheck cycler, PrintWriter out) {
        _sysTime = systemTime;
        _cycler = cycler;
        _out = out;
    }

    @Override
    protected void postVisit(Graph<Target, String>.Vertex v) {
        _cycler.reset();
        _cycler.depthFirstTraverse(theGraph(), v);
        Target t = (Target) v.getLabel();
        if (t.changed() < t.youngest()) {
            _sysTime += 1;
            _out.print(t.execute(_sysTime));
        }
        if (!t.isExtant()) {
            _sysTime += 1;
            _out.print(t.execute(_sysTime));
        }
        _out.flush();
    }

    /** The current time.
     *  All new objects must be older than this time. */
    private int _sysTime;
    /** Checks for cycles and gets ancestor times. */
    private CycleCheck _cycler;
    /** The standard output.*/
    private PrintWriter _out;
}
