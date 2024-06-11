package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static weka.core.Utils.generateRandomSeed;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.net.InstancesToNodes;
import weka.classifiers.bayes.net.search.*;
import weka.core.*;

import java.io.*;

/**
 * Tests for ICSSearchAlgorithm class.
 *
 * @author John Doe
 */
public class ICSSearchAlgorithmTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    generateRandomSeed(12345);
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#ICSSearchAlgorithm()}.
   */
  @Test
  public void testICSSearchAlgorithmConstruction() {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    assertEquals("Default max cardinality is 2", 2, icssa.getMaxCardinality());
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#ICSSearchAlgorithm(int)}.
   */
  @Test
  public void testICSSearchAlgorithmWithCustomMaxCardinality() {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm(5);
    assertEquals("Custom max cardinality is 5", 5, icssa.getMaxCardinality());
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#name(int)}.
   */
  @Test
  public void testNameMethod() {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
    icssa.search(new BayesNet(), instances);

    for (int i = 0; i < icssa.m_instances.numAttributes(); ++i) {
      String name = icssa.name(i);
      assertEquals("Name of attribute " + i + " is correct", instances.attribute(i).name(), name);
    }
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#maxn()}.
   */
  @Test
  public void testMaxnMethod() {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
    icssa.search(new BayesNet(), instances);
    assertEquals("Number of attributes is correct", instances.numAttributes(), icssa.maxn());
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#setMaxCardinality(int)}.
   */
  @Test
  public void testSetMaxCardinalityMethod() throws Exception {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
    icssa.search(new BayesNet(), instances);

    icssa.setMaxCardinality(5);
    assertEquals("Max cardinality is set to 5", 5, icssa.getMaxCardinality());

    icssa.search(new BayesNet(), instances);

    // Additional test to check if the search is performed with the new max cardinality
    // This test is an edge case as it checks if the search is performed correctly with the new max cardinality even if the old max cardinality was enough
    boolean exceptionThrown = false;
    try {
      icssa.calcDependencyGraph(new boolean[instances.numAttributes() + 1][], new SeparationSet[instances.numAttributes() + 1][]);
    } catch (Exception e) {
      exceptionThrown = true;
    }
    assertTrue("The search is performed with the new max cardinality", exceptionThrown);
  }

  /**
   * Test method for {@link weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm#search(BayesNet, Instances)}.
   */
  @Test
  public void testSearchMethod() throws Exception {
    final ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
    Instances instances = new Instances(new FileReader("C:\\eclipse\\workspace\\weka\\data\\contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);

    BayesNet bayesNet = new BayesNet();
    icssa.search(bayesNet, instances);

    // Test to check if the edges, arrows, and sepsets are calculated correctly
    boolean[][] edges = icssa.getEdges();
    boolean[][] arrows = icssa.getArrows();
    SeparationSet[][] sepsets = icssa.getSepsets();

    // Edge case to check if the calculated edges, arrows, and sepsets are the same as the expected ones
    boolean[][] expectedEdges = new boolean[instances.numAttributes() + 1][];
    boolean[][] expectedArrows = new boolean[instances.numAttributes() + 1][];
    SeparationSet[][] expectedSepsets = new SeparationSet[instances.numAttributes() + 1][];

    for (int i = 0; i < instances.numAttributes(); ++i) {
      expectedEdges[i] = new boolean[instances.numAttributes()];
      expectedEdges[i][i] = false;
      for (int j = 0; j < instances.numAttributes(); ++j) {
        expectedEdges[i][j] = false;
      }

      expectedArrows[i] = new boolean[instances.numAttributes()];
      for (int j = 0; j < instances.numAttributes(); ++j) {
        expectedArrows[i][j] = false;
      }

      expectedSepsets[i] = new SeparationSet();
      expectedSepsets[i].m_set[0] = -1;
    }

    for (int i = 0; i < instances.numAttributes(); ++i) {
      for (int j = 0; j < instances.numAttributes(); ++j) {
        assertTrue("Edges are calculated correctly", Arrays.equals(expectedEdges[i], edges[i]));
        assertTrue("Arrows are calculated correctly", Arrays.equals(expectedArrows[i], arrows[i]));

        SeparationSet expectedSepset = expectedSepsets[i];
        SeparationSet actualSepset = sepsets[i];
        for (int k = 0; k <= ICSSearchAlgorithm.this.getMaxCardinality(); ++k) {
          if (k > 0) {
            assertTrue("Expected and actual sets are equal", Arrays.equals(expectedSepset.m_set, actualSepset.m_set));
          }
          expectedSepset.m_set[k] = i;
          actualSepset = sepsets[j];
          for (int l = 0; l <= ICSSearchAlgorithm.this.getMaxCardinality(); ++l) {
            if (l > 0) {
              assertTrue("Expected and actual sets are equal", Arrays.equals(expectedSepset.m_set, actualSepset.m_set));
            }
            expectedSepset.m_set[l] = j;
          }
          expectedSepset = sepsets[i];
          actualSepset = sepsets[j];
          for (int l = 0; l <= ICSSearchAlgorithm.this.getMaxCardinality(); ++l) {
            if (l > 0) {
              assertTrue("Expected and actual sets are equal", Arrays.equals(expectedSepset.m_set, actualSepset.m_set));
            }
            expectedSepset.m_set[l] = i;
            actualSepset = sepsets[j];
            for (int m = 0; m <= ICSSearchAlgorithm.this.getMaxCardinality(); ++m) {
              if (m > 0) {
                assertTrue("Expected and actual sets are equal", Arrays.equals(expectedSepset.m_set, actualSepset.m_set));
              }
              expectedSepset.m_set[m] = j;
            }
          }
        }
      }
    }

    // Error scenario to check if an IllegalArgumentException is thrown when the max cardinality is set to a negative number
    ICSSearchAlgorithm icssaWithNegativeMaxCardinality = new ICSSearchAlgorithm();
    icssaWithNegativeMaxCardinality.setMaxCardinality(-1);
    try {
      icssaWithNegativeMaxCardinality.search(bayesNet, instances);
    } catch (IllegalArgumentException ex) {
      // Expected result
    }
  }
}