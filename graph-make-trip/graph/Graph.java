package graph;

import java.util.Comparator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges and may have multiple edges between vertices.
 *
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Andrew Berger
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex marked ID with LABEL as the value of getLabel(). */
        Vertex(VLabel label, int id) {
            _label = label;
            _id = id;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** Returns my unique id. */
        public int getId() {
            return _id;
        }

        @Override
        public int hashCode() {
            return _id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return false;
        }

        /** The label on this vertex. */
        private final VLabel _label;
        /** My unique id.*/
        private int _id;
    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) marked ID with label LABEL.
         *  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label, int id) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
            _id = id;
            _null = false;
        }

        /** A null edge of ID.*/
        Edge(int id) {
            _label = null;
            _v0 = null;
            _v1 = null;
            _id = id;
            _null = true;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }


        @Override
        public int hashCode() {
            return _id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            return false;
        }


        /** Returns my unique id. */
        public int getId() {
            return _id;
        }

        /** Returns true if I am a null edge. */
        public boolean isNull() {
            return _null;
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;
        /** My unique id.*/
        private int _id;
        /** True iff I am a null edge.*/
        private boolean _null;
    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public abstract int vertexSize();

    /** Returns the number of edges in me. */
    public abstract int edgeSize();

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public abstract int outDegree(Vertex v);

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public abstract int inDegree(Vertex v);

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public abstract boolean contains(Vertex u, Vertex v);

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public abstract boolean contains(Vertex u, Vertex v,
                            ELabel label);

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public abstract Vertex add(VLabel label);

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public abstract Edge add(Vertex from,
                    Vertex to,
                    ELabel label);

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public abstract void remove(Vertex v);

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public abstract void remove(Edge e);

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public abstract void remove(Vertex v1, Vertex v2);

    /** Returns an Iterator over all vertices in arbitrary order. */
    public abstract Iteration<Vertex> vertices();

    /** Returns an iterator over all successors of V. */
    public abstract Iteration<Vertex> successors(Vertex v);

    /** Returns an iterator over all predecessors of V. */
    public abstract Iteration<Vertex> predecessors(Vertex v);

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public abstract Iteration<Edge> edges();

    /** Returns iterator over all outgoing edges from V. */
    public abstract Iteration<Edge> outEdges(Vertex v);

    /** Returns iterator over all incoming edges to V. */
    public abstract Iteration<Edge> inEdges(Vertex v);

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if intComp = Graph.<Integer>naturalOrder(), then
     *  intComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public abstract void orderEdges(Comparator<ELabel> comparator);

}
