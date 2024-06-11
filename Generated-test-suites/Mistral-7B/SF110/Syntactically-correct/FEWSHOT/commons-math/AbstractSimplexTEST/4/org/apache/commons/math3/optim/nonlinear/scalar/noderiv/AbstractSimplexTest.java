package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.util.Precision;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexDimension() {
        AbstractSimplex simplex = new Simplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        AbstractSimplex simplex = new Simplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullParameter() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexEmptyConfiguration() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroVertex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConfigureDifferentDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVertices() {
        AbstractSimplex simplex = new AbstractSimplex(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {0.1, 0.2, 0.3};
        simplex.build(startPoint);

        assertArrayEquals(new double[]{0.1, 0.1, 0.2, 0.2, 0.3, 0.3}, convertPointsToArray(simplex.getPoints()));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexBuildDifferentDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {0.1, 0.2};
        simplex.build(startPoint);
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        FunctionWithGradient function = new FunctionWithGradient() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2.0 * x[0], 2.0 * x[1]};
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {0.1, 0.1, 0.1};
        simplex.build(startPoint);
        simplex.evaluate(function, ComparatorUtils.reverseOrder());

        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        assertEquals(-0.1321, worstPoint.getValue(), Precision.SAFE_EPSILON);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        FunctionWithGradient function = new FunctionWithGradient() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2.0 * x[0], 2.0 * x[1]};
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {0.1, 0.1, 0.1};
        simplex.build(startPoint);
        simplex.evaluate(function, ComparatorUtils.reverseOrder());

        PointValuePair newPoint = new PointValuePair(new double[]{0.2, 0.2}, 0.01);
        simplex.replaceWorstPoint(newPoint, ComparatorUtils.reverseOrder());

        assertEquals(0.01, simplex.getPoint(simplex.getSize() - 1).getValue(), Precision.SAFE_EPSILON);
    }

    private PointValuePair[] convertPointsToArray(PointValuePair[] points) {
        PointValuePair[] array = new PointValuePair[points.length];
        System.arraycopy(points, 0, array, 0, points.length);
        return array;
    }
}