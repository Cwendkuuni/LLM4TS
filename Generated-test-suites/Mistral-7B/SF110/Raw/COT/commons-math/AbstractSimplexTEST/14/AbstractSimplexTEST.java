package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;
    private PointValuePair[] testPoints;
    private MultivariateFunction testFunction;
    private Comparator<PointValuePair> comparator;

    @Before
    public void setUp() {
        simplex = new SimplexNelderMead(2); // Instantiate a subclass of AbstractSimplex
        testFunction = new TestMultivariateFunction();
        comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }

    @Test
    public void testConstructorWithInt() {
        simplex = new AbstractSimplex(2);
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testConstructorWithIntAndSideLength() {
        simplex = new AbstractSimplex(2, 1.0);
        assertEquals(2, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullArray() {
        simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithEmptyArray() {
        simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroSteps() {
        simplex = new AbstractSimplex(new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEqualVertices() {
        simplex = new AbstractSimplex(new double[]{1.0, 1.0});
    }

    @Test
    public void testGetDimension() {
        assertEquals(2, simplex.getDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSizeWithNegativeSize() {
        simplex.setSize(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSizeWithNonMatchingSize() {
        simplex.setSize(3);
    }

    @Test
    public void testIterate() {
        simplex.build(new double[]{0.0, 0.0});
        simplex.iterate(testFunction, comparator);
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[]{1.0, 1.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(3, points.length);
        for (PointValuePair point : points) {
            assertTrue(point.getValue() > Double.NaN);
        }
    }

    @Test
    public void testEvaluate() {
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(testFunction, comparator);
        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        assertTrue(worstPoint.getValue() >= Double.MAX_VALUE);
    }

    @Test
    public void testReplaceWorstPoint() {
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY);
        simplex.replaceWorstPoint(newPoint, comparator);
        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        assertEquals(newPoint, worstPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithNegativeIndex() {
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointWithGreaterIndex() {
        simplex.getPoint(5);
    }

    @Test
    public void testSetPointWithNegativeIndex() {
        simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 1.0}));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointWithGreaterIndex() {
        simplex.setPoint(5, new PointValuePair(new double[]{1.0, 1.0}));
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = new PointValuePair[3];
        newPoints[0] = new PointValuePair(new double[]{1.0, 1.0}, Double.POSITIVE_INFINITY);
        newPoints[1] = new PointValuePair(new double[]{1.0, 2.0}, Double.POSITIVE_INFINITY);
        newPoints[2] = new PointValuePair(new double[]{2.0, 1.0}, Double.POSITIVE_INFINITY);
        simplex.setPoints(newPoints);
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals(newPoints, points);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithMismatchingSize() {
        PointValuePair[] newPoints = new PointValuePair[4];
        simplex.setPoints(newPoints);
    }

    static class TestMultivariateFunction implements MultivariateFunction {
        @Override
        public double value(double[] arg0) {
            return Math.pow(Math.sin(arg0[0]) * Math.sin(arg0[1]), 2.0);
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }
    }
}