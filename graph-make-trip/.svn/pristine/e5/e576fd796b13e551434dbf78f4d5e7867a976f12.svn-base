package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

/** An adjacency matrix implementation.
 *  Used by directed and undirected
 * @author Andrew Berger
 */
abstract class AdjMat<VLabel, ELabel> extends Graph<VLabel, ELabel> {


    @Override
    public int vertexSize() {
        return _vertices.size();
    }

    @Override
    public int edgeSize() {
        return _edgeMap.size();
    }


    @Override
    public int outDegree(Vertex v) {
        int ind = _vertMap.get(v);
        int outDeg = 0;
        for (int i = 0; i < _edges.size(); i++) {
            if (!_edges.get(ind).get(i).isNull()) {
                outDeg += 1;
            }
            if (!isDirected() && !_edges.get(i).get(ind).isNull()) {
                outDeg += 1;
            }
        }
        return outDeg;
    }

    @Override
    public int inDegree(Vertex v) {
        int ind = _vertMap.get(v);
        int inDeg = 0;
        for (int i = 0; i < _edges.size(); i++) {
            if (!_edges.get(i).get(ind).isNull()) {
                inDeg += 1;
            }
        }
        return inDeg;
    }

    @Override
    public boolean contains(Vertex u, Vertex v) {
        int from = _vertMap.get(u);
        int to = _vertMap.get(v);
        if (!_edges.get(from).get(to).isNull()) {
            return true;
        }
        if (!isDirected() && !_edges.get(to).get(from).isNull()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        int from = _vertMap.get(u);
        int to = _vertMap.get(v);
        if (contains(u, v)) {
            if (_edges.get(from).get(to).getLabel() == label) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Vertex add(VLabel label) {
        Vertex v = new Vertex(label, _idIncr);
        _idIncr += 1;
        increaseSize(1);
        _vertices.add(v);
        _vertMap.put(v, size() - 1);
        return v;
    }

    @Override
    public Edge add(Vertex from, Vertex to, ELabel label) {
        int fromInd = _vertMap.get(from);
        int toInd = _vertMap.get(to);
        Edge e = new Edge(from, to, label, _idIncr);
        _idIncr += 1;
        addEdge(fromInd, toInd, e);
        return e;
    }


    /** Internal method for making edge E between vertices FROM and TO .
     *  Need fast mapping from vertices to indices */
    private void addEdge(int from, int to, Edge e) {
        _edges.get(from).set(to, e);
        _edgeMap.put(e, new int [] {from, to});
    }

    @Override
    public void remove(Edge e) {
        if (_edgeMap.get(e) != null) {
            int[] coord = _edgeMap.get(e);
            int from = coord[0];
            int to = coord[1];
            _edgeMap.remove(_edges.get(from).get(to));
            _edges.get(from).set(to, new Edge(_idIncr));
            _idIncr += 1;
            if (!isDirected()) {
                _edgeMap.remove(_edges.get(to).get(from));
                _edges.get(to).set(from, new Edge(_idIncr));
                _idIncr += 1;
            }
        }
    }

    @Override
    public void remove(Vertex v) {
        removeFromEdgeMat(v);
        removeFromVertexList(v);
    }

    @Override
    public void remove(Vertex v1, Vertex v2) {
        if (_vertMap.get(v1) != null && _vertMap.get(v2) != null) {
            int from = _vertMap.get(v1);
            int to = _vertMap.get(v2);
            if (!_edges.get(from).get(to).isNull()) {
                remove(_edges.get(from).get(to));
            }
            if (!_edges.get(to).get(from).isNull()) {
                remove(_edges.get(to).get(from));
            }
        }
    }

    @Override
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(_vertices.iterator());
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        List<Vertex> successors = new ArrayList<Vertex>();
        if (_vertMap.get(v) != null) {
            int ind = _vertMap.get(v);
            for (int i = 0; i < _edges.size(); i++) {
                if  (!_edges.get(ind).get(i).isNull()) {
                    successors.add(_vertices.get(i));
                }
                if (!isDirected() && !_edges.get(i).get(ind).isNull()) {
                    successors.add(_vertices.get(i));
                }
            }
        }
        return Iteration.iteration(successors.iterator());
    }

    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        List<Vertex> predecessors = new ArrayList<Vertex>();
        if (_vertMap.get(v) != null) {
            int ind = _vertMap.get(v);
            for (int i = 0; i < _edges.size(); i++) {
                if (!_edges.get(i).get(ind).isNull()) {
                    predecessors.add(_vertices.get(i));
                }
            }
        }
        return Iteration.iteration(predecessors.iterator());
    }

    @Override
    public Iteration<Edge> edges() {
        _orderedEdges.clear();
        for (int i = 0; i < _edges.size(); i++) {
            for (int j = 0; j < _edges.size(); j++) {
                if (!_edges.get(i).get(j).isNull()) {
                    _orderedEdges.add(_edges.get(i).get(j));
                }
            }
        }
        return Iteration.iteration(_orderedEdges.iterator());
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        _orderedEdges.clear();
        if (_vertMap.get(v) != null) {
            int ind = _vertMap.get(v);
            for (int i = 0; i < _edges.size(); i++) {
                if (!_edges.get(ind).get(i).isNull()) {
                    _orderedEdges.add(_edges.get(ind).get(i));
                }
            }
        }
        return Iteration.iteration(_orderedEdges.iterator());
    }

    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        _orderedEdges.clear();
        if (_vertMap.get(v) != null) {
            int ind = _vertMap.get(v);
            for (int i = 0; i < _edges.size(); i++) {
                if (!_edges.get(i).get(ind).isNull()) {
                    _orderedEdges.add(_edges.get(i).get(ind));
                }
            }
        }
        return Iteration.iteration(_orderedEdges.iterator());
    }

