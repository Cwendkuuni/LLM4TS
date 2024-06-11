package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.FitnessFunction;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test function: f(x) = x^3 - 2x^2 + x
    static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x * x - 2 * x * x + x;
        }

        @Override
        public double derivative(double x) {
            return 3 * x * x - 4 * x + 1;
        }
    }

    @Test
    public void testBrentOptimizer() {
        TestFunction function = new TestFunction();
        SimpleBounds bounds = new SimpleBounds(-2, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(new UnivariateFunctionAdapter(function), bounds);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(-2.928275825118548E9, result.getValue(), 1e-10);

        optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        result = optimizer.optimize(new UnivariateFunctionAdapter(function), bounds);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(-2.928275825118548E9, result.getValue(), 1e-10);
    }

    static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double error = current.getValue() - previous.getValue();
            return Math.abs(error) < 1e-8;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithInvalidArguments() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0);
    }

    @Test
    public void testBrentOptimizerWithGoalType() {
        TestFunction function = new TestFunction();
        SimpleBounds bounds = new SimpleBounds(-2, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);

        PointValuePair result = optimizer.optimize(new UnivariateFunctionAdapter(function), bounds);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(3.233275825118548E9, result.getValue(), 1e-10);
    }

    @Test
    public void testBrentOptimizerWithFitnessFunction() {
        TestFunction function = new TestFunction();
        SimpleBounds bounds = new SimpleBounds(-2, 2);
        FitnessFunction fitnessFunction = new FitnessFunction() {
            @Override
            public double value(double x) {
                return function.value(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, fitnessFunction);
        PointValuePair result = optimizer.optimize(bounds);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(-2.928275825118548E9, result.getValue(), 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithInvalidFitnessFunction() {
        SimpleBounds bounds = new SimpleBounds(-2, 2);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
    }
}