package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.junit.*;

public class AbstractSimplexTest {

 private AbstractSimplex abstractSimplex;
 private MultivariateFunction testFunction;
 private Comparator<PointValuePair> comparator;

 @Before
 public void setUp() {
 testFunction = new QuadraticFunction(new double[]{-1.0, 2.0}, new double[]{4.0, -3.0});
 comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 abstractSimplex = new SimplexTestSimplex(2);
 }

 @Test
 public void testAbstractSimplex() {
 assertEquals(2, abstractSimplex.getDimension());
 assertEquals(3, abstractSimplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullArguments() {
 abstractSimplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexZeroArguments() {
 abstractSimplex = new AbstractSimplex(new double[0]);
 }

 @Test
 public void testAbstractSimplexCreateHypercubeSteps() {
 double[] steps = AbstractSimplex.createHypercubeSteps(3, 1.0);
 assertEquals(3, steps.length);
 for (int i = 0; i < steps.length; ++i) {
 assertEquals(1.0, steps[i], 1e-12);
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexCreateReferenceSimplexTooFewPoints() {
 abstractSimplex = new AbstractSimplex(new double[][]{{1.0}});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexCreateReferenceSimplexMismatchedDimensions() {
 abstractSimplex = new AbstractSimplex(new double[][]{{1.0}, {2.0, 3.0}});
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexCreateReferenceSimplexEqualVertices() {
 abstractSimplex = new AbstractSimplex(new double[][]{{1.0}, {1.0}});
 }

 @Test
 public void testAbstractSimplexBuild() {
 double[] startPoint = new double[]{1.0, 1.0};
 abstractSimplex.build(startPoint);
 PointValuePair[] points = abstractSimplex.getPoints();
 for (int i = 0; i < points.length; ++i) {
 double[] point = points[i].getPointRef();
 assertEquals(startPoint[i], point[0], 1e-12);
 assertEquals(startPoint[i % 2] + abstractSimplex.startConfiguration[i % 2][i % 2], point[i % 2], 1e-12);
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexBuildMismatchedDimensions() {
 double[] startPoint = new double[]{1.0, 1.0, 2.0};
 abstractSimplex.build(startPoint);
 }

 @Test
 public void testAbstractSimplexIterate() {
 abstractSimplex.iterate(testFunction, comparator);
 PointValuePair worstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
 assertEquals(Double.POSITIVE_INFINITY, worstPoint.getValue(), 1e-12);
 abstractSimplex.evaluate(testFunction, comparator);
 worstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
 assertFalse(Double.isNaN(worstPoint.getValue()));
 assertEquals(testFunction.value(worstPoint.getPointRef()), worstPoint.getValue(), 1e-12);
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
 PointValuePair newWorstPoint = new PointValuePair(new double[]{2.0, 2.0}, Double.NEGATIVE_INFINITY);
 abstractSimplex.replaceWorstPoint(newWorstPoint, comparator);
 PointValuePair actualNewWorstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
 assertEquals(newWorstPoint, actualNewWorstPoint);
 }
}