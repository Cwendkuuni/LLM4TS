package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Arrays;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConstructorArgument() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConstructorArgument() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroSteps() {
        double[] steps = new double[3];
        new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidReferenceSimplexDimension() {
        double[][] referenceSimplex = new double[2][3];
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplexPoint() {
        double[][] referenceSimplex = new double[][]{new double[]{1.0, 2.0}, new double[]{-1.0, 2.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesInSimplex() {
        double[][] referenceSimplex = new double[][]{new double[]{1.0, 2.0}, new double[]{1.0, 2.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidStartPointDimension() {
        double[] startPoint = new double[2];
        new AbstractSimplex(new AbstractSimplex(3)).build(startPoint);
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, simplex.getSize());
        for (PointValuePair point : points) {
            assertNotNull(point.getPointRef());
        }
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = new MockMultivariateFunction();
        simplex.evaluate(evaluationFunction, null);
        for (PointValuePair point : simplex.getPoints()) {
            assertEquals(evaluationFunction.value(point.getPointRef()), point.getValue(), 0.001);
        }
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair worstPoint = simplex.getPoint(2);
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 2.0);
        simplex.replaceWorstPoint(newPoint, null);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, simplex.getSize());
        assertNotEquals(worstPoint.getPointRef(), points[2].getPointRef());
        assertArrayEquals(newPoint.getPointRef(), points[2].getPointRef());
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(1);
        assertEquals(2, point.getIndex());
        assertArrayEquals(new double[]{1.0 + startPoint[0], 2.0 + startPoint[1], 3.0 + startPoint[2]}, point.getPointRef());
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 2.0);
        simplex.setPoint(-1, point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsInvalidSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = new PointValuePair[2];
        simplex.setPoints(points);
    }

    static class MockMultivariateFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            return 0.0;
        }
    }
}