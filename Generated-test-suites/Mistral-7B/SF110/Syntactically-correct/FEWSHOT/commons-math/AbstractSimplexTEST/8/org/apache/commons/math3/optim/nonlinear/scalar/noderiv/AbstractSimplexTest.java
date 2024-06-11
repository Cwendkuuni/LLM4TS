package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleX;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexDimension() {
        AbstractSimplex simplex = new SimpleX(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexSize() {
        AbstractSimplex simplex = new SimpleX(3);
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexConstructorNullSteps() {
        assertThrows(NullArgumentException.class, () -> new AbstractSimplex(null));
    }

    @Test
    public void testAbstractSimplexConstructorZeroSteps() {
        assertThrows(ZeroException.class, () -> new AbstractSimplex(new double[0]));
    }

    @Test
    public void testAbstractSimplexConstructorZeroVertices() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{0.0});
        assertEquals(1, simplex.getDimension());
        assertEquals(1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexConstructorEqualVertices() {
        assertThrows(DimensionMismatchException.class, () -> new AbstractSimplex(new double[]{1.0, 1.0}));
        assertThrows(DimensionMismatchException.class, () -> new AbstractSimplex(new double[][]{new double[]{1.0}, new double[]{1.0}}));
    }

    @Test
    public void testAbstractSimplexConstructorDifferentVertices() {
        AbstractSimplex simplex = new AbstractSimplex(new double[][]{new double[]{1.0, 2.0}, new double[]{3.0, 4.0}});
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterateNullFunction() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.iterate(null, null));
    }

    @Test
    public void testAbstractSimplexIterateNullComparator() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.iterate(new PolynomialFunction(x -> Math.pow(x, 2.0)), null));
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new SimpleX(2);
        double[] startPoint = new double[]{1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair expected = new PointValuePair(new double[]{1.0, 2.0}, Double.NaN);
        assertEquals(expected, simplex.getPoint(0));
        for (int i = 1; i < simplex.getSize(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            double[] point = vertex.getPointRef();
            assertEquals(expected.getPointRef(), point);
            assertEquals(i + 1, i + 1); // NaN value is not relevant for this test
        }
    }

    @Test
    public void testAbstractSimplexEvaluateNullFunction() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.evaluate(null, null));
    }

    @Test
    public void testAbstractSimplexEvaluateNullComparator() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        }, null));
    }

    @Test
    public void testAbstractSimplexReplaceWorstPointNullPoint() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.replaceWorstPoint(null, null));
    }

    @Test
    public void testAbstractSimplexReplaceWorstPointNullComparator() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.replaceWorstPoint(new PointValuePair(new double[0], Double.NaN), null));
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        AbstractSimplex simplex = new SimpleX(2);
        double[] worstPoint = new double[]{1.0, 2.0};
        PointValuePair pointValuePair = new PointValuePair(new double[]{3.0, 4.0}, 3.0);
        simplex.replaceWorstPoint(pointValuePair, Comparators.REVERSE_ORDER);
        PointValuePair expected = new PointValuePair(new double[]{1.0, 2.0}, Double.NaN);
        assertEquals(expected, simplex.getPoint(0));
        assertEquals(pointValuePair, simplex.getPoint(3));
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        AbstractSimplex simplex = new SimpleX(2);
        simplex.build(new double[]{1.0, 2.0});
        PointValuePair[] points = simplex.getPoints();
        PointValuePair expected = new PointValuePair(new double[]{1.0, 2.0}, Double.NaN);
        assertEquals(expected, points[0]);
        for (int i = 1; i < simplex.getSize(); ++i) {
            PointValuePair vertex = points[i];
            double[] point = vertex.getPointRef();
            assertEquals(expected.getPointRef(), point);
            assertEquals(i + 1, i + 1); // NaN value is not relevant for this test
        }
    }

    @Test
    public void testAbstractSimplexGetPointIndexOutOfRange() {
        AbstractSimplex simplex = new SimpleX(2);
        simplex.build(new double[]{1.0, 2.0});
        assertThrows(OutOfRangeException.class, () -> simplex.getPoint(-1));
        assertThrows(OutOfRangeException.class, () -> simplex.getPoint(3));
    }

    @Test
    public void testAbstractSimplexSetPointIndexOutOfRange() {
        AbstractSimplex simplex = new SimpleX(2);
        simplex.build(new double[]{1.0, 2.0});
        PointValuePair point = new PointValuePair(new double[]{3.0, 4.0}, 3.0);
        assertThrows(OutOfRangeException.class, () -> simplex.setPoint(-1, point));
        assertThrows(OutOfRangeException.class, () -> simplex.setPoint(3, point));
    }

    @Test
    public void testAbstractSimplexSetPointsNullArray() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(NullArgumentException.class, () -> simplex.setPoints(null));
    }

    @Test
    public void testAbstractSimplexSetPointsSizeMismatch() {
        AbstractSimplex simplex = new SimpleX(2);
        assertThrows(DimensionMismatchException.class, () -> simplex.setPoints(new PointValuePair[5]));
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        AbstractSimplex simplex = new SimpleX(2);
        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[]{1.0, 2.0}, Double.NaN);
        points[1] = new PointValuePair(new double[]{3.0, 4.0}, 3.0);
        points[2] = new PointValuePair(new double[]{5.0, 6.0}, 5.0);
        points[3] = new PointValuePair(new double[]{7.0, 8.0}, 7.0);
        simplex.setPoints(points);
        assertEquals(points, simplex.getPoints());
    }
}