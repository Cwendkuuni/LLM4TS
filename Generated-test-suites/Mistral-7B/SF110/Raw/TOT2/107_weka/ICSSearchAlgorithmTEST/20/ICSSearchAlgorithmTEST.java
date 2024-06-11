package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NetUtils;
import weka.classifiers.bayes.net.InstancesBayes;
import weka.classifiers.bayes.net.SearchAlgorithm;
import weka.classifiers.bayes.net.search.CISearchAlgorithm;
import weka.classifiers.bayes.net.search.ICSSearchAlgorithm;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class ICSSearchAlgorithmTest {

  private Instances m_instances;
  private ICSSearchAlgorithm m_algorithm;
  private BayesNet m_net;

  @Before
  public void setUp() throws Exception {
    m_instances = new Instances(new FileReader("contact-lenses.arff"));
    m_instances.setClassIndex(m_instances.numAttributes() - 1);
    m_algorithm = new ICSSearchAlgorithm();
    m_algorithm.setMaxCardinality(3);
    m_net = new BayesNet();
    m_net.setSearchAlgorithm(m_algorithm);
  }

  @Test
  public void testName() {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
    icssa.search(m_net, instances);
    int nameReturned = icssa.name(0);
    String expected = "age";
    assertTrue(nameReturned.equals(expected));
  }

  @Test
  public void testMaxn() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
    icssa.search(m_net, instances);
    int maxnReturned = icssa.maxn();
    int expected = 9;
    assertTrue(maxnReturned == expected);
  }

  @Test
  public void testSetMaxCardinality() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    icssa.setMaxCardinality(5);
    int maxCardinality = icssa.getMaxCardinality();
    int expected = 5;
    assertTrue(maxCardinality == expected);
  }

  @Test
  public void testGetMaxCardinality() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    icssa.setMaxCardinality(3);
    int maxCardinality = icssa.getMaxCardinality();
    int expected = 3;
    assertTrue(maxCardinality == expected);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMaxCardinalityNegative() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    icssa.setMaxCardinality(-1);
  }

  @Test
  public void testSearch() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
    icssa.search(m_net, instances);
    InstancesBayes ib = (InstancesBayes) m_net.getStructure();
    int numEdges = 13;
    int numVertices = 10;
    int[] expectedEdges = { 0, 1, 0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6 };
    int[] actualEdges = new int[numVertices * numVertices];
    int edgeIndex = 0;
    for (int i = 0; i < numVertices; i++) {
      for (int j = 0; j < numVertices; j++) {
        if (icssa.edges[i][j]) {
          actualEdges[edgeIndex] = i * numVertices + j;
          edgeIndex++;
        }
      }
    }
    assertArrayEquals(expectedEdges, actualEdges);
  }

  @Test
  public void testCalcDependencyGraph() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    boolean[][] edges = new boolean[10][10];
    SeparationSet[][] sepsets = new SeparationSet[10][];
    icssa.calcDependencyGraph(edges, sepsets);
    // TODO: Add test cases for the printed debug output
  }

  @Test
  public void testExistSepSet() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    int iNode1 = 0;
    int iNode2 = 1;
    int nCardinality = 2;
    boolean[][] edges = new boolean[10][10];
    SeparationSet sepset = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
    int[] expectedSepset = { 3, 4 };
    assertTrue(sepset.m_set[0] == expectedSepset[0]);
    assertTrue(sepset.m_set[1] == expectedSepset[1]);
    assertTrue(sepset.m_set[2] == -1);
  }

  @Test
  public void testNext() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    int x = icssa.next(-1, 0, 1, new boolean[10][10]);
    int expected = 2;
    assertTrue(x == expected);
  }

  @Test
  public void testCalcVeeNodes() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    boolean[][] edges = new boolean[10][10];
    boolean[][] arrows = new boolean[10][10];
    SeparationSet[][] sepsets = new SeparationSet[10][];
    icssa.calcVeeNodes(edges, arrows, sepsets);
    // TODO: Add test cases for the printed debug output
  }

  @Test
  public void testCalcArcDirections() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    boolean[][] edges = new boolean[10][10];
    boolean[][] arrows = new boolean[10][10];
    icssa.calcArcDirections(edges, arrows);
    // TODO: Add test cases for the printed debug output
  }

  @Test
  public void testListOptions() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String[] options = icssa.listOptions();
    String expectedOption = "cardinality";
    assertTrue(NetUtils.isOptionPresent(options, expectedOption));
  }

  @Test
  public void testSetOptions() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String[] options = { "-U", "-cardinality", "5" };
    icssa.setOptions(options);
    int maxCardinality = icssa.getMaxCardinality();
    int expected = 5;
    assertTrue(maxCardinality == expected);
  }

  @Test
  public void testGetOptions() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String[] options = { "-U", "-cardinality", "3" };
    icssa.setOptions(options);
    String[] actualOptions = icssa.getOptions();
    String expectedOption1 = "-cardinality";
    String expectedOption2 = "3";
    assertTrue(NetUtils.isOptionPresent(actualOptions, expectedOption1));
    assertTrue(NetUtils.isOptionPresent(actualOptions, expectedOption2));
  }

  @Test
  public void testMaxCardinalityTipText() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String expected = "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
    String actual = icssa.maxCardinalityTipText();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testGlobalInfo() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String expected = "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
    String actual = icssa.globalInfo();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testGetRevision() throws Exception {
    ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    String expected = "$Revision: 8034 $";
    String actual = icssa.getRevision();
    assertTrue(expected.equals(actual));
  }

  @Test
  public void testMain() throws Exception {
    ICSSearchAlgorithm.main(new String[0]);
  }
}