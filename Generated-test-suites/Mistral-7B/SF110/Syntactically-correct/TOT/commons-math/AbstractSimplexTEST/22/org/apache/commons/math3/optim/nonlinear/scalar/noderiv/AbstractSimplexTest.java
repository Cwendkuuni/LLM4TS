package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplexConstructionWithValidArguments() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexConstructionWithNullArgument() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructionWithZeroDimension() {
 new AbstractSimplex(0);
 }

 @Test
 public void testAbstractSimplexConstructionWithCustomSteps() {
 double[] steps = {1.0, 2.0, 3.0};
 AbstractSimplex simplex = new AbstractSimplex(steps);
 assertEquals(3, simplex.getDimension());
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexConstructionWithZeroSteps() {
 double[] steps = {};
 new AbstractSimplex(steps);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexConstructionWithEqualVertices() {
 double[] steps = {1.0, 1.0, 1.0};
 new AbstractSimplex(steps);
 }

 @Test
 public void testAbstractSimplexConstructionWithReferenceSimplex() {
 double[][] referenceSimplex = {
 {0.0, 0.0}, // Vertex 0
 {1.0, 1.0}, // Vertex 1
 {1.0, 0.0} // Vertex 2
 };
 AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(2, simplex.getDimension());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexConstructionWithInvalidReferenceSimplexSize() {
 double[][] referenceSimplex = {};
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexConstructionWithInvalidReferenceSimpleVertexSize() {
 double[][] referenceSimplex = {{0.0}, {1.0, 1.0}};
 new AbstractSimplex(referenceSimplex);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexConstructionWithEqualVerticesInReferenceSimple() {
 double[][] referenceSimplex = {
 {0.0, 0.0},
 {1.0, 1.0},
 {1.0, 1.0}
 };
 new AbstractSimplex(referenceSimplex);
 }

 @Test
 public void testAbstractSimplexGetters() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 assertEquals(13, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexIterate() {
 MultivariateFunction function = new Add();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.iterate(function, comparator);
 }

 @Test
 public void testAbstractSimplexBuild() {
 MultivariateFunction function = new Add();
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[]{1.0, 1.0, 1.0};
 simplex.build(startPoint);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexBuildWithInvalidStartPointDimension() {
 MultivariateFunction function = new Add();
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[]{1.0, 1.0};
 simplex.build(startPoint);
 }

 @Test
 public void testAbstractSimplexEvaluate() {
 MultivariateFunction function = new Add();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.evaluate(function, comparator);
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
 MultivariateFunction function = new Add();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 simplex.replaceWorstPoint(worstPoint, comparator);
 }

 @Test
 public void testAbstractSimplexGetPoints() {
 MultivariateFunction function = new Add();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.evaluate(function, comparator);
 PointValuePair[] points = simplex.getPoints();
 }

 @Test
 public void testAbstractSimplexGetPoint() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[]{1.0, 1.0, 1.0};
 simplex.build(startPoint);
 PointValuePair point = simplex.getPoint(0);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexGetPointWithInvalidIndex() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0, 1.0});
 PointValuePair point = simplex.getPoint(-1);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexGetPointWithInvalidIndexGreaterThanSize() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[]{1.0, 1.0, 1.0});
 PointValuePair point = simplex.getPoint(13);
 }

 @Test
 public void testAbstractSimplexSetPoint() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0);
 simplex.setPoint(0, point);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexSetPointWithInvalidIndex() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0);
 simplex.setPoint(-1, point);
 }

 @Test(expected = OutOfRangeException.class)
 public void testAbstractSimplexSetPointWithInvalidIndexGreaterThanSize() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 1.0);
 simplex.setPoint(13, point);
 }

 @Test
 public void testAbstractSimplexSetPoints() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair[] points = new PointValuePair[13];
 simplex.setPoints(points);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexSetPointsWithInvalidPointsDimension() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair[] points = new PointValuePair[13];
 simplex.setPoints(points);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexSetPointsWithInvalidPointsSize() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair[] points = new PointValuePair[12];
 simplex.setPoints(points);
 }
}