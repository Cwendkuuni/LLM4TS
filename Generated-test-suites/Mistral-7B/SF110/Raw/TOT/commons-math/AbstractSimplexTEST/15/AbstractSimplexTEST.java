package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.go.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithValidArguments() {
        int dimension = 3;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension, simplex.getDimension());
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexWithZeroSteps() {
        int dimension = 3;
        try {
            new AbstractSimplex(new double[dimension]);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEmptyReferenceSimplex() {
        int dimension = 3;
        try {
            new AbstractSimplex(new double[0][dimension]);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVerticesInReferenceSimplex() {
        int dimension = 3;
        double[][] referenceSimplex = {
            {1.0, 2.0, 3.0},
            {1.0, 2.0, 3.0}
        };
        try {
            new AbstractSimplex(referenceSimplex);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithInvalidStartPoint() {
        int dimension = 3;
        try {
            new AbstractSimplex(new int[dimension]);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testBuildMethod() {
        int dimension = 3;
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(startPoint);
        AbstractSimplex expectedSimplex = new AbstractSimplex(createHypercubeSteps(dimension, 1.0));
        assertArrayEquals(expectedSimplex.getPoints(), simplex.getPoints());
    }

    @Test
    public void testEvaluateMethod() {
        int dimension = 3;
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(startPoint);
        MultivariateFunction evaluationFunction = f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0) + Math.pow(f[2], 2.0);
        simplex.evaluate(evaluationFunction, Comparator.comparingDouble(p -> p.getValue()));
        double[] expectedValues = {1.0, 5.0, 10.0, 17.0};
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair pointValuePair = simplex.getPoint(i);
            double expectedValue = expectedValues[i];
            assertEquals(expectedValue, pointValuePair.getValue(), 0.001);
        }
    }

    @Test
    public void testReplaceWorstPointMethod() {
        int dimension = 3;
        double[] startPoint = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(startPoint);
        PointValuePair pointValuePair = new PointValuePair(new double[dimension], Double.MAX_VALUE);
        simplex.evaluate(f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0) + Math.pow(f[2], 2.0), Comparator.comparingDouble(p -> p.getValue()));
        simplex.replaceWorstPoint(pointValuePair, Comparator.comparingDouble(p -> p.getValue()));
        PointValuePair expectedPointValuePair = new PointValuePair(new double[dimension], Double.MAX_VALUE);
        assertEquals(expectedPointValuePair, simplex.getPoint(simplex.getSize() - 1));
    }

    private static double[] createHypercubeSteps(final int n, final double sideLength) {
        double[] steps = new double[n];
        for (int i = 0; i < n; ++i) {
            steps[i] = sideLength;
        }
        return steps;
    }
}