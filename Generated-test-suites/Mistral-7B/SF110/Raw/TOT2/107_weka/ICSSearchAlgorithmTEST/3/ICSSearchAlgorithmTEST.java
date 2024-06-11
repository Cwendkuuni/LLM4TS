import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances data;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 data = new Instances(new FileReader("path/to/your/data.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 }

 @Test
 public void testConstructor() {
 icssa = new ICSSearchAlgorithm();
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test
 public void testName() throws Exception {
 Instances instances = new Instances(new FileReader("path/to/your/data.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 String expected = "attributeName";
 int attributeIndex = 3;
 String result = icssa.name(attributeIndex);
 assertEquals(expected, result);
 }

 @Test
 public void testMaxn() throws Exception {
 Instances instances = new Instances(new FileReader("path/to/your/data.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 int expected = instances.numAttributes();
 int result = icssa.maxn();
 assertEquals(expected, result);
 }

 @Test
 public void testSetMaxCardinality() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(5);
 int result = icssa.getMaxCardinality();
 assertEquals(5, result);
 }

 @Test
 public void testGetMaxCardinality() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(3);
 int result = icssa.getMaxCardinality();
 assertEquals(3, result);
 }

 @Test
 public void testSearch() throws Exception {
 Instances instances = new Instances(new FileReader("path/to/your/data.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 assertNull(bayesNet.getStructure());
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet.getStructure());
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 icssa.calcDependencyGraph(edges, sepsets);
 // Add checks for the printed output here
 }

 @Test(expected = Exception.class)
 public void testCalcDependencyGraphWithInvalidParameter() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 SeparationSet[][] sepsets = null;
 icssa.calcDependencyGraph(edges, sepsets);
 fail("Expected an Exception to be thrown");
 }

 @Test
 public void testExistsSepSet() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 2;
 boolean[][] edges = new boolean[5][];
 SeparationSet Z = new SeparationSet();
 Z.m_set[nCardinality] = -1;
 int expectedIndex = 3;
 SeparationSet result = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 SeparationSet expected = new SeparationSet();
 expected.m_set[0] = expectedIndex;
 expected.m_set[1] = 4;
 assertEquals(expected, result);
 }

 @Test
 public void testNext() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 int x = 0;
 int iNode1 = 1;
 int iNode2 = 2;
 boolean[][] edges = new boolean[5][];
 int expected = 4;
 int result = icssa.next(x, iNode1, iNode2, edges);
 assertEquals(expected, result);
 }

 @Test
 public void testCalcVeeNodes() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 boolean[][] arrows = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 icssa.calcVeeNodes(edges, arrows, sepsets);
 // Add checks for the printed output here
 }

 @Test
 public void testCalcArcDirections() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 boolean[][] arrows = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 icssa.calcArcDirections(edges, arrows);
 // Add checks for the printed output here
 }

 @Test
 public void testListOptions() throws Exception {
 String[] options = icssa.listOptions();
 String expectedOption = " cardinality <num>";
 boolean result = contains(options, expectedOption);
 assertTrue(result);
 }

 @Test
 public void testSetOptions() throws Exception {
 String[] options = {"-cardinality 5"};
 icssa.setOptions(options);
 int result = icssa.getMaxCardinality();
 assertEquals(5, result);
 }

 @Test
 public void testGetOptions() throws Exception {
 String[] options = icssa.getOptions();
 String expectedOption = "-cardinality 5";
 int index = indexOf(options, expectedOption);
 assertEquals(expectedOption, options[index]);
 }

 @Test
 public void testMaxCardinalityTipText() throws Exception {
 String result = icssa.maxCardinalityTipText();
 String expected = "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
 assertEquals(expected, result);
 }

 @Test
 public void testGlobalInfo() throws Exception {
 String result = icssa.globalInfo();
 String expected = "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
 assertEquals(expected, result);
 }

 @Test
 public void testGetRevision() throws Exception {
 String result = icssa.getRevision();
 String expected = RevisionUtils.extract("$Revision: 8034 $");
 assertEquals(expected, result);
 }

 @After
 public void tearDown() throws Exception {
 icssa = null;
 data = null;
 }

}