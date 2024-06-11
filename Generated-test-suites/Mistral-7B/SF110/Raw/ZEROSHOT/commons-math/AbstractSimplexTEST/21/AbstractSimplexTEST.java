package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplexWithIntConstructor() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(3);
 assertEquals(3, testAbstractSimplex.getDimension());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexWithNullSteps() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexWithEmptySteps() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[0]);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexWithZeroStep() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[] { 0.0 });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexWithEqualVertices() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[] { 1.0, 1.0, 1.0 });
 }

 @Test
 public void testAbstractSimplexWithIntAndDoubleConstructor() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(4, 1.5);
 assertEquals(4, testAbstractSimplex.getDimension());
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplexConstructor() {
 double[][] referenceSimplex = { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 1.0 }, { 3.0, 1.0, 2.0 } };
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(referenceSimplex);
 assertEquals(3, testAbstractSimplex.getDimension());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexWithZeroSizeReferenceSimplex() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[0][0]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexWithReferenceSimplexWithWrongPointSize() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[][] { { 1.0 }, { 2.0, 3.0 } });
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexWithReferenceSimplexWithWrongNumberOfPoints() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[][] { { 1.0, 2.0 }, { 1.0, 2.0 } });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexWithReferenceSimplexWithEqualVertices() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(new double[][] { { 1.0, 2.0, 1.0 }, { 2.0, 1.0, 1.0 } });
 }

 @Test
 public void testGetDimension() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(4);
 assertEquals(4, testAbstractSimplex.getDimension());
 }

 @Test
 public void testGetSize() {
 AbstractSimplex testAbstractSimplex = new AbstractSimplex(4);
 assertEquals(5, testAbstractSimplex.getSize());
 }

 @Test
 public void testIterate() {
 // Provide a stub for the iterate method as it's abstract and not testable in this context
 }

 @Test
 public void testBuild() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 double[] startPoint = { 1.0, 2.0, 3.0, 4.0 };
 testAbstractSimplex.build(startPoint);
 // Check the vertex points
 for (int i = 0; i < testAbstractSimplex.getSize(); i++) {
 PointValuePair point = testAbstractSimplex.getPoint(i);
 double[] pointValues = point.getPointRef();
 assertEquals(startPoint[i * 4], pointValues[0], 0.001);
 assertEquals(startPoint[i * 4 + 1], pointValues[1], 0.001);
 assertEquals(startPoint[i * 4 + 2], pointValues[2], 0.001);
 assertEquals(startPoint[i * 4 + 3], pointValues[3], 0.001);
 }
 }

 @Test
 public void testEvaluate() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 testAbstractSimplex.build(new double[] { 1.0, 2.0, 3.0, 4.0 });
 testAbstractSimplex.evaluate(mockFunction, mockComparator);
 // Check if the worst point has the minimum value
 PointValuePair worstPoint = testAbstractSimplex.getPoint(testAbstractSimplex.getSize() - 1);
 double minValue = Double.POSITIVE_INFINITY;
 for (int i = 0; i < testAbstractSimplex.getSize(); i++) {
 PointValuePair point = testAbstractSimplex.getPoint(i);
 double pointValue = point.getValue();
 if (pointValue < minValue) {
 minValue = pointValue;
 worstPoint = point;
 }
 }
 assertEquals(worstPoint.getValue(), mockFunction.value(worstPoint.getPointRef()));
 }

 @Test
 public void testReplaceWorstPoint() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 testAbstractSimplex.build(new double[] { 1.0, 2.0, 3.0, 4.0 });
 PointValuePair newPoint = new PointValuePair(new double[] { 5.0, 5.0, 5.0, 5.0 }, 42.0);
 testAbstractSimplex.replaceWorstPoint(newPoint, mockComparator);
 PointValuePair newWorstPoint = testAbstractSimplex.getPoint(testAbstractSimplex.getSize() - 1);
 assertEquals(newPoint, newWorstPoint);
 }

 @Test
 public void testGetPoints() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 testAbstractSimplex.build(new double[] { 1.0, 2.0, 3.0, 4.0 });
 PointValuePair[] points = testAbstractSimplex.getPoints();
 assertEquals(testAbstractSimplex.getSize(), points.length);
 for (int i = 0; i < points.length; i++) {
 assertEquals(testAbstractSimplex.getPoint(i), points[i]);
 }
 }

 @Test
 public void testGetPoint() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 testAbstractSimplex.build(new double[] { 1.0, 2.0, 3.0, 4.0 });
 PointValuePair point = testAbstractSimplex.getPoint(1);
 double[] pointValues = point.getPointRef();
 assertEquals(2.0, pointValues[0], 0.001);
 assertEquals(3.0, pointValues[1], 0.001);
 assertEquals(4.0, pointValues[2], 0.001);
 assertEquals(4.0, pointValues[3], 0.001);
 }

 @Test
 public void testSetPoint() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 testAbstractSimplex.build(new double[] { 1.0, 2.0, 3.0, 4.0 });
 PointValuePair newPoint = new PointValuePair(new double[] { 5.0, 5.0, 5.0, 5.0 }, 42.0);
 testAbstractSimplex.setPoint(1, newPoint);
 PointValuePair newPointInSimplex = testAbstractSimplex.getPoint(1);
 assertEquals(newPoint, newPointInSimplex);
 }

 @Test
 public void testSetPoints() {
 MultivariateFunction mockFunction = mock(MultivariateFunction.class);
 Comparator<PointValuePair> mockComparator = mock(Comparator.class);
 AbstractSimplex testAbstractSimplex = new AbstractSimplexFourDimensions();
 PointValuePair[] points = new PointValuePair[5];
 for (int i = 0; i < 5; i++) {
 points[i] = new PointValuePair(new double[] { i, i, i, i }, i * 2.0);
 }
 testAbstractSimplex.setPoints(points);
 PointValuePair[] pointsInSimplex = testAbstractSimplex.getPoints();
 assertEquals(5, testAbstractSimplex.getSize());
 for (int i = 0; i < points.length; i++) {
 assertEquals(points[i], pointsInSimplex[i]);
 }
 }
}

class AbstractSimplexFourDimensions extends AbstractSimplex {
 AbstractSimplexFourDimensions() {
 super(4);
 }
}