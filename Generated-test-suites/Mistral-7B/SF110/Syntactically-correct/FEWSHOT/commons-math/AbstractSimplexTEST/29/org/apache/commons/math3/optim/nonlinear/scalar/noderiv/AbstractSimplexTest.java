package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.util.Precision;

public class AbstractSimplexTest {

    @Test(expected = NotStrictivelyPositiveException.class)
    public void testAbstractSimplexNegativeDimension() {
        new AbstractSimplex(-2);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConfiguration() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexEmptyConfiguration() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedConfiguration() {
        new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConfiguration() {
        new AbstractSimplex(new double[]{0.0, 0.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVertices() {
        new AbstractSimplex(new double[]{0.0, 0.0, 0.0});
    }

    @Test
    public void testAbstractSimplexConstructorWithDimension() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexConstructorWithSideLength() {
        AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexConstructorWithSteps() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 1.0}, {1.0, 1.0}, {3.0, 3.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate((f, p) -> {}, null);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testEvaluateNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.evaluate(null, Comparator.comparingDouble(p -> p.getValue()));
    }

    @Test(expected = MaxEvalException.class)
    public void testEvaluateMaxEvalException() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = f -> Double.MAX_VALUE;
        simplex.evaluate(function, Comparator.comparingDouble(p -> p.getValue()));
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        simplex.replaceWorstPoint(point, Comparator.comparingDouble(p -> p.getValue()));
        PointValuePair worstPoint = simplex.getPoint(3);
        assertEquals(point, worstPoint);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair point = simplex.getPoint(1);
        assertNotNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        simplex.getPoint(5);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        simplex.setPoint(1, point);
        PointValuePair newPoint = simplex.getPoint(1);
        assertEquals(point, newPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0});
        simplex.setPoint(5, new PointValuePair(new double[]{1.0, 1.0}, 1.0));
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[4];
        points[0] = new PointValuePair(new double[]{0.0, 0.0}, Double.NaN);
        points[1] = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
        points[2] = new PointValuePair(new double[]{2.0, 2.0}, 2.0);
        points[3] = new PointValuePair(new double[]{3.0, 3.0}, 3.0);
        simplex.setPoints(points);
        PointValuePair[] newPoints = simplex.getPoints();
        assertArrayEquals(points, newPoints);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsMismatchedSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[3];
        simplex.setPoints(points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
        assertEquals(3, steps.length);
        assertEquals(2.0, steps[0], 0.0);
        assertEquals(2.0, steps[1], 0.0);
        assertEquals(2.0, steps[2], 0.0);
    }
}