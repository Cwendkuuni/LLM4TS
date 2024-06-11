package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.core.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;
    private BayesNet bayesNet;

    @Before
    public void setUp() throws Exception {
        instances = new Instances(new FileReader("path/to/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        bayesNet = new BayesNet();
        icssa = new ICSSearchAlgorithm();
        icssa.setInstances(instances);
        icssa.setMaxCardinality(2);
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); i++) {
            String name = icssa.name(i);
            assertEquals("Expected name of attribute " + i, instances.attribute(i).name(), name);
        }
    }

    @Test
    public void testMaxn() {
        int maxn = icssa.maxn();
        assertEquals("Expected number of attributes in Instances", instances.numAttributes(), maxn);
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(3);
        assertEquals("Expected max cardinality to be set to 3", 3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals("Expected max cardinality to be 2 by default", 2, icssa.getMaxCardinality());
    }

    @Test
    public void testSearch() throws Exception {
        icssa.search(bayesNet, instances);
        assertNotNull("Expected BayesNet to not be null", bayesNet);
    }

    @Test(expected = Exception.class)
    public void testSearchThrowsException() throws Exception {
        ICSSearchAlgorithm icssa2 = new ICSSearchAlgorithm();
        icssa2.search(bayesNet, null);
    }

    @Test
    public void testCalcDependencyGraph() {
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        for (int i = 0; i < icssa.maxn(); i++) {
            for (int j = 0; j < icssa.maxn(); j++) {
                assertFalse("Expected no edges between " + icssa.name(i) + " and " + icssa.name(j) + " by default", edges[i][j]);
                assertFalse("Expected no edges between " + icssa.name(j) + " and " + icssa.name(i) + " by default", edges[j][i]);
            }
        }
    }

    @Test
    public void testExistsSepSet() {
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
            for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
                edges[iNode1][iNode2] = true;
                SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, 2, edges);
                assertNull("Expected null if no separation set is found", sepSet);
                edges[iNode1][iNode2] = false;
            }
        }

        int[] expectedSepSet = {1, 3};
        for (int iNode1 = 0; iNode1 < icssa.maxn() - 1; ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
                if (!edges[iNode1][iNode2]) {
                    for (int iNode3 = 0; iNode3 < icssa.maxn(); ++iNode3) {
                        if ((iNode3 != iNode1 && iNode3 != iNode2) && edges[iNode1][iNode3] && edges[iNode2][iNode3]) {
                            edges[iNode1][iNode3] = false;
                            edges[iNode2][iNode3] = false;
                            SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, 2, edges);
                            assertEquals("Expected a separation set with 2 elements", Arrays.toString(expectedSepSet), Arrays.toString(sepSet.m_set));
                            edges[iNode1][iNode3] = true;
                            edges[iNode2][iNode3] = true;
                        }
                    }
                }
            }
        }
    }
}

class SeparationSet {
    public int[] m_set;

    public SeparationSet() {
        this.m_set = new int[ICSSearchAlgorithmTest.icssa.getMaxCardinality() + 1];
    }

    public boolean contains(final int nItem) {
        for (int iItem = 0; iItem < ICSSearchAlgorithmTest.icssa.getMaxCardinality() && this.m_set[iItem] != -1; ++iItem) {
            if (this.m_set[iItem] == nItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRevision() {
        return "Empty revision";
    }
}