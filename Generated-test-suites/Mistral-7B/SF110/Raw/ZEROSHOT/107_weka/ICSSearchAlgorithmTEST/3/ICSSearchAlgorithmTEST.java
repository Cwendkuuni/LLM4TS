package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;

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
 private Instances instances;
 private BayesNet bayesNet;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 }

 @Test
 public void testSearch() throws Exception {
 icssa.search(bayesNet, instances);
 assertNotNull(bayesNet.getClassifier());
 }

 @Test
 public void testName() {
 assertEquals("age", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 assertEquals(8, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testCalcDependencyGraph() throws Exception {
 final boolean[][] edges = new boolean[icssa.maxn() + 1][];
 final SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 // Printing the dependency graph for verification
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 System.out.print(edges[i][j] + " ");
 }
 System.out.println();
 }
 }

 // Add more test methods here to test other methods in ICSSearchAlgorithm

}