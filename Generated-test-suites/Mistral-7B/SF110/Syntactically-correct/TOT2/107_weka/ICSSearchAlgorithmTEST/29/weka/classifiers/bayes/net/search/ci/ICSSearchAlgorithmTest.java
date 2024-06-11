import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;

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
 public void testName() {
 assertEquals("Pupil Size", icssa.name(0));
 assertEquals("Contact Lens Wearer", icssa.name(1));
 assertEquals("Age", icssa.name(2));
 assertEquals("Tear Film Quality", icssa.name(3));
 assertEquals("Diabetes", icssa.name(4));
 }

 @Test
 public void testMaxn() {
 assertEquals(5, icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 assertEquals(2, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxCardinality() {
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 final BayesNet bayesNet = new BayesNet();
 bayesNet.setSearchAlgorithm(icssa);
 icssa.search(bayesNet, data);
 // Add assertions to check the structure and content of the BayesNet instance here
 }

 @Test
 public void testCalcDependencyGraph() {
 // Add assertions to check the edges and sepsets array here
 }

 @Test
 public void testExistsSepSet() {
 SeparationSet sepset = icssa.existsSepSet(0, 1, 2, new boolean[][]{{true, true}, {false, false}});
 assertNotNull(sepset);
 assertEquals(2, sepset.m_set[0]);
 assertEquals(4, sepset.m_set[1]);
 }

 @Test
 public void testNext() {
 assertEquals(3, icssa.next(0, 0, 1, new boolean[][]{{true, true}, {false, false}}));
 assertEquals(5, icssa.next(4, 0, 1, new boolean[][]{{true, true}, {false, false}}));
 }

 @Test
 public void testCalcVeeNodes() {
 // Add assertions to check the arrows array here
 }

 @Test
 public void testCalcArcDirections() {
 // Add assertions to check the arrows array here
 }

 @Test
 public void testListOptions() {
 final Option[] options = icssa.listOptions();
 assertNotNull(options);
 assertEquals(2, options.length);
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. (default 2)", options[0].description);
 assertEquals("-cardinality", options[0].getLongName());
 assertEquals("2", options[1].getValue());
 }

 @Test
 public void testSetOptions() throws Exception {
 icssa.setOptions(new String[]{"-cardinality", "3"});
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetOptions() {
 final String[] options = icssa.getOptions();
 assertEquals(3, options.length);
 assertEquals("-cardinality", options[1]);
 assertEquals("3", options[2]);
 }

 @Test
 public void testMaxCardinalityTipText() {
 assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
 }

 @Test
 public void testGlobalInfo() {
 assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
 }
}