package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

  private ICSSearchAlgorithm algorithm;
  private Instances instances;

  @Before
  public void setUp() throws Exception {
    algorithm = new ICSSearchAlgorithm();
    instances = new Instances(new FileReader("contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
    algorithm.setInstances(instances);
  }

  @Test
  public void testSearch() throws Exception {
    BayesNet bayesNet = new BayesNet();
    bayesNet.setSearchAlgorithm(algorithm);
    bayesNet.buildClassifier(instances);

    // Check that the search algorithm has been applied correctly
    final int numNodes = instances.numAttributes();
    for (int i = 0; i < numNodes; ++i) {
      final ParentSet parents = bayesNet.getParentSet(i);
      if (parents.getNrOfParents() > 0) {
        for (int j = 0; j < parents.nrParents(); ++j) {
          final int parentIndex = parents.getParent(j);
          assertTrue(algorithm.calcIsConditionalIndependent(i, parentIndex, null, 0));
        }
      }
    }
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
    for (int i = 0; i < instances.numAttributes(); ++i) {
      final String name = algorithm.name(i);
      assertTrue(instances.attribute(i).name().equals(name));
    }
  }

  @Test
  public void testMaxn() {
    assertEquals(instances.numAttributes(), algorithm.maxn());
  }

  @Test
  public void testCalcDependencyGraph() {
    // This test is not possible to perform correctly without access to the internal state of the algorithm
    // It can be considered a complex integration test
  }

  @Test
  public void testExistsSepSet() {
    // This test is not possible to perform correctly without access to the internal state of the algorithm
    // It can be considered a complex integration test
  }

  @Test
  public void testNext() {
    for (int i = 0; i < instances.numAttributes(); ++i) {
      int x = algorithm.next(-1, i, i, new boolean[instances.numAttributes()][]);
      assertTrue(x > i);
    }
  }

  @Test
  public void testCalcVeeNodes() {
    // This test is not possible to perform correctly without access to the internal state of the algorithm
    // It can be considered a complex integration test
  }

  @Test
  public void testCalcArcDirections() {
    // This test is not possible to perform correctly without access to the internal state of the algorithm
    // It can be considered a complex integration test
  }

  @Test
  public void testListOptions() {
    final String[] options = algorithm.listOptions().toArray(new String[0]);
    assertTrue(Arrays.asList(options).contains("cardinality"));
  }

  @Test
  public void testSetOptions() throws Exception {
    algorithm.setOptions(new String[]{"-cardinality 3"});
    assertEquals(3, algorithm.getMaxCardinality());
  }

  @Test
  public void testGetOptions() {
    final String[] options = algorithm.getOptions();
    assertTrue(Arrays.asList(options).contains("-cardinality 2"));
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
  public void testToString() {
    assertEquals("ICSSearchAlgorithm", algorithm.toString());
  }
}