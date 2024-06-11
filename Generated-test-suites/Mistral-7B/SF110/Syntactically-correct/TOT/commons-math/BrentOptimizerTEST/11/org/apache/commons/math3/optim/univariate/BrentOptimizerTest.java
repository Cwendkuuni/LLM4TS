package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.initializer.ValueSelector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MINIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        FunctionTestFunction function = new FunctionTestFunction();
        double expectedResult = 1.0;
        PointValuePair result = optimizerWithSorter.optimize(function, 0.0, 2.0);

        Assert.assertNotNull(result);
        Assert.assertEquals(expectedResult, result.getPoint(), 1e-5);
        Assert.assertEquals(expectedResult, result.getValue(), 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativelySmallRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-10, 1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MINIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        FunctionTestFunction function = new FunctionTestFunction();
        optimizerWithSorter.optimize(function, 0.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MINIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        FunctionTestFunction function = new FunctionTestFunction();
        optimizerWithSorter.optimize(function, 0.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        UnivariateOptimizer optimizerWithoutSorter = optimizer;

        FunctionTestFunction function = new FunctionTestFunction();
        optimizerWithoutSorter.optimize(function, 0.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullValueSelector() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, null);

        FunctionTestFunction function = new FunctionTestFunction();
        optimizerWithSorter.optimize(function, 0.0, 2.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testBrentOptimizerConvergenceCheckerThrowsDuringIteration() {
        class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair best) {
                if (iteration >= 10) {
                    throw new IllegalStateException("Convergence checker threw an exception!");
                }
                return false;
            }
        }

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, new MyConvergenceChecker());
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MINIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        FunctionTestFunction function = new FunctionTestFunction();
        optimizerWithSorter.optimize(function, 0.0, 2.0);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MAXIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        FunctionTestFunctionMax function = new FunctionTestFunctionMax();
        double expectedResult = 2.0;
        PointValuePair result = optimizerWithSorter.optimize(function, 0.0, 2.0);

        Assert.assertNotNull(result);
        Assert.assertEquals(expectedResult, result.getPoint(), 1e-5);
        Assert.assertEquals(expectedResult, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MINIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        double initialGuess = 1.5;
        FunctionTestFunction function = new FunctionTestFunction();
        PointValuePair result = optimizerWithSorter.optimize(function, initialGuess, 2.0);

        Assert.assertNotNull(result);
        Assert.assertEquals(1.0, result.getPoint(), 1e-5);
        Assert.assertEquals(0.0, result.getValue(), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        GoalTypeSorter sorter = new GoalTypeSorter(GoalType.MAXIMIZE);
        UnivariateOptimizer optimizerWithSorter = new OptimizerAdapter(optimizer, sorter);

        double initialGuess = 1.5;
        FunctionTestFunctionMax function = new FunctionTestFunctionMax();
        PointValuePair result = optimizerWithSorter.optimize(function, initialGuess, 2.0);

        Assert.assertNotNull(result);
        Assert.assertEquals(2.0, result.getPoint(), 1e-5);
        Assert.assertEquals(4.0, result.getValue(), 1e-5);
    }

    private static class FunctionTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 4.0 * x + 4.0;
        }
    }

    private static class FunctionTestFunctionMax implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x + 4.0 * x;
        }
    }

    private static class OptimizerAdapter implements UnivariateOptimizer {
        private UnivariateOptimizer optimizer;
        private ConvergenceChecker<PointValuePair> convergenceChecker;
        private ValueSelector valueSelector;

        public OptimizerAdapter(UnivariateOptimizer optimizer, ValueSelector valueSelector) {
            this.optimizer = optimizer;
            this.valueSelector = valueSelector;
        }

        public OptimizerAdapter(UnivariateOptimizer optimizer, ConvergenceChecker<PointValuePair> convergenceChecker) {
            this.optimizer = optimizer;
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public PointValuePair optimize(UnivariateFunction function, double start, double end) {
            return optimizer.optimize(new AdaptorFunction(function), start, end);
        }

        private class AdaptorFunction implements UnivariateFunction {
            private UnivariateFunction function;

            public AdaptorFunction(UnivariateFunction function) {
                this.function = function;
            }

            @Override
            public double value(double x) {
                return function.value(x);
            }
        }
    }
}