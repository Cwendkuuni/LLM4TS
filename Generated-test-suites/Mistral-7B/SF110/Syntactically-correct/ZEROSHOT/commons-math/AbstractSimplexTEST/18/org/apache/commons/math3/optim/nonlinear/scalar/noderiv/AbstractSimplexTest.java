package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        int n = 3;
        simplex = new Simplex(n);
    }

    @Test
    public void testAbstractSimplex() {
        assertEquals(n, simplex.getDimension());
        assertEquals(n + 1, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConstructor() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConstructor() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConfiguration() {
        double[] steps = new double[3];
        steps[0] = 0;
        try {
            new AbstractSimplex(steps);
        } catch (Exception e) {
            fail("Expected ZeroException, but got: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedDimensionConfiguration() {
        double[] steps = new double[3];
        steps[0] = 3;
        try {
            new AbstractSimplex(steps);
        } catch (Exception e) {
            fail("Expected DimensionMismatchException, but got: " + e.getMessage());
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {1.0, 2.0}};
        try {
            new AbstractSimplex(referenceSimplex);
        } catch (Exception e) {
            fail("Expected MathIllegalArgumentException, but got: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplexReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
        simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(new Object[][]{
                new Object[]{0.0, 1.0, Double.NaN},
                new Object[]{1.0, 0.0, Double.NaN},
                new Object[]{1.0, 1.0, Double.NaN}
        }, Arrays.stream(points).map(p -> new Object[]{p.getPointRef()[0], p.getPointRef()[1], p.getValue()}).toArray());
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[]{0.5, 1.5, 2.5};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(new Object[][]{
                new Object[]{0.5, 1.5, Double.NaN},
                new Object[]{1.0, 1.5, Double.NaN},
                new Object[]{1.5, 1.5, Double.NaN},
                new Object[]{0.5, 2.5, Double.NaN},
                new Object[]{1.0, 2.5, Double.NaN},
                new Object[]{1.5, 2.5, Double.NaN}
        }, Arrays.stream(points).map(p -> new Object[]{p.getPointRef()[0], p.getPointRef()[1], p.getValue()}).toArray());
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction function = new Add();
        simplex.evaluate(function, null);
        PointValuePair[] points = simplex.getPoints();
        double[] expectedValues = {3.0, 3.0, 3.0, 0.0, 0.0, 0.0};
        for (int i = 0; i < 6; ++i) {
            assertEquals(expectedValues[i], points[i].getValue(), 1e-12);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        MultivariateFunction function = new Add();
        simplex.evaluate(function, null);
        PointValuePair worstPoint = simplex.getPoint(1);
        PointValuePair newPoint = new PointValuePair(new double[]{1.1, 1.1}, function.value(new double[]{1.1, 1.1}), false);
        simplex.replaceWorstPoint(newPoint, null);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(newPoint.getValue(), points[1].getValue(), 1e-12);
    }

    @Test
    public void testGetPoint() {
        PointValuePair point = simplex.getPoint(2);
        assertArrayEquals(new double[]{1.5, 1.5}, point.getPointRef());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        simplex.getPoint(-1);
    }

    @Test
    public void testSetPoint() {
        PointValuePair point = new PointValuePair(new double[]{3.0, 3.0}, 12.0, false);
        simplex.setPoint(1, point);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(12.0, points[1].getValue(), 1e-12);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = new PointValuePair[6];
        for (int i = 0; i < 6; ++i) {
            newPoints[i] = new PointValuePair(new double[]{i, i}, i * 2.0, false);
        }
        simplex.setPoints(newPoints);
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < 6; ++i) {
            assertEquals(newPoints[i].getValue(), points[i].getValue(), 1e-12);
        }
    }
}