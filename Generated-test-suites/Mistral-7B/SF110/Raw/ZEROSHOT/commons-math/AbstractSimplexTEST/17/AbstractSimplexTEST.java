package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplex_int() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplex_int_nullSteps() {
 new AbstractSimplex(3, null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplex_int_zeroSteps() {
 new AbstractSimplex(3, new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplex_int_equalVertices() {
 Assert.assertThrows(ZeroException.class, () -> new AbstractSimplex(3, new double[] { 0.0, 0.0, 0.0, 0.0 }));
 }

 @Test
 public void testAbstractSimplex_int_correctConfiguration() {
 AbstractSimplex simplex = new AbstractSimplex(3, new double[] { 1.0, 1.0, 1.0, 1.0 });
 assertArrayEquals(new double[] { 0.0, 1.0, 2.0, 3.0 }, Arrays.stream(simplex.startConfiguration).map(a -> Arrays.toString(a)).toArray());
 }

 @Test
 public void testAbstractSimplex_doubleArray() {
 AbstractSimplex simplex = new AbstractSimplex(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, simplex.getDimension());
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplex_doubleArray_nullReferenceSimplex() {
 new AbstractSimplex(null);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplex_doubleArray_negativeSize() {
 new AbstractSimplex(new double[0][0]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplex_doubleArray_wrongReferenceSize() {
 new AbstractSimplex(new double[][] { new double[3], new double[2] });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplex_doubleArray_wrongVertexSize() {
 new AbstractSimplex(new double[][] { new double[3], new double[4] });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplex_doubleArray_equalVertices() {
 Assert.assertThrows(MathIllegalArgumentException.class, () -> new AbstractSimplex(new double[][] { new double[] { 1.0, 1.0, 1.0 }, new double[] { 1.0, 1.0, 1.0 } }));
 }

 @Test
 public void testAbstractSimplex_doubleArray_correctConfiguration() {
 AbstractSimplex simplex = new AbstractSimplex(new double[][] { new double[] { 0.0, 0.0 }, new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 } });
 assertArrayEquals(new double[][] { new double[] { 1.0, -1.0 }, new double[] { 0.0, 1.0 }, new double[] { -1.0, 1.0 } }, Arrays.stream(simplex.startConfiguration).map(a -> Arrays.toString(a)).toArray());
 }

 @Test
 public void testGetDimension() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex = new AbstractSimplex(new double[][] { new double[] { 0.0, 0.0 }, new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 } });
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex = new AbstractSimplex(new double[][] { new double[] { 0.0, 0.0 }, new double[] { 1.0, 1.0 }, new double[] { 2.0, 2.0 } });
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate() {
 // Abstract method, not testing here
 }

 @Test
 public void testBuild() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 double[] startPoint = new double[] { 0.0, 0.0 };
 simplex.build(startPoint);
 assertEquals(4, simplex.getSize());
 assertArrayEquals(new double[][] { new double[] { 0.0, 0.0 }, new double[] { 1.0, 0.0 }, new double[] { 0.0, 1.0 }, new double[] { 1.1, 1.1 } }, Arrays.stream(simplex.startConfiguration).map(a -> Arrays.toString(a)).toArray());
 }

 @Test
 public void testEvaluate() {
 MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
 Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[] { 0.0, 0.0 });
 simplex.evaluate(evaluationFunction, comparator);
 assertTrue(simplex.getPoint(0).getValue() > simplex.getPoint(1).getValue());
 }

 @Test
 public void testReplaceWorstPoint() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[] { 0.0, 0.0 });
 PointValuePair pointValuePair = new PointValuePair(new double[] { 1.0, 1.0 }, 1.0);
 simplex.replaceWorstPoint(pointValuePair, comparator);
 assertTrue(simplex.getPoint(3).getValue() == 1.0 && simplex.getPoint(0).getValue() < 1.0);
 }

 @Test
 public void testGetPoints() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[] { 0.0, 0.0 });
 PointValuePair[] points = simplex.getPoints();
 assertEquals(4, points.length);
 assertArrayEquals(new double[][] { new double[] { 0.0, 0.0 }, new double[] { 1.0, 0.0 }, new double[] { 0.0, 1.0 }, new double[] { 1.1, 1.1 } }, Arrays.stream(points).map(p -> Arrays.toString(p.getPointRef())).toArray());
 }

 @Test
 public void testGetPoint() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[] { 0.0, 0.0 });
 PointValuePair point = simplex.getPoint(1);
 assertEquals(new double[] { 1.0, 0.0 }, point.getPointRef());
 }

 @Test
 public void testSetPoint() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 simplex.build(new double[] { 0.0, 0.0 });
 PointValuePair point = new PointValuePair(new double[] { 2.0, 2.0 }, 4.0);
 simplex.setPoint(1, point);
 assertEquals(4.0, simplex.getPoint(1).getValue());
 }

 @Test
 public void testSetPoints() {
 AbstractSimplex simplex = new AbstractSimplex(3);
 PointValuePair[] points = new PointValuePair[] { new PointValuePair(new double[] { 0.0, 0.0 }, 0.0), new PointValuePair(new double[] { 1.0, 1.0 }, 1.0), new PointValuePair(new double[] { 1.1, 1.1 }, 1.1) };
 simplex.setPoints(points);
 assertArrayEquals(points, simplex.getPoints());
 }

 @Test
 public void testCreateHypercubeSteps() {
 assertEquals(3, AbstractSimplex.createHypercubeSteps(3, 1.0).length);
 assertEquals(3, AbstractSimplex.createHypercubeSteps(3, 1.0)[0], 0.0);
 }
}