    @Override
    public void orderEdges(final Comparator<ELabel> comparator) {
        _orderedEdges = new PriorityQueue<Edge>(QUEUE, new Comparator<Edge>() {
                @Override
                public int compare(Edge e1, Edge e2) {
                    return comparator.compare(e1.getLabel(), e2.getLabel());
                }
            });
    }

    /** Adds NUM new spots to _EDGE, initally all null.
     *  Ensures _EDGE is square
     *  IMPORTANT: Does not modify _VERTICES*/
    private void increaseSize(int num) {
        int increasedSize = _edges.size() + num;
        for (int i = 0; i < _edges.size(); i++) {
            for (int j = 0; j < num; j++) {
                _edges.get(i).add(new Edge(_idIncr));
                _idIncr += 1;
            }
        }
        for (int k = 0; k < num; k += 1) {
            _edges.add(new ArrayList<Edge>());
            for (int z = 0; z < increasedSize; z++) {
                _edges.get(increasedSize - num + k).add(new Edge(_idIncr));
                _idIncr += 1;
            }
        }
    }

    /** Returns the current size.*/
    private int size() {
        assert _vertices.size() == _edges.size();
        return _edges.size();
    }


    /** Subroutine of remove V.
     * Removes V from _VERTICES at pos IND, then shifts the
     * positions of the i+1..n] vertices in the hastable my -1.*/
    private void removeFromVertexList(Vertex v) {
        int ind = _vertices.indexOf(v);
        for (int i = ind + 1; i < _vertices.size(); i++) {
            _vertMap.put(_vertices.get(i), i - 1);
        }
        _vertices.remove(v);
    }

    /** Subroutine of remove V.
     * CALL THIS BEFORE REMOVEFROMVERTEXLIST
     * Takes out a collumn and row in V.*/
    private void removeFromEdgeMat(Vertex v) {
        int ind = _vertices.indexOf(v);
        for (int i = 0; i < _edges.size(); i++) {
            remove(_edges.get(i).get(ind));
            remove(_edges.get(ind).get(i));
        }
        _edges.remove(ind);
        for (int j = 0; j < _edges.size(); j++) {
            _edges.get(j).remove(ind);
        }
        _edgeMap.clear();
        for (int x = 0; x < _edges.size(); x++) {
            for (int y = 0; y < _edges.size(); y++) {
                if (!_edges.get(x).get(y).isNull()) {
                    _edgeMap.put(_edges.get(x).get(y), new int[] {x, y});
                }
            }
        }
    }


    /** Holds vertice labels. Is of dimension SIZE.*/
    private List<Vertex> _vertices = new ArrayList<Vertex>();
    /** Fast mapping from vertice to its indice. */
    private HashMap<Vertex, Integer> _vertMap = new HashMap<Vertex, Integer>();
    /** Holds edge labels. Each list of dimension SIZE. */
    private ArrayList<ArrayList<Edge>> _edges =
        new ArrayList<ArrayList<Edge>>();
    /** Fast mapping from edge to indices. */
    private HashMap<Edge, int[]> _edgeMap = new HashMap<Edge, int[]>();
    /** An ordered set of edges. */
    private PriorityQueue<Edge> _orderedEdges =
        new PriorityQueue<Edge>(QUEUE, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                return e2.getId() - e1.getId();
            }
        });
    /** Increment every time a new vertex or edge is created.*/
    private int _idIncr = 0;
    /** Starting size of queue. */
    private static final int QUEUE = 11;
}
