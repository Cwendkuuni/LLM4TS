package weka.classifiers.bayes.net.search.ci;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private BayesNet bayesNet;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 bayesNet = new BayesNet();
 instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.setInstances(instances);
 icssa.setMaxCardinality(3);
 bayesNet.setSearchAlgorithm(icssa);
 }

 @Test
 public void testNameMethod() {
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 int iAttribute = 2;
 String expectedName = "age";
 String actualName = icssa.name(iAttribute);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testMaxnMethod() throws Exception {
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 int expectedNumAttributes = instances.numAttributes();
 int actualNumAttributes = icssa.maxn();
 assertEquals(expectedNumAttributes, actualNumAttributes);
 }

 @Test
 public void testSetMaxCardinalityMethod() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(5);
 int expectedMaxCardinality = 5;
 int actualMaxCardinality = icssa.getMaxCardinality();
 assertEquals(expectedMaxCardinality, actualMaxCardinality);
 }

 @Test
 public void testSearchMethodThrowsExceptionWhenInstancesIsNull() throws Exception {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 try {
 icssa.search(new BayesNet(), null);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSearchMethodThrowsExceptionWhenInstancesHaveNoAttributes() throws Exception {
 Instances instances = new Instances(new ByteArrayInputStream(new byte[0]));
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 try {
 icssa.search(new BayesNet(), instances);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSearchMethodThrowsExceptionWhenInstancesHaveNoClass() throws Exception {
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(-1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 try {
 icssa.search(new BayesNet(), instances);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSearchMethod() throws Exception {
 Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setInstances(instances);
 BayesNet bayesNet = new BayesNet();
 icssa.search(bayesNet, instances);
 // Add assertions here to check the structure and contents of the BayesNet object
 }

 @Test
 public void testCalcDependencyGraphMethod() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[3][3];
 SeparationSet[][] sepsets = new SeparationSet[3][];
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 edges[iNode1][iNode2] = true;
 }
 }
 icssa.calcDependencyGraph(edges, sepsets);
 // Add assertions here to check the structure and contents of the edges and sepsets arrays
 }

 @Test
 public void testExistsSepSetMethod() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[3][3];
 SeparationSet[][] sepsets = new SeparationSet[3][];
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 edges[iNode1][iNode2] = true;
 }
 }
 SeparationSet expectedSepSet = new SeparationSet();
 expectedSepSet.m_set[0] = 1;
 expectedSepSet.m_set[1] = 2;
 int iNode3 = 2;
 int nCardinality = 2;
 SeparationSet actualSepSet = icssa.existsSepSet(0, 1, nCardinality, edges);
 assertEquals(expectedSepSet, actualSepSet);
 }

 @Test
 public void testNextMethod() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 int x = 0;
 int iNode1 = 0;
 int iNode2 = 1;
 boolean[][] edges = new boolean[2][2];
 edges[iNode1][iNode2] = true;
 SeparationSet[][] sepsets = new SeparationSet[2][];
 SeparationSet expectedSepSet = new SeparationSet();
 expectedSepSet.m_set[0] = 2;
 SeparationSet actualSepSet = icssa.existsSepSet(iNode1, iNode2, 1, edges);
 assertEquals(expectedSepSet, actualSepSet);
 }

 @Test
 public void testCalcVeeNodesMethod() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[3][3];
 boolean[][] arrows = new boolean[3][3];
 SeparationSet[][] sepsets = new SeparationSet[3][];
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 edges[iNode1][iNode2] = true;
 }
 }
 icssa.calcVeeNodes(edges, arrows, sepsets);
 // Add assertions here to check the structure and contents of the arrows array
 }

 @Test
 public void testCalcArcDirectionsMethod() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[3][3];
 boolean[][] arrows = new boolean[3][3];
 SeparationSet[][] sepsets = new SeparationSet[3][];
 for (int iNode1 = 0; iNode1 < 3; ++iNode1) {
 for (int iNode2 = 0; iNode2 < 3; ++iNode2) {
 edges[iNode1][iNode2] = true;
 }
 }
 icssa.calcArcDirections(edges, arrows);
 // Add assertions here to check the structure and contents of the arrows array
 }
}