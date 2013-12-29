package graph;

import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;

/** Assorted graph algorithms.
 *  @author Andrew Berger
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {

        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            if (v != V0) {
                vweighter.setWeight(v.getLabel(), Double.POSITIVE_INFINITY);
            }
        }
        OrderedSearch<VLabel, ELabel> search =
            new OrderedSearch<VLabel, ELabel>(V1, vweighter, eweighter);
        search.traverse(G, V0, new Comparator<VLabel>() {
                @Override
                public int compare(VLabel v1, VLabel v2) {
                    return (int) (h.dist(v2, V1.getLabel())
                                  - h.dist(v1, V1.getLabel()));
                }
            });

        return (search.getPath());
    }

    /** An A* search. */
    private static class OrderedSearch<VLabel, ELabel>
        extends Traversal<VLabel, ELabel> {
        /** A new A* search from DEST.
         *  Uses VWEIGHTER and EWEIGHTER.*/
        OrderedSearch(Graph<VLabel, ELabel>.Vertex dest,
                      Weighter<? super VLabel> vweighter,
                      Weighting<? super ELabel> eweighter) {
            _dest = dest;
            _vweighter = vweighter;
            _eweighter = eweighter;
            _parents = new HashMap<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge>();
        }

        @Override
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            if (v == _dest) {
                throw new StopException();
            }
        }

        @Override
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v0) {
            Graph<VLabel, ELabel>.Vertex w = e.getV(v0);
            double v = _vweighter.weight(v0.getLabel());
            double vw = _eweighter.weight(e.getLabel());
            if (_vweighter.weight(w.getLabel()) > (v + vw)) {
                _vweighter.setWeight(w.getLabel(), v + vw);
                _parents.put(w, e);
            }
        }

        /** Walks through our parent pointers and makes a list.
         *  Returns the shortest path.*/
        public List<Graph<VLabel, ELabel>.Edge> getPath() {
            Graph<VLabel, ELabel>.Vertex last = finalVertex();
            List<Graph<VLabel, ELabel>.Edge> path =
                new ArrayList<Graph<VLabel, ELabel>.Edge>();
            while (_parents.get(last) != null) {
                Graph<VLabel, ELabel>.Edge lastCrossed = _parents.get(last);
                path.add(0, lastCrossed);
                last = lastCrossed.getV(last);
            }
            if (!path.isEmpty()) {
                return path;
            }
            return null;
        }

        /** Maps to the edge we traversed to get here.*/
        private HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> _parents;
        /** The vertex we are looking for.*/
        private Graph<VLabel, ELabel>.Vertex _dest;
        /** Returns weight on vertex.*/
        private Weighter<? super VLabel> _vweighter;
        /** Returns weight on edge.*/
        private Weighting<? super ELabel> _eweighter;
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        Weighter<VLabel> vweight = new ComponentWeighter<VLabel>();
        Weighting<ELabel> eweight = new ComponentWeighting<ELabel>();
        return shortestPath(G, V0, V1, h, vweight, eweight);
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };


    /** Class wrapping weighted objects.*/
    private static class ComponentWeighting<Label extends Weighted>
        implements Weighting<Label> {

        /** A new component weighting. */
        ComponentWeighting() {
        }

        @Override
        public double weight(Label x) {
            return x.weight();
        }
    }

    /** Class wrapping weightable objects.*/
    private static class ComponentWeighter<Label extends Weightable>
        extends ComponentWeighting<Label> implements Weighter<Label> {
        /** A new component weighter. */
        ComponentWeighter() {
            super();
        }

        @Override
        public void setWeight(Label x, double v) {
            x.setWeight(v);
        }

    }



}
