package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertNull(simplex.getPoints());
    }

    @Test
    public void testAbstractSimplex_int_double() {
        AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
        assertNull(simplex.getPoints());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_doubleArray_NullArgumentException() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArray_ZeroException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArray_DimensionMismatchException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 3.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_doubleArray_MathIllegalArgumentException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 2.0});
    }

    @Test
    public void testAbstractSimplex_doubleArrayArray() {
        AbstractSimplex simplex = new AbstractSimplex(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}});
        assertEquals(2, simplex.getDimension());
        PointValuePair[] points = simplex.getPoints();
        assertEquals(3, points.length);
        assertEquals(1.0, points[0].getPointRef()[0], 0.001);
        assertEquals(1.0, points[0].getPointRef()[1], 0.001);
        assertEquals(2.0, points[1].getPointRef()[0], 0.001);
        assertEquals(1.0, points[1].getPointRef()[1], 0.001);
        assertEquals(1.0, points[2].getPointRef()[0], 0.001);
        assertEquals(1.0, points[2].getPointRef()[1], 0.001);
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIterate_MultivariateFunction_Comparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        simplex.iterate(function, comparator);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(1.0, points[0].getPointRef()[0], 0.001);
        assertEquals(1.0, points[0].getPointRef()[1], 0.001);
        assertEquals(1.0, points[1].getPointRef()[0], 0.001);
        assertEquals(1.0, points[1].getPointRef()[1], 0.001);
        assertEquals(2.0, points[2].getPointRef()[0], 0.001);
        assertEquals(2.0, points[2].getPointRef()[1], 0.001);
        assertEquals(2.0, points[3].getPointRef()[0], 0.001);
        assertEquals(2.0, points[3].getPointRef()[1], 0.001);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        simplex.evaluate(function, comparator);
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; i++) {
            double value = function.value(points[i].getPointRef());
            assertEquals(points[i].getValue(), value, 0.001);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        PointValuePair pointToReplace = new PointValuePair(new double[]{3.0, 3.0}, 10.0);
        simplex.replaceWorstPoint(pointToReplace, comparator);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(3.0, points[3].getPointRef()[0], 0.001);
        assertEquals(3.0, points[3].getPointRef()[1], 0.001);
        assertEquals(10.0, points[3].getValue(), 0.001);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        PointValuePair[] copy = simplex.getPoints();
        assertArrayEquals(points, copy);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(1);
        assertEquals(2.0, point.getPointRef()[0], 0.001);
        assertEquals(2.0, point.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, point.getValue(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_OutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{3.0, 3.0}, 10.0);
        simplex.setPoint(-1, point);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{3.0, 3.0}, 10.0);
        simplex.setPoint(1, point);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(3.0, points[1].getPointRef()[0], 0.001);
        assertEquals(3.0, points[1].getPointRef()[1], 0.001);
        assertEquals(10.0, points[1].getValue(), 0.001);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[]{
                new PointValuePair(new double[]{1.0, 2.0}, 1.0),
                new PointValuePair(new double[]{2.0, 1.0}, 2.0),
                new PointValuePair(new double[]{3.0, 3.0}, 3.0)
        };
        simplex.setPoints(points);
        PointValuePair[] simplexPoints = simplex.getPoints();
        assertArrayEquals(points, simplexPoints);
    }
}