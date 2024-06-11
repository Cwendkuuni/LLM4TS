package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
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
 instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.setInstances(instances);
 icssa.setMaxCardinality(3);
 }

 @Test
 public void testName() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 for (int i = 0; i < instances.numAttributes(); ++i) {
 String expected = instances.attribute(i).name();
 String actual = icssa.name(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testMaxn() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 int expected = instances.numAttributes();
 int actual = icssa.maxn();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMaxCardinality() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 icssaCopy.setMaxCardinality(5);
 int expected = 5;
 int actual = icssaCopy.getMaxCardinality();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxCardinality() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 icssaCopy.setMaxCardinality(3);
 int expected = 3;
 int actual = icssaCopy.getMaxCardinality();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxCardinalityNegativeNumber() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 icssaCopy.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 icssaCopy.setMaxCardinality(3);
 bayesNet.buildClassifier(instances);
 icssaCopy.search(bayesNet, instances);
 assertTrue(bayesNet.structureIsLearned());
 }

 @Test
 public void testCalcDependencyGraph() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 boolean[][] edges = new boolean[instances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
 icssaCopy.calcDependencyGraph(edges, sepsets);
 // Printing edges for debugging purposes
 for (int i = 0; i < edges.length; ++i) {
 for (int j = 0; j < edges[i].length; ++j) {
 System.out.print(edges[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testExistsSepSet() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 boolean[][] edges = new boolean[instances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
 SeparationSet expected = new SeparationSet();
 expected.m_set[0] = 0;
 expected.m_set[1] = 1;
 SeparationSet actual = icssaCopy.existsSepSet(0, 1, 2, edges);
 assertArrayEquals(expected.m_set, actual.m_set);
 }

 @Test
 public void testNext() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 int x = icssaCopy.next(-1, 0, 1, edges);
 assertEquals(1, x);
 }

 @Test
 public void testCalcVeeNodes() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 boolean[][] edges = new boolean[instances.numAttributes() + 1][];
 boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
 icssaCopy.calcDependencyGraph(edges, sepsets);
 icssaCopy.calcVeeNodes(edges, arrows, sepsets);
 // Printing arrows for debugging purposes
 for (int i = 0; i < arrows.length; ++i) {
 for (int j = 0; j < arrows[i].length; ++j) {
 System.out.print(arrows[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testCalcArcDirections() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 boolean[][] edges = new boolean[instances.numAttributes() + 1][];
 boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
 icssaCopy.calcDependencyGraph(edges, null);
 icssaCopy.calcVeeNodes(edges, arrows, null);
 icssaCopy.calcArcDirections(edges, arrows);
 // Printing arrows for debugging purposes
 for (int i = 0; i < arrows.length; ++i) {
 for (int j = 0; j < arrows[i].length; ++j) {
 System.out.print(arrows[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testListOptions() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 CommandLineParser parser = new BasicParser();
 CommandLine cmd = parser.parse(new String[]{"-R", icssaCopy.getClass().getName()});
 String[] options = icssaCopy.listOptions().toArray(new String[0]);
 String[] expectedOptions = {"-R", "-U", "-I", "-S", "-T", "-K", "-V", "-N", "-M", "-C", "-cardinality"};
 assertArrayEquals(expectedOptions, options);
 }

 @Test
 public void testSetOptions() throws Exception {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 CommandLineParser parser = new BasicParser();
 CommandLine cmd = parser.parse(new String[]{"-R", icssaCopy.getClass().getName(), "-cardinality", "4"});
 icssaCopy.setOptions(cmd.getOptions());
 int expected = 4;
 int actual = icssaCopy.getMaxCardinality();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetOptions() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 String[] options = icssaCopy.getOptions();
 String[] expectedOptions = {"-R", "weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm"};
 assertArrayEquals(expectedOptions, options);
 }

 @Test
 public void testMaxCardinalityTipText() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssaCopy.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfo() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssaCopy.globalInfo());
 }

 @Test
 public void testGetRevision() {
 Instances instancesCopy = new Instances(instances);
 ICSSearchAlgorithm icssaCopy = new ICSSearchAlgorithm();
 icssaCopy.setInstances(instancesCopy);
 assertEquals("$Revision: 8034 $", icssaCopy.getRevision());
 }

 @After
 public void tearDown() throws Exception {
 bayesNet = null;
 instances = null;
 icssa = null;
 }
}