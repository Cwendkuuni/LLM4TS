package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
 icssa.search(bayesNet, instances);
 }

 @Test
 public void testName() {
 for (int i = 0; i < icssa.maxn(); ++i) {
 String expected = "attribute" + i;
 String actual = icssa.name(i);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testMaxn() {
 int expected = instances.numAttributes();
 int actual = icssa.maxn();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(3);
 int actual = icssa.getMaxCardinality();
 assertEquals(3, actual);
 }

 @Test
 public void testGetMaxCardinality() {
 int expected = icssa.getMaxCardinality();
 int actual = icssa.getMaxCardinality();
 assertEquals(expected, actual);
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 boolean[][] edges = icssa.getEdges();
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 assertEquals(false, edges[i][j]);
 }
 }
 for (int i = 0; i < icssa.maxn(); ++i) {
 edges[i][i] = false;
 }
 }

 @Test
 public void testExistsSepSet() throws Exception {
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 2;
 boolean[][] edges = icssa.getEdges();
 SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertNull(sepSet);
 }

 @Test
 public void testNext() {
 int x = 0;
 int iNode1 = 0;
 int iNode2 = 1;
 boolean[][] edges = icssa.getEdges();
 int actual = icssa.next(x, iNode1, iNode2, edges);
 assertEquals(2, actual);
 }

 @Test
 public void testCalcVeeNodes() throws Exception {
 boolean[][] arrows = icssa.getArrows();
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 assertEquals(false, arrows[i][j]);
 }
 }
 }

 @Test
 public void testCalcArcDirections() throws Exception {
 boolean[][] edges = icssa.getEdges();
 boolean[][] arrows = icssa.getArrows();
 assertFalse(arrows[0][1]);
 assertFalse(arrows[1][0]);
 assertFalse(arrows[0][2]);
 assertFalse(arrows[2][0]);
 assertFalse(arrows[1][2]);
 assertFalse(arrows[2][1]);
 assertTrue(arrows[0][2]);
 assertTrue(arrows[2][0]);
 assertTrue(arrows[1][2]);
 assertTrue(arrows[2][1]);
 }
}