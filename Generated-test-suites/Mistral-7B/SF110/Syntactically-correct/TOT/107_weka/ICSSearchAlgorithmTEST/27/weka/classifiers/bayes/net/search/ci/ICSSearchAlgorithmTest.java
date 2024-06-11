import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances instances;
 private BayesNet bayesNet;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 bayesNet.buildClassifier(instances);
 }

 @Test
 public void testSearchWithDefaultCardinality() throws Exception {
 icssa.setMaxCardinality(2);
 Instances testInstances = new Instances(new FileReader("contact-lenses.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 BayesNet testBayesNet = new BayesNet();
 testBayesNet.setSearchAlgorithm(icssa);
 testBayesNet.buildClassifier(testInstances);
 assertNotNull(testBayesNet);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeCardinality() {
 icssa.setMaxCardinality(-1);
 try {
 icssa.search(bayesNet, instances);
 fail("IllegalArgumentException expected but not thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSearchWithMaxCardinalityGreaterThanNumberOfAttributes() throws Exception {
 icssa.setMaxCardinality(instances.numAttributes() + 1);
 Instances testInstances = new Instances(new FileReader("contact-lenses.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 BayesNet testBayesNet = new BayesNet();
 testBayesNet.setSearchAlgorithm(icssa);
 testBayesNet.buildClassifier(testInstances);
 assertNotNull(testBayesNet);
 }
}