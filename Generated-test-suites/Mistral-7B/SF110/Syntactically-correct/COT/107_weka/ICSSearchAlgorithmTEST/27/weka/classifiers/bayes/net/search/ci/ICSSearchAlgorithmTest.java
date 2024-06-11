package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;

public class ICSSearchAlgorithmTest {

    @Test
    public void testICSSearchAlgorithm() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        assertEquals(2, algorithm.getMaxCardinality());
        algorithm.setMaxCardinality(3);
        assertEquals(3, algorithm.getMaxCardinality());

        Instances instances = new Instances(new FileReader("path/to/instances.arff"));
        algorithm.search(new BayesNet(), instances);
        assertNotNull(algorithm.m_BayesNet);
        assertNotNull(algorithm.m_instances);
    }

    @Test
    public void testName() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        String name = algorithm.name(0);
        assertEquals("attribute0", name);
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        int maxn = algorithm.maxn();
        int numAttributes = 5;
        assertEquals(numAttributes, maxn);
    }

    @Test
    public void testSetMaxCardinality() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(3);
        assertEquals(3, algorithm.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegativeValue() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(-1);
    }

    @Test
    public void testExistsSepSetNull() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        SeparationSet sepSet = algorithm.existsSepSet(0, 1, 0, edges);
        assertNull(sepSet);
    }

    @Test
    public void testExistsSepSetEmptySet() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
        }

        int iNode1 = 0;
        int iNode2 = 1;
        int nCardinality = 0;
        SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
        assertNull(sepSet);
    }

    @Test
    public void testExistsSepSetWithParents() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[1][i] = true;
        }

        int iNode1 = 0;
        int iNode2 = 1;
        int nCardinality = 2;
        SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
        assertNull(sepSet);
    }

    @Test
    public void testExistsSepSetWithChildren() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[i][3] = true;
            edges[i][4] = true;
        }

        int iNode1 = 3;
        int iNode2 = 4;
        int nCardinality = 2;
        SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
        assertNull(sepSet);
    }

    @Test
    public void testExistsSepSetWithParentAndChildren() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[1][i] = true;
            edges[i][3] = true;
            edges[i][4] = true;
        }

        int iNode1 = 0;
        int iNode2 = 4;
        int nCardinality = 2;
        SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
        assertNull(sepSet);
    }

    @Test
    public void testExistsSepSetPositiveResult() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[1][i] = true;
            edges[1][2] = true;
            edges[2][3] = true;
            edges[3][4] = true;
        }

        int iNode1 = 0;
        int iNode2 = 4;
        int nCardinality = 2;
        SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
        assertNotNull(sepSet);
        assertTrue(Arrays.contains(sepSet.m_set, 1));
        assertTrue(Arrays.contains(sepSet.m_set, 2));
        assertTrue(Arrays.contains(sepSet.m_set, 3));
    }

    @Test
    public void testNextPositiveResult() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        int x = algorithm.next(-1, 0, 1, new boolean[5][]);
        assertEquals(1, x);

        x = algorithm.next(1, 0, 1, new boolean[5][]);
        assertEquals(2, x);

        x = algorithm.next(2, 0, 1, new boolean[5][]);
        assertEquals(3, x);

        x = algorithm.next(3, 0, 1, new boolean[5][]);
        assertEquals(4, x);

        x = algorithm.next(4, 0, 1, new boolean[5][]);
        assertEquals(5, x);

        x = algorithm.next(5, 0, 1, new boolean[5][]);
        assertEquals(-1, x);
    }

    @Test
    public void testNextNegativeCycle() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[i][(i + 1) % 5] = true;
        }

        int x = algorithm.next(-1, 0, 1, edges);
        assertEquals(-1, x);

        x = algorithm.next(1, 0, 1, edges);
        assertEquals(2, x);

        x = algorithm.next(2, 0, 1, edges);
        assertEquals(3, x);

        x = algorithm.next(3, 0, 1, edges);
        assertEquals(4, x);

        x = algorithm.next(4, 0, 1, edges);
        assertEquals(0, x);

        x = algorithm.next(0, 0, 1, edges);
        assertEquals(1, x);
    }

    @Test
    public void testIsConditionalIndependentFalse() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[1][i] = true;
            edges[2][i] = true;
        }

        int iNode1 = 0;
        int iNode2 = 1;
        SeparationSet[][] sepsets = new SeparationSet[5][];
        for (int i = 0; i < 5; ++i) {
            sepsets[i] = new SeparationSet[5];
            Arrays.fill(sepsets[i], null);
        }

        boolean result = algorithm.isConditionalIndependent(iNode1, iNode2, sepsets[iNode1], 0);
        assertFalse(result);
    }

    @Test
    public void testIsConditionalIndependentTrue() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[5][];
        for (int i = 0; i < 5; ++i) {
            Arrays.fill(edges[i], false);
            edges[0][i] = true;
            edges[1][i] = true;
            edges[2][i] = true;
            edges[3][i] = true;
            edges[4][i] = true;
        }

        int iNode1 = 0;
        int iNode2 = 2;
        SeparationSet[][] sepsets = new SeparationSet[5][];
        for (int i = 0; i < 5; ++i) {
            sepsets[i] = new SeparationSet[5];
            Arrays.fill(sepsets[i], null);
        }

        boolean result = algorithm.isConditionalIndependent(iNode1, iNode2, sepsets[iNode1], 0);
        assertTrue(result);
    }

}