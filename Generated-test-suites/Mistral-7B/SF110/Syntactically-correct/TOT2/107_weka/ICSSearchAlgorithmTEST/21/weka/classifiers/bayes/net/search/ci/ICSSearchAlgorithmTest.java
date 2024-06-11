import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.util.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private BayesNet bnet;
 private Instances data;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 bnet = new BayesNet();
 data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 bnet.setSearchAlgorithm(icssa);
 icssa.setMaxCardinality(3);
 }

 @Test
 public void testName() {
 Instances instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 assertEquals("pupilSize", iCSA.name(0));
 }

 @Test
 public void testMaxn() {
 Instances instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 assertEquals(13, iCSA.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 iCSA.setMaxCardinality(5);
 assertEquals(5, iCSA.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 iCSA.setMaxCardinality(3);
 assertEquals(3, iCSA.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 iCSA.setMaxCardinality(-1);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testSearch() throws Exception {
 Instances instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 BayesNet bnet = new BayesNet();
 bnet.setSearchAlgorithm(iCSA);
 iCSA.setMaxCardinality(3);
 bnet.buildClassifier(instances);
 assertNotNull(bnet);
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullInstances() throws Exception {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 BayesNet bnet = new BayesNet();
 bnet.setSearchAlgorithm(iCSA);
 iCSA.setMaxCardinality(3);
 bnet.buildClassifier(null);
 fail("Should throw an Exception");
 }

 @Test(expected = Exception.class)
 public void testSearchWithNullBayesNet() throws Exception {
 Instances instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 iCSA.setMaxCardinality(3);
 BayesNet bnet = null;
 bnet.setSearchAlgorithm(iCSA);
 bnet.buildClassifier(instances);
 fail("Should throw an Exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithEmptyInstances() throws Exception {
 Instances instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 iCSA.setMaxCardinality(3);
 Instances emptyInstances = new Instances(new Instances(instances, 0));
 BayesNet bnet = new BayesNet();
 bnet.setSearchAlgorithm(iCSA);
 bnet.buildClassifier(emptyInstances);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[14][];
 SeparationSet[][] sepsets = new SeparationSet[14][];
 for (int i = 0; i < 14; ++i) {
 edges[i] = new boolean[14];
 sepsets[i] = new SeparationSet[14];
 }
 iCSA.calcDependencyGraph(edges, sepsets);
 for (int i = 0; i < 14; ++i) {
 for (int j = 0; j < 14; ++j) {
 System.out.print(edges[i][j] + " ");
 }
 System.out.println();
 }
 }

 @Test
 public void testExistsSepSet() {
 ICSSearchAlgorithm iCSA = new ICSSearchAlgorithm();
 boolean[][] edges = {
 {true, true, true, true, true, true, true, true, true, true, true, true, true},
 {true, false, false, false, false, false, false, false, false, false, false, false, false},
 {false, true, false, false, false, false, false, false, false, false, false, false, false},
 {false, false, true, false, false, false, false, false, false, false, false, false, false},
 {false, false, false, true, false, false, false, false, false, false, false, false, false},
 {false, false, false, false, true, false, false, false, false, false, false, false, false},
 {false, false, false, false, false, true, false, false, false, false, false, false, false},
 {false, false, false, false, false, false, true, false, false, false, false, false, false},
 {false, false, false, false, false, false, false, true, false, false, false, false, false},
 {false, false, false, false, false, false, false, false, true, false, false, false, false},
 {false, false, false, false, false, false, false, false, false, true, false, false, false},
 {false, false, false, false, false, false, false, false, false, false, true, false, false},
 {false, false, false, false, false, false, false, false, false, false, false, true, false},
 {false, false, false, false, false, false, false, false, false, false, false, false, true}
 };
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 3;
 SeparationSet sepSet = iCSA.existsSepSet(iNode1, iNode2, nCardinality, edges);
 if (sepSet != null) {
 for (int i = 0; i < nCardinality; ++i) {
 System.out.print(sepSet.m_set[i] + " ");
 }
 System.out.println();
 }
 else {
 System.out.println("Null SeparationSet");
 }
 }

 @Test
 public void testNext() {
 int x = 0;
 int iNode1 = 0;
 int iNode2 = 1;
 boolean[][] edges = {
 {false, true, false, false, false, false, false, false, false, false, false, false, false},
 {true, false, false, false, false, false, false, false, false, false, false, false, false},
 {false, false, true, false, false, false, false, false, false, false, false, false, false},
 {false, false, false, true, false, false, false, false, false, false, false, false, false},
 {false, false, false, false, true, false, false, false, false, false, false, false, false},
 {false, false, false, false, false, true, false, false, false, false, false, false, false},
 {false, false, false, false, false, false, true, false, false, false, false, false, false},
 {false, false, false, false, false, false, false, true, false, false, false, false, false},
 {false, false, false, false, false, false, false, false, true, false, false, false, false},
 {false, false, false, false, false, false, false, false, false, true, false, false, false},
 {false, false, false, false, false, false, false, false, false, false, true, false, false},
 {false, false, false, false, false, false, false, false, false, false, false, true, false},
 {false, false, false, false, false, false, false, false, false, false, false, false, true}
 };
 int expected = 10;
 int actual = iCSA.next(x, iNode1, iNode2, edges);
 assertEquals(expected, actual);
 }
}