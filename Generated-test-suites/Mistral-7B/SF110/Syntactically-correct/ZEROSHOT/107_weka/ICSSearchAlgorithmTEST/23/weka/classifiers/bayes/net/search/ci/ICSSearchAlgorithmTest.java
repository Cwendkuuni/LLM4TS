package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;

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
    public void setUp() throws Exception {
        instances = new Instances(new FileReader("path/to/your/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        bayesNet = new BayesNet();
        icssa = new ICSSearchAlgorithm();
        icssa.setInstances(instances);
        icssa.setMaxCardinality(2);
        bayesNet.setSearchAlgorithm(icssa);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, icssa.getMaxCardinality());
    }

    @Test
    public void testName() throws Exception {
        final int attributeIndex = 0;
        assertEquals("name of attribute at index " + attributeIndex, icssa.name(attributeIndex));
    }

    @Test
    public void testMaxn() throws Exception {
        assertEquals(instances.numAttributes(), icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() throws Exception {
        icssa.setMaxCardinality(5);
        assertEquals(5, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegativeValue() throws Exception {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        icssa.search(bayesNet, instances);
        // Add asserts here to test the state of the BayesNet and other variables after the search method call.
    }

    @Test
    public void testCalcDependencyGraph() throws Exception {
        // Add asserts here to test the edges array and sepsets array after the calcDependencyGraph method call.
    }

    @Test
    public void testExistsSepSet() throws Exception {
        // Add asserts here to test the SeparationSet object returned by existsSepSet method.
    }

    @Test
    public void testNext() throws Exception {
        // Add asserts here to test the next index returned by the next method.
    }

    @Test
    public void testCalcVeeNodes() throws Exception {
        // Add asserts here to test the arrows array after the calcVeeNodes method call.
    }

    @Test
    public void testCalcArcDirections() throws Exception {
        // Add asserts here to test the arrows array after the calcArcDirections method call.
    }

    @Test
    public void testListOptions() throws Exception {
        final Enumeration<Object> options = icssa.listOptions();
        assertTrue(options.hasMoreElements());
        assertEquals("cardinality <num>", options.nextElement().toString());
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality 5"});
        assertEquals(5, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() throws Exception {
        final String[] options = icssa.getOptions();
        assertEquals(2, options.length);
        assertEquals("-cardinality", options[0]);
        assertEquals("5", options[1]);
    }

    @Test
    public void testMaxCardinalityTipText() throws Exception {
        assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
    }

    @Test
    public void testGlobalInfo() throws Exception {
        assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
    }

    @Test
    public void testGetRevision() throws Exception {
        assertEquals("$Revision: 8034 $", icssa.getRevision());
    }
}