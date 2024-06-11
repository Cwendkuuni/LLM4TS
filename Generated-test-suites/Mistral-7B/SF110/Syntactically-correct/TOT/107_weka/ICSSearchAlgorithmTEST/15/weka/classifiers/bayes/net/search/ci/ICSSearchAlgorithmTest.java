package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static weka.core.Utils.getClasspath;
import static weka.core.Utils.loadFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

  private ICSSearchAlgorithm icssa;
  private Instances instances;

  @Before
  public void setUp() throws Exception {
    icssa = new ICSSearchAlgorithm();
    instances = loadFile(new File(getClasspath() + "data/contact-lenses.arff"));
    instances.setClassIndex(instances.numAttributes() - 1);
  }

  // Test initial state of ICSSearchAlgorithm
  @Test
  public void testInitialState() {
    assertEquals(2, icssa.getMaxCardinality());
  }

  // Test setting and getting maxCardinality
  @Test
  public void testSetAndGetMaxCardinality() {
    icssa.setMaxCardinality(5);
    assertEquals(5, icssa.getMaxCardinality());
  }

  // Test search method with default maxCardinality
  @Test
  public void testSearchDefault() throws Exception {
    BayesNet bayesNet = new BayesNet();
    Instances instancesCopy = new Instances(instances);
    icssa.search(bayesNet, instancesCopy);
    // Add assertions based on expected structure of BayesNet
  }

  // Test search method with custom maxCardinality
  @Test
  public void testSearchCustom() throws Exception {
    icssa.setMaxCardinality(3);
    BayesNet bayesNet = new BayesNet();
    Instances instancesCopy = new Instances(instances);
    icssa.search(bayesNet, instancesCopy);
    // Add assertions based on expected structure of BayesNet with maxCardinality=3
  }

  // Test exception when maxCardinality is negative
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMaxCardinality() {
    icssa.setMaxCardinality(-1);
  }

  // Test exception when maxCardinality is greater than number of attributes
  @Test(expected = IllegalArgumentException.class)
  public void testMaxCardinalityGreaterThanNumberOfAttributes() {
    instances = loadFile(new File(getClasspath() + "data/iris.arff"));
    icssa.setMaxCardinality(instances.numAttributes() + 1);
  }
}