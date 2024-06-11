package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testICSSearchAlgorithm() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        icssa.setMaxCardinality(3);
        Instances instances = new Instances(new FileReader("path/to/your/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        BayesNet bayesNet = new BayesNet();
        bayesNet.setSearchAlgorithm(icssa);
        try {
            bayesNet.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception raised during BayesNet construction");
        }
        // Add more test cases here
    }

    @Test
    public void testSetGetMaxCardinality() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        icssa.setMaxCardinality(5);
        int maxCardinality = icssa.getMaxCardinality();
        assertEquals(5, maxCardinality);
    }

    @Test
    public void testName() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Instances instances = new Instances(new FileReader("path/to/your/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa.m_instances = instances;
        String name = icssa.name(0);
        assertEquals("attribute name 0", name);
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Instances instances = new Instances(new FileReader("path/to/your/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa.m_instances = instances;
        int maxn = icssa.maxn();
        assertEquals(instances.numAttributes(), maxn);
    }

    @Test
    public void testExistsSepSetNull() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        SeparationSet sepset = icssa.existsSepSet(-1, 0, 0, edges);
        assertNull(sepset);
    }

    @Test
    public void testExistsSepSetEmpty() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        SeparationSet sepset = icssa.existsSepSet(0, 0, 0, edges);
        assertNull(sepset);
    }

    @Test
    public void testExistsSepSetSingle() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        edges[1][0] = true;
        edges[1][1] = true;
        SeparationSet sepset = icssa.existsSepSet(0, 1, 1, edges);
        int[] expectedSet = {1};
        assertArrayEquals(expectedSet, sepset.m_set);
    }

    @Test
    public void testExistsSepSetMultiple() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        edges[1][0] = true;
        edges[2][0] = true;
        edges[1][2] = true;
        SeparationSet sepset = icssa.existsSepSet(0, 1, 2, edges);
        int[] expectedSet = {1, 2};
        assertArrayEquals(expectedSet, sepset.m_set);
    }

    @Test
    public void testNext() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        int x = icssa.next(-1, 0, 0, new boolean[10][]);
        assertEquals(0, x);

        int maxn = 5;
        boolean[][] edges = new boolean[maxn][];
        for (int i = 0; i < maxn; ++i) {
            edges[i] = new boolean[maxn];
        }
        x = icssa.next(0, 0, 0, edges);
        assertEquals(1, x);

        x = icssa.next(1, 0, 0, edges);
        assertEquals(2, x);

        x = icssa.next(2, 0, 0, edges);
        assertEquals(3, x);

        x = icssa.next(3, 0, 0, edges);
        assertEquals(4, x);

        x = icssa.next(4, 0, 0, edges);
        assertEquals(5, x);

        x = icssa.next(5, 0, 0, edges);
        assertEquals(-1, x);

        x = icssa.next(0, 0, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(0, 1, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(1, 1, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(1, 2, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(2, 2, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(2, 3, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(3, 3, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(4, 3, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(4, 4, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(5, 4, 1, edges);
        assertEquals(-1, x);

        x = icssa.next(5, 5, 1, edges);
        assertEquals(-1, x);
    }

    @Test
    public void testCalcDependencyGraph() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        SeparationSet[][] sepsets = new SeparationSet[10][];

        for (int iNode1 = 0; iNode1 < 10; ++iNode1) {
            for (int iNode2 = 0; iNode2 < 10; ++iNode2) {
                edges[iNode1][iNode2] = true;
            }
        }
        for (int iNode1 = 0; iNode1 < 10; ++iNode1) {
            edges[iNode1][iNode1] = false;
        }
        icssa.calcDependencyGraph(edges, sepsets);
        // Add more test cases here
    }

    @Test
    public void testIsConditionalIndependent() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        int iNode1 = 1, iNode2 = 2, iNode3 = 3;
        SeparationSet[][] sepsets = new SeparationSet[10][];

        // Test when X, Y, and Z are independent
        sepsets[iNode1] = new SeparationSet();
        sepsets[iNode2] = new SeparationSet();
        assertFalse(icssa.isConditionalIndependent(iNode2, iNode1, sepsets[iNode1].m_set, 0));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 0));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode1].m_set, 1));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 1));

        // Test when X and Y are dependent
        sepsets[iNode1].m_set[0] = iNode3;
        sepsets[iNode2].m_set[0] = iNode3;
        assertTrue(icssa.isConditionalIndependent(iNode2, iNode1, sepsets[iNode1].m_set, 0));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 0));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode1].m_set, 1));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 1));

        // Test when X and Z are independent given Y
        sepsets[iNode1].m_set[0] = iNode3;
        sepsets[iNode2].m_set[0] = iNode3;
        sepsets[iNode3] = new SeparationSet();
        assertTrue(icssa.isConditionalIndependent(iNode2, iNode1, sepsets[iNode1].m_set, 0));
        assertTrue(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 0));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode1].m_set, 1));
        assertFalse(icssa.isConditionalIndependent(iNode1, iNode2, sepsets[iNode2].m_set, 1));
        // Add more test cases here
    }
}