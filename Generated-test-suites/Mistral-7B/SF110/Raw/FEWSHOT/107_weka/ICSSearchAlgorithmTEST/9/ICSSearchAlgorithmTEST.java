package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;
    private BayesNet bayesNet;

    @Before
    public void setup() throws Exception {
        instances = Mockito.spy(new Instances(new FileReader("data/contact-lenses.arff")));
        instances.setClassIndex(instances.numAttributes() - 1);
        bayesNet = mock(BayesNet.class);
        icssa = new ICSSearchAlgorithm();
        icssa.setInstances(instances);
        icssa.setBayesNet(bayesNet);
    }

    @Test
    public void testName() {
        assertEquals("age", icssa.name(0));
    }

    @Test
    public void testMaxn() {
        assertEquals(10, icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(5);
        assertEquals(5, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals(2, icssa.getMaxCardinality());
    }

    @Test
    public void testSearch() throws Exception {
        doNothing().when(bayesNet).getParentSet(anyInt());
        icssa.search(bayesNet, instances);

        // Verify that every node has been added parents
        for (int i = 0; i < icssa.maxn(); ++i) {
            ParentSet parents = bayesNet.getParentSet(i);
            assertTrue(parents.size() > 0);
        }
    }

    @Test(expected = Exception.class)
    public void testSearchThrowsException() throws Exception {
        doThrow(new Exception("Test Exception")).when(bayesNet).getParentSet(anyInt());
        icssa.search(bayesNet, instances);
    }

    @Test
    public void testCalcDependencyGraph() {
        // Stub dependencies graph
        boolean[][] edges = new boolean[11][];
        for (int i = 0; i < 11; ++i) {
            edges[i] = new boolean[11];
        }

        icssa.calcDependencyGraph(edges, null);

        // Check every node is connected to itself
        for (int i = 0; i < 11; ++i) {
            assertTrue(edges[i][i]);
        }

        // Check every node is connected to every other node
        for (int i = 0; i < 11; ++i) {
            for (int j = 0; j < 11; ++j) {
                if (i != j) {
                    assertTrue(edges[i][j]);
                    assertTrue(edges[j][i]);
                }
            }
        }
    }

    @Test
    public void testExistsSepSet() {
        boolean[][] edges = new boolean[11][];
        for (int i = 0; i < 11; ++i) {
            edges[i] = new boolean[11];
        }

        SeparationSet sepset = icssa.existsSepSet(0, 1, 2, edges);
        assertNull(sepset);

        // Test with existing set
        edges[0][1] = false;
        edges[0][2] = true;
        edges[1][2] = true;
        sepset = icssa.existsSepSet(0, 1, 2, edges);
        assertNotNull(sepset);
        assertEquals(2, sepset.m_set.length);
        assertTrue(sepset.m_set[0] == 3);
        assertTrue(sepset.m_set[1] == 4);
    }

    @Test
    public void testNext() {
        int x = icssa.next(-1, 0, 1, new boolean[11][]);
        assertEquals(1, x);

        x = icssa.next(0, 0, 1, new boolean[11][]);
        assertEquals(2, x);

        x = icssa.next(1, 0, 1, new boolean[11][]);
        assertEquals(3, x);

        x = icssa.next(1, 0, 1, new boolean[11][]);
        assertEquals(4, x);

        x = icssa.next(4, 0, 1, new boolean[11][]);
        assertEquals(5, x);

        x = icssa.next(5, 0, 1, new boolean[11][]);
        assertEquals(6, x);

        x = icssa.next(6, 0, 1, new boolean[11][]);
        assertEquals(7, x);

        x = icssa.next(7, 0, 1, new boolean[11][]);
        assertEquals(8, x);

        x = icssa.next(8, 0, 1, new boolean[11][]);
        assertEquals(9, x);

        x = icssa.next(9, 0, 1, new boolean[11][]);
        assertEquals(10, x);

        x = icssa.next(10, 0, 1, new boolean[11][]);
        assertEquals(-1, x);
    }

    @Test
    public void testCalcVeeNodes() {
        boolean[][] edges = new boolean[11][];
        boolean[][] arrows = new boolean[11][];
        SeparationSet[][] sepsets = new SeparationSet[11][];

        icssa.calcVeeNodes(edges, arrows, sepsets);

        // Check every node has at least one incoming arrow
        for (int i = 0; i < 11; ++i) {
            int incoming = 0;
            for (int j = 0; j < 11; ++j) {
                if (arrows[i][j]) {
                    ++incoming;
                }
            }
            assertTrue(incoming > 0);
        }

        // Check every node has at least one outgoing arrow
        for (int i = 0; i < 11; ++i) {
            int outgoing = 0;
            for (int j = 0; j < 11; ++j) {
                if (arrows[j][i]) {
                    ++outgoing;
                }
            }
            assertTrue(outgoing > 0);
        }
    }

    @Test
    public void testCalcArcDirections() {
        boolean[][] edges = new boolean[11][];
        boolean[][] arrows = new boolean[11][];

        icssa.calcArcDirections(edges, arrows);

        // Check every node has at least one incoming arrow
        for (int i = 0; i < 11; ++i) {
            int incoming = 0;
            for (int j = 0; j < 11; ++j) {
                if (arrows[i][j]) {
                    ++incoming;
                }
            }
            assertTrue(incoming > 0);
        }

        // Check every node has at least one outgoing arrow
        for (int i = 0; i < 11; ++i) {
            int outgoing = 0;
            for (int j = 0; j < 11; ++j) {
                if (arrows[j][i]) {
                    ++outgoing;
                }
            }
            assertTrue(outgoing > 0);
        }

        // Check no node has a self-loop
        for (int i = 0; i < 11; ++i) {
            for (int j = 0; j < 11; ++j) {
                if (i == j) {
                    assertFalse(arrows[i][j]);
                }
            }
        }
    }

    @Test
    public void testListOptions() {
        assertArrayEquals(new String[]{"-cardinality <num>", "-U", "--", "-help"}, icssa.listOptions().toArray(new String[0]));
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality 3"});
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        assertArrayEquals(new String[]{"-cardinality", "3"}, icssa.getOptions());
    }

    @Test
    public void testMaxCardinalityTipText() {
        assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
    }

    @Test
    public void testGlobalInfo() {
        assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
    }

    @Test
    public void testGetRevision() {
        assertEquals("$Revision: 8034 $", icssa.getRevision());
    }

    @Test
    public void testMain() {
        try {
            icssa.main(new String[0]);
            fail();
        } catch (Exception e) {
            // Expected
        }
    }
}