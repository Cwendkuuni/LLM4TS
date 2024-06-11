package weka.classifiers.bayes.net.search.ci;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.*;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances testInstances;
 private BayesNet testNet;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 testInstances = new Instances(new FileReader("data/contact-lenses.arff"));
 testInstances.setClassIndex(testInstances.numAttributes() - 1);
 testNet = new BayesNet();
 testNet.setSearchAlgorithm(icssa);
 testNet.buildClassifier(testInstances);
 }

 @Test
 public void testSearchWithMaxCardinalityZero() throws Exception {
 icssa.setMaxCardinality(0);
 BayesNet testNet2 = new BayesNet();
 testNet2.setSearchAlgorithm(icssa);
 Instances instances2 = new Instances(new FileReader("data/contact-lenses.arff"));
 instances2.setClassIndex(instances2.numAttributes() - 1);
 testNet2.buildClassifier(instances2);
 assertNotNull(testNet2);
 assertEquals(testNet2.toString(), testNet.toString());
 }

 @Test
 public void testSearchWithMaxCardinalityOne() throws Exception {
 icssa.setMaxCardinality(1);
 BayesNet testNet3 = new BayesNet();
 testNet3.setSearchAlgorithm(icssa);
 Instances instances3 = new Instances(new FileReader("data/contact-lenses.arff"));
 instances3.setClassIndex(instances3.numAttributes() - 1);
 testNet3.buildClassifier(instances3);
 assertNotNull(testNet3);
 assertFalse(testNet3.toString().equals(testNet.toString()));
 }

 @Test
 public void testSearchWithMaxCardinalityTwo() throws Exception {
 icssa.setMaxCardinality(2);
 BayesNet testNet4 = new BayesNet();
 testNet4.setSearchAlgorithm(icssa);
 Instances instances4 = new Instances(new FileReader("data/contact-lenses.arff"));
 instances4.setClassIndex(instances4.numAttributes() - 1);
 testNet4.buildClassifier(instances4);
 assertNotNull(testNet4);
 assertFalse(testNet4.toString().equals(testNet.toString()));
 }

 @Test
 public void testSearchWithMaxCardinalityThree() throws Exception {
 icssa.setMaxCardinality(3);
 BayesNet testNet5 = new BayesNet();
 testNet5.setSearchAlgorithm(icssa);
 Instances instances5 = new Instances(new FileReader("data/contact-lenses.arff"));
 instances5.setClassIndex(instances5.numAttributes() - 1);
 testNet5.buildClassifier(instances5);
 assertNotNull(testNet5);
 assertFalse(testNet5.toString().equals(testNet.toString()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeMaxCardinality() throws Exception {
 icssa.setMaxCardinality(-1);
 BayesNet testNet6 = new BayesNet();
 testNet6.setSearchAlgorithm(icssa);
 Instances instances6 = new Instances(new FileReader("data/contact-lenses.arff"));
 instances6.setClassIndex(instances6.numAttributes() - 1);
 testNet6.buildClassifier(instances6);
 fail("Should throw an IllegalArgumentException");
 }
}