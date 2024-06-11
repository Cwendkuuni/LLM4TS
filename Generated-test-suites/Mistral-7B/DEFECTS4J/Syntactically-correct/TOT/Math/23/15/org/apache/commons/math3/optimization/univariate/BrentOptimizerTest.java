package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SerialDifferenciationMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Brent;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@code BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractTest {

    /**
     * A simple function for testing.
     */
    private static final ObjectiveFunction FUNCTION = new ObjectiveFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 3) - 0.1 * Math.cos(2 * x);
        }
    };

    /**
     * Test optimization with a very simple function.
     */
    @Test
    public void testOptimizationSimpleFunction() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        final PointValuePair result = optimizer.optimize(FUNCTION, -2.0, 2.0);
        final double expected = -1.2617; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function having a local minimum and a local maximum.
     */
    @Test
    public void testOptimizationLocalMinAndMax() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 3) - 0.1 * Math.cos(x * 2) + 0.1;
            }
        }, -2.0, 2.0);
        final double expectedMin = -1.2617; // Approximate minimum
        final double expectedMax = 0.4815;  // Approximate maximum
        assertTrue(result.getValue() < expectedMin || result.getValue() > expectedMax);
    }

    /**
     * Test optimization with a function that has no solution in the interval.
     */
    @Test
    public void testOptimizationNoSolution() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x) - 1;
            }
        }, -2.0, 2.0);
        assertEquals(Double.NaN, result.getPoint(), 0.0);
        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    /**
     * Test optimization with a function that does not converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizationNoConvergence() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-14, 1e-14);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        }, -2.0, 2.0);
    }

    /**
     * Test optimization with a function that converges very slowly.
     */
    @Test
    public void testOptimizationSlowConvergence() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) / Math.sqrt(Math.abs(x));
            }
        }, -2.0, 2.0);
        final double expected = 0.0; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that needs a more accurate initial guess.
     */
    @Test
    public void testOptimizationInitialGuess() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(Math.sin(x) / x) + 1);
            }
        }, -10.0, 10.0);
        final double expected = 0.0; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has linear behavior at the minimum.
     */
    @Test
    public void testOptimizationLinearBehavior() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * (x - 1) * (x - 2);
            }
        }, -10.0, 10.0);
        final double expected = 1.0; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(expected * (expected - 1) * (expected - 2), result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has a singularity at the minimum.
     */
    @Test
    public void testOptimizationSingularity() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(Math.sin(Math.PI / 2 - x))) / Math.abs(Math.sin(Math.PI / 2 - x));
            }
        }, -0.1, 0.1);
        final double expected = Math.PI / 2; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has a non-differentiable minimum.
     */
    @Test
    public void testOptimizationNonDifferentiableMinimum() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(Math.PI / 2 - x)) + Math.abs(Math.sin(Math.PI / 4 - x));
            }
        }, -0.5, 0.5);
        final double expected = Math.PI / 2; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has a differentiable minimum.
     */
    @Test
    public void testOptimizationDifferentiableMinimum() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 2;
            }
        }, -2.0, 2.0);
        final double expected = 1.0; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-6);
        assertEquals(Math.pow(expected, 2) - 2 * expected + 1, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has a differentiable minimum and a local maximum.
     */
    @Test
    public void testOptimizationDifferentiableMinMax() {
        final UnivariateOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        final PointValuePair result = optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x - 2;
            }
        }, -2.0, 2.0);
        final double expectedMin = 1.0; // Approximate minimum
        final double expectedMax = 2.0; // Approximate local maximum
        assertTrue(result.getValue() < expectedMin || result.getValue() > expectedMax);
    }

    /**
     * Test optimization with a function that needs a more accurate initial guess
     * and a differentiation method.
     */
    @Test
    public void testOptimizationAccurateInitialGuessDifferentiation() {
        final UnivariateOptimizer optimizer = new BrentMinimizer(new SerialDifferenciationMethod(new Brent()), 1e-14, 1e-14);
        final PointValuePair result = optimizer.optimize(FUNCTION, -2.0, 2.0);
        final double expected = -1.2617; // Approximate minimum
        assertEquals(expected, result.getPoint(), 1e-14);
        assertEquals(expected, result.getValue(), 1e-14);
    }
}