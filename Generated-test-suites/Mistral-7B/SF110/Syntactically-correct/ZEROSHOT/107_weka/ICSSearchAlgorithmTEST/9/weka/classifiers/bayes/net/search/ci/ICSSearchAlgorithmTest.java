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

/**
 * ICSSearchAlgorithmTest class file.
 *
 * @author YourName
 */
public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;

    @Before
    public void setUp() throws Exception {
        instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa = new ICSSearchAlgorithm();
        icssa.setInstances(instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); ++i) {
            assertEquals(icssa.name(i), instances.attribute(i).name());
        }
    }

    @Test
    public void testMaxn() {
        assertEquals(icssa.maxn(), instances.numAttributes());
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(3);
        assertEquals(icssa.getMaxCardinality(), 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegative() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        BayesNet bayesNet = new BayesNet();
        bayesNet.setSearchAlgorithm(icssa);
        icssa.search(bayesNet, instances);
        // Add assertions here to check the state of bayesNet after search method call
    }

    @Test(expected = NullPointerException.class)
    public void testSearchNullBayesNet() {
        icssa.search(null, instances);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchNullInstances() {
        icssa.search(new BayesNet(), null);
    }

    @Test
    public void testCalcDependencyGraph() {
        // Add assertions here to check the state of edges, sepsets after calcDependencyGraph method call
    }

    @Test
    public void testExistsSepSet() {
        // Add assertions here to check the return value and the state of sepsets after existsSepSet method call
    }

    @Test
    public void testNext() {
        // Add assertions here to check the return value after next method call
    }

    @Test
    public void testCalcVeeNodes() {
        // Add assertions here to check the state of arrows after calcVeeNodes method call
    }

    @Test
    public void testCalcArcDirections() {
        // Add assertions here to check the state of arrows after calcArcDirections method call
    }

    @Test
    public void testListOptions() {
        List<Option> options = new ArrayList<>();
        List<Option> icssaOptions = icssa.listOptions();
        options.addAll(icssaOptions);
        Option cardinalityOption = new Option("\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)", "cardinality", 1, "-cardinality <num>");
        options.add(cardinalityOption);
        List<Option> superOptions = new ArrayList<>();
        Option[] superOptionsArray = icssa.super.listOptions().toArray(new Option[icssa.super.listOptions().size()]);
        for (Option option : superOptionsArray) {
            superOptions.add(option);
        }
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.addAll(superOptions);
        expectedOptions.addAll(options);
        Collections.sort(expectedOptions);
        List<Option> actualOptions = new ArrayList<>();
        for (Option option : icssa.listOptions()) {
            actualOptions.add(option);
        }
        Collections.sort(actualOptions);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality", "3"});
        assertEquals(icssa.getMaxCardinality(), 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOptionsInvalidCardinality() {
        icssa.setOptions(new String[]{"-cardinality", "x"});
    }

    @Test
    public void testGetOptions() {
        String[] options = icssa.getOptions();
        List<String> expectedOptions = new ArrayList<>();
        expectedOptions.add("-W");
        expectedOptions.add("-h");
        expectedOptions.add("-P");
        expectedOptions.add("weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm");
        expectedOptions.add("-cardinality");
        expectedOptions.add("2");
        Arrays.sort(options);
        Collections.sort(expectedOptions);
        assertEquals(Arrays.toString(options), Arrays.toString(expectedOptions.toArray(new String[expectedOptions.size()])));
    }

    @Test
    public void testMaxCardinalityTipText() {
        assertEquals(icssa.maxCardinalityTipText(), "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.");
    }

    @Test
    public void testGlobalInfo() {
        assertEquals(icssa.globalInfo(), "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.");
    }

    @Test
    public void testGetRevision() {
        assertEquals(icssa.getRevision(), "Revision: 8034 $");
    }

    @Test
    public void testMain() throws Exception {
        // Add test code here to check the output of main method
    }

}