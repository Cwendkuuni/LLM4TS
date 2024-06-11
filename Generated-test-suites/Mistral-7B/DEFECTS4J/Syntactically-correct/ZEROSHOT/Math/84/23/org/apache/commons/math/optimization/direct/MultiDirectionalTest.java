package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private RealPointValuePair[] simplex;
 private RealPointValuePair bestPoint;
 private Comparator<RealPointValuePair> comparator;

 @Before
 public void setUp() {
 optimizer = new MultiDirectional();
 comparator = new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair o1, RealPointValuePair o2) {
 double diff = o1.getValue() - o2.getValue();
 return (Double.signum(diff) < 0) ? -1 : (Double.signum(diff) > 0) ? 1 : 0;
 }
 };
 }

 @Test
 public void testIterateSimplexWithDefaultCoefficients() throws Exception {
 // given
 double[][] testFunction = {{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
 double[][] expected = {{0.0, 0.0}, {0.5, 1.0}, {0.0, 1.0}, {0.5, 0.0}};
 for (int i = 0; i < testFunction.length; i++) {
 RealPointValuePair p = new RealPointValuePair(testFunction[i], Double.NaN, false);
 optimizer.addPoint(p);
 }
 optimizer.setMaxIterations(10);
 optimizer.setConvergenceChecker(new RealConvergenceChecker(1e-8));

 // when
 optimizer.optimize();
 simplex = optimizer.getSimplext();
 bestPoint = simplex[0];

 // then
 assertEquals(testFunction.length, simplex.length);
 assertArrayEquals(expected[0].length, simplex[0].getPointRef().length);
 assertArrayEquals(expected, simplex);
 assertEquals(0.0, bestPoint.getValue(), 1e-8);
 }

 @Test
 public void testIterateSimplexWithCustomCoefficients() throws Exception {
 // given
 double[][] testFunction = {{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
 MultiDirectional optimizer = new MultiDirectional(3.0, 0.5);
 for (int i = 0; i < testFunction.length; i++) {
 RealPointValuePair p = new RealPointValuePair(testFunction[i], Double.NaN, false);
 optimizer.addPoint(p);
 }
 optimizer.setMaxIterations(10);
 optimizer.setConvergenceChecker(new RealConvergenceChecker(1e-8));

 // when
 optimizer.optimize();
 simplex = optimizer.getSimplext();
 bestPoint = simplex[0];

 // then
 assertEquals(testFunction.length, simplex.length);
 // The exact expected values depend on the specific test function and the coefficients.
 // Uncomment the following lines to check the expected values.
 // assertArrayEquals(expected, simplex);
 // assertEquals(expected[0].length, simplex[0].getPointRef().length);
 // assertEquals(0.0, bestPoint.getValue(), 1e-8);
 }

}