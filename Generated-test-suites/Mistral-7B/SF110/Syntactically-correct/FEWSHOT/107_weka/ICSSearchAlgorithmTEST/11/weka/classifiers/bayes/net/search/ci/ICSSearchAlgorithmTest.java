package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import org.junit.Test;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testMaxCardinality() {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        assertEquals(2, icse.getMaxCardinality());
    }

    @Test
    public void testSetMaxCardinality() {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        icse.setMaxCardinality(5);
        assertEquals(5, icse.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegativeValue() {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        icse.setMaxCardinality(-1);
    }

    @Test
    public void testName() {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        Instances instances = new Instances(new FileReader("src/test/resources/testData.arff"));
        String result = icse.name(2);
        assertEquals("attribute2", result);
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        Instances instances = new Instances(new FileReader("src/test/resources/testData.arff"));
        assertEquals(3, icse.maxn(instances));
    }

    @Test
    public void testSearch() throws Exception {
        ICSSearchAlgorithm icse = new ICSSearchAlgorithm();
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/testData.arff"));
        icse.search(bayesNet, instances);
        // Add asserts here to check the state of bayesNet and instances after search method call.
    }

    // Add more test methods here as needed for other methods in the ICSSearchAlgorithm class.

}