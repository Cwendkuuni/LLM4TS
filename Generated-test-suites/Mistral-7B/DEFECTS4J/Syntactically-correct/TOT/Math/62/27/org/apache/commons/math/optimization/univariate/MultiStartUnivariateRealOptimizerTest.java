package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.initializer.RandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSensitiveOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSensitiveOptimizer.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSensitiveOptimizer.Result;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.RealPointValuePair;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with a function that has a unique global minimum.
     */
    @Test
    public void testMinimizeUniqueGlobalMinimum() {
        // Test with a simple function that has a unique global minimum.
        UnivariateRealFunction function = new SinFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -3.0;
        double max = 3.0;
        int maxEvaluations = 100;
        int starts = 3;

        MultiStartUnivariateRealOptimizer<SinFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer<>(new RandomInitializer(), new MersenneTwister(), 10),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return current.getValue() - previous.getValue() < tolerance;
            }

            @Override
            public double getTolerance() {
                return 1e-8;
            }
        });

        PointValuePair result = optimizer.optimize(function, goalType, min, max);

        Assert.assertNotNull(result);
        Assert.assertEquals(goalType, result.getGoalType(), 0);
        Assert.assertEquals(Math.PI / 2, result.getPoint().getValue(), 1e-8);
    }

    /**
     * Test optimization with a function that has multiple local minima and a global minimum.
     */
    @Test
    public void testMinimizeMultipleLocalMinima() {
        // Test with a function that has multiple local minima and a global minimum.
        UnivariateRealFunction function = new QuarticFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        int maxEvaluations = 100;
        int starts = 5;

        MultiStartUnivariateRealOptimizer<QuarticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionSearchOptimizer<>(new RandomInitializer(), new MersenneTwister(), 10),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return current.getValue() - previous.getValue() < tolerance;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        PointValuePair result = optimizer.optimize(function, goalType, min, max);

        Assert.assertNotNull(result);
        Assert.assertEquals(goalType, result.getGoalType(), 0);
        Assert.assertEquals(-1.5, result.getPoint().getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has no solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testMinimizeNoSolution() {
        // Test with a function that has no solution.
        UnivariateRealFunction function = new ExpFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        int maxEvaluations = 100;
        int starts = 5;

        MultiStartUnivariateRealOptimizer<ExpFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer<>(new RandomInitializer(), new MersenneTwister()),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return false;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test optimization with a function that has no starting point in the search interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeNoStartingPoint() {
        // Test with a function that has no starting point in the search interval.
        UnivariateRealFunction function = new AbsoluteValueFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 0.0; // No starting point in [min, max]
        int maxEvaluations = 100;
        int starts = 5;

        MultiStartUnivariateRealOptimizer<AbsoluteValueFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new BrentOptimizer<>(new RandomInitializer(), new MersenneTwister()),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return false;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        optimizer.optimize(function, goalType, min, max);
    }

    /**
     * Test optimization with a function that has multiple global maxima and a global minimum.
     */
    @Test
    public void testMaximizeMultipleGlobalMaxima() {
        // Test with a function that has multiple global maxima and a global minimum.
        UnivariateRealFunction function = new SigmoidFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;
        int maxEvaluations = 100;
        int starts = 5;

        MultiStartUnivariateRealOptimizer<SigmoidFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionSearchOptimizer<>(new RandomInitializer(), new MersenneTwister(), 10),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return current.getValue() - previous.getValue() < tolerance;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        PointValuePair result = optimizer.optimize(function, goalType, min, max);

        Assert.assertNotNull(result);
        Assert.assertEquals(goalType, result.getGoalType(), 0);
        Assert.assertEquals(2.0, result.getPoint().getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has multiple local maxima and a global maximum.
     */
    @Test
    public void testMaximizeMultipleLocalMaxima() {
        // Test with a function that has multiple local maxima and a global maximum.
        UnivariateRealFunction function = new QuarticFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;
        int maxEvaluations = 100;
        int starts = 5;

        MultiStartUnivariateRealOptimizer<QuarticFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionSearchOptimizer<>(new RandomInitializer(), new MersenneTwister(), 10),
                starts,
                new MersenneTwister());

        optimizer.setMaxEvaluations(maxEvaluations);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return current.getValue() - previous.getValue() < tolerance;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        PointValuePair result = optimizer.optimize(function, goalType, min, max);

        Assert.assertNotNull(result);
        Assert.assertEquals(goalType, result.getGoalType(), 0);
        Assert.assertEquals(1.5, result.getPoint().getValue(), 1e-6);
    }

    /**
     * Test optimization with a function that has no solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testMaximizeNoSolution() {
        // Test with a function that has no solution.
        UnivariateRealFunction function = new ExpFunction();
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<ExpFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new GoldenSectionSearchOptimizer<>(new RandomInitializer(), new MersenneTwister()),
                1,
                new MersenneTwister());

        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new ConvergenceChecker<RealPointValuePair>() {
            @Override
            public boolean isConverged(RealPointValuePair current,
                                       RealPointValuePair previous,
                                       double tolerance) {
                return false;
            }

            @Override
            public double getTolerance() {
                return 1e-6;
            }
        });

        optimizer.optimize(function, goalType, min, max);
    }
}

class SinFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.sin(x);
    }
}

class QuarticFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 4) + Math.pow(x, 3) - 4.0 * Math.pow(x, 2) + Math.PI;
    }
}

class ExpFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.exp(x);
    }
}

class AbsoluteValueFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return Math.abs(x);
    }
}

class SigmoidFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
}