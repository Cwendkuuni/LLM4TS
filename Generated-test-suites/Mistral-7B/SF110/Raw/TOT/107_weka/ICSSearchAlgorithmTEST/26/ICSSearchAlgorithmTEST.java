import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.Random;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances data;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 }

 @Test
 public void testSearchWithMaxCardinalityTwo() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, data);
 // TODO: Add assertions for the expected structure of the BayesNet
 }

 @Test
 public void testSearchWithMaxCardinalityFour() throws Exception {
 icssa.setMaxCardinality(4);
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, data);
 // TODO: Add assertions for the expected structure of the BayesNet
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeMaxCardinality() throws Exception {
 icssa.setMaxCardinality(-1);
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNullInstances() throws Exception {
 icssa.search(null, data);
 }

 @Test(expected = NullPointerException.class)
 public void testSearchWithNullInstancesAttribute() throws Exception {
 data.deleteAttributeAt(new Random().nextInt(data.numAttributes()));
 icssa.search(new BayesNet(), data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNonExistentAttributeIndex() throws Exception {
 icssa.search(new BayesNet(), data);
 icssa.search(new BayesNet(), data);
 icssa.search(new BayesNet(), data);
 data.deleteAttributeAt(0);
 icssa.search(new BayesNet(), data);
 }
}