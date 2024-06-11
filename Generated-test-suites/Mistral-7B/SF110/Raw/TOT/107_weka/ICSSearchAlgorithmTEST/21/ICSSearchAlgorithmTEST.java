import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.SeparationSet;
import weka.core.Instances;

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
 public void testName() {
 assertEquals("age", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 assertEquals(9, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(5);
 assertEquals(5, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test
 public void testSearch() throws Exception {
 ICSSearchAlgorithm icssa1 = new ICSSearchAlgorithm();
 Instances instances1 = new Instances(new FileReader("contact-lenses.arff"));
 instances1.setClassIndex(instances1.numAttributes() - 1);
 icssa1.search(new BayesNet(), instances1);
 assertNotNull(icssa1.m_BayesNet);
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullBayesNet() throws Exception {
 ICSSearchAlgorithm icssa2 = new ICSSearchAlgorithm();
 icssa2.search(null, instances);
 fail("Search method should throw an Exception with null BayesNet argument");
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullInstances() throws Exception {
 ICSSearchAlgorithm icssa3 = new ICSSearchAlgorithm();
 icssa3.search(new BayesNet(), null);
 fail("Search method should throw an Exception with null Instances argument");
 }

 @Test
 public void testCalcDependencyGraph() {
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
 assertEquals(iNode1 != iNode2, icssa.m_BayesNet.getEdges()[iNode1][iNode2]);
 }
 }
 }

 @Test
 public void testExistsSepSet() {
 final int iNode1 = 0;
 final int iNode2 = 1;
 final int nCardinality = 2;
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 SeparationSet Z = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNotNull(Z);
 assertTrue(Z.contains(iNode1));
 assertTrue(Z.contains(iNode2));
 }

 @Test
 public void testNext() {
 int x = 0;
 assertEquals(3, icssa.next(x, 0, 1, edges));
 }

 @Test
 public void testCalcVeeNodes() {
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 icssa.calcVeeNodes(edges, arrows, sepsets);
 for (int iNode1 = 0; iNode1 < icssa.maxn() - 1; ++iNode1) {
 for (int iNode2 = iNode1 + 1; iNode2 < icssa.maxn(); ++iNode2) {
 assertEquals(iNode1 != iNode2, icssa.m_BayesNet.getEdges()[iNode1][iNode2]);
 assertEquals(iNode1 != iNode2, arrows[iNode1][iNode2]);
 assertEquals(iNode1 != iNode2, arrows[iNode2][iNode1]);
 }
 }
 }

 @Test
 public void testCalcArcDirections() {
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 icssa.calcVeeNodes(edges, arrows, sepsets);
 icssa.calcArcDirections(edges, arrows);
 for (int iNode1 = 0; iNode1 < icssa.maxn(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < icssa.maxn(); ++iNode2) {
 assertEquals(iNode1 != iNode2, arrows[iNode1][iNode2]);
 assertEquals(iNode1 != iNode2, arrows[iNode2][iNode1]);
 }
 }
 }
}