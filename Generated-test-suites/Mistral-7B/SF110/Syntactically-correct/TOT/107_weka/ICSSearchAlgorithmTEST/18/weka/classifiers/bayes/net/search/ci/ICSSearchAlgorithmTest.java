package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import junit.framework.*;
import junit.framework.TestCase;

public class ICSSearchAlgorithmTest extends TestCase {

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
 icssa.search(bayesNet, instances);
 }

 @Test
 public void testSearch() throws Exception {
 // Test when dependency graph is empty
 ICSSearchAlgorithm emptySearch = new ICSSearchAlgorithm();
 BayesNet emptyBayesNet = new BayesNet();
 Instances emptyInstances = new Instances(new FileReader("data/empty.arff"));
 emptyInstances.setClassIndex(emptyInstances.numAttributes() - 1);
 emptySearch.setInstances(emptyInstances);
 emptySearch.search(emptyBayesNet, emptyInstances);
 assertTrue(emptyBayesNet.structureIsEmpty());

 // Test when dependency graph contains edges
 ICSSearchAlgorithm testSearch = new ICSSearchAlgorithm();
 Instances testInstances = new Instances(new FileReader("data/contact-lenses.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 testSearch.setInstances(testInstances);
 testSearch.setMaxCardinality(2);
 testSearch.search(bayesNet, testInstances);

 // Check if edges exist in the expected directions
 for (int i = 0; i < icssa.maxn(); ++i) {
 for (int j = 0; j < icssa.maxn(); ++j) {
 if (icssa.arrows[i][j]) {
 assertEquals("Arrow from node " + icssa.name(i) + " to node " + icssa.name(j) + " should be present", true, bayesNet.isDirectedEdge(i, j));
 assertEquals("Arrow from node " + icssa.name(j) + " to node " + icssa.name(i) + " should not be present", false, bayesNet.isDirectedEdge(j, i));
 }
 }
 }
 }

 @Test
 public void testSetMaxCardinality() {
 ICSSearchAlgorithm testICSSA = new ICSSearchAlgorithm();
 testICSSA.setMaxCardinality(3);
 assertEquals("Max cardinality set to 3", 3, testICSSA.getMaxCardinality());
 }

 @After
 public void tearDown() throws Exception {
 bayesNet = null;
 instances = null;
 icssa = null;
 }
}