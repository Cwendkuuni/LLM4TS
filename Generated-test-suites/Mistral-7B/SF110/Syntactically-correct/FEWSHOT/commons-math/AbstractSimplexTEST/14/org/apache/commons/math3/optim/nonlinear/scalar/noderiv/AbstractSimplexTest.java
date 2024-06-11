package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullConstructorArgs() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroDimension() {
        new AbstractSimplex(0);
    }

    @Test
    public void testAbstractSimplexWithCustomStartConfiguration() {
        double[][] referenceSimplex = {
                {0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.5}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
        assertArrayEquals(new double[][]{
                {0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.5}
        }, simplex.startConfiguration);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithInvalidNumberOfPoints() {
        double[][] referenceSimplex = {
                {0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.5},
                {-1.0, -1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithPointsOfDifferentDimensions() {
        double[][] referenceSimplex = {
                {0.0, 0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.5, 1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualPoints() {
        double[][] referenceSimplex = {
                {0.0, 0.0},
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

    @Test
    public void testAbstractSimplexIterate() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return FunctionUtils.pow(Precision.round(PointValuePairComparator.KEY_TO_POINTS.get(1)[0][0], 2), 2) + 1;
            }
        };
        PointValuePairComparator comparator = new PointValuePairComparator();
        simplex.iterate(f, comparator);
        PointValuePair worstPoint = simplex.getPoint(simplexi.size() - 1);
        assertEquals(2.0, worstPoint.getValue(), 0.001);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        double[] startPoint = {-1.0, -1.0};
        simplex.build(startPoint);
        PointValuePair worstPoint = simplexi.getPoint(simplexi.size() - 1);
        assertEquals(-1.0, worstPoint.getPointRef()[0], 0.001);
        assertEquals(-1.0, worstPoint.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, worstPoint.getValue());
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (PointValuePair point : points) {
            assertEquals(Double.class, point.getClass().getComponentType());
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        PointValuePair point = simplex.getPoint(0);
        assertEquals(0.0, point.getPointRef()[0], 0.001);
        assertEquals(0.0, point.getPointRef()[1], 0.001);
        assertEquals(0.0, point.getPointRef()[2], 0.001);
        assertEquals(Double.NaN, point.getValue());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.getPoint(-1);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        double[] newPoint = new double[]{1.0, 1.0, 1.0};
        simplex.setPoint(0, new PointValuePair(newPoint, Double.NaN));
        PointValuePair point = simplex.getPoint(0);
        assertEquals(1.0, point.getPointRef()[0], 0.001);
        assertEquals(1.0, point.getPointRef()[1], 0.001);
        assertEquals(1.0, point.getPointRef()[2], 0.001);
        assertEquals(Double.NaN, point.getValue());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 1.0, 1.0}, Double.NaN));
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] newPoints = new PointValuePair[4];
        newPoints[0] = new PointValuePair(new double[]{1.0, 1.0, 1.0}, Double.NaN);
        newPoints[1] = new PointValuePair(new double[]{2.0, 2.0, 2.0}, Double.NaN);
        newPoints[2] = new PointValuePair(new double[]{3.0, 3.0, 3.0}, Double.NaN);
        newPoints[3] = new PointValuePair(new double[]{4.0, 4.0, 4.0}, Double.NaN);
        simplex.setPoints(newPoints);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (PointValuePair point : points) {
            assertEquals(Double.class, point.getClass().getComponentType());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithInvalidSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] newPoints = new PointValuePair[3];
        simplex.setPoints(newPoints);
    }
}