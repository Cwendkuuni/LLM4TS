package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NetUtils;
import weka.classifiers.bayes.net.InstanceListDataSource;
import weka.classifiers.bayes.net.search.*;
import java.util.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances testInstances;
 private BayesNet testNet;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 testInstances = new Instances(new FileReader("testData.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 testNet = new BayesNet();
 testNet.setSearchAlgorithm(icssa);
 }

 @Test
 public void testName() throws Exception {
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 String expected = testInstances.attribute(i).name();
 String actual = icssa.name(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testMaxn() throws Exception {
 int expected = testInstances.numAttributes();
 int actual = icssa.maxn();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMaxCardinality() throws Exception {
 icssa.setMaxCardinality(3);
 int actual = icssa.getMaxCardinality();
 assertEquals(3, actual);
 }

 @Test
 public void testGetMaxCardinality() throws Exception {
 icssa.setMaxCardinality(2);
 int actual = icssa.getMaxCardinality();
 assertEquals(2, actual);
 }

 @Test
 public void testSearch() throws Exception {
 testNet.buildClassifier(testInstances);
 assertTrue(testNet.structureIsAcyclic());
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[testInstances.numAttributes() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 // Printing edges matrix for verification
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 for (int j = 0; j < testInstances.numAttributes(); ++j) {
 System.out.print(edges[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testExistsSepSet() throws Exception {
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 2;
 boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 for (int j = 0; j < testInstances.numAttributes(); ++j) {
 edges[i][j] = false;
 }
 edges[iNode1][iNode1] = false;
 edges[iNode2][iNode2] = false;
 SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNull(oSepSet);
 }

 int x1 = 3;
 int x2 = 4;
 iNode1 = 0;
 iNode2 = 1;
 nCardinality = 2;
 edges[iNode1][iNode1] = false;
 edges[iNode2][iNode2] = false;
 edges[x1][x1] = false;
 edges[x2][x2] = false;
 edges[iNode1][x1] = true;
 edges[iNode1][x2] = true;
 edges[iNode2][x1] = true;
 edges[iNode2][x2] = true;
 sepsets[iNode1] = new SeparationSet();
 sepsets[iNode1].m_set[nCardinality] = -1;
 oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertEquals(2, oSepSet.m_set.length);
 assertTrue(oSepSet.m_set[0] < testInstances.numAttributes());
 assertTrue(oSepSet.m_set[1] < testInstances.numAttributes());
 assertFalse(oSepSet.m_set[0] == iNode1);
 assertFalse(oSepSet.m_set[0] == iNode2);
 assertFalse(oSepSet.m_set[1] == iNode1);
 assertFalse(oSepSet.m_set[1] == iNode2);
 }

 @Test
 public void testNext() throws Exception {
 int x = 0;
 int iNode1 = 0;
 int iNode2 = 1;int iNode3 = 2;
 boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 edges[i][i] = false;
 }
 edges[iNode1][iNode2] = true;
 edges[iNode1][iNode3] = true;
 edges[iNode2][iNode3] = true;
 edges[iNode3][iNode1] = true;
 edges[iNode3][iNode2] = true;
 int actual = icssa.next(x, iNode1, iNode2, edges);
 assertEquals(iNode3, actual);
 }

 @Test
 public void testCalcVeeNodes() throws Exception {
 boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
 boolean[][] arrows = new boolean[testInstances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[testInstances.numAttributes() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 icssa.calcVeeNodes(edges, arrows, sepsets);
 // Printing arrows matrix for verification
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 for (int j = 0; j < testInstances.numAttributes(); ++j) {
 System.out.print(arrows[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testCalcArcDirections() throws Exception {
 boolean[][] edges = new boolean[testInstances.numAttributes() + 1][];
 boolean[][] arrows = new boolean[testInstances.numAttributes() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[testInstances.numAttributes() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 icssa.calcVeeNodes(edges, arrows, sepsets);
 icssa.calcArcDirections(edges, arrows);
 // Printing arrows matrix for verification
 for (int i = 0; i < testInstances.numAttributes(); ++i) {
 for (int j = 0; j < testInstances.numAttributes(); ++j) {
 System.out.print(arrows[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testListOptions() throws Exception {
 List<Option> options = new ArrayList<Option>();
 options.add(new Option("\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)", "cardinality", 1, "-cardinality <num>"));
 assertEquals(options.size(), icssa.listOptions().size());
 }

 @Test
 public void testSetOptions() throws Exception {
 icssa.setOptions(new String[]{"-cardinality", "3"});
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetOptions() throws Exception {
 String[] options = icssa.getOptions();
 assertEquals(2, options.length);
 assertEquals("-cardinality", options[0]);
 assertEquals("3", options[1]);
 }

 @Test
 public void testMaxCardinalityTipText() throws Exception {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
 }
}