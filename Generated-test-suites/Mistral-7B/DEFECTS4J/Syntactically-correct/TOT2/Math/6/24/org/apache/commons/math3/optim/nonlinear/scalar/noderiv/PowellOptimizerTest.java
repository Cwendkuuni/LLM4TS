package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeComparator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Function for testing the Powell optimizer.
     */
    private static class TestFunction implements UnivariateFunction {
        public double value(double x) {
            return Math.pow(x, 3.0) - 2 * x * Math.pow(Math.E, x) - Math.pow(Math.E, x);
        }
    }

    /**
     * Function for testing the convergence checker.
     */
    private static class TestChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iterations, PointValuePair previous, PointValuePair current) {
            final double precision = 1e-8;
            final double maxIterations = 1000;
            return (Math.abs(current.getValue() - previous.getValue()) < precision ||
                    iterations >= maxIterations);
        }
    }

    /**
     * Test the Powell optimizer with a simple test function.
     */
    @Test
    public void testSimple() {
        final double relTolerance = 1e-8;
        final double absTolerance = 1e-8;
        final double x0[] = { -1.0, -1.0 };
        final TestFunction testFunction = new TestFunction();

        PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);
        PointValuePair solution = optimizer.optimize(testFunction, x0);

        PointValuePair expectedSolution = new PointValuePair(new double[] { 0.5, 0.5 }, -1.3127375355468561);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-8);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-8);
    }

    /**
     * Test the Powell optimizer with a test function having multiple local minima.
     */
    @Test
    public void testMultipleMinima() {
        final double relTolerance = 1e-6;
        final double absTolerance = 1e-6;
        final double x0[] = { 1.0, 1.0 };
        final TestFunction testFunction = new TestFunction();

        PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);
        PointValuePair solution = optimizer.optimize(testFunction, x0);

        PointValuePair expectedSolution = new PointValuePair(new double[] { 1.0, 1.0 }, 3.0);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-6);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-6);

        GoalTypeSorter sorter = new GoalTypeSorter();
        GoalTypeComparator comparator = new GoalTypeComparator(GoalType.MINIMIZE);
        PointValuePair[] initialGuesses = new PointValuePair[] {
            new PointValuePair(new double[] { -1.0, 1.0 }, Double.POSITIVE_INFINITY),
            new PointValuePair(new double[] { 1.0, -1.0 }, Double.POSITIVE_INFINITY),
            new PointValuePair(new double[] { 1.0, 1.0 }, Double.POSITIVE_INFINITY)
        };
        InitialGuess initialGuess = optimizer.getInitialGuess();
        Arrays.sort(initialGuesses, sorter);
        for (PointValuePair p : initialGuesses) {
            PointValuePair result = optimizer.optimize(testFunction, p.getPoint());
            assertEquals(expectedSolution.getValue(), result.getValue(), 1e-6);
            assertArrayEquals(expectedSolution.getPoint(), result.getPoint(), 1e-6);
        }
    }

    /**
     * Test the Powell optimizer with a non-differentiable function.
     */
    @Test
    public void testNonDifferentiable() {
        final double relTolerance = 1e-2;
        final double absTolerance = 1e-2;
        final TestFunction testFunction = new TestFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x - 0.5)) * Math.sin(1 / (x - 0.5));
            }
        };
        final double x0[] = { 1.0 };

        PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);
        try {
            optimizer.optimize(testFunction, x0);
            fail("Expected NumberIsDefinedException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test the Powell optimizer with a non-convex function.
     */
    @Test
    public void testNonConvex() {
        final double relTolerance = 1e-2;
        final double absTolerance = 1e-2;
        final TestFunction testFunction = new TestFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * Math.log(x);
                } else {
                    return -Math.pow(x, 2) - Math.log(-x);
                }
            }
        };
        final double x0[] = { -1.0 };

        PowellOptimizer optimizer = new PowellOptimizer(relTolerance, absTolerance);
        PointValuePair solution = optimizer.optimize(testFunction, x0);

        PointValuePair expectedSolution = new PointValuePair(new double[] { 0.0 }, 0.0);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1e-2);
        assertArrayEquals(expectedSolution.getPoint(), solution.getPoint(), 1e-2);
    }
}