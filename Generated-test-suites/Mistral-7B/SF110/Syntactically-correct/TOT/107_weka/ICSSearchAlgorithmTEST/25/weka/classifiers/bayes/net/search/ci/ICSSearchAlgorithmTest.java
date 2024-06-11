import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm algorithm;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 Instances data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 this.instances = new Instances(data);
 this.algorithm = new ICSSearchAlgorithm();
 this.algorithm.setMaxCardinality(2);
 }

 @Test
 public void testSearch() throws Exception {
 BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(this.algorithm);
 this.algorithm.setInstances(this.instances);
 this.algorithm.search(bayesNet, this.instances);
 // Verify that the search was performed correctly
 // Add assertions here to check the dependencies, arrows, and separation sets
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 this.algorithm.setMaxCardinality(-1);
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, this.algorithm.getMaxCardinality());
 }

 @Test
 public void testSetMaxCardinality() {
 this.algorithm.setMaxCardinality(5);
 assertEquals(5, this.algorithm.getMaxCardinality());
 }

 @Test
 public void testName() {
 assertEquals("age", this.algorithm.name(0));
 }

 @Test
 public void testMaxn() {
 assertEquals(9, this.algorithm.maxn());
 }
}