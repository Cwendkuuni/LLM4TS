package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.multivariate.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        try {
            new AbstractSimplex(-1);
            fail("Expected NegativeArgumentException");
        } catch (NegativeArgumentException e) {
            // expected
        }

        try {
            new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // expected
        }

        AbstractSimplex simplex1 = new AbstractSimplex(3);
        assertEquals(3, simplex1.getDimension());
        assertEquals(4, simplex1.getSize());

        AbstractSimplex simplex2 = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex2.getDimension());
        assertEquals(4, simplex2.getSize());

        AbstractSimplex simplex3 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, simplex3.getDimension());
        assertEquals(4, simplex3.getSize());

        AbstractSimplex simplex4 = new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0}});
        assertEquals(2, simplex4.getDimension());
        assertEquals(3, simplex4.getSize());

        try {
            new AbstractSimplex(new double[][]{});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new AbstractSimplex(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }

        try {
            new AbstractSimplex(new double[][]{{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}});
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            new AbstractSimplex(new double[][]{{0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}});
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
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
        // Abstract method, can't test
    }

    @Test
    public void testBuild() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);

        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            double[] point = vertex.getPointRef();
            assertEquals(startPoint[i], point[0], 1e-9);
            assertEquals(Double.NaN, vertex.getValue());
        }

        simplex.evaluate(mockFunction, null);
        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            assertNotNull(vertex.getValue());
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            double fx = mockFunction.value(startPoint);
            assertEquals(fx, vertex.getValue(), 1e-9);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        PointValuePair worstPoint = simplex.getPoint(0);
        double worstValue = worstPoint.getValue();

        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN);
        simplex.replaceWorstPoint(newPoint, null);

        PointValuePair newWorstPoint = simplex.getPoint(0);
        double newWorstValue = newWorstPoint.getValue();

        assertTrue(newWorstValue < worstValue);
    }

    @Test
    public void testGetPoints() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        PointValuePair[] points = simplex.getPoints();

        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair point = points[i];
            double[] pointRef = point.getPointRef();
            assertEquals(startPoint[i], pointRef[0], 1e-9);
            assertNotNull(point.getValue());
        }

        assertEquals(simplex.getSize(), points.length);
    }

    @Test
    public void testGetPoint() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        PointValuePair point = simplex.getPoint(1);
        double[] pointRef = point.getPointRef();
        assertEquals(startPoint[1], pointRef[0], 1e-9);
        assertNotNull(point.getValue());
    }

    @Test
    public void testSetPoint() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        PointValuePair point = simplex.getPoint(1);
        double[] newPoint = new double[]{4.0, 5.0, 6.0};
        simplex.setPoint(1, new PointValuePair(newPoint, Double.NaN));

        PointValuePair newPointInSimplex = simplex.getPoint(1);
        assertArrayEquals(newPoint, newPointInSimplex.getPointRef());
        assertEquals(Double.NaN, newPointInSimplex.getValue());

        simplex.evaluate(mockFunction, null);
        assertEquals(Double.NaN, newPointInSimplex.getValue());
    }

    @Test
    public void testSetPoints() {
        MultivariateFunction mockFunction = mock(MultivariateFunction.class);
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate(mockFunction, null);

        PointValuePair[] newPoints = new PointValuePair[]{
            new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN),
            new PointValuePair(new double[]{7.0, 8.0, 9.0}, Double.NaN),
            new PointValuePair(new double[]{10.0, 11.0, 12.0}, Double.NaN)
        };
        simplex.setPoints(newPoints);

        PointValuePair[] points = simplex.getPoints();

        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair point = points[i];
            double[] pointRef = point.getPointRef();
            assertArrayEquals(newPoints[i].getPointRef(), pointRef);
            assertEquals(Double.NaN, point.getValue());
        }

        assertEquals(simplex.getSize(), points.length);
    }

    @Test
    public void testCreateHypercubeSteps() {
        assertEquals(3, AbstractSimplex.createHypercubeSteps(3, 1.0).length);
        assertEquals(4, AbstractSimplex.createHypercubeSteps(3, 1.0).length);
    }
}