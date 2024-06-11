package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_intNull() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_intZero() {
        new AbstractSimplex(0);
    }

    @Test
    public void testAbstractSimplex_int_double() {
        AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_doubleArrayNull() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayEmpty() {
        new AbstractSimplex(new double[0]);
    }

    @Test
    public void testAbstractSimplex_doubleArray() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayInvalidDimension() {
        double[] steps = {1.0, 2.0, 3.0, 4.0};
        new AbstractSimplex(steps);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_doubleArrayEqualVertices() {
        double[] steps = {1.0, 1.0, 2.0};
        new AbstractSimplex(steps);
    }

    @Test
    public void testAbstractSimplex_doubleArrayRefSimplex() {
        double[][] referenceSimplex = {
            {0.0, 0.0},
            {1.0, 1.0},
            {2.0, 2.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_doubleArrayRefSimplexInvalidLength() {
        double[][] referenceSimplex = {};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayRefSimplexInvalidVertexLength() {
        double[][] referenceSimplex = {{1.0, 1.0}, {1.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_doubleArrayRefSimplexEqualVertices() {
        double[][] referenceSimplex = {
            {0.0, 0.0},
            {0.0, 0.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIterateNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new SimpleFunction(), null);
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        simplex.iterate(new SimpleFunction(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0}, simplex.getPoints()[0].getPointRef());
        assertArrayEquals(new double[]{0.0, 0.0}, simplex.getPoints()[1].getPointRef());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)}, simplex.getPoints()[2].getPointRef());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)}, simplex.getPoints()[3].getPointRef());
        assertArrayEquals(new double[]{-1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)}, simplex.getPoints()[4].getPointRef());
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0}, simplex.getPoints()[0].getPointRef());
        assertArrayEquals(new double[]{2.0, 1.0, 2.0, 2.0}, simplex.getPoints()[1].getPointRef());
        assertArrayEquals(new double[]{1.0, 2.0, 1.0, 2.0}, simplex.getPoints()[2].getPointRef());
        assertArrayEquals(new double[]{1.0, 1.0, 2.0, 2.0}, simplex.getPoints()[3].getPointRef());
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        MultivariateFunction function = new SimpleFunction();
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, 0.0, 0.0}, simplex.getPoints()[0].getValueRef());
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN, -Math.sqrt(3.0)}, simplex.getPoints()[1].getValueRef());
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, 0.0, 0.0}, simplex.getPoints()[2].getValueRef());
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, 0.0, 0.0}, simplex.getPoints()[3].getValueRef());
        assertArrayEquals(new double[]{0.0, 0.0, Double.NaN, Double.NaN}, simplex.getPoints()[4].getValueRef());
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        simplex.replaceWorstPoint(pointValuePair, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0}, simplex.getPoints()[0].getPointRef());
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0}, simplex.getPoints()[1].getPointRef());
        assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0}, simplex.getPoints()[2].getPointRef());
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0}, simplex.getPoints()[3].getPointRef());
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0}, simplex.getPoints()[4].getPointRef());
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(new double[]{0.0, 0.0}, points[0].getPointRef());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)}, points[1].getPointRef());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)}, points[2].getPointRef());
        assertArrayEquals(new double[]{-1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)}, points[3].getPointRef());
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)}, points[4].getPointRef());
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair point = simplex.getPoint(2);
        assertArrayEquals(new double[]{1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)}, point.getPointRef());
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        simplex.setPoint(2, point);
        assertArrayEquals(new double[]{1.0, 1.0}, simplex.getPoints()[2].getPointRef());
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[]{
                new PointValuePair(new double[]{1.0, 1.0}, 1.0),
                new PointValuePair(new double[]{2.0, 2.0}, 2.0),
                new PointValuePair(new double[]{3.0, 3.0}, 3.0)
        };
        simplex.setPoints(points);
        PointValuePair[] expectedPoints = new PointValuePair[]{
                new PointValuePair(new double[]{1.0, 1.0}, 1.0),
                new PointValuePair(new double[]{2.0, 2.0}, 2.0),
                new PointValuePair(new double[]{3.0, 3.0}, 3.0)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }
}