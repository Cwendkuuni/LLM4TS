package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeStoppingCondition;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairBracketFinder;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for the Powell optimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test for the Powell optimizer when the objective function is a simple quadratic.
     */
    @Test
    public void testQuadraticFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(f, bounds);

        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
    }

    /**
     * Test for the Powell optimizer when the objective function is a simple quadratic with negative coefficients.
     */
    @Test
    public void testNegativeQuadraticFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return -Math.pow(x, 2);
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(-1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(f, bounds);

        Assert.assertEquals(-1.0, solution.getPoint(), 1e-6);
    }

    /**
     * Test for the Powell optimizer when the objective function is a non-quadratic function with multiple local minima.
     */
    @Test
    public void testNonQuadraticFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x) - 0.5 * Math.pow(x, 2);
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(f, bounds);

        double expectedMinimum = -1.1238;
        Assert.assertEquals(expectedMinimum, solution.getPoint(), 1e-4);
        Assert.assertEquals(expectedMinimum, f.value(solution.getPoint()), 1e-4);
    }

    /**
     * Test for the Powell optimizer when the objective function is a non-quadratic function with a local maximum.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNonQuadraticFunctionWithLocalMaxima() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x) * Math.exp(-x) + 0.5 * Math.pow(x, 2);
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(f, bounds);

        Assert.fail(); // The optimizer should find a local maximum instead.
    }

    /**
     * Test for the Powell optimizer when the objective function is a constant function.
     */
    @Test
    public void testConstantFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return 0.0;
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(f, bounds);

        Assert.assertEquals(0.0, solution.getPoint(), 1e-6);
    }

    /**
     * Test for the Powell optimizer when the objective function is a non-defined function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonDefinedFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        PointValuePairBracketFinder bracketFinder = new PointValuePairBracketFinder();
        SimpleValueChecker valueChecker = new SimpleValueChecker(1e-6);
        GoalTypeConvergenceChecker<PointValuePair> convergenceChecker = new GoalTypeConvergenceChecker<>(GoalType.MINIMIZE, valueChecker);
        GoalTypeStoppingCondition stoppingCondition = new GoalTypeStoppingCondition(GoalType.MINIMIZE);

        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.setStoppingCondition(stoppingCondition);

        SimpleBounds bounds = new SimpleBounds(-10, 10);
        optimizer.optimize(f, bounds);

        // The test should fail due to an IllegalArgumentException.
    }
}