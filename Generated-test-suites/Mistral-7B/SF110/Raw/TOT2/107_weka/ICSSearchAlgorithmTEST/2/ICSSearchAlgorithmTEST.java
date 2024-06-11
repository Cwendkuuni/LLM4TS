import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances instances;

 @Before
 public void setUp() throws Exception {
 icssa = new ICSSearchAlgorithm();
 instances = new Instances(new FileReader("contact-lenses.arff"));
 instances.setClassIndex(instances.numAttributes() - 1);
 icssa.setMaxCardinality(2);
 icssa.search(null, instances);
 }

 @Test
 public void testNameMethod() {
 assertEquals("Age", icssa.name(0));
 assertEquals("Gender", icssa.name(1));
 assertEquals("Contact lens prescription", icssa.name(instances.numAttributes() - 1));
 }

 @Test
 public void testMaxnMethod() {
 assertEquals(instances.numAttributes(), icssa.maxn());
 }

 @Test
 public void testSetMaxCardinalityMethod() {
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testSearchMethodWithValidInput() throws Exception {
 // Testing the search method with valid input
 assertTrue(icssa.m_BayesNet != null);
 assertTrue(icssa.m_instances != null);
 }

 @Test(expected = Exception.class)
 public void testSearchMethodWithNullInput() throws Exception {
 icssa.search(null, null);
 }

 @Test(expected = Exception.class)
 public void testSearchMethodWithEmptyInstances() throws Exception {
 icssa.search(null, new Instances());
 }

 @Test
 public void testCalcDependencyGraphMethod() {
 // Testing the calcDependencyGraph method with valid input
 // Printing the dependency graph to check the output
 icssa.calcDependencyGraph(icssa.edges, icssa.sepsets);
 }

 @Test(expected = Exception.class)
 public void testCalcDependencyGraphMethodWithNullEdgesOrSepsets() {
 icssa.calcDependencyGraph(null, icssa.sepsets);
 icssa.calcDependencyGraph(icssa.edges, null);
 }

 @Test(expected = Exception.class)
 public void testExistsSepSetMethodWithInvalidArguments() {
 icssa.existsSepSet(-1, 0, 0, icssa.edges);
 icssa.existsSepSet(0, 0, 2, icssa.edges);
 icssa.existsSepSet(0, 0, 0, null);
 }

 @Test
 public void testExistsSepSetMethodWithValidArguments() {
 final SeparationSet sepSet = icssa.existsSepSet(0, 1, 1, icssa.edges);
 assertNotNull(sepSet);
 assertEquals(1, sepSet.m_set.length);
 assertEquals(1, sepSet.m_set[0]);
 }

 @Test(expected = Exception.class)
 public void testNextMethodWithInvalidArguments() {
 icssa.next(-1, 0, 0, icssa.edges);
 icssa.next(0, 0, instances.numAttributes(), icssa.edges);
 }

 @Test
 public void testNextMethodWithValidArguments() {
 final int x = icssa.next(0, 0, 1, icssa.edges);
 assertEquals(1, x);
 }
}