package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;

public class AbstractSimplexTest {

    @Test
    public void testConstructors() {
        try {
            new AbstractSimplex(-1);
            fail("Expected exception");
        } catch (NegativeArraySizeException ignored) {
        }

        try {
            new AbstractSimplex(0);
            fail("Expected exception");
        } catch (ZeroException ignored) {
        }

        AbstractSimplex simplex1 = new AbstractSimplex(3);
        assertEquals(3, simplex1.getDimension());
        AbstractSimplex simplex2 = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex2.getDimension());

        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex3 = new AbstractSimplex(steps);
        assertEquals(3, simplex3.getDimension());

        double[][] referenceSimplex = {{0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        AbstractSimplex simplex4 = new AbstractSimplex(referenceSimplex);
        assertEquals(3, simplex4.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullArgumentException() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroException() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDimensionMismatchException() {
        double[] steps = {1.0, 2.0, 3.0};
        new AbstractSimplex(new double[2][3]);

        double[][] referenceSimplex = {{0.0, 0.0}, {1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0, 3.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesInSimplex() {
        double[] steps = {1.0, 1.0, 0.0};
        new AbstractSimplex(steps);

        double[][] referenceSimplex = {{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {1.0, 1.0, 1.0}};
        new AbstractSimplex(referenceSimplex);

        double[][] referenceSimplex2 = {{0.0, 0.0}, {0.0, 0.0}};
        new AbstractSimplex(referenceSimplex2);
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

    @Test
    public void testIterate() {
        // Implement this method in a subclass and test it there.
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] expected = {
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN),
                new PointValuePair(new double[]{2.0, 2.0, 3.0}, Double.NaN),
                new PointValuePair(new double[]{3.0, 2.0, 3.0}, Double.NaN),
                new PointValuePair(new double[]{3.0, 3.0, 3.0}, Double.NaN)
        };
        assertArrayEquals(expected, simplex.getPoints());
    }

    @Test
    public void testEvaluate() {
        // Implement this method in a subclass and test it there.
    }

    @Test
    public void testReplaceWorstPoint() {
        // Implement this method in a subclass and test it there.
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(points, simplex.simplex);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = simplex.getPoint(0);
        assertEquals(0, point.getIndex());
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, point.getPointRef());
        assertNull(point.getValue());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRangeException2() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simplex.setPoint(0, point);
        assertEquals(point, simples.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRangeException() {
        AbstractSimplex simples = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simples.setPoint(-1, point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRangeException2() {
        AbstractSimplex simples = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        simples.setPoint(4, point);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simples = new AbstractSimplex(3);
        PointValuePair[] points = {
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0),
                new PointValuePair(new double[]{2.0, 2.0, 3.0}, 2.0),
                new PointValuePair(new double[]{3.0, 2.0, 3.0}, 3.0)
        };
        simples.setPoints(points);
        assertArrayEquals(points, simples.getPoints());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsDimensionMismatchException() {
        AbstractSimplex simples = new AbstractSimplex(3);
        PointValuePair[] points = {
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0),
                new PointValuePair(new double[]{1.0, 2.0}, 2.0),
                new PointValuePair(new double[]{3.0, 2.0, 3.0}, 3.0)
        };
        simples.setPoints(points);
    }
}