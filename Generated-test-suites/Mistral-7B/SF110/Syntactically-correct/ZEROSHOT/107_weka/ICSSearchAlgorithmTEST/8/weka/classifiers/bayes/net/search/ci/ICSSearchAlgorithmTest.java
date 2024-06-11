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

    @Before
    public void setup() throws Exception {
        instances = new Instances(new FileReader("path/to/your/instances.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa = new ICSSearchAlgorithm();
        icssa.setInstances(instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); ++i) {
            assertEquals("Attribute name expected", icssa.name(i), instances.attribute(i).name());
        }
    }

    @Test
    public void testMaxn() {
        assertEquals("Number of attributes expected", icssa.maxn(), instances.numAttributes());
    }

    @Test
    public void testSetAndGetMaxCardinality() {
        int maxCardinality = 3;
        icssa.setMaxCardinality(maxCardinality);
        assertEquals("Maximum cardinality not set correctly", maxCardinality, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxCardinality() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        BayesNet bayesNet = new BayesNet();
        icssa.search(bayesNet, instances);
        // Additional assertions can be added here to test the state of the BayesNet object.
    }

    @Test
    public void testCalcDependencyGraphPrinting() throws Exception {
        boolean[][] edges = new boolean[icssa.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        // Additional assertions can be added here to test the edges and sepsets arrays.
    }

    // Add more test methods for other methods in the ICSSearchAlgorithm class.
    
}