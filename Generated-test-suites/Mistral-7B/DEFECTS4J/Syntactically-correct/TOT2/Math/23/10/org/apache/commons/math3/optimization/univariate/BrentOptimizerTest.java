package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.AbstractNonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleLineSearch;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;
    private ConvergenceChecker<UnivariatePointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        function = new MyFunction();
        convergenceChecker = new SimpleLineSearch(0.1);
        optimizer = new BrentOptimizer(1e-4, 1e-4, convergenceChecker);
    }

    @Test
    public void testMinimum() {
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0);
        PointValuePair expected = new PointValuePair(1.234, 3.14159265); // Expected minimum
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        new BrentOptimizer(1e-4, 0, convergenceChecker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-4, convergenceChecker);
    }

    @Test
    public void testMaximum() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0);
        PointValuePair expected = new PointValuePair(2.467, 8.12345678); // Expected maximum
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test
    public void testAroundMinimum() {
        AroundMinimumInitializer initializer = new AroundMinimumInitializer(function, 1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, initializer);
        PointValuePair expected = new PointValuePair(1.234, 3.14159265); // Expected minimum
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test
    public void testConvergence() {
        UnivariateFunction convergenceFunction = new ConvergenceFunction();
        UnivariatePointValuePair result = optimizer.optimize(convergenceFunction, 1.0);
        PointValuePair expected = new PointValuePair(1.123, 1.5); // Expected minimum
        assertEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        new BrentOptimizer(1e-4, 1e-4, null);
    }

    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) * Math.exp(-x) - 2 * x;
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) * Math.exp(-x) + 2;
        }
    }

    private static class ConvergenceFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return 1.0 / (1.0 + Math.abs(x));
        }

        @Override
        public double derivative(double x) {
            return -1.0 / ((1.0 + Math.abs(x)) * (1.0 + Math.abs(x)));
        }
    }
}