package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        int dimension = 3;
        try {
            new AbstractSimplex(dimension, -1.0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }

        double[] steps = new double[] {1.0, 2.0, 3.0};
        AbstractSimplex abstractSimplex = new AbstractSimplex(steps);
        assertEquals(dimension, abstractSimplex.getDimension());
        assertEquals(steps.length, abstractSimplex.getSize());

        try {
            new AbstractSimplex(steps, dimension + 1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        double[][] referenceSimplex = new double[][] {{1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0, 3.0}};
        AbstractSimplex abstractSimplexFromReference = new AbstractSimplex(referenceSimplex);
        assertEquals(dimension, abstractSimplexFromReference.getDimension());
        assertEquals(3, abstractSimplexFromReference.getSize());
    }

    @Test
    public void testBuild() {
        int dimension = 3;
        double[] startPoint = new double[] {0.0, 0.0, 0.0};
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        abstractSimplex.build(startPoint);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(dimension + 1, points.length);
        for (PointValuePair pointValuePair : points) {
            assertNotNull(pointValuePair);
            double[] point = pointValuePair.getPointRef();
            assertArrayEquals(startPoint, point);
        }
    }

    @Test
    public void testEvaluate() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        double[] startPoint = new double[] {0.0, 0.0, 0.0};
        abstractSimplex.build(startPoint);

        abstractSimplex.evaluate(function, comparator);

        PointValuePair[] points = abstractSimplex.getPoints();
        for (PointValuePair pointValuePair : points) {
            assertEquals(Double.NaN, pointValuePair.getValue(), 0.0);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        double[] startPoint = new double[] {0.0, 0.0, 0.0};
        abstractSimplex.build(startPoint);

        double[] worstPoint = new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        PointValuePair worstPointPair = new PointValuePair(worstPoint, Double.POSITIVE_INFINITY);

        abstractSimplex.replaceWorstPoint(worstPointPair, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair[] points = abstractSimplex.getPoints();
        PointValuePair worstPointInSimple = points[dimension];
        assertArrayEquals(worstPoint, worstPointInSimple.getPointRef());
        assertEquals(Double.POSITIVE_INFINITY, worstPointInSimple.getValue(), 0.0);
    }

    @Test
    public void testGetPoint() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        double[] startPoint = new double[] {0.0, 0.0, 0.0};
        abstractSimplex.build(startPoint);

        PointValuePair point = abstractSimplex.getPoint(0);
        assertArrayEquals(startPoint, point.getPointRef());
    }

    @Test
    public void testSetPoint() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        double[] startPoint = new double[] {0.0, 0.0, 0.0};
        PointValuePair point = new PointValuePair(new double[] {1.0, 1.0, 1.0}, Double.NaN);

        abstractSimplex.build(startPoint);
        abstractSimplex.setPoint(0, point);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(point, points[0]);
    }

    @Test
    public void testSetPoints() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        double[][] points = new double[][] {{1.0, 1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0, 3.0}};
        PointValuePair[] pointValuePairs = new PointValuePair[points.length];
        for (int i = 0; i < points.length; i++) {
            pointValuePairs[i] = new PointValuePair(points[i], Double.NaN);
        }

        abstractSimplex.setPoints(pointValuePairs);

        PointValuePair[] simplex = abstractSimplex.getPoints();
        assertArrayEquals(pointValuePairs, simplex);
    }
}