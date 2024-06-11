import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;
import java.io.IOException;

public class ICSSearchAlgorithmTest {

 private ICSSearchAlgorithm icssa;
 private Instances data;

 @Before
 public void setUp() throws IOException {
 icssa = new ICSSearchAlgorithm();
 data = new Instances(new FileReader("contact-lenses.arff"));
 data.setClassIndex(data.numAttributes() - 1);
 icssa.setData(data);
 }

 @Test
 public void testName() {
 Instances instances = data;
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 assertEquals("pupil.size", icssa.name(0));
 }

 @Test
 public void testMaxn() {
 Instances instances = data;
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 assertEquals(instances.numAttributes(), icssa.maxn());
 }

 @Test
 public void testSetMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test
 public void testGetMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(3);
 assertEquals(3, icssa.getMaxCardinality());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxCardinality() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.setMaxCardinality(-1);
 }

 @Test
 public void testSearch() throws Exception {
 Instances instances = data;
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 icssa.search(new BayesNet(), instances);
 // add asserts for specific edge cases and exceptions here
 }

 @Test
 public void testCalcDependencyGraph() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet[][] sepsets = new SeparationSet[icssa.maxn() + 1][];
 icssa.calcDependencyGraph(edges, sepsets);
 // add asserts for specific edge cases and exceptions here
 }

 @Test
 public void testExistsSepSet() {
 ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
 boolean[][] edges = new boolean[icssa.maxn() + 1][];
 SeparationSet sepset = icssa.existsSepSet(0, 1, 2, edges);
 assertNotNull(sepset);
 // add asserts for specific edge cases and exceptions here
 }

 // add more test methods for other methods in the class here
}