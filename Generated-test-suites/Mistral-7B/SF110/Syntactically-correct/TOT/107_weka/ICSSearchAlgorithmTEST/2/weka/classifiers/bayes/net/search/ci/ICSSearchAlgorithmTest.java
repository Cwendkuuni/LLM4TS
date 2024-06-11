package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private BayesNet bayesNet;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.search(bayesNet, instances);
 }

 @Test
 public void testSearchWithDefaultCardinality() {
 icssa = new ICSSearchAlgorithm();
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet);
 }

 @Test
 public void testSearchWithCustomCardinality() {
 icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(3);
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeCardinality() {
 icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(-1);
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 try {
 icssa.search(bayesNet, instances);
 } catch (Exception e) {
 fail("Exception should have been thrown");
 }
 }

 @Test
 public void testListOptions() {
 final String[] options = icssa.listOptions().toArray(new String[0]);
 assertTrue(Arrays.asList(options).contains("cardinality"));
 }

 @Test
 public void testSetOptionsWithValidCardinality() {
 icssa.setOptions(new String[]{"-cardinality 3"});
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOptionsWithInvalidCardinality() {
 icssa.setOptions(new String[]{"-cardinality -1"});
 fail("IllegalArgumentException should have been thrown");
 }

 @Test
 public void testGetOptions() {
 final String[] options = icssa.getOptions();
 assertTrue(Arrays.asList(options).contains("-cardinality"));
 }

 @Test
 public void testMaxCardinalityTipText() {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfo() {
 assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
 }
}