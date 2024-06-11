import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 static Instances data;

 @BeforeClass
 public static void setup() throws Exception {
 data = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 }

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(2);
 }

 @Test
 public void testName() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 assertEquals("pupilSize", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 assertEquals(8, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(4);
 assertEquals(4, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 BayesNet bayesNet = new BayesNet();
 Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.search(bayesNet, instances);
 assertTrue(bayesNet.getClassNumNodes() > 0);
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullBayesNet() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.search(null, instances);
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullInstances() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 BayesNet bayesNet = new BayesNet();
 icssa.search(bayesNet, null);
 }

 @Test
 public void testCalcDependencyGraph() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 if (j != i && !edges[i][j]) {
 fail("Expected edge between " + icssa.name(i) + " and " + icssa.name(j));
 }
 }
 }
 }

 @Test
 public void testExistsSepSet() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 2;
 SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNotNull(sepSet);
 for (int i = 0; i < nCardinality; ++i) {
 int node = icssa.next(sepSet.m_set[i], iNode1, iNode2, edges);
 assertEquals(iNode1, node);
 }
 }

 @Test
 public void testNext() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 int x = icssa.next(-1, 0, 1, new boolean[icssa.maxn() + 1][]);
 assertEquals(1, x);
 }

 @Test
 public void testCalcVeeNodes() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcVeeNodes(edges, arrows, sepsets);
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 if (i != j && !arrows[i][j]) {
 fail("Expected arrow between " + icssa.name(i) + " and " + icssa.name(j));
 }
 }
 }
 }

 @Test
 public void testCalcArcDirections() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 boolean[][] arrows = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 icssa.calcVeeNodes(edges, arrows, sepsets);
 icssa.calcArcDirections(edges, arrows);
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 if (i != j && arrows[i][j]) {
 fail("Expected no arrow between " + icssa.name(i) + " and " + icssa.name(j));
 }
 }
 }
 }

}