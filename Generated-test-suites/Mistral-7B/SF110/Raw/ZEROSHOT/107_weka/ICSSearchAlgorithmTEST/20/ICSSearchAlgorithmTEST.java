import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm algorithm;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 algorithm = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 }

 @Test
 public void testSearch() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(algorithm);
 algorithm.search(bayesNet, instances);
 }

 @Test
 public void testSetMaxCardinality() {
 algorithm.setMaxCardinality(3);
 assertEquals(3, algorithm.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, algorithm.getMaxCardinality());
 }

 @Test
 public void testName() {
 final int attributeIndex = 0;
 assertEquals("age", algorithm.name(attributeIndex));
 }

 @Test
 public void testMaxn() {
 assertEquals(9, algorithm.maxn());
 }

 @Test
 public void testCalcDependencyGraph() {
 final boolean[][] edges = new boolean[algorithm.maxn() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[algorithm.maxn() + 1][];
 for (int i = 0; i < algorithm.maxn() + 1; ++i) {
 edges[i] = new boolean[algorithm.maxn()];
 sepsets[i] = new SeparationSet[algorithm.maxn()];
 }
 algorithm.calcDependencyGraph(edges, sepsets);
 // Printing the dependency graph for verification
 for (int i = 0; i < algorithm.maxn(); ++i) {
 System.out.println("Node " + i + ":");
 for (int j = 0; j < algorithm.maxn(); ++j) {
 System.out.println("  " + (edges[i][j] ? "X" : ".") + " " + algorithm.name(j));
 }
 System.out.println();
 }
 }

 @Test
 public void testExistsSepSet() {
 final int iNode1 = 0;
 final int iNode2 = 1;
 final int nCardinality = 2;
 final boolean[][] edges = new boolean[algorithm.maxn() + 1][];
 for (int i = 0; i < algorithm.maxn() + 1; ++i) {
 edges[i] = new boolean[algorithm.maxn()];
 }
 SeparationSet sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNull(sepSet);
 // Manually create a suitable separation set for testing
 sepSet = new SeparationSet();
 sepSet.m_set[0] = 2;
 sepSet.m_set[1] = 3;
 edges[iNode1][iNode2] = false;
 edges[iNode1][sepSet.m_set[0]] = false;
 edges[iNode1][sepSet.m_set[1]] = false;
 edges[iNode2][sepSet.m_set[0]] = false;
 edges[iNode2][sepSet.m_set[1]] = false;
 edges[sepSet.m_set[0]][sepSet.m_set[1]] = false;
 sepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertEquals(sepSet, algorithm.sepsets[iNode1][iNode2]);
 }

 @Test
 public void testNext() {
 final int x = 4;
 final int iNode1 = 0;
 final int iNode2 = 1;
 final boolean[][] edges = new boolean[algorithm.maxn() + 1][];
 for (int i = 0; i < algorithm.maxn() + 1; ++i) {
 edges[i] = new boolean[algorithm.maxn()];
 }
 int nextNode = algorithm.next(x, iNode1, iNode2, edges);
 assertEquals(5, nextNode);
 }

 @Test
 public void testCalcVeeNodes() {
 final boolean[][] edges = new boolean[algorithm.maxn() + 1][];
 final boolean[][] arrows = new boolean[algorithm.maxn() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[algorithm.maxn() + 1][];
 for (int i = 0; i < algorithm.maxn() + 1; ++i) {
 edges[i] = new boolean[algorithm.maxn()];
 arrows[i] = new boolean[algorithm.maxn()];
 sepsets[i] = new SeparationSet[algorithm.maxn()];
 }
 algorithm.calcVeeNodes(edges, arrows, sepsets);
 // Printing the arrows for verification
 for (int i = 0; i < algorithm.maxn(); ++i) {
 for (int j = 0; j < algorithm.maxn(); ++j) {
 System.out.println("Arrow from node " + i + " to node " + j + ": " + arrows[i][j]);
 }
 }
 }

 @Test
 public void testCalcArcDirections() {
 final boolean[][] edges = new boolean[algorithm.maxn() + 1][];
 final boolean[][] arrows = new boolean[algorithm.maxn() + 1][];
 algorithm.calcArcDirections(edges, arrows);
 // Printing the arrows for verification
 for (int i = 0; i < algorithm.maxn(); ++i) {
 for (int j = 0; j < algorithm.maxn(); ++j) {
 System.out.println("Arrow from node " + i + " to node " + j + ": " + arrows[i][j]);
 }
 }
 }

 @Test
 public void testListOptions() {
 List<Option> options = new ArrayList<>();
 List<Option> result = new ArrayList<>();
 options.add(new Option("\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)", "cardinality", 1, "-cardinality <num>"));
 ICSSearchAlgorithm.addOptions(options, result);
 assertEquals(1, result.size());
 assertEquals("cardinality", result.get(0).getName());
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes.", result.get(0).getDescription());
 assertEquals(1, result.get(0).getMinimumNumberOfValues());
 assertEquals("The maximum size of the set Z.", result.get(0).getDefaultValue());
 }

 @Test
 public void testSetOptions() throws Exception {
 algorithm.setOptions(new String[]{"-cardinality", "3"});
 assertEquals(3, algorithm.getMaxCardinality());
 }

 @Test
 public void testGetOptions() {
 String[] options = algorithm.getOptions();
 assertEquals(1, options.length);
 assertEquals("-cardinality", options[0].substring(0, 11));
 assertEquals("3", options[0].substring(13));
 }

 @Test
 public void testMaxCardinalityTipText() {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", algorithm.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfo() {
 assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", algorithm.globalInfo());
 }

 @Test
 public void testGetRevision() {
 assertEquals("$Revision: 8034 $", algorithm.getRevision());
 }
}