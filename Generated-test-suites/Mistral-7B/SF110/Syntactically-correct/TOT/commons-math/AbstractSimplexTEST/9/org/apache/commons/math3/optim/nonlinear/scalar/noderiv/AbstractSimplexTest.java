package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithNullArgumentException() {
        try {
            new AbstractSimplex(null);
            fail("Should not reach this point");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithZeroStepsException() {
        try {
            new AbstractSimplex(new double[0]);
            fail("Should not reach this point");
        } catch (ZeroException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithZeroDimensionException() {
        try {
            new AbstractSimplex(0);
            fail("Should not reach this point");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVerticesInSimplexException() {
        try {
            new AbstractSimplex(new double[] { 1.0, 0.0, 0.0, 0.0 });
            fail("Should not reach this point");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithNotStrictlyPositiveException() {
        try {
            new AbstractSimplex(new double[] { 1.0, -1.0, 0.0 });
            fail("Should not reach this point");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithDimensionMismatchException() {
        try {
            new AbstractSimplex(new double[][] { { 1.0, 0.0 }, { 1.0, 0.0, 0.0 } });
            fail("Should not reach this point");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVerticesInSimplexException2() {
        try {
            new AbstractSimplex(new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 } });
            fail("Should not reach this point");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexWithEmptyReferenceSimpleException() {
        try {
            new AbstractSimplex(new double[][] {});
            fail("Should not reach this point");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testAbstractSimplexSuccess() {
        int dimension = 2;
        double sideLength = 1.0;
        AbstractSimplex simplex = new AbstractSimplex(dimension, sideLength);
        PointValuePair[] points = simplex.getPoints();

        for (PointValuePair point : points) {
            MultivariateFunction function = new MultivariateFunction() {
                @Override
                public double value(double[] point) {
                    return Math.pow(point[0], 2) + Math.pow(point[1], 2);
                }
            };

            simplex.build(point.getPointRef());
            simplex.evaluate(function, new PointValuePairComparator());
            assertEquals(points.length, simplex.getSize());
        }
    }
}