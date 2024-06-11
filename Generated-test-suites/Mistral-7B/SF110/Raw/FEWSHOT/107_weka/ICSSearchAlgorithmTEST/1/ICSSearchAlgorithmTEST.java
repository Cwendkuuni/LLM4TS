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
    public void testICSSearchAlgorithm() throws Exception {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();

        // Test initial settings
        assertEquals(2, icssa.getMaxCardinality());

        // Test setting maxCardinality
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());

        // Test name method
        Instances instances = new Instances(new FileReader("data/testInstances.arff"));
        ICSSearchAlgorithm icssaInstance = new ICSSearchAlgorithm();
        icssaInstance.setMaxCardinality(3);
        icssaInstance.setInstances(instances);
        String name = icssaInstance.name(0);
        assertEquals("attribute 0", name);

        // Test maxn method
        assertEquals(instances.numAttributes(), icssaInstance.maxn());

        // Test search method
        BayesNet bayesNet = new BayesNet();
        bayesNet.setSearchAlgorithm(icssaInstance);
        Instances testInstances = new Instances(new FileReader("data/testInstances.arff"));
        icssaInstance.search(bayesNet, testInstances);

        // Test calcDependencyGraph method
        boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[testInstances.numAttributes() + 1][];
        ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
        icssaTest.setMaxCardinality(3);
        icssaTest.setInstances(testInstances);
        icssaTest.calcDependencyGraph(edges, sepsets);
        // Add assertions for the edges and sepsets arrays here

        // Test existsSepSet method
        SeparationSet sepSet = icssaTest.existsSepSet(0, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 0, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 0, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 0, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 2, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 2, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 0, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(0, 2, 1, edges);
        assertNotNull(sepSet);
        int[] set = sepSet.m_set;
        assertArrayEquals(new int[]{1, 2}, set);

        sepSet = icssaTest.existsSepSet(1, 2, 0, edges);
        assertNotNull(sepSet);
        set = sepSet.m_set;
        assertArrayEquals(new int[]{0, 2}, set);

        sepSet = icssaTest.existsSepSet(2, 0, 1, edges);
        assertNotNull(sepSet);
        set = sepSet.m_set;
        assertArrayEquals(new int[]{1, 0}, set);

        sepSet = icssaTest.existsSepSet(0, 2, 2, edges);
        assertNotNull(sepSet);
        set = sepSet.m_set;
        assertArrayEquals(new int[]{1, 2}, set);

        sepSet = icssaTest.existsSepSet(1, 2, 0, edges);
        assertNotNull(sepSet);
        set = sepSet.m_set;
        assertArrayEquals(new int[]{0, 2}, set);

        sepSet = icssaTest.existsSepSet(0, 1, 2, edges);
        assertNotNull(sepSet);
        set = sepSet.m_set;
        assertArrayEquals(new int[]{1, 2}, set);

        sepSet = icssaTest.existsSepSet(0, 1, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 0, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 0, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 1, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 1, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(1, 2, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 0, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 0, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 0, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 1, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 2, 0, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 2, 1, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 2, 2, edges);
        assertNull(sepSet);

        sepSet = icssaTest.existsSepSet(2, 2, 3, edges);
        assertNull(sepSet);

        // Test next method
        int x = icssaTest.next(-1, 0, 1, edges);
        assertEquals(1, x);

        x = icssaTest.next(0, 0, 1, edges);
        assertEquals(2, x);

        x = icssaTest.next(1, 0, 1, edges);
        assertEquals(3, x);

        x = icssaTest.next(2, 0, 1, edges);
        assertEquals(4, x);

        x = icssaTest.next(3, 0, 1, edges);
        assertEquals(5, x);

        x = icssaTest.next(4, 0, 1, edges);
        assertEquals(-1, x);

        x = icssaTest.next(1, 0, 1, edges);
        assertEquals(2, x);

        x = icssaTest.next(2, 0, 1, edges);
        assertEquals(3, x);

        x = icssaTest.next(3, 0, 1, edges);
        assertEquals(4, x);

        x = icssaTest.next(4, 0, 1, edges);
        assertEquals(5, x);

        x = icssaTest.next(5, 0, 1, edges);
        assertEquals(-1, x);

        // Test calcVeeNodes method
        boolean[][] arrows = new boolean[testInstances.numAttributes() + 1][];
        icssaTest.calcVeeNodes(edges, arrows, sepsets);
        // Add assertions for the arrows array here

        // Test calcArcDirections method
        boolean[][] edges2 = new boolean[testInstances.numAttributes() + 1][];
        boolean[][] arrows2 = new boolean[testInstances.numAttributes() + 1][];
        ICSSearchAlgorithm icssaTest2 = new ICSSearchAlgorithm();
        icssaTest2.setMaxCardinality(3);
        icssaTest2.setInstances(testInstances);
        icssaTest2.calcDependencyGraph(edges2, sepsets);
        icssaTest2.calcVeeNodes(edges2, arrows2, sepsets);
        icssaTest2.calcArcDirections(edges2, arrows2);
        // Add assertions for the edges, arrows, and arrows2 arrays here

        // Test listOptions method
        String[] options = icssaInstance.listOptions();
        assertTrue(options.length > 0);
        String option = options[0];
        assertTrue(option.startsWith("\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)"));

        // Test setOptions and getOptions methods
        String[] testOptions = {"-cardinality 4"};
        icssaInstance.setOptions(testOptions);
        assertArrayEquals(new String[]{"-cardinality", "4"}, icssaInstance.getOptions());

        // Test maxCardinalityTipText method
        assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssaInstance.maxCardinalityTipText());

        // Test globalInfo method
        assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssaInstance.globalInfo());

        // Test getRevision method
        assertEquals("$Revision: 8034 $", icssaInstance.getRevision());

        // Test main method
        ICSSearchAlgorithm.main(new String[0]);
    }
}