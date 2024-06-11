import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
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
 bayesNet.setSearchAlgorithm(icssa);
 }

 @Test
 public void testSearchWithDefaultCardinality() throws Exception {
 icssa.search(bayesNet, instances);
 // Add assertions to check the state of the BayesNet object after search
 }

 @Test
 public void testSearchWithSpecifiedCardinality() throws Exception {
 icssa.setMaxCardinality(3);
 icssa.search(bayesNet, instances);
 // Add assertions to check the state of the BayesNet object after search
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCardinality() {
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testListOptions() {
 Instances testInstances = new Instances(new FileReader("data/contact-lenses.arff"));
 BayesNet testBayesNet = new BayesNet();
 ICSSearchAlgorithm testICSSearchAlgorithm = new ICSSearchAlgorithm();
 Assert.assertEquals("Incorrect option list", icssa.listOptions().toString(), testICSSearchAlgorithm.listOptions().toString());
 }

 @Test
 public void testSetOptions() throws Exception {
 String[] options = {"-cardinality 3"};
 ICSSearchAlgorithm testICSSearchAlgorithm = new ICSSearchAlgorithm();
 Assert.assertArrayEquals("Incorrect default options", new String[]{"", "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", ""}, testICSSearchAlgorithm.getOptions());
 testICSSearchAlgorithm.setOptions(options);
 Assert.assertEquals("Incorrect options after setting", "3", testICSSearchAlgorithm.getOptions()[2]);
 }

 @Test
 public void testGetOptions() throws Exception {
 ICSSearchAlgorithm testICSSearchAlgorithm = new ICSSearchAlgorithm();
 Assert.assertArrayEquals("Incorrect options after getting", new String[]{"", "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", "-cardinality", "3"}, testICSSearchAlgorithm.getOptions());
 }
}