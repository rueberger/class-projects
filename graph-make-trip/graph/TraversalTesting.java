package graph;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class TraversalTesting {

    public DirectedGraph<Integer, Integer> getTestGraph() {
        DirectedGraph<Integer, Integer> d =
            new DirectedGraph<Integer, Integer>();
        DirectedGraph<Integer, Integer>.Vertex v = d.add(5);
        DirectedGraph<Integer, Integer>.Vertex w = d.add(5);
        DirectedGraph<Integer, Integer>.Vertex x = d.add(5);
        DirectedGraph<Integer, Integer>.Vertex y = d.add(5);
        DirectedGraph<Integer, Integer>.Vertex z = d.add(5);
        DirectedGraph<Integer, Integer>.Edge vw = d.add(v, w, 5);
        DirectedGraph<Integer, Integer>.Edge vx = d.add(v, x, 5);
        DirectedGraph<Integer, Integer>.Edge vy = d.add(v, y, 5);
        DirectedGraph<Integer, Integer>.Edge vz = d.add(v, z, 5);
        DirectedGraph<Integer, Integer>.Edge xy = d.add(x, y, 5);
        DirectedGraph<Integer, Integer>.Edge xz = d.add(x, z, 5);
        return d;
    }

    /** Returns a simple directed graph with ELabel set to 5*depth. */
    public DirectedGraph<Integer, Integer> getSimpleTestGraph() {
        DirectedGraph<Integer, Integer> d =
            new DirectedGraph<Integer, Integer>();
        DirectedGraph<Integer, Integer>.Vertex v = d.add(5);
        DirectedGraph<Integer, Integer>.Vertex w = d.add(10);
        DirectedGraph<Integer, Integer>.Vertex x = d.add(10);
        DirectedGraph<Integer, Integer>.Vertex y = d.add(15);
        DirectedGraph<Integer, Integer>.Edge vw = d.add(v, w, 5);
        DirectedGraph<Integer, Integer>.Edge vx = d.add(v, x, 5);
        DirectedGraph<Integer, Integer>.Edge wy = d.add(w, y, 5);
        DirectedGraph<Integer, Integer>.Edge xy = d.add(x, y, 5);
        return d;
    }

    @Test
    public void testPV() {
        DirectedGraph<Integer, Integer> d =
            new DirectedGraph<Integer, Integer>();
        DirectedGraph<Integer, Integer>.Vertex v = d.add(1);
        DirectedGraph<Integer, Integer>.Vertex w = d.add(2);
        DirectedGraph<Integer, Integer>.Vertex x = d.add(3);
        DirectedGraph<Integer, Integer>.Edge vw = d.add(v, w, 5);
        DirectedGraph<Integer, Integer>.Edge wx = d.add(w, x, 5);
        TestTraversal<Integer> t = new TestTraversal<Integer>();
        System.out.println("Depth first");
        t.depthFirstTraverse(d, v);
        System.out.println("Breadth first");
        t.breadthFirstTraverse(d, v);
    }


    public void trivialTraverse() {
        DirectedGraph<Integer, Integer> d =
            new DirectedGraph<Integer, Integer>();
        DirectedGraph<Integer, Integer>.Vertex v = d.add(1);
        DirectedGraph<Integer, Integer>.Vertex w = d.add(2);
        DirectedGraph<Integer, Integer>.Vertex x = d.add(3);
        DirectedGraph<Integer, Integer>.Edge vw = d.add(v, w, 5);
        DirectedGraph<Integer, Integer>.Edge vx = d.add(v, x, 5);
        TestTraversal<Integer> t = new TestTraversal<Integer>();
        t.breadthFirstTraverse(d, v);
    }



    public void checkBFS() {
        DirectedGraph<Integer, Integer> d =
            new DirectedGraph<Integer, Integer>();
        DirectedGraph<Integer, Integer>.Vertex v = d.add(1);
        DirectedGraph<Integer, Integer>.Vertex w = d.add(2);
        DirectedGraph<Integer, Integer>.Vertex x = d.add(3);
        DirectedGraph<Integer, Integer>.Vertex y = d.add(4);
        DirectedGraph<Integer, Integer>.Edge vw = d.add(v, w, 5);
        DirectedGraph<Integer, Integer>.Edge vx = d.add(v, x, 5);
        DirectedGraph<Integer, Integer>.Edge wy = d.add(w, y, 5);
        DirectedGraph<Integer, Integer>.Edge xy = d.add(x, y, 5);
        TestTraversal<Integer> traversal = new TestTraversal<Integer>();
        traversal.breadthFirstTraverse(d, v);
        ArrayList<Integer> results = traversal.getResults();
        assertEquals("Start with v",
                     Integer.valueOf(5), Integer.valueOf(results.get(0)));
        assertEquals("Second level vertices",
                     Integer.valueOf(10), Integer.valueOf(results.get(1)));
        assertEquals("Second level vertices",
                     Integer.valueOf(10), Integer.valueOf(results.get(2)));
        assertEquals("Third level vertex",
                     Integer.valueOf(15), Integer.valueOf(results.get(3)));
    }

    /** Test class for traversal.*/
    private class TestTraversal<Integer> extends Traversal<Integer, Integer> {
        @Override
        protected void visit(Graph<Integer, Integer>.Vertex v) {
            _test.add(v.getLabel());
        }

        /** A new traversal that stores labels in TESTLIST. */
        TestTraversal() {
        }

        public ArrayList<Integer> getResults() {
            return _test;
        }

        /** Holds my Vlabels in the order the vertices are visited. */
        ArrayList<Integer> _test = new ArrayList<Integer>();
    }


}
