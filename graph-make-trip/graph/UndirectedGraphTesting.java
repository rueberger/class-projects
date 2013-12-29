package graph;

import org.junit.Test;
import static org.junit.Assert.*;

public class UndirectedGraphTesting {


    @Test
    public void checkEdgeCounts() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        assertEquals("No edges", Integer.valueOf(0),
                     Integer.valueOf(u.edgeSize()));
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Edge vw  = u.add(v, w, 5);
        assertEquals("One edge", Integer.valueOf(1),
                     Integer.valueOf(u.edgeSize()));
        u.remove(vw);
        assertEquals("No edges", Integer.valueOf(0),
                     Integer.valueOf(u.edgeSize()));
        UndirectedGraph<Integer, Integer>.Edge vww  = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        assertEquals("3 edges", Integer.valueOf(3),
                     Integer.valueOf(u.edgeSize()));
    }

    @Test
    public void simpleRemoveVertex() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        assertEquals("3 edges", Integer.valueOf(3),
                     Integer.valueOf(u.edgeSize()));
        u.remove(v);
        assertEquals("No edges", Integer.valueOf(0),
                     Integer.valueOf(u.edgeSize()));
        assertEquals("Now disconnected", Integer.valueOf(0),
                     Integer.valueOf(u.degree(w)));
        assertEquals("Now disconnected", Integer.valueOf(0),
                     Integer.valueOf(u.degree(y)));
        assertEquals("Now disconnected", Integer.valueOf(0),
                     Integer.valueOf(u.degree(x)));
    }

    @Test
    public void checkAdd() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Edge z  = u.add(v, w, 5);
        assertEquals("V value is wrong",
                     Integer.valueOf(10), Integer.valueOf(v.getLabel()));
        assertEquals("w value is wrong",
                     Integer.valueOf(15), Integer.valueOf(w.getLabel()));
        assertEquals("Edge exists", true, u.contains(v, w, 5));
    }

    @Test
    public void checkDegree() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        UndirectedGraph<Integer, Integer>.Edge xy = u.add(x, y, 5);
        assertEquals("Wrong degree", 3, u.degree(v));
        assertEquals("Wrong degree", 1, u.degree(w));
        assertEquals("Wrong degree", 2, u.degree(y));
        assertEquals("Wrong degree", 2, u.degree(x));
    }

    @Test
    public void checkEdgeRemoval() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        assertEquals("Edge exists", true, u.contains(v, w, 5));
        u.remove(vw);
        assertEquals("Edge has been removed", false, u.contains(v, w, 5));
        assertEquals("Edge exists", true, u.contains(v, x, 5));
        u.remove(vx);
        assertEquals("Edge has been removed", false, u.contains(v, x, 5));
    }

    @Test
    public void checkVertexRemoval() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        assertEquals("Edge exists", true, u.contains(v, w, 5));
        assertEquals("Edge exists", true, u.contains(v, x, 5));
        assertEquals("2 Edges", 2, u.degree(v));
        assertEquals("2 edges exist", 2, u.edgeSize());
        assertEquals("3 vertices exist", 3, u.vertexSize());
        u.remove(v);
        assertEquals("Now unconnected", 0, u.degree(w));
        assertEquals("Now unconnected", 0, u.degree(x));
        assertEquals("No edges exist", 0 , u.edgeSize());
        assertEquals("2 vertices exist", 2 , u.vertexSize());
    }

    @Test
    public void checkVertexIterator() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        Iteration<UndirectedGraph<Integer, Integer>.Vertex> verts =
            u.vertices();
        int vertCount = 0;
        for (UndirectedGraph<Integer, Integer>.Vertex unused : verts) {
            vertCount += 1;
        }
        assertEquals("Wrong # of elements", u.vertexSize(), vertCount);
    }

    @Test
    public void checkSuccessorIterator() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        UndirectedGraph<Integer, Integer>.Edge xy = u.add(x, y, 5);
        Iteration<UndirectedGraph<Integer, Integer>.Vertex> successors =
            u.neighbors(v);
        int vertCount = 0;
        for (UndirectedGraph<Integer, Integer>.Vertex unused : successors) {
            vertCount += 1;
        }
        assertEquals("3 neighbors", 3, vertCount);
        successors = u.neighbors(x);
        vertCount = 0;
        for (UndirectedGraph<Integer, Integer>.Vertex unused : successors) {
            vertCount += 1;
        }
        assertEquals("2 neighbors", 2, vertCount);
    }

    @Test
    public void checkEdgeIterator() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        UndirectedGraph<Integer, Integer>.Edge xy = u.add(x, y, 5);
        Iteration<UndirectedGraph<Integer, Integer>.Edge> edges = u.edges();
        int edgeCount = 0;
        for (UndirectedGraph<Integer, Integer>.Edge e: edges) {
            edgeCount += 1;
        }
        assertEquals("Matches total edges", u.edgeSize(), edgeCount);
    }

    @Test
    public void checkOutgoingEdgeIterator() {
        UndirectedGraph<Integer, Integer> u =
            new UndirectedGraph<Integer, Integer>();
        UndirectedGraph<Integer, Integer>.Vertex v = u.add(10);
        UndirectedGraph<Integer, Integer>.Vertex w = u.add(15);
        UndirectedGraph<Integer, Integer>.Vertex x = u.add(20);
        UndirectedGraph<Integer, Integer>.Vertex y = u.add(25);
        UndirectedGraph<Integer, Integer>.Edge vx = u.add(v, x, 5);
        UndirectedGraph<Integer, Integer>.Edge vw = u.add(v, w, 5);
        UndirectedGraph<Integer, Integer>.Edge vy = u.add(v, y, 5);
        UndirectedGraph<Integer, Integer>.Edge xy = u.add(x, y, 5);
        Iteration<UndirectedGraph<Integer, Integer>.Edge> edges = u.edges(v);
        int edgeCount = 0;
        for (UndirectedGraph<Integer, Integer>.Edge e : edges) {
            edgeCount += 1;
        }
        assertEquals("3 edges", 3, edgeCount);
    }

}
