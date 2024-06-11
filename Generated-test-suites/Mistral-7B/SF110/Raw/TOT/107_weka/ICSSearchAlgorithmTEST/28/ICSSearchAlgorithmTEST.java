import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances data;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 }

 @Test
 public void testSearchWithDefaultCardinality() throws Exception {
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 ICSSearchAlgorithm expectedICSSA = new ICSSearchAlgorithm(); // create an instance with default cardinality (2)
 expectedICSSA.setData(data);
 bn.buildClassifier(data);
 icssa.setData(data);
 icssa.search(bn, data);
 assertArrayEquals(expectedICSSA.getDependencyGraph(), icssa.getDependencyGraph());
 }

 @Test
 public void testSearchWithCustomCardinality() throws Exception {
 ICSSearchAlgorithm icssaWithCustomCardinality = new ICSSearchAlgorithm();
 icssaWithCustomCardinality.setData(data);
 icssaWithCustomCardinality.setMaxCardinality(3);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssaWithCustomCardinality);
 icssa.setData(data);
 icssa.search(bn, data);
 // Add custom assertions based on the expected output of the search algorithm with maxCardinality = 3
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeCardinality() throws Exception {
 ICSSearchAlgorithm icssaWithNegativeCardinality = new ICSSearchAlgorithm();
 icssaWithNegativeCardinality.setData(data);
 icssaWithNegativeCardinality.setMaxCardinality(-1);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssaWithNegativeCardinality);
 icssa.setData(data);
 icssa.search(bn, data);
 }

 @Test(expected = NullPointerException.class)
 public void testSearchWithNullData() {
 ICSSearchAlgorithm icssaWithNullData = new ICSSearchAlgorithm();
 icssaWithNullData.setData(null);
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssaWithNullData);
 icssa.setData(null);
 icssa.search(bn, null);
 }

 @Test
 public void testListOptions() throws Exception {
 BayesNet bn = new BayesNet();
 bn.setSearchAlgorithm(icssa);
 String options[] = icssa.listOptions().toArray(new String[0]);
 assertArrayEquals(new String[]{"\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)", "\tSearch algorithm for learning a Bayes Net from data."}, options);
 }

 @Test
 public void testSetOptionsWithDefaultCardinality() throws Exception {
 String[] options = new String[]{"-cardinality 2"};
 icssa.setOptions(options);
 assertEquals("2", icssa.getMaxCardinalityTipText());
 }

 @Test
 public void testSetOptionsWithCustomCardinality() throws Exception {
 String[] options = new String[]{"-cardinality 3"};
 icssa.setOptions(options);
 assertEquals("3", icssa.getMaxCardinalityTipText());
 }

 @Test(expected = NumberFormatException.class)
 public void testSetOptionsWithInvalidCardinality() throws Exception {
 String[] options = new String[]{"-cardinality A"};
 icssa.setOptions(options);
 }

 @Test
 public void testGetMaxCardinality() throws Exception {
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinalityTipText() throws Exception {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.getMaxCardinalityTipText());
 }

 // Add more tests as needed
}