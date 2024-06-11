package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;
import static weka.core.Utils.getOption;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private BayesNet bayesNet;
    private Instances instances;

    @Before
    public void setup() throws Exception {
        icssa = new ICSSearchAlgorithm();
        bayesNet = new BayesNet();
        instances = new Instances(new FileReader("data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        bayesNet.setSearchAlgorithm(icssa);
        icssa.search(bayesNet, instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); ++i) {
            assertEquals(instances.attribute(i).name(), icssa.name(i));
        }
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals(2, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegative() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() {
        assertNotNull(bayesNet.structure());
    }

    @Test
    public void testCalcDependencyGraph() {
        final boolean[][] edges = icssa.getEdges();
        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
                assertTrue(edges[iNode1][iNode2] || edges[iNode2][iNode1]);
            }
        }
    }

    @Test
    public void testExistsSepSet() {
        final boolean[][] edges = icssa.getEdges();
        final SeparationSet sepSet = icssa.existsSepSet(0, 1, 1, edges);
        assertNull(sepSet);

        sepSet = icssa.existsSepSet(0, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssa.existsSepSet(1, 2, 1, edges);
        assertNull(sepSet);

        sepSet = icssa.existsSepSet(1, 0, 2, edges);
        assertArrayEquals(new int[]{2}, sepSet.m_set);

        sepSet = icssa.existsSepSet(1, 0, 3, edges);
        assertArrayEquals(new int[]{1, 2}, sepSet.m_set);

        sepSet = icssa.existsSepSet(2, 1, 2, edges);
        assertArrayEquals(new int[]{0}, sepSet.m_set);

        sepSet = icssa.existsSepSet(2, 1, 3, edges);
        assertArrayEquals(new int[]{1}, sepSet.m_set);

        sepSet = icssa.existsSepSet(1, 2, 3, edges);
        assertNull(sepSet);
    }

    @Test
    public void testNext() {
        final int maxn = icssa.maxn();
        for (int x = 0; x < maxn; ++x) {
            assertEquals(x + 1, icssa.next(x, 0, 1, icssa.getEdges()));
        }

        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
                if (icssa.getEdges()[iNode1][iNode2]) {
                    for (int j = 0; j < icssa.maxn(); ++j) {
                        if (!icssa.getEdges()[iNode1][j] || j == iNode2) {
                            assertEquals(j + 1, icssa.next(x, iNode1, iNode2, icssa.getEdges()));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testCalcVeeNodes() {
        final boolean[][] edges = icssa.getEdges();
        final boolean[][] arrows = icssa.getArrows();

        for (int iNode1 = 0; iNode1 < icssa.maxn() - 1; ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
                if (!edges[iNode1][iNode2]) {
                    for (int iNode3 = 0; iNode3 < icssa.maxn(); ++iNode3) {
                        if ((iNode3 != iNode1 && iNode3 != iNode2 && edges[iNode1][iNode3] && edges[iNode2][iNode3])) {
                            assertTrue(arrows[iNode1][iNode3] && arrows[iNode2][iNode3]);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testCalcArcDirections() {
        final boolean[][] edges = icssa.getEdges();
        final boolean[][] arrows = icssa.getArrows();

        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
                if (iNode1 != iNode2 && arrows[iNode1][iNode2]) {
                    for (int iNode3 = 0; iNode3 < icssa.maxn(); ++iNode3) {
                        if (iNode3 != iNode1 && iNode3 != iNode2 && edges[iNode3][iNode1] && arrows[iNode2][iNode3] && !arrows[iNode1][iNode3] && !arrows[iNode3][iNode1]) {
                            assertTrue(arrows[iNode3][iNode1]);
                            assertTrue(arrows[iNode3][iNode2]);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testListOptions() {
        String[] options = icssa.listOptions();
        List<String> optionList = Arrays.asList(options);
        assertTrue(optionList.contains("-cardinality"));
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality", "3"});
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOptionsInvalidOption() throws Exception {
        icssa.setOptions(new String[]{"-invalidOption", "3"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOptionsInvalidValue() throws Exception {
        icssa.setOptions(new String[]{"-cardinality", "abc"});
    }

    @Test
    public void testGetOptions() {
        String[] options = icssa.getOptions();
        assertEquals(2, options.length);
        assertEquals("-cardinality", options[1]);
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
}