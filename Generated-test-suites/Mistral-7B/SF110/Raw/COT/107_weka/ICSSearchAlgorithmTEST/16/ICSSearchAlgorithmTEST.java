package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import static weka.classifiers.functions.Util.generateRandomInstances;
import java.util.*;
import org.junit.*;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;
    private BayesNet bayesNet;

    @Before
    public void setUp() throws Exception {
        icssa = new ICSSearchAlgorithm();
        instances = generateRandomInstances(10, 4);
        bayesNet = new BayesNet();
        icssa.search(bayesNet, instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); ++i) {
            assertEquals(instances.attribute(i).name(), icssa.name(i));
        }
    }

    @Test
    public void testMaxn() {
        assertEquals(instances.numAttributes(), icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxCardinality() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        assertNotNull(icssa.m_BayesNet);
        assertNotNull(icssa.m_instances);
    }

    @Test
    public void testCalcDependencyGraph() {
        // Check edges[iNode1][iNode2] = true for all iNode1 and iNode2
        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
                assertTrue(icssa.edges[iNode1][iNode2]);
                assertTrue(icssa.edges[iNode2][iNode1]);
            }
        }

        // Check edges[iNode1][iNode1] = false for all iNode1
        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            assertFalse(icssa.edges[iNode1][iNode1]);
        }

        // Check for correct SeparationSet instances
        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
                if (icssa.edges[iNode1][iNode2]) {
                    SeparationSet sepSet = icssa.sepsets[iNode1][iNode2];
                    for (int i = 0; i < icssa.getMaxCardinality(); ++i) {
                        assertEquals(icssa.name(sepSet.m_set[i]), instances.attribute(sepSet.m_set[i]).name());
                    }
                    assertEquals(icssa.name(iNode1), instances.attribute(iNode1).name());
                    assertEquals(icssa.name(iNode2), instances.attribute(iNode2).name());
                }
            }
        }
    }

    @Test
    public void testExistsSepSet() {
        for (int iNode1 = 0; iNode1 < icssa.maxn() - 1; ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
                int nCardinality = icssa.getMaxCardinality();
                SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, icssa.edges);
                if (sepSet != null) {
                    for (int i = 0; i < nCardinality; ++i) {
                        assertEquals(icssa.name(sepSet.m_set[i]), instances.attribute(sepSet.m_set[i]).name());
                    }
                    assertEquals(icssa.name(iNode1), instances.attribute(iNode1).name());
                    assertEquals(icssa.name(iNode2), instances.attribute(iNode2).name());
                }
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        ICSSearchAlgorithm icssaNull = new ICSSearchAlgorithm() {
            @Override
            Instances attribute(int i) {
                return null;
            }
        };
        icssaNull.search(bayesNet, instances);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        ICSSearchAlgorithm icssaNegative = new ICSSearchAlgorithm();
        icssaNegative.search(bayesNet, instances);
        icssaNegative.name(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMaxCardinalityOutOfBounds() {
        ICSSearchAlgorithm icssaMaxCardinality = new ICSSearchAlgorithm();
        icssaMaxCardinality.search(bayesNet, instances);
        icssaMaxCardinality.existsSepSet(0, 1, icssaMaxCardinality.getMaxCardinality() + 1, icssa.edges);
    }

    @After
    public void tearDown() {
        icssa = null;
        instances = null;
        bayesNet = null;
    }
}