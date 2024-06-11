import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

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
 }

 @Test
 public void testName() {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 assertEquals("name", "pupilSize", icssaTest.name(0));
 }

 @Test
 public void testMaxn() throws Exception {
 assertEquals("maxn", 9, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(5);
 assertEquals("setMaxCardinality", 5, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxCardinalityNegative() {
 icssa.setMaxCardinality(-1);
 fail("setMaxCardinality with negative number not thrown an exception");
 }

 @Test
 public void testSearch() throws Exception {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 bayesNet.buildClassifier(testInstances);
 assertNotNull("search method not called", icssaTest.m_BayesNet);
 assertNotNull("m_instances not set", icssaTest.m_instances);
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 icssaTest.search(bayesNet, testInstances);
 boolean[][] edgesTest = icssaTest.edges;
 for (int iNode1 = 0; iNode1 < icssaTest.maxn(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < icssaTest.maxn(); ++iNode2) {
 assertEquals("edges[" + iNode1 + "][" + iNode2 + "]", false, edgesTest[iNode1][iNode2]);
 assertEquals("edges[" + iNode2 + "][" + iNode1 + "]", false, edgesTest[iNode2][iNode1]);
 }
 }
 }

 @Test
 public void testExistsSepSet() {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 icssaTest.search(bayesNet, testInstances);
 boolean[][] edgesTest = icssaTest.edges;
 SeparationSet sepsetTest = icssaTest.existsSepSet(0, 1, 2, edgesTest);
 assertNotNull("sepsetTest is null", sepsetTest);
 assertFalse("sepsetTest.m_set[0] is not less than 0", sepsetTest.m_set[0] < 0);
 assertFalse("sepsetTest.m_set[1] is not less than 0", sepsetTest.m_set[1] < 0);
 assertTrue("sepsetTest.m_set[1] is not the same as iNode2", sepsetTest.m_set[1] == 1);
 }

 @Test
 public void testNext() {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 int x = icssaTest.next(-1, 0, 1, edgesTest);
 assertEquals("next when x is -1", 0, x);
 }

 @Test
 public void testCalcVeeNodes() throws Exception {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 icssaTest.search(bayesNet, testInstances);
 boolean[][] arrowsTest = icssaTest.arrows;
 for (int iNode1 = 0; iNode1 < icssaTest.maxn(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < icssaTest.maxn(); ++iNode2) {
 assertEquals("arrows[" + iNode1 + "][" + iNode2 + "]", false, arrowsTest[iNode1][iNode2]);
 assertEquals("arrows[" + iNode2 + "][" + iNode1 + "]", false, arrowsTest[iNode2][iNode1]);
 }
 }
 }

 @Test
 public void testCalcArcDirections() throws Exception {
 Instances testInstances = new Instances(new ByteArrayInputStream(new byte[]{"S:weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm\n".getBytes()}));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 ICSSearchAlgorithm icssaTest = new ICSSearchAlgorithm();
 icssaTest.search(bayesNet, testInstances);
 boolean[][] edgesTest = icssaTest.edges;
 boolean[][] arrowsTest = icssaTest.arrows;
 for (int iNode1 = 0; iNode1 < icssaTest.maxn(); ++iNode1) {
 for (int iNode2 = 0; iNode2 < icssaTest.maxn(); ++iNode2) {
 assertEquals("arrows[" + iNode1 + "][" + iNode2 + "]", false, arrowsTest[iNode1][iNode2]);
 assertEquals("arrows[" + iNode2 + "][" + iNode1 + "]", false, arrowsTest[iNode2][iNode1]);
 }
 }
 }

 @Test
 public void testListOptions() {
 assertEquals("listOptions", 2, icssa.listOptions().size());
 }

 @Test
 public void testSetOptions() throws Exception {
 String[] options = {"-U", "-cardinality 4"};
 icssa.setOptions(options);
 assertEquals("setOptions", 4, icssa.getMaxCardinality());
 }

 @Test
 public void testGetOptions() {
 assertEquals("getOptions", "[-U, -cardinality 4]", Arrays.toString(icssa.getOptions()));
 }

 @Test
 public void testMaxCardinalityTipText() {
 assertEquals("maxCardinalityTipText", "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfo() {
 assertEquals("globalInfo", "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
 }

 @Test
 public void testGetRevision() {
 assertEquals("getRevision", "Revision: 8034 ", icssa.getRevision());
 }
}