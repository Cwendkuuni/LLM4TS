import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private BayesNet bayesNet;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test
 public void testSearchWithDefaultValues() throws Exception {
 assertEquals(2, icssa.getMaxCardinality());
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test
 public void testSearchWithMaxCardinalitySet() throws Exception {
 icssa.setMaxCardinality(3);
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test(expected = Exception.class)
 public void testSearchWithNegativeMaxCardinality() throws Exception {
 icssa.setMaxCardinality(-1);
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNullInstances() throws Exception {
 icssa.setMaxCardinality(3);
 bayesNet = new BayesNet();
 instances = null;
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test(expected = IOException.class)
 public void testSearchWithInvalidDataFile() throws Exception {
 icssa.setMaxCardinality(3);
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("nonexistentfile.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test
 public void testListOptions() {
 final String expected = "Options:\n\n" +
         "\tWhen determining whether an edge exists a search is performed\n" +
         "\tfor a set Z that separates the nodes. MaxCardinality determines\n" +
         "\tthe maximum size of the set Z. This greatly influences the\n" +
         "\tlength of the search. (default 2)\n" +
         "\t[cardinality <num>]";
 assertEquals(expected, icssa.listOptions().toString());
 }

 @Test
 public void testSetOptionsWithValidValues() throws Exception {
 icssa.setOptions(new String[]{"-cardinality 3"});
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOptionsWithInvalidValues() throws Exception {
 icssa.setOptions(new String[]{"-cardinality -1"});
 }

 @Test
 public void testGetOptions() {
 final String expected = "-cardinality 3";
 assertEquals(expected, icssa.getOptions()[icssa.getOptions().length - 1]);
 }

 @Test
 public void testMaxCardinalityTipText() {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
 }
}