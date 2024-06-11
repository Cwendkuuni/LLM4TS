package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;

import org.apache.commons.cli.util.PointValuePair;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithZeroStepsArray() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithNegativeDimension() {
        new AbstractSimplex(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithZeroDimension() {
        new AbstractSimplex(0);
    }

    @Test
    public void testAbstractSimplexConstructorWithValidDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithZeroSideLength() {
        new AbstractSimplex(3, 0);
    }

    @Test
    public void testAbstractSimplexConstructorWithValidSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexConstructorWithValidSteps() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithEqualVertices() {
        double[] steps = {1.0, 1.0, 1.0};
        new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedReferenceSimple() {
        double[][] referenceSimplex = new double[1][];
        referenceSimplex[0] = new double[3];
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithNegativeSideLengthInReferenceSimple() {
        double[][] referenceSimplex = new double[][]{{-1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexConstructorWithValidReferenceSimple() {
        double[][] referenceSimplex = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(3, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithNegativeStartPoint() {
        double[] startPoint = {-1.0, 1.0, 1.0};
        new AbstractSimplex(3).build(startPoint);
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
    public void testAbstractSimplexBuildWithInvalidStartPointDimension() {
        double[] startPoint = {1.0, 1.0, 2.0};
        new AbstractSimplex(3).build(startPoint);
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(function, null);
        for (PointValuePair pointValuePair : simplex.getPoints()) {
            assertEquals(0.0, pointValuePair.getValue(), 1e-9);
        }
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(function, null);
        PointValuePair worstPoint = simplex.getPoints()[3];
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0, 1.0}, Double.POSITIVE_INFINITY);
        simplex.replaceWorstPoint(newPoint, null);
        assertNotEquals(worstPoint.getValue(), newPoint.getValue());
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0];
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(function, null);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(null, null);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointWithInvalidIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(null, null);
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 0.0);
        simplex.setPoint(-1, point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsWithInvalidSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(null, null);
        PointValuePair[] points = new PointValuePair[2];
        simplex.setPoints(points);
    }

    @Test
    public void testAbstractSimplexSetPointsWithValidSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(null, null);
        PointValuePair[] points = new PointValuePair[4];
        simplex.setPoints(points);
    }
}