package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NetUtils;
import weka.classifiers.bayes.net.BayesNetFactory;
import weka.classifiers.bayes.net.InstancesToBayesNet;
import weka.classifiers.bayes.net.search.CISearchAlgorithm;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class ICSSearchAlgorithmTest {

  private Instances m_instances;
  private ICSSearchAlgorithm m_algorithm;
  private BayesNet m_bayesNet;

  @Before
  public void setUp() throws Exception {
    m_instances = new Instances(new FileReader("contact-lenses.arff"));
    m_instances.setClassIndex(m_instances.numAttributes() - 1);
    m_algorithm = new ICSSearchAlgorithm();
    m_bayesNet = new BayesNet();
    m_bayesNet.setSearchAlgorithm(m_algorithm);
  }

  @Test
  public void testSearchAlgorithmWithDefaultCardinality() throws Exception {
    InstancesToBayesNet itbn = new InstancesToBayesNet();
    m_bayesNet = itbn.buildNetFromInstances(m_instances, 0.9, 0.9, null);
    m_algorithm.search(m_bayesNet, m_instances);
    assertEquals(12, m_bayesNet.numNodes());
    // Add check for edges, arrows, and parent sets here
  }

  @Test
  public void testSearchAlgorithmWithSpecifiedCardinality() throws Exception {
    m_algorithm.setMaxCardinality(3);
    InstancesToBayesNet itbn = new InstancesToBayesNet();
    m_bayesNet = itbn.buildNetFromInstances(m_instances, 0.9, 0.9, null);
    m_algorithm.search(m_bayesNet, m_instances);
    assertEquals(12, m_bayesNet.numNodes());
    // Add check for edges, arrows, and parent sets here
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMaxCardinality() throws Exception {
    m_algorithm.setMaxCardinality(-1);
    InstancesToBayesNet itbn = new InstancesToBayesNet();
    m_bayesNet = itbn.buildNetFromInstances(m_instances, 0.9, 0.9, null);
    m_algorithm.search(m_bayesNet, m_instances);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInstances() throws Exception {
    m_algorithm.setMaxCardinality(3);
    m_instances = null;
    InstancesToBayesNet itbn = new InstancesToBayesNet();
    m_bayesNet = itbn.buildNetFromInstances(m_instances, 0.9, 0.9, null);
    m_algorithm.search(m_bayesNet, m_instances);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBayesNet() throws Exception {
    m_algorithm.setMaxCardinality(3);
    m_bayesNet = null;
    m_search = m_bayesNet;
    m_instances = m_instances;
    m_algorithm.search(m_bayesNet, m_instances);
  }

  @Test(expected = Exception.class)
  public void testEmptyInstances() throws Exception {
    m_algorithm.setMaxCardinality(3);
    m_instances = new Instances(new ByteArrayInputStream(new byte[0]));
    m_bayesNet = new BayesNet();
    m_bayesNet.setSearchAlgorithm(m_algorithm);
    m_algorithm.search(m_bayesNet, m_instances);
  }

  @Test(expected = Exception.class)
  public void testNullInstancesClassIndex() throws Exception {
    m_algorithm.setMaxCardinality(3);
    m_instances = new Instances(new FileReader("contact-lenses.arff"));
    m_instances.setClassIndex(-1);
    m_bayesNet = new BayesNet();
    m_bayesNet.setSearchAlgorithm(m_algorithm);
    m_algorithm.search(m_bayesNet, m_instances);
  }
}