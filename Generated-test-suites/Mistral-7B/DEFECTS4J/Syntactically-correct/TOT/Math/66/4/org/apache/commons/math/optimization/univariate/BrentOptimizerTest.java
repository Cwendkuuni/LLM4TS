package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeAugmented;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@code BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /** A simple function for testing. */
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
    }

    /**
     * Test optimization with a positive interval.
     */
    @Test
    public void testOptimizePositiveInterval() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 3.0);
        assertEquals(2.0, result, 1e-8);
    }

    /**
     * Test optimization with a negative interval.
     */
    @Test
    public void testOptimizeNegativeInterval() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -3.0, -1.0);
        assertEquals(-2.0, result, 1e-8);
    }

    /**
     * Test optimization with a goal of maximum.
     */
    @Test
    public void testOptimizeMaximum() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, -3.0, 3.0);
        assertEquals(3.0, result, 1e-8);
    }

    /**
     * Test optimization with a non-strictly positive tolerance.
     */
    @Test
    public void testOptimizeWithNonPositiveTolerance() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> {
            optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, -0.1);
        });
    }

    /**
     * Test optimization with a non-positive interval.
     */
    @Test
    public void testOptimizeWithNonPositiveInterval() throws Exception {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 0.0);
        });
    }

    /**
     * Test optimization with a non-finite function.
     */
    @Test
    public void testOptimizeWithNonFiniteFunction() throws Exception {
        UnivariateRealFunction testFunction = new TestFunctionWithException();
        UnivariateOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxEvalExceededException.class, () -> {
            optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        });
    }

    /**
     * A simple function for testing.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x - 4.0 * x + 4.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x - 4.0;
        }
    }

    /**
     * A simple function with an exception for testing.
     */
    private static class TestFunctionWithException implements UnivariateRealFunction {
        @Override
        public double value(double x) throws MaxEvalExceededException {
            if (x > 1.0) {
                throw new MaxEvalExceededException();
            }
            return Math.sin(Math.PI / x);
        }

        @Override
        public double derivative(double x) {
            double sinX = Math.sin(Math.PI / x);
            double cosX = Math.cos(Math.PI / x);
            return -Math.PI * cosX / (x * x);
        }
    }
}