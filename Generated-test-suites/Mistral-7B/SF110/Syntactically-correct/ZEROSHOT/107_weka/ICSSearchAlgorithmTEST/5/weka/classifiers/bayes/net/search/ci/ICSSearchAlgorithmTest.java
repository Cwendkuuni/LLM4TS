package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private BayesNet bayesNetMock;
 private Instances instancesMock;

 @Before
 public void setUp() {
 icssa = new ICSSearchAlgorithm();
 bayesNetMock = mock(BayesNet.class);
 instancesMock = mock(Instances.class);
 }

 @Test
 public void testName() {
 Instances instances = new Instances(new ByteArrayInputStream(new byte[]{}));
 instances.setClassIndex(0);
 icssa.setInstances(instances);
 int iAttribute = 1;
 String expectedName = "attributeName";
 when(instances.attribute(iAttribute)).thenReturn(new Attribute("attributeName"));
 String actualName = icssa.name(iAttribute);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testMaxn() throws Exception {
 Instances instances = new Instances(new ByteArrayInputStream(new byte[]{}));
 instances.setClassIndex(0);
 icssa.setInstances(instances);
 int expectedMaxn = 3;
 when(instances.numAttributes()).thenReturn(expectedMaxn);
 int actualMaxn = icssa.maxn();
 assertEquals(expectedMaxn, actualMaxn);
 }

 @Test
 public void testSetMaxCardinality() throws Exception {
 icssa.setMaxCardinality(3);
 int expectedMaxCardinality = 3;
 assertEquals(expectedMaxCardinality, icssa.getMaxCardinality());
 }

 @Test(expected = AssertionError.class)
 public void testSetMaxCardinalityNegativeValue() throws Exception {
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 Instances instances = new Instances(new ByteArrayInputStream(new byte[]{}));
 instances.setClassIndex(0);
 icssa.setInstances(instances);
 BayesNet bayesNet = new BayesNet();
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet.getStructure());
 }

 @Test
 public void testCalcDependencyGraph() {
 boolean[][] edges = new boolean[4][];
 SeparationSet[][] sepsets = new SeparationSet[4][];
 for (int i = 0; i < 4; ++i) {
 edges[i] = new boolean[4];
 sepsets[i] = new SeparationSet[4];
 for (int j = 0; j < 4; ++j) {
 edges[i][j] = false;
 sepsets[i][j] = new SeparationSet();
 }
 }
 icssa.calcDependencyGraph(edges, sepsets);
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 4; ++j) {
 assertEquals(false, edges[i][j]);
 }
 }
 }

 @Test
 public void testExistsSepSet() {
 boolean[][] edges = new boolean[4][];
 SeparationSet[][] sepsets = new SeparationSet[4][];
 for (int i = 0; i < 4; ++i) {
 edges[i] = new boolean[4];
 sepsets[i] = new SeparationSet();
 for (int j = 0; j < 4; ++j) {
 edges[i][j] = true;
 }
 }
 SeparationSet expectedSepSet = new SeparationSet();
 expectedSepSet.m_set[0] = 1;
 expectedSepSet.m_set[1] = 2;
 SeparationSet actualSepSet = icssa.existsSepSet(0, 3, 2, edges);
 assertNull(actualSepSet);
 actualSepSet = icssa.existsSepSet(0, 1, 2, edges);
 assertEquals(expectedSepSet, actualSepSet);
 }

 @Test
 public void testNext() {
 int x = 0;
 int expectedNext = 3;
 int actualNext = icssa.next(x, 1, 2, edges);
 assertEquals(expectedNext, actualNext);
 }

 @Test
 public void testCalcVeeNodes() {
 boolean[][] edges = new boolean[4][];
 boolean[][] arrows = new boolean[4][];
 SeparationSet[][] sepsets = new SeparationSet[4][];
 for (int i = 0; i < 4; ++i) {
 edges[i] = new boolean[4];
 arrows[i] = new boolean[4];
 sepsets[i] = new SeparationSet[4];
 for (int j = 0; j < 4; ++j) {
 edges[i][j] = true;
 arrows[i][j] = false;
 sepsets[i][j] = new SeparationSet();
 }
 }
 icssa.calcVeeNodes(edges, arrows, sepsets);
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 4; ++j) {
 assertEquals(false, arrows[i][j]);
 }
 }
 }

 @Test
 public void testCalcArcDirections() {
 boolean[][] edges = new boolean[4][];
 boolean[][] arrows = new boolean[4][];
 SeparationSet[][] sepsets = new SeparationSet[4][];
 for (int i = 0; i < 4; ++i) {
 edges[i] = new boolean[4];
 arrows[i] = new boolean[4];
 sepsets[i] = new SeparationSet[4];
 for (int j = 0; j < 4; ++j) {
 edges[i][j] = true;
 sepsets[i][j] = new SeparationSet();
 }
 }
 icssa.calcArcDirections(edges, arrows);
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 4; ++j) {
 if (i != j) {
 assertEquals(false, arrows[i][j]);
 }
 }
 }
 }

 @Test(expected = AssertionError.class)
 public void testCalcArcDirectionsCycleDetected() {
 boolean[][] edges = new boolean[4][];
 boolean[][] arrows = new boolean[4][];
 for (int i = 0; i < 4; ++i) {
 edges[i] = new boolean[4];
 arrows[i] = new boolean[4];
 for (int j = 0; j < 4; ++j) {
 edges[i][j] = true;
 arrows[i][j] = true;
 }
 }
 icssa.calcArcDirections(edges, arrows);
 }

 @Test
 public void testListOptions() throws Exception {
 assertEquals(3, icssa.listOptions().size());
 }

 @Test
 public void testSetOptions() throws Exception {
 icssa.setOptions(new String[]{"-cardinality 3"});
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetOptionsInvalidOption() throws Exception {
 icssa.setOptions(new String[]{"-invalidOption 3"});
 }

 @Test
 public void testGetOptions() {
 String[] expectedOptions = new String[]{"-cardinality", "3"};
 String[] actualOptions = icssa.getOptions();
 assertArrayEquals(expectedOptions, actualOptions);
 }

 @Test
 public void testMaxCardinalityTipText() {
 String expectedTipText = "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
 String actualTipText = icssa.maxCardinalityTipText();
 assertEquals(expectedTipText, actualTipText);
 }

 @Test
 public void testGlobalInfo() {
 String expectedGlobalInfo = "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
 String actualGlobalInfo = icssa.globalInfo();
 assertEquals(expectedGlobalInfo, actualGlobalInfo);
 }

 @Test
 public void testGetRevision() {
 String expectedRevision = "8034";
 String actualRevision = icssa.getRevision();
 assertEquals(expectedRevision, actualRevision.substring(0, expectedRevision.length()));
 }

 @Test
 public void testMain() {
 try {
 icssa.main(new String[]{"-R"});
 fail("Expected an Exception to be thrown");
 }
 catch (Exception e) {
 // Expected
 }
 }
}