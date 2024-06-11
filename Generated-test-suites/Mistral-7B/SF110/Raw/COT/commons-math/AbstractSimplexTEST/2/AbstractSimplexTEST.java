package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testConstructorWithInt() {
        try {
            AbstractSimplex abstractSimplex = new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected exception
        }

        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        assertEquals(3, abstractSimplex.getDimension());
    }

    @Test
    public void testConstructorWithIntAndSideLength() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, abstractSimplex.getDimension());
    }

    @Test
    public void testConstructorWithSteps() {
        double[] steps = {0.5, 1.0, 1.5};
        AbstractSimplex abstractSimplex = new AbstractSimplex(steps);
        assertEquals(3, abstractSimplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullSteps() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroSteps() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidSteps() {
        double[] steps = {1.0, 2.0, 3.0, 4.0};
        AbstractSimplex abstractSimplex = new AbstractSimplex(steps);
    }

    @Test
    public void testConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        AbstractSimplex abstractSimplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, abstractSimplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyReferenceSimplex() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[0][]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidReferenceSimplexDimension() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 3.0}, {3.0}};
        AbstractSimplex abstractSimplex = new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidReferenceSimplexEqualVertices() {
        double[][] referenceSimplex = {{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}};
        AbstractSimplex abstractSimplex = new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        assertEquals(3, abstractSimplex.getDimension());

        abstractSimplex = new AbstractSimplex(new double[0][]);
        assertEquals(0, abstractSimplex.getDimension());

        abstractSimplex = new AbstractSimplex(new double[] {1.0});
        assertEquals(1, abstractSimplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        assertEquals(4, abstractSimplex.getSize());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateWithNullFunction() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        abstractSimplex.iterate(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateWithNullComparator() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        abstractSimplex.iterate(new DummyFunction(), null);
    }

    @Test
    public void testBuild() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(4, points.length);
        for (PointValuePair point : points) {
            assertTrue(point.getPointRef()[0].length == 3);
        }

        double[][] expectedPoints = {{1.0, 1.0, 1.0}, {1.0, 1.0, 2.0}, {1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}, {1.0, 3.0, 1.0}, {1.0, 3.0, 2.0}, {1.0, 3.0, 3.0}, {1.1, 1.1, 1.1}, {1.1, 1.1, 2.1}, {1.1, 1.1, 3.1}, {1.2, 1.2, 1.2}, {1.2, 1.2, 2.2}, {1.2, 1.2, 3.2}, {1.3, 1.3, 1.3}, {1.3, 1.3, 2.3}, {1.3, 1.3, 3.3}};
        assertArrayEquals(expectedPoints, abstractSimplex.getStartConfiguration());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithInvalidStartPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        abstractSimplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        MultivariateFunction function = new DummyFunction();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        abstractSimplex.evaluate(function, comparator);
        PointValuePair worstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
        assertEquals(Double.NaN, worstPoint.getValue(), 0.0);
        assertArrayEquals(new double[] {1.3, 1.3, 1.3}, worstPoint.getPointRef());
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        MultivariateFunction function = new DummyFunction();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        abstractSimplex.evaluate(function, comparator);
        PointValuePair worstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
        abstractSimplex.replaceWorstPoint(new PointValuePair(new double[] {1.5, 1.5, 1.5}, 5.0), comparator);
        PointValuePair newWorstPoint = abstractSimplex.getPoint(abstractSimplex.getSize() - 1);
        assertEquals(5.0, newWorstPoint.getValue(), 0.0);
        assertArrayEquals(new double[] {1.5, 1.5, 1.5}, newWorstPoint.getPointRef());
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        PointValuePair[] expectedPoints = abstractSimplex.getPoints();
        PointValuePair[] points = new PointValuePair[expectedPoints.length];
        System.arraycopy(expectedPoints, 0, points, 0, expectedPoints.length);
        assertArrayEquals(points, abstractSimplex.getPoints());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        abstractSimplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithInvalidIndex() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        abstractSimplex.setPoint(-1, new PointValuePair(new double[] {1.0, 1.0, 1.0}, Double.NaN));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointsWithInvalidSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        PointValuePair[] points = new PointValuePair[5];
        abstractSimplex.setPoints(points);
    }

    @Test
    public void testSetPointsWithValidSize() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        abstractSimplex.build(startPoint);
        PointValuePair[] points = new PointValuePair[4];
        System.arraycopy(abstractSimplex.getPoints(), 0, points, 0, abstractSimplex.getSize());
        abstractSimplex.setPoints(points);
        assertEquals(points.length, abstractSimplex.getSize());
    }
}