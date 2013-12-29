package make;

import java.util.HashSet;

import graph.Traversal;
import graph.Graph;

/** A cycle checker.
 *  @author Andrew Berger*/
public class CycleCheck extends Traversal<Target, String> {
    /** A new cycleCheck. */
    CycleCheck() {
        _visited = new HashSet<Graph<Target, String>.Vertex>();
        _youngestAncestor = Integer.MIN_VALUE;
    }

    /** Reinitializes this. */
    public void reset() {
        _visited = new HashSet<Graph<Target, String>.Vertex>();
        _youngestAncestor = Integer.MIN_VALUE;
    }

    @Override
    protected void visit(Graph<Target, String>.Vertex v) {
        _visited.add(v);
        for (Graph<Target, String>.Vertex w : theGraph().successors(v)) {
            if (_visited.contains(w)) {
                throw new MakeFormatException("Cyclic dependency detected");
            }
        }
    }

    @Override
    protected void postVisit(Graph<Target, String>.Vertex v) {
        Target t = v.getLabel();
        if (t.youngest() > _youngestAncestor) {
            _youngestAncestor = t.youngest();
        } else {
            t.setAncestor(_youngestAncestor);
        }
    }

    /** Ind true iff the vertex with that id has been visited.*/
    private HashSet<Graph<Target, String>.Vertex> _visited;
    /** The change date of the youngest ancestor.*/
    private int _youngestAncestor;

}
