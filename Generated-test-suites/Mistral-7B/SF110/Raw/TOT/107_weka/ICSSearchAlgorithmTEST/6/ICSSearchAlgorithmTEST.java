package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm algorithm;
 private BayesNet bayesNet;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 algorithm = new ICSSearchAlgorithm();
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet.setSearchAlgorithm(algorithm);
 }

 @Test
 public void testName() {
 final int iAttribute = 0;
 assertEquals("Age", algorithm.name(iAttribute));
 }

 @Test
 public void testMaxn() {
 final int expected = 9;
 assertEquals(expected, algorithm.maxn());
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
 public void testSearchNullArguments() {
 try {
 algorithm.search(null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 algorithm.search(bayesNet, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testSearchSuccess() throws Exception {
 algorithm.search(bayesNet, instances);
 assertNotNull(bayesNet.structure());
 }

 @Test
 public void testSearchFailure() throws Exception {
 final Instances emptyInstances = new Instances(new ByteArrayInputStream(new byte[0]));
 try {
 algorithm.search(bayesNet, emptyInstances);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testExistsSepSetNullArguments() {
 try {
 algorithm.existsSepSet(0, 1, -1, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testExistsSepSetSuccess() {
 final int iNode1 = 0;
 final int iNode2 = 1;
 final int nCardinality = 2;
 final boolean[][] edges = new boolean[3][3];
 edges[iNode1][iNode2] = true;
 edges[iNode2][iNode1] = true;
 final SeparationSet expectedSepSet = new SeparationSet();
 expectedSepSet.m_set[0] = 2;
 expectedSepSet.m_set[1] = 3;
 final SeparationSet actualSepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertEquals(expectedSepSet, actualSepSet);
}

 @Test
 public void testExistsSepSetFailure() {
 final int iNode1 = 0;
 final int iNode2 = 1;
 final int nCardinality = 2;
 final boolean[][] edges = new boolean[3][3];
 edges[iNode1][iNode2] = true;
 edges[iNode2][iNode1] = true;
 try {
 algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testNextNullArguments() {
 try {
 algorithm.next(-1, iNode1, iNode2, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testNextSuccess() {
 final int x = 0;
 final int iNode1 = 0;
 final int iNode2 = 1;
 final boolean[][] edges = new boolean[3][3];
 edges[iNode1][iNode2] = true;
 edges[iNode2][iNode1] = true;
 final int actual = algorithm.next(x, iNode1, iNode2, edges);
 assertEquals(2, actual);
}

 @Test
 public void testCalcDependencyGraphSuccess() {
 final boolean[][] edges = new boolean[3][3];
 final SeparationSet[][] sepsets = new SeparationSet[3][3];
 algorithm.calcDependencyGraph(edges, sepsets);
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 if (iNode1 != iNode2 && edges[iNode1][iNode2]) {
 assertFalse(sepsets[iNode1][iNode2].contains(iNode2));
 assertFalse(sepsets[iNode2][iNode1].contains(iNode1));
 }
 }
 }
}

 @Test
 public void testCalcDependencyGraphFailure() {
 final boolean[][] edges = new boolean[3][3];
 final SeparationSet[][] sepsets = new SeparationSet[3][3];
 try {
 algorithm.calcDependencyGraph(null, sepsets);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 algorithm.calcDependencyGraph(edges, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testCalcVeeNodesSuccess() {
 final boolean[][] edges = new boolean[3][3];
 final boolean[][] arrows = new boolean[3][3];
 final SeparationSet[][] sepsets = new SeparationSet[3][3];
 algorithm.calcVeeNodes(edges, arrows, sepsets);
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 if (iNode1 != iNode2 && arrows[iNode1][iNode2]) {
 assertTrue(arrows[iNode2][iNode1]);
 }
 }
 }
}

 @Test
 public void testCalcVeeNodesFailure() {
 final boolean[][] edges = new boolean[3][3];
 final boolean[][] arrows = new boolean[3][3];
 final SeparationSet[][] sepsets = new SeparationSet[3][3];
 try {
 algorithm.calcVeeNodes(null, arrows, sepsets);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 algorithm.calcVeeNodes(edges, null, sepsets);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 algorithm.calcVeeNodes(edges, arrows, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testCalcArcDirectionsSuccess() {
 final boolean[][] edges = new boolean[3][3];
 final boolean[][] arrows = new boolean[3][3];
 algorithm.calcArcDirections(edges, arrows);
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 if (iNode1 != iNode2 && arrows[iNode1][iNode2]) {
 assertFalse(arrows[iNode2][iNode1]);
 }
 }
 }
}

 @Test
 public void testCalcArcDirectionsFailure() {
 final boolean[][] edges = new boolean[3][3];
 final boolean[][] arrows = new boolean[3][3];
 try {
 algorithm.calcArcDirections(null, arrows);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 algorithm.calcArcDirections(edges, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testListOptionsSuccess() {
 final List<Option> options = new ArrayList<>();
 options.addAll(Collections.list(algorithm.listOptions()));
 assertEquals(2, options.size());
}

 @Test
 public void testSetOptionsSuccess() throws Exception {
 algorithm.setOptions(new String[]{"-cardinality 3"});
 assertEquals(3, algorithm.getMaxCardinality());
}

 @Test
 public void testSetOptionsFailure() throws Exception {
 try {
 algorithm.setOptions(new String[]{"-cardinality 0"});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testGetOptionsSuccess() {
 final List<String> options = new ArrayList<>();
 options.addAll(Arrays.asList(algorithm.getOptions()));
 assertEquals(3, options.size());
 options.remove(2);
 assertEquals("-cardinality", options.get(1));
 assertEquals("2", options.get(2));
}

 @Test
 public void testGetOptionsFailure() {
 try {
 algorithm.getOptions();
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
}

 @Test
 public void testMaxCardinalityTipTextSuccess() {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", algorithm.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfoSuccess() {
 assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", algorithm.globalInfo());
 }
}