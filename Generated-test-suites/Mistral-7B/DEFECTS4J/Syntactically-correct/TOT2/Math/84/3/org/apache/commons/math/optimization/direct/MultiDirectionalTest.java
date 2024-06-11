import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleXSearch;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private SimpleXSearch simplexSearch;
 private RealPointValuePair[] simplex;
 private PointValuePair bestPoint;

 @Before
 public void setUp() {
 optimizer = new MultiDirectional();
 simplexSearch = new SimpleXSearch(optimizer);
 simplex = new RealPointValueValuePair[6]; // simplex with 6 vertices
 bestPoint = new PointValuePair(new double[]{0.0}, 0.0);
 }

 @Test
 public void testMultiDirectionalWithDefaultCoefficients() throws Exception {
 optimizer.iterateSimplex(new DefaultComparator());
 assertNotEquals(bestPoint, simplex[0]); // check that best point is not the same as the first vertex
 }

 @Test
 public void testMultiDirectionalWithCustomCoefficients() throws Exception {
 optimizer = new MultiDirectional(3.0, 0.8);
 optimizer.iterateSimplex(new DefaultComparator());
 assertNotEquals(bestPoint, simplex[0]);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testThrowsFunctionEvaluationException() throws Exception {
 optimizer = new MultiDirectional(0.0, 0.5);
 optimizer.iterateSimplex(new DefaultComparator());
 fail("FunctionEvaluationException should be thrown");
 }

 @Test(expected = OptimizationException.class)
 public void testThrowsOptimizationException() throws Exception {
 optimizer = new MultiDirectional(0.0, 0.5);
 optimizer.setMaxEvaluations(1);
 optimizer.iterateSimplex(new DefaultComparator());
 fail("OptimizationException should be thrown");
 }

 @Test
 public void testReflectStep() throws Exception {
 simplex[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
 optimizer.iterateSimplex(new DefaultComparator());
 final RealPointValuePair reflected = simplex[1];
 optimizer.iterateSimplex(new DefaultComparator());
 final RealPointValuePair expanded = simplex[1];
 assertTrue(comparator.compare(reflected, bestPoint) < 0);
 assertTrue(comparator.compare(expanded, bestPoint) <= 0);
 }

 @Test
 public void testContractStep() throws Exception {
 simplex[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
 optimizer.iterateSimplex(new DefaultComparator());
 optimizer.iterateSimplex(new DefaultComparator());
 final RealPointValuePair contracted = simplex[1];
 assertTrue(comparator.compare(contracted, bestPoint) < 0);
 }

 @Test
 public void testConvergence() throws Exception {
 simplex[1] = new RealPointValuePair(new double[]{1.0}, 1.0);
 optimizer.iterateSimplex(new DefaultComparator());
 optimizer.iterateSimplex(new DefaultComparator());
 optimizer.iterateSimplex(new DefaultComparator());
 assertTrue(optimizer.isConverged());
 }

}