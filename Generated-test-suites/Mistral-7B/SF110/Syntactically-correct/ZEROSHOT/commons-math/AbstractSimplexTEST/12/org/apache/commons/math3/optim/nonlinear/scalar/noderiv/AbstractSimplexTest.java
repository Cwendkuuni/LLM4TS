package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private int dimension;
 private double[] steps;
 private PointValuePair[] expectedPoints;

 public AbstractSimplexTest(int dimension, double[] steps, PointValuePair[] expectedPoints) {
 this.dimension = dimension;
 this.steps = steps;
 this.expectedPoints = expectedPoints;
 }

 @Parameterized.Parameters(name = "{index}: dimension={0}, steps={1}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 new Object[] {2, new double[] {0.5, 0.5}, new PointValuePair[] {
 new PointValuePair(new double[] {0.0, 0.0}, Double.NaN),
 new PointValuePair(new double[] {0.5, 0.0}, Double.NaN),
 new PointValuePair(new double[] {0.0, 0.5}, Double.NaN),
 new PointValuePair(new double[] {0.5, 0.5}, Double.NaN)
 }},
 new Object[] {3, new double[] {1.0, 1.0, 1.0}, new PointValuePair[] {
 new PointValuePair(new double[] {0.0, 0.0, 0.0}, Double.NaN),
 new PointValuePair(new double[] {1.0, 0.0, 0.0}, Double.NaN),
 new PointValuePair(new double[] {0.0, 1.0, 0.0}, Double.NaN),
 new PointValuePair(new double[] {0.0, 0.0, 1.0}, Double.NaN),
 new PointValuePair(new double[] {1.0, 1.0, 0.0}, Double.NaN),
 new PointValuePair(new double[] {1.0, 0.0, 1.0}, Double.NaN),
 new PointValuePair(new double[] {0.0, 1.0, 1.0}, Double.NaN),
 new PointValuePair(new double[] {1.1, 1.1, 1.1}, Double.NaN)
 }}
 });
 }

 @Before
 public void setUp() throws Exception {
 simplex = new SimplexNelderMead(dimension, steps);
 }

 @Test
 public void testGetDimension() throws Exception {
 assertEquals("Incorrect dimension", dimension, simplex.getDimension());
 }

 @Test
 public void testGetSize() throws Exception {
 assertEquals("Incorrect size", simplex.getSize(), expectedPoints.length);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInvalidStartConfiguration() throws Exception {
 new AbstractSimplex(new double[0]);
 }

 @Test(expected = NullArgumentException.class)
 public void testNullStartConfiguration() throws Exception {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testZeroStartConfiguration() throws Exception {
 new AbstractSimplex(new double[] {0.0});
 }

 @Test(expected = ZeroException.class)
 public void testZeroSteps() throws Exception {
 new AbstractSimplex(new double[0]);
 }

 @Test(expected = EqualVerticesException.class)
 public void testEqualVertices() throws Exception {
 new AbstractSimplex(new double[] {0.5, 0.5});
 }

 @Test
 public void testCreateHypercubeSteps() throws Exception {
 double[] calculatedSteps = AbstractSimplex.createHypercubeSteps(dimension, 1.0);
 assertArrayEquals("Incorrectly created hypercube steps", steps, calculatedSteps);
 }

 @Test
 public void testBuild() throws Exception {
 PointValuePair[] actualPoints = simplex.getPoints();
 assertArrayEquals("Incorrectly built simplex", expectedPoints, actualPoints);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMismatchedStartPoints() throws Exception {
 new SimplexNelderMead(dimension + 1, steps);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeDimensions() throws Exception {
 new AbstractSimplex(-1);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeStep() throws Exception {
 new AbstractSimplex(dimension, -1.0);
 }

 @Test
 public void testEvaluate() throws Exception {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 when(mockFunction.value(isA(double[].class))).thenReturn(MathFunctions.SINE.value(new DoublePoint(0.0)));
 simplex.evaluate(mockFunction, new SimpleComparator());
 for (int i = 0; i < simplex.getSize(); i++) {
 PointValuePair actualPoint = simplex.getPoint(i);
 PointValuePair expectedPoint = expectedPoints[i];
 assertEquals("Mismatched point " + i, expectedPoint.getPointRef(), actualPoint.getPointRef());
 assertEquals("Mismatched value " + i, expectedPoint.getValue(), actualPoint.getValue(), 0.001);
 }
 }

 @Test
 public void testReplaceWorstPoint() throws Exception {
 PointValuePair worstPoint = expectedPoints[dimension];
 simplex.replaceWorstPoint(worstPoint, new SimpleComparator());
 PointValuePair actualWorstPoint = simplex.getPoint(dimension);
 assertEquals("Mismatched worst point", worstPoint, actualWorstPoint);
 }
}