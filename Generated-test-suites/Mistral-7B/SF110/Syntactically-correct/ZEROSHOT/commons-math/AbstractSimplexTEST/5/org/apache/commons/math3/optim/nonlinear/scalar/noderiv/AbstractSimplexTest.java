package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.go.PointValuePair;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithEmptyConstructor() throws OptimizationException {
        try {
            AbstractSimplex simplex = new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // expected
        }
    }

    @Test
    public void testAbstractSimplexWithOneDimension() {
        AbstractSimplex simplex = new AbstractSimplex(1);
        assertEquals(1, simplex.getDimension());
        assertEquals(2, simplex.getSize());
        assertArrayEquals(new double[]{-0.5, 0.5}, simplex.startConfiguration[0], 0.0);
        assertArrayEquals(new double[]{0.0}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{0.5}, simplexic.simplex[1].getPointRef());
        assertNull(simplexic.simplex[1].getValue());
    }

    @Test
    public void testAbstractSimplexWithOneDimensionAndSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(1, 1.0);
        assertEquals(1, simplex.getDimension());
        assertEquals(2, simplex.getSize());
        assertArrayEquals(new double[]{0.0, 1.0}, simplex.startConfiguration[0], 0.0);
        assertArrayEquals(new double[]{0.0}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{1.0}, simplexic.simplex[1].getPointRef());
        assertNull(simplexic.simplex[1].getValue());
    }

    @Test
    public void testAbstractSimplexWithCustomSteps() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        assertEquals(1, simplex.getDimension());
        assertEquals(2, simplex.getSize());
        assertArrayEquals(new double[]{-1.0, 1.0}, simplex.startConfiguration[0], 0.0);
        assertArrayEquals(new double[]{0.0}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{1.0}, simplexic.simplex[1].getPointRef());
        assertNull(simplexic.simplex[1].getValue());
    }

    @Test
    public void testAbstractSimplexWithCustomReferenceSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0}});
        assertEquals(1, simplex.getDimension());
        assertEquals(2, simplex.getSize());
        assertArrayEquals(new double[]{-1.0, 1.0}, simplex.startConfiguration[0], 0.0);
        assertArrayEquals(new double[]{0.0}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{1.0}, simplexic.simplex[1].getPointRef());
        assertNull(simplexic.simplex[1].getValue());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithInvalidReferenceSimplex() {
        new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0, 1.0}});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithInvalidNumberOfPoints() {
        new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0}, {0.0, 0.0}});
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        assertEquals(3, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateWithNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        simplex.iterate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateWithNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        simplex.iterate((MultivariateFunction) () -> 0.0, null);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        assertArrayEquals(new double[]{0.0, 1.0}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{1.0, 0.0}, simplex.simplex[1].getPointRef());
        assertNull(simplexic.simplex[1].getValue());
        assertArrayEquals(new double[]{0.5, 0.5}, simplex.simplex[2].getPointRef());
        assertNull(simplexic.simplex[2].getValue());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        assertArrayEquals(new double[]{0.5, 0.5}, simplex.simplex[0].getPointRef());
        assertNull(simplexic.simplex[0].getValue());
        assertArrayEquals(new double[]{1.0, 0.5}, simplex.simplex[1].getPointRef());
        assertEquals(1.5, simplexic.simplex[1].getValue(), 0.001);
        assertArrayEquals(new double[]{0.5, 1.0}, simplex.simplex[2].getPointRef());
        assertEquals(1.5, simplexic.simplex[2].getValue(), 0.001);
        assertArrayEquals(new double[]{0.5, 0.5}, simplex.simplex[3].getPointRef());
        assertEquals(Double.NaN, simplexic.simplex[3].getValue());
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        PointValuePair worstPoint = simplex.getPoint(3);
        double originalValue = worstPoint.getValue();
        double newValue = 2.0;
        PointValuePair newPoint = new PointValuePair(new double[]{1.1, 1.1}, newValue);
        simplex.replaceWorstPoint(newPoint, Comparator.comparingDouble(o -> o.getValue()));
        PointValuePair newWorstPoint = simplex.getPoint(3);
        assertEquals(newValue, newWorstPoint.getValue(), 0.001);
        assertNotEquals(originalValue, newWorstPoint.getValue());
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(new double[]{0.5, 0.5}, points[0].getPointRef());
        assertNull(points[0].getValue());
        assertArrayEquals(new double[]{1.0, 0.5}, points[1].getPointRef());
        assertEquals(1.5, points[1].getValue(), 0.001);
        assertArrayEquals(new double[]{0.5, 1.0}, points[2].getPointRef());
        assertEquals(1.5, points[2].getValue(), 0.001);
        assertArrayEquals(new double[]{1.1, 1.1}, points[3].getPointRef());
        assertEquals(2.0, points[3].getValue(), 0.001);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        PointValuePair point = simplex.getPoint(1);
        assertArrayEquals(new double[]{1.0, 0.5}, point.getPointRef());
        assertEquals(1.5, point.getValue(), 0.001);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        double[] newPoint = {1.2, 1.2};
        simplex.setPoint(1, new PointValuePair(newPoint, Double.NaN));
        PointValuePair point = simplex.getPoint(1);
        assertArrayEquals(new double[]{1.2, 1.2}, point.getPointRef());
        assertNull(point.getValue());
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0});
        double[] startPoint = {0.5, 0.5};
        simplex.build(startPoint);
        MultivariateFunction function = (x) -> Math.pow(Math.sin(Math.PI / 2 * x[0]) + Math.sin(Math.PI / 2 * x[1]), 2);
        simplex.evaluate(function, Comparator.comparingDouble(o -> o.getValue()));
        PointValuePair[] newPoints = new PointValuePair[]{new PointValuePair(new double[]{1.1, 1.1}, 2.0), new PointValuePair(new double[]{0.6, 0.6}, Double.NaN)};
        simplex.setPoints(newPoints);
        PointValuePair point1 = simplex.getPoint(1);
        PointValuePair point2 = simplex.getPoint(2);
        assertArrayEquals(new double[]{1.1, 1.1}, point1.getPointRef());
        assertEquals(2.0, point1.getValue(), 0.001);
        assertArrayEquals(new double[]{0.6, 0.6}, point2.getPointRef());
        assertNull(point2.getValue());
    }
}