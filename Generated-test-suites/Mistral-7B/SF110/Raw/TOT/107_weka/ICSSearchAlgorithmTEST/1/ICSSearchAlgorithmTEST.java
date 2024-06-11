import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm algorithm;
 private Instances data;

 @Before
 public void setUp() throws Exception {
 algorithm = new ICSSearchAlgorithm();
 data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 }

 @Test
 public void testSearch() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(algorithm);
 algorithm.search(bayesNet, data);
 // Verify some properties of the learned Bayes net
 // Add asserts here
 }

 @Test
 public void testSetMaxCardinality() {
 algorithm.setMaxCardinality(5);
 assertEquals(5, algorithm.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 algorithm.setMaxCardinality(-1);
 fail("Should not accept negative max cardinality");
 }

 @Test
 public void testExistsSepSetNull() {
 final boolean[][] edges = new boolean[data.numAttributes() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[data.numAttributes() + 1][];
 assertNull(algorithm.existsSepSet(0, 1, 0, edges));
 }

 @Test
 public void testNext() {
 int x = 0;
 assertEquals(1, algorithm.next(x, 1, 2, edges));
 assertEquals(3, algorithm.next(2, 1, 2, edges));
 }

 @Test
 public void testCalcDependencyGraph() {
 final boolean[][] edges = new boolean[data.numAttributes() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[data.numAttributes() + 1][];
 algorithm.calcDependencyGraph(edges, sepsets);
 // Add asserts here to verify the calculated dependency graph
 }

 @Test
 public void testIsConditionalIndependent() {
 // Add test cases for isConditionalIndependent method here
 }
}