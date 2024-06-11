import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 }

 @Test
 public void testSearch() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, instances);
 // Add assertions to check the behavior of the search method here
 }

 @Test
 public void testName() {
 assertEquals("age", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 assertEquals(8, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 icssa.setMaxCardinality(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMaxCardinalityGreaterThanNumberOfAttributes() {
 icssa.setMaxCardinality(10);
 instances.setClassIndex(instances.numAttributes() + 1);
 icssa.search(bayesNet, instances);
 }

 @Test
 public void testExistsSepSet() {
 final boolean[][] edges = new boolean[instances.numAttributes() + 1][];
 for (int iNode1 = 0; iNode1 < instances.numAttributes(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < instances.numAttributes(); ++iNode2) {
 edges[iNode1][iNode2] = true;
 }
 }
 final SeparationSet sepSet = icssa.existsSepSet(0, 1, 2, edges);
 assertNotNull(sepSet);
 assertEquals(2, sepSet.m_set.length);
 assertEquals(1, sepSet.m_set[0]);
 assertEquals(3, sepSet.m_set[1]);
 }

 @Test
 public void testNext() {
 int x = icssa.next(-1, 0, 1, edges);
 assertEquals(2, x);
 }

 @Test
 public void testCalcDependencyGraph() {
 icssa.calcDependencyGraph(edges, sepsets);
 // Add assertions to check the behavior of the calcDependencyGraph method here
 }

 @Test
 public void testCalcVeeNodes() {
 icssa.calcVeeNodes(edges, arrows, sepsets);
 // Add assertions to check the behavior of the calcVeeNodes method here
 }

 @Test
 public void testCalcArcDirections() {
 icssa.calcArcDirections(edges, arrows);
 // Add assertions to check the behavior of the calcArcDirections method here
 }
}