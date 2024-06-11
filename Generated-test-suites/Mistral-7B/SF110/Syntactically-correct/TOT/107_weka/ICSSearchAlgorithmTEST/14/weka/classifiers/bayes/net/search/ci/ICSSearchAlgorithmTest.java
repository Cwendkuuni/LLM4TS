package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static weka.core.Utils.toIntArray;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.core.*;
import weka.classifiers.bayes.net.search.*;

public class ICSSearchAlgorithmTest {

  private Instances m_instances;
  private ICSSearchAlgorithm m_algorithm;

  @Before
  public void setUp() throws Exception {
    Instances data = new Instances(new FileReader("contact-lenses.arff"));
    data.setClassIndex(data.numAttributes() - 1);
    m_instances = data;
    m_algorithm = new ICSSearchAlgorithm();
  }

  @Test
  public void testSearch() throws Exception {
    BayesNet bayesNet = new BayesNet();
    bayesNet.setSearchAlgorithm(m_algorithm);
    m_algorithm.search(bayesNet, m_instances);

    // Check number of edges
    int numEdges = 0;
    for (int i = 0; i < bayesNet.numNodes(); i++) {
      for (int j = 0; j < bayesNet.numNodes(); j++) {
        if (bayesNet.edgeExists(i, j)) {
          numEdges++;
        }
      }
    }
    assertEquals(m_instances.numAttributes() * (m_instances.numAttributes() - 1) / 2, numEdges);

    // Check dependency graph
    boolean[][] edges = m_algorithm.getEdges();
    for (int i = 0; i < m_instances.numAttributes(); i++) {
      for (int j = 0; j < m_instances.numAttributes(); j++) {
        if (i != j) {
          assertTrue(edges[i][j] || edges[j][i]);
          if (edges[i][j]) {
            assertTrue(m_algorithm.existsSepSet(i, j, 1, edges).m_set[0] == -1 || m_algorithm.isConditionalIndependent(j, i, toIntArray(m_algorithm.existsSepSet(i, j, 1, edges).m_set), 1));
          }
          if (edges[j][i]) {
            assertTrue(m_algorithm.existsSepSet(j, i, 1, edges).m_set[0] == -1 || m_algorithm.isConditionalIndependent(i, j, toIntArray(m_algorithm.existsSepSet(j, i, 1, edges).m_set), 1));
          }
        }
      }
    }
  }

  @Test
  public void testSetMaxCardinality() {
    m_algorithm = new ICSSearchAlgorithm();
    assertEquals(2, m_algorithm.getMaxCardinality());

    m_algorithm.setMaxCardinality(3);
    assertEquals(3, m_algorithm.getMaxCardinality());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxCardinalityNegative() {
    m_algorithm = new ICSSearchAlgorithm();
    m_algorithm.setMaxCardinality(-1);
  }
}