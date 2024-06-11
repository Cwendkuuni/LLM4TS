package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.AbstractOptimizer;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Tests the basic functionality of the optimizer.
     */
    @Test
    public void testOptimizeFunction() {
        // Prepare the test
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        double[] startPoint = new double[]{1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        AbstractOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        // Run the test
        OptimizationData data = new OptimizationData(new InitialGuess(startPoint), f, goalType);
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, goalType, data);

        // Check the results
        assertNotNull(result);
        double[] expected = new double[]{1.0, 1.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of a function with multiple local minima.
     */
    @Test
    public void testOptimizeFunctionMultipleMinima() {
        // Prepare the test
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] * x[0] + x[1] * x[1] < 2.5) {
                    return x[0] * x[0] + x[1] * x[1];
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            }
        };
        double[] startPoint = new double[]{3.0, 3.0};
        GoalType goalType = GoalType.MINIMIZE;
        AbstractOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        // Run the test
        OptimizationData data = new OptimizationData(new InitialGuess(startPoint), f, goalType);
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, goalType, data);

        // Check the results
        assertNotNull(result);
        double[] expected = new double[]{1.5, 1.5};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(2.25, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of a function with a singularity.
     */
    @Test(expected = java.lang.ArithmeticException.class)
    public void testOptimizeFunctionSingularity() {
        // Prepare the test
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] == 0.0) {
                    throw new ArithmeticException("Divide by zero");
                }
                return 1.0 / x[0];
            }
        };
        double[] startPoint = new double[]{1.0};
        GoalType goalType = GoalType.MINIMIZE;
        AbstractOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        // Run the test
        OptimizationData data = new OptimizationData(new InitialGuess(startPoint), f, goalType);
        optimizer.optimize(new MaxEval(100), f, goalType, data);

        // The previous line should throw an exception
    }

    /**
     * Tests the optimization of a function with bounds.
     */
    @Test
    public void testOptimizeFunctionBounds() {
        // Prepare the test
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        double[] startPoint = new double[]{1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = new double[]{
            Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
        };
        AbstractOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        // Run the test
        OptimizationData data = new OptimizationData(
            new InitialGuess(startPoint),
            f,
            goalType,
            new OptimizationData[] { new OptimizationData(new Bounds(bounds)) }
        );
        PointValuePair result = optimizer.optimize(new MaxEval(100), f, goalType, data);

        // Check the results
        assertNotNull(result);
        double[] expected = new double[]{0.0, 0.0};
        assertEquals(expected[0], result.getPoint()[0], 1e-6);
        assertEquals(expected[1], result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}