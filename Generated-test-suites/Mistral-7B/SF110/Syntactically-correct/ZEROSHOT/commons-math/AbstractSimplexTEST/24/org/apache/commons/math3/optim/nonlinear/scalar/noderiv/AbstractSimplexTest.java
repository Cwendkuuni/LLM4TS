package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractSimplexTest {

 private AbstractSimplex simplex = null;
 private MultivariateFunction testFunction = null;
 private Comparator<PointValuePair> comparator = null;

 @Before
 public void setUp() throws Exception {
 testFunction = new Add(); // Add function for testing
 comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 }

 @Test
 public void testAbstractSimplex() {
 simplex = new AbstractSimplex(3); // Test no-argument constructor
 assertEquals(3, simplex.getDimension());
 assertArrayEquals(new double[]{}, simplex.startConfiguration, 0);

 simplex = new AbstractSimplex(3, 1.0); // Test constructor with number of dimensions and side length
 assertEquals(3, simplex.getDimension());
 assertArrayEquals(new double[][]{ { 0.0, 1.0, 1.0 }, { 1.0, 0.0, 1.0 }, { 1.0, 1.0, 0.0 } }, simplex.startConfiguration, 0);

 simplex = new AbstractSimplex(new double[]{}); // Test constructor with empty array
 throwException(IllegalArgumentException.class, () -> new AbstractSimplex(new double[]{}));

 simplex = new AbstractSimplex(new double[]{-1.0, -1.0, -1.0}); // Test constructor with negative array
 throwException(IllegalArgumentException.class, () -> new AbstractSimplex(new double[]{-1.0, -1.0, -1.0}));

 simplex = new AbstractSimplex(new double[]{-1.0, 1.0, 1.0}); // Test constructor with non-equal array
 throwException(IllegalArgumentException.class, () -> new AbstractSimplex(new double[]{-1.0, 1.0, 1.0}));
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][]{ { 1.0, 1.0 }, { 1.0, 2.0 }, { 2.0, 1.0 }, { 2.0, 2.0 } });
 assertEquals(2, simplex.getDimension());
 assertArrayEquals(new double[][]{ { 0.0, 1.0 }, { 1.0, 1.0-1.0 }, { 1.0, 1.0 }, { 1.0, 2.0-1.0 } }, simplex.startConfiguration, 0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexWithNegativeStepSize() {
 simplex = new AbstractSimplex(new double[]{-1.0, -1.0});
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexWithNullReferenceSimplex() {
 simplex = new AbstractSimplex(null);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexWithInvalidReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][]{ { 1.0, 1.0 }, { 1.0, 1.0 } });
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexWithEqualVerticesInSimplex() {
 simplex = new AbstractSimplex(new double[][]{ { 1.0, 1.0 }, { 1.0, 1.0 } });
 }

 @Test
 public void testGetDimension() {
 simplex = new AbstractSimplex(3);
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 simplex = new AbstractSimplex(3);
 assertEquals(4, simplex.getSize());
 }

 @Test
 public void testIterate() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.iterate(testFunction, comparator);
 }

 @Test
 public void testBuild() throws Exception {
 simplex = new AbstractSimplex(3);
 double[] startPoint = { 1.0, 1.0, 1.0 };
 simplex.build(startPoint);
 PointValuePair[] expected = new PointValuePair[]{new PointValuePair(startPoint, Double.NaN), new PointValuePair(new double[]{ 1.0+startPoint[0], 1.0 }, Double.NaN), new PointValuePair(new double[]{ 1.0, 1.0+startPoint[1] }, Double.NaN), new PointValuePair(new double[]{ 1.0+startPoint[0], 1.0+startPoint[1] }, Double.NaN)};
 assertArrayEquals(expected, simplex.getPoints());
 }

 @Test
 public void testEvaluate() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 }

 @Test
 public void testReplaceWorstPoint() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 PointValuePair worstPoint = simplex.getPoint(simplex.size()-1);
 PointValuePair replacementPoint = new PointValuePair(new double[]{ 0.5, 0.5, 0.5 }, testFunction.value(new double[]{ 0.5, 0.5, 0.5 }), false);
 simplex.replaceWorstPoint(replacementPoint, comparator);
 assertNotEquals(worstPoint.getValue(), simplex.getPoint(simplex.size()-1).getValue());
 }

 @Test
 public void testGetPoints() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 PointValuePair[] expected = simplex.getPoints();
 }

 @Test
 public void testGetPoint() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 PointValuePair expected = simplex.getPoint(0);
 }

 @Test
 public void testSetPoint() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 PointValuePair pointToSet = simplex.getPoint(1);
 PointValuePair newPoint = new PointValuePair(new double[]{ 2.0, 2.0, 2.0 }, Double.NaN, false);
 simplex.setPoint(1, newPoint);
 assertNotEquals(pointToSet.getValue(), simplex.getPoint(1).getValue());
 }

 @Test
 public void testSetPoints() throws Exception {
 simplex = new AbstractSimplex(3);
 simplex.build(new double[]{ 1.0, 1.0, 1.0 });
 simplex.evaluate(testFunction, comparator);
 PointValuePair[] newPoints = new PointValuePair[] { new PointValuePair(new double[]{ 2.0, 2.0, 2.0 }, Double.NaN, false), new PointValuePair(new double[]{ 3.0, 3.0, 3.0 }, Double.NaN, false), new PointValuePair(new double[]{ 4.0, 4.0, 4.0 }, Double.NaN, false) };
 simplex.setPoints(newPoints);
 assertArrayEquals(newPoints, simplex.getPoints());
 }

 @Test
 public void testCreateHypercubeSteps() {
 double[] steps = AbstractSimplex.createHypercubeSteps(3, 1.0);
 assertEquals(3, steps.length);
 assertArrayEquals(new double[]{ 1.0, 1.0, 1.0 }, steps, 0);
 }
}