import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;

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
 icssa.setInstances(instances);
 icssa.search(bayesNet, instances);
 }

 @Test
 public void testName() {
 for (int i = 0; i < icssa.maxn(); ++i) {
 String expected = instances.attribute(i).name();
 assertEquals(expected, icssa.name(i));
 }
 }

 @Test
 public void testMaxn() {
 int expected = instances.numAttributes();
 assertEquals(expected, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(3);
 int expected = 3;
 assertEquals(expected, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxCardinalityNegative() {
 icssa.setMaxCardinality(-1);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxCardinalityGreaterThanNumberOfAttributes() {
 icssa.setMaxCardinality(instances.numAttributes() + 1);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testSearch() throws Exception {
 // Add test cases for the search method here
 }

 @Test
 public void testCalcDependencyGraph() {
 // Add test cases for the calcDependencyGraph method here
 }

 @Test
 public void testExistsSepSet() {
 // Add test cases for the existsSepSet method here
 }

 @Test
 public void testNext() {
 // Add test cases for the next method here
 }

 @Test
 public void testCalcVeeNodes() {
 // Add test cases for the calcVeeNodes method here
 }

 @Test
 public void testCalcArcDirections() {
 // Add test cases for the calcArcDirections method here
 }
}