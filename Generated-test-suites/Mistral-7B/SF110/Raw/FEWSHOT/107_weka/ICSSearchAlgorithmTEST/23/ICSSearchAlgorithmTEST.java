package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.net.search.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {
    private ICSSearchAlgorithm algorithm;
    private Instances instances;
    private BayesNet bayesNet;

    @Before
    public void setUp() throws Exception {
        algorithm = new ICSSearchAlgorithm();
        instances = mock(Instances.class);
        bayesNet = mock(BayesNet.class);
        when(instances.numAttributes()).thenReturn(3);
        algorithm.search(bayesNet, instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < algorithm.maxn(); ++i) {
            assertEquals("Attribute " + i, algorithm.name(i));
        }
    }

    @Test
    public void testSetMaxCardinality() {
        algorithm.setMaxCardinality(3);
        assertEquals(3, algorithm.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals(2, algorithm.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegative() {
        algorithm.setMaxCardinality(-1);
    }

    @Test
    public void testCalcDependencyGraph() {
        // Check initial state
        for (int i = 0; i < algorithm.maxn(); ++i) {
            for (int j = 0; j < algorithm.maxn(); ++j) {
                assertFalse(algorithm.edges[i][j]);
            }
        }

        // Add dependency
        algorithm.edges[1][2] = true;
        algorithm.calcDependencyGraph(algorithm.edges, algorithm.sepsets);

        // Check expected edges
        for (int i = 0; i < algorithm.maxn(); ++i) {
            for (int j = 0; j < algorithm.maxn(); ++j) {
                if (i != j && algorithm.edges[i][j]) {
                    System.out.println("Error: unexpected edge from " + i + " to " + j);
                    fail();
                }
            }
            if (i > 0) {
                assertTrue(algorithm.edges[i - 1][i]);
            }
        }

        // Check expected separation sets
        assertEquals(1, algorithm.sepsets[1].length);
        assertEquals(0, algorithm.sepsets[1][0]);
        assertEquals(2, algorithm.sepsets[2].length);
        assertEquals(1, algorithm.sepsets[2][0]);
        assertEquals(1, algorithm.sepsets[2][1]);
    }

    @Test
    public void testExistsSepSet() {
        // Test empty case
        assertNull(algorithm.existsSepSet(0, 1, 0, algorithm.edges));

        // Test case with one element
        int[] set = new int[1];
        set[0] = 2;
        SeparationSet sepSet = new SeparationSet();
        sepSet.m_set[0] = 2;
        when(algorithm.next(0, 1, 2, algorithm.edges)).thenReturn(3);
        when(algorithm.isConditionalIndependent(1, 0, sepSet.m_set, 1)).thenReturn(true);
        SeparationSet result = algorithm.existsSepSet(0, 1, 1, algorithm.edges);
        assertEquals(sepSet, result);

        // Test case with multiple elements
        set = new int[2];
        set[0] = 2;
        set[1] = 3;
        sepSet = new SeparationSet();
        sepSet.m_set[0] = 2;
        sepSet.m_set[1] = 3;
        when(algorithm.next(0, 1, 2, algorithm.edges)).thenReturn(4);
        when(algorithm.next(1, 1, 2, algorithm.edges)).thenReturn(5);
        when(algorithm.next(1, 0, 2, algorithm.edges)).thenReturn(6);
        when(algorithm.isConditionalIndependent(1, 0, sepSet.m_set, 2)).thenReturn(true);
        SeparationSet result2 = algorithm.existsSepSet(0, 1, 2, algorithm.edges);
        assertEquals(sepSet, result2);
    }

    @Test
    public void testListOptions() {
        assertEquals(2, algorithm.listOptions().size());
    }

    @Test
    public void testSetOptions() throws Exception {
        algorithm.setOptions(new String[]{"-cardinality 3"});
        assertEquals(3, algorithm.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        String[] options = algorithm.getOptions();
        assertEquals(3, options.length);
        assertEquals("-cardinality", options[2]);
        assertEquals("3", options[3]);
    }
}