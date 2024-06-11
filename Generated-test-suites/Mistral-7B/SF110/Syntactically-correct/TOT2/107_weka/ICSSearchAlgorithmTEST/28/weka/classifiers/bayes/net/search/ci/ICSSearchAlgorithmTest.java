import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;

 @Test
 public void testName() {
 icssa = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 icssa.setInstances(instances);
 assertEquals("Age", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 icssa = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 icssa.setInstances(instances);
 assertEquals(instances.numAttributes(), icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 icssa = new ICSSearchAlgorithm();
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 icssa = new ICSSearchAlgorithm();
 BayesNet bayesNet = new BayesNet();
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 icssa.setInstances(instances);
 icssa.setMaxCardinality(3);
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, instances);
 // Check if the search was performed with the correct cardinality
 // This might require checking the internal structures of the BayesNet object
 // or by checking the output of the algorithm
 }

 @Test
 public void testCalcDependencyGraph() {
 icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[10][10];
 SeparationSet[][] sepsets = new SeparationSet[10][];
 for (int i = 0; i < 10; ++i) {
 sepsets[i] = new SeparationSet();
 }
 icssa.calcDependencyGraph(edges, sepsets);
 // Check if the dependency graph was calculated correctly
 // This might require checking the output of the algorithm
 // or by using a reference dependency graph
 }

 @Test
 public void testExistsSepSet() {
 icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[10][10];
 SeparationSet Z = new SeparationSet();
 Z.m_set[0] = 1;
 Z.m_set[1] = 2;
 Z.m_set[2] = 3;
 int cardinality = 3;
 boolean result = icssa.existsSepSet(0, 4, cardinality, edges);
 assertFalse(result);
 Z.m_set[2] = 4;
 result = icssa.existsSepSet(0, 4, cardinality, edges);
 assertTrue(result);
 }

 @Test
 public void testNext() {
 icssa = new ICSSearchAlgorithm();
 int x = 0;
 int iNode1 = 1;
 int iNode2 = 2;
 boolean[][] edges = new boolean[10][10];
 edges[iNode1][iNode2] = true;
 int expected = 3;
 int actual = icssa.next(x, iNode1, iNode2, edges);
 assertEquals(expected, actual);
 }

 @Test
 public void testCalcVeeNodes() {
 icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[10][10];
 boolean[][] arrows = new boolean[10][10];
 SeparationSet[][] sepsets = new SeparationSet[10][];
 for (int i = 0; i < 10; ++i) {
 sepsets[i] = new SeparationSet();
 }
 icssa.calcVeeNodes(edges, arrows, sepsets);
 // Check if the vee nodes were calculated correctly
 // This might require checking the output of the algorithm
 // or by using a reference vee nodes
 }

 @Test
 public void testCalcArcDirections() {
 icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[10][10];
 boolean[][] arrows = new boolean[10][10];
 SeparationSet[][] sepsets = new SeparationSet[10][];
 for (int i = 0; i < 10; ++i) {
 sepsets[i] = new SeparationSet();
 }
 icssa.calcArcDirections(edges, arrows);
 // Check if the arc directions were calculated correctly
 // This might require checking the output of the algorithm
 // or by using a reference arc directions
 }
}