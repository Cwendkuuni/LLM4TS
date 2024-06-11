import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

 @Test
 public void testNameMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/test.arff"));
 int attributeIndex = 0;
 String expectedName = "attributeName";
 String actualName = algorithm.name(attributeIndex);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testMaxnMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/test.arff"));
 int expectedNumberOfAttributes = 5;
 int actualNumberOfAttributes = algorithm.maxn();
 assertEquals(expectedNumberOfAttributes, actualNumberOfAttributes);
 }

 @Test
 public void testSetMaxCardinalityMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 algorithm.setMaxCardinality(3);
 int expectedMaxCardinality = 3;
 int actualMaxCardinality = algorithm.getMaxCardinality();
 assertEquals(expectedMaxCardinality, actualMaxCardinality);
 }

 @Test
 public void testSearchMethodWithValidArguments() throws Exception {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 BayesNet bayesNet = new BayesNet();
 Instances instances = new Instances(new FileReader("data/test.arff"));
 algorithm.search(bayesNet, instances);
 // Perform assertions on the BayesNet object here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchMethodWithNullInstances() throws Exception {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 BayesNet bayesNet = new BayesNet();
 algorithm.search(bayesNet, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchMethodWithNullBayesNet() throws Exception {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/test.arff"));
 algorithm.search(null, instances);
 }

 @Test
 public void testCalcDependencyGraphMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 algorithm.calcDependencyGraph(edges, sepsets);
 // Perform assertions on the edges and sepsets arrays here
 }

 @Test
 public void testExistsSepSetMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 int iNode1 = 0;
 int iNode2 = 1;
 int nCardinality = 2;
 boolean[][] edges = new boolean[5][];
 SeparationSet expectedSepSet = new SeparationSet();
 expectedSepSet.m_set[0] = 2;
 expectedSepSet.m_set[1] = 3;
 SeparationSet actualSepSet = algorithm.existsSepSet(iNode1, iNode2, nCardinality, edges);
 assertArrayEquals(expectedSepSet.m_set, actualSepSet.m_set);
 }

 @Test
 public void testNextMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 int x = 0;
 int iNode1 = 0;
 int iNode2 = 1;
 boolean[][] edges = new boolean[5][];
 int expectedNextNode = 3;
 int actualNextNode = algorithm.next(x, iNode1, iNode2, edges);
 assertEquals(expectedNextNode, actualNextNode);
 }

 @Test
 public void testCalcVeeNodesMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 boolean[][] arrows = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 algorithm.calcVeeNodes(edges, arrows, sepsets);
 // Perform assertions on the arrows array here
 }

 @Test
 public void testCalcArcDirectionsMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[5][];
 boolean[][] arrows = new boolean[5][];
 SeparationSet[][] sepsets = new SeparationSet[5][];
 algorithm.calcArcDirections(edges, arrows);
 // Perform assertions on the arrows array here
 }

 @Test
 public void testListOptionsMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 String[] options = algorithm.listOptions().toArray(new String[algorithm.listOptions().size()]);
 String expectedOption = "cardinality";
 int indexOfExpectedOption = -1;
 for (int i = 0; i < options.length; ++i) {
 if (options[i].startsWith(expectedOption)) {
 indexOfExpectedOption = i;
 break;
 }
 }
 assertTrue(indexOfExpectedOption >= 0);
 }

 @Test
 public void testSetOptionsMethod() throws Exception {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 String[] options = new String[]{"-cardinality", "3"};
 algorithm.setOptions(options);
 int expectedMaxCardinality = 3;
 int actualMaxCardinality = algorithm.getMaxCardinality();
 assertEquals(expectedMaxCardinality, actualMaxCardinality);
 }

 @Test
 public void testGetOptionsMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 Instances instances = new Instances(new FileReader("data/test.arff"));
 algorithm.setInstances(instances);
 String[] options = algorithm.getOptions();
 String expectedOption = "-cardinality";
 int indexOfExpectedOption = -1;
 for (int i = 0; i < options.length; ++i) {
 if (options[i].startsWith(expectedOption)) {
 indexOfExpectedOption = i;
 break;
 }
 }
 assertTrue(indexOfExpectedOption >= 0);
 }

 @Test
 public void testMaxCardinalityTipTextMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 String expectedTipText = "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
 String actualTipText = algorithm.maxCardinalityTipText();
 assertEquals(expectedTipText, actualTipText);
 }

 @Test
 public void testGlobalInfoMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 String expectedGlobalInfo = "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
 String actualGlobalInfo = algorithm.globalInfo();
 assertEquals(expectedGlobalInfo, actualGlobalInfo);
 }

 @Test
 public void testGetRevisionMethod() {
 ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
 String expectedRevision = "$Revision: 8034 $";
 String actualRevision = algorithm.getRevision();
 assertEquals(expectedRevision, actualRevision);
 }

 @Test
 public void testMainMethod() throws Exception {
 ICSSearchAlgorithm.main(new String[0]);
 }
}