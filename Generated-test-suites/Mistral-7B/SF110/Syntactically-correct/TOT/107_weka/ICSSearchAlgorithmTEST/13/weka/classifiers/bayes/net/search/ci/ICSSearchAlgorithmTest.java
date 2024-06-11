import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances testData;

 @Before
 public void setup() throws Exception {
 icssa = new ICSSearchAlgorithm();
 testData = new Instances(new FileReader("data/testData.arff"));
 testData.setClassIndex(testData.numAttributes() - 1);
 }

 @Test
 public void testSearchWithDefaultCardinality() throws Exception {
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 Instances instances = new Instances(new FileReader("data/testData.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bn.buildClassifier(instances);
 assertTrue(bn.structureIsConnected());
 }

 @Test
 public void testSearchWithCustomCardinality() throws Exception {
 icssa.setMaxCardinality(3);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 Instances instances = new Instances(new FileReader("data/testData.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bn.buildClassifier(instances);
 assertTrue(bn.structureIsConnected());
 }

 @Test(expected = Exception.class)
 public void testSearchWithNegativeCardinality() {
 icssa.setMaxCardinality(-1);
 try {
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 Instances instances = new Instances(new FileReader("data/testData.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bn.buildClassifier(instances);
 } catch (Exception e) {
 assertEquals("MaxCardinality must be a non-negative integer.", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxCardinalityWithNull() {
 icssa.setMaxCardinality(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetMaxCardinalityWithNullInstances() {
 icssa.setMaxCardinality(3);
 icssa.setInstances(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetInstancesWithNull() {
 icssa.setInstances(null);
 }

 @Test
 public void testListOptions() throws Exception {
 Instances testInstances = new Instances(new FileReader("data/testData.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 Enumeration options = bn.getSearchAlgorithm().listOptions(new Instances(testInstances), new String[0]);
 assertNotNull(options);
 String cardinalityOption = "";
 while (options.hasMoreElements()) {
 String option = (String) options.nextElement();
 if (option.contains("cardinality")) {
 cardinalityOption = option;
 break;
 }
 }
 assertNotNull(cardinalityOption);
}

 @Test
 public void testGetMaxCardinality() throws Exception {
 Instances testInstances = new Instances(new FileReader("data/testData.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 assertEquals(2, icssa.getMaxCardinality());
 }
}