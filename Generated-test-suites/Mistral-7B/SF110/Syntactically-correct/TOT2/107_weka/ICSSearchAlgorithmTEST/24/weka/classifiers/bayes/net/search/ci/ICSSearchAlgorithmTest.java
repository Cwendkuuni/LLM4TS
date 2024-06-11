import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.setInstances(instances);
 icssa.setMaxCardinality(2);
 }

 @Test
 public void testNameMethod() {
 final int iAttribute = 1;
 assertEquals("Age", icssa.name(iAttribute));
 }

 @Test
 public void testMaxnMethod() {
 assertEquals(3, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinalityMethod() {
 icssa.setMaxCardinality(5);
 assertEquals(5, icssa.getMaxCardinality());
 }

 @Test
 public void testSearchMethodNormalCase() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet);
 }

 @Test(expected = Exception.class)
 public void testSearchMethodNullInstances() throws Exception {
 icssa.search(null, instances);
 fail("Search method should throw an exception with null instances");
 }

 @Test(expected = Exception.class)
 public void testSearchMethodNullBayesNet() throws Exception {
 icssa.search(bayesNet, null);
 fail("Search method should throw an exception with null bayes net");
 }

 @Test
 public void testCalcDependencyGraphMethodNormalCase() {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 assertNotNull(edges);
 assertNotNull(sepsets);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcDependencyGraphMethodNullEdges() {
 icssa.calcDependencyGraph(null, sepsets);
 fail("calcDependencyGraph method should throw an IllegalArgumentException with null edges");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcDependencyGraphMethodNullSepsets() {
 icssa.calcDependencyGraph(edges, null);
 fail("calcDependencyGraph method should throw an IllegalArgumentException with null sepsets");
 }

 @Test
 public void testExistsSepSetMethodNormalCase() {
 final int iNode1 = 1;
 final int iNode2 = 2;
 final int nCardinality = 2;
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 edges[iNode1][iNode2] = true;
 final SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNotNull(oSepSet);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExistsSepSetMethodNullEdges() {
 final int iNode1 = 1;
 final int iNode2 = 2;
 final int nCardinality = 2;
 final SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, null);
 fail("existsSepSet method should throw an IllegalArgumentException with null edges");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExistsSepSetMethodNegativeCardinality() {
 final int iNode1 = 1;
 final int iNode2 = 2;
 final int nCardinality = -1;
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 edges[iNode1][iNode2] = true;
 final SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 fail("existsSepSet method should throw an IllegalArgumentException with negative cardinality");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExistsSepSetMethodCardinalityBiggerThanMax() {
 final int iNode1 = 1;
 final int iNode2 = 2;
 final int nCardinality = icssa.getMaxCardinality() + 1;
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 edges[iNode1][iNode2] = true;
 final SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 fail("existsSepSet method should throw an IllegalArgumentException with cardinality bigger than max");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExistsSepSetMethodCycle() {
 final int iNode1 = 1;
 final int iNode2 = 2;
 final int iNode3 = 3;
 final boolean[][] edges = { {false, true, true}, {true, false, true}, {true, true, false} };
 final SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, 2, edges);
 fail("existsSepSet method should throw an IllegalArgumentException with cycle");
 }

 @Test
 public void testNextMethodNormalCase() {
 final int x = 0;
 final int iNode1 = 1;
 final int iNode2 = 2;
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 edges[iNode1][iNode2] = true;
 final int nNext = icssa.next(x, iNode1, iNode2, edges);
 assertEquals(3, nNext);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextMethodIndexOutOfBounds() {
 final int x = icssa.maxn();
 final int iNode1 = 1;
 final int iNode2 = 2;
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 edges[iNode1][iNode2] = true;
 icssa.next(x, iNode1, iNode2, edges);
 fail("next method should throw an IndexOutOfBoundsException when index out of bounds");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextMethodNullEdges() {
 final int x = 0;
 final int iNode1 = 1;
 final int iNode2 = 2;
 final boolean[][] edges = null;
 final int nNext = icssa.next(x, iNode1, iNode2, edges);
 fail("next method should throw an IllegalArgumentException with null edges");
 }

 @Test
 public void testCalcVeeNodesMethodNormalCase() {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 icssa.calcVeeNodes(edges, arrows, sepsets);
 assertNotNull(arrows);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcVeeNodesMethodNullEdges() {
 final boolean[][] edges = null;
 final boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 icssa.calcVeeNodes(edges, arrows, sepsets);
 fail("calcVeeNodes method should throw an IllegalArgumentException with null edges");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcVeeNodesMethodNullArrow() {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final boolean[][] arrows = null;
 icssa.calcVeeNodes(edges, arrows, sepsets);
 fail("calcVeeNodes method should throw an IllegalArgumentException with null arrows");
 }

 @Test
 public void testCalcArcDirectionsMethodNormalCase() {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 icssa.calcArcDirections(edges, arrows);
 assertNotNull(arrows);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcArcDirectionsMethodNullEdges() {
 final boolean[][] edges = null;
 final boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 icssa.calcArcDirections(edges, arrows);
 fail("calcArcDirections method should throw an IllegalArgumentException with null edges");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCalcArcDirectionsMethodNullArrows() {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final boolean[][] arrows = null;
 icssa.calcArcDirections(edges, arrows);
 fail("calcArcDirections method should throw an IllegalArgumentException with null arrows");
 }
}