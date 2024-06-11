package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testValidBrentOptimizer() {
        DoubleFunction testFunction = new DoubleFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-3, new GoalTypeChecker(GoalType.MINIMIZE));
        UnivariateOptimizerResult result = optimizer.optimize(testFunction, -5.0, 5.0);
        assertEquals(2.0, FastMath.abs(result.getValue().getValue() - 0.0), 1e-5);
        assertEquals(0.0, result.getValue().getX(), 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-3, null);
        throw new RuntimeException("Test not executed due to constructor exception");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, -1e-3, null);
        throw new RuntimeException("Test not executed due to constructor exception");
    }

    @Test
    public void testBrentOptimizerWithNullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3);
        UnivariateOptimizerResult result = optimizer.optimize(new SquareRootFunction(), -5.0, 5.0);
        assertEquals(1.0, FastMath.abs(result.getValue().getValue() - 2.0), 1e-5);
        assertEquals(2.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue().getValue() - previous.getValue().getValue() < 1e-6;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, convergenceChecker);
        UnivariateOptimizerResult result = optimizer.optimize(new SquareRootFunction(), -5.0, 5.0);
        assertEquals(1.0, FastMath.abs(result.getValue().getValue() - 2.0), 1e-5);
        assertEquals(2.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithMinimizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3);
        UnivariateOptimizerResult result = optimizer.optimize(new AbsFunction(), 0.0, 10.0, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue().getValue(), 1e-5);
        assertEquals(0.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithMaximizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3);
        UnivariateOptimizerResult result = optimizer.optimize(new AbsFunction(), 0.0, 10.0, GoalType.MAXIMIZE);
        assertEquals(10.0, result.getValue().getValue(), 1e-5);
        assertEquals(10.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithConvergenceCheckerAndMinimizeGoal() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue().getValue() - previous.getValue().getValue() < 1e-6;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, convergenceChecker);
        UnivariateOptimizerResult result = optimizer.optimize(new AbsFunction(), 0.0, 10.0, GoalType.MINIMIZE);
        assertEquals(0.0, result.getValue().getValue(), 1e-5);
        assertEquals(0.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithConvergenceCheckerAndMaximizeGoal() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue().getValue() - previous.getValue().getValue() < 1e-6;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3, convergenceChecker);
        UnivariateOptimizerResult result = optimizer.optimize(new AbsFunction(), 0.0, 10.0, GoalType.MAXIMIZE);
        assertEquals(10.0, result.getValue().getValue(), 1e-5);
        assertEquals(10.0, result.getValue().getX(), 1e-5);
    }

    @Test
    public void testBrentOptimizerWithFunctionReturningNaN() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3);
        try {
            optimizer.optimize(new NaNFunction(), -5.0, 5.0);
            fail("Expected NumberIsDefinedException to be thrown");
        } catch (NumberIsDefinedException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsException() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-3);
        try {
            optimizer.optimize(new ThrowingFunction(), -5.0, 5.0);
            fail("Expected OptimizationException to be thrown");
        } catch (OptimizationException e) {
            // Expected
        }
    }

    private static class SquareRootFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sqrt(x);
        }
    }

    private static class AbsFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.abs(x);
        }
    }

    private static class NaNFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new ArithmeticException("NaN Function");
        }
    }

    private static class ThrowingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            throw new RuntimeException("Throwing Function");
        }
    }
}