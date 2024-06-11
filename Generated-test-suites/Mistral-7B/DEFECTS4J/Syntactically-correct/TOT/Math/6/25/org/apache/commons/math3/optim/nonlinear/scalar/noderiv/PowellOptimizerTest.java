package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Tests the optimizer with the default settings.
     */
    @Test
    public void testDefaultSettings() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(new RastriginFunction(), new double[]{2.0});
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a specified goal type.
     */
    @Test
    public void testSpecifiedGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(new RastriginFunction(), new double[]{2.0});
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3, new MyConvergenceChecker());
        PointValuePair result = optimizer.optimize(new RastriginFunction(), new double[]{2.0});
        assertEquals(0.0, result.getValue(), 1e-3);
    }

    /**
     * Tests the optimizer with an error scenario.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testErrorScenario() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(new ExceptionFunction(), new double[]{0.5});
    }

    /**
     * Tests the optimizer with an empty search space.
     */
    @Test
    public void testEmptySearchSpace() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        try {
            optimizer.optimize(new ConstantFunction(0.0), new double[]{});
            assertFalse("Expected optimization exception", true);
        } catch (UnconstrainedOptimizationException e) {
            // Expected
        }
    }

    /**
     * Tests the optimizer with a multivariate function.
     */
    @Test
    public void testMultivariateFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(new SphereFunction(3), new double[]{1.0, 1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a multivariate function and a custom line search.
     */
    @Test
    public void testMultivariateFunctionCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyLineSearch());
        PointValuePair result = optimizer.optimize(new SphereFunction(3), new double[]{1.0, 1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a multivariate function and a custom bracketFinder.
     */
    @Test
    public void testMultivariateFunctionCustomBracketFinder() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyBracketFinder());
        PointValuePair result = optimizer.optimize(new SphereFunction(3), new double[]{1.0, 1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimizer with a custom convergence checker and a custom line search.
     */
    @Test
    public void testCustomConvergenceCheckerAndCustomLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3,
                new MyConvergenceChecker(), new MyLineSearch());
        PointValuePair result = optimizer.optimize(new RastriginFunction(), new double[]{2.0});
        assertEquals(0.0, result.getValue(), 1e-3);
    }

    /**
     * Helper function that tests the optimizer with a specific function and
     * initial point.
     *
     * @param function The function to optimize.
     * @param initialPoint The initial point.
     * @param expectedValue The expected value at the optimum.
     */
    private void testFunction(UnivariateFunction function, double[] initialPoint, double expectedValue) {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, initialPoint);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    /**
     * Helper function that tests the optimizer with a specific function,
     * initial point, and goal type.
     *
     * @param function The function to optimize.
     * @param initialPoint The initial point.
     * @param expectedValue The expected value at the optimum.
     * @param goalType The goal type.
     */
    private void testFunction(UnivariateFunction function, double[] initialPoint, double expectedValue, GoalType goalType) {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(goalType);
        PointValuePair result = optimizer.optimize(function, initialPoint);
        assertEquals(expectedValue, goalType == GoalType.MINIMIZE ? -result.getValue() : result.getValue(), 1e-6);
    }

    /**
     * A simple function that always throws an exception.
     */
    private static class ExceptionFunction implements UnivariateFunction {
        public double value(double x) {
            throw new RuntimeException("Exception in test");
        }
    }

    /**
     * A constant function.
     */
    private static class ConstantFunction implements UnivariateFunction {
        private final double value;

        public ConstantFunction(double value) {
            this.value = value;
        }

        public double value(double x) {
            return value;
        }
    }

    /**
     * A multivariate function for a sphere.
     */
    private static class SphereFunction implements MultivariateFunction {
        private final int dimensions;

        public SphereFunction(int dimensions) {
            this.dimensions = dimensions;
        }

        public double value(double[] x) {
            double sum = 0.0;
            for (int i = 0; i < dimensions; i++) {
                sum += Math.pow(x[i], 2);
            }
            return sum;
        }

        public int getNumberOfVariables() {
            return dimensions;
        }
    }

    /**
     * A custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return previous.getValue() > current.getValue() && current.getValue() < 1e-6;
        }
    }

    /**
     * A custom bracketFinder.
     */
    private static class MyBracketFinder extends BracketFinder {
        @Override
        public void search(UnivariateFunction f, GoalType goalType, double min, double max) {
            // Empty implementation
        }
    }

    /**
     * A custom line search.
     */
    private static class MyLineSearch extends BrentOptimizer {
        public UnivariatePointValuePair search(double[] p, double[] d) {
            return new UnivariatePointValuePair(0.5, computeObjectiveValue(p, d));
        }

        private double computeObjectiveValue(double[] p, double[] d) {
            PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
            UnivariateFunction f = new UnivariateFunction() {
                public double value(double alpha) {
                    double[] x = new double[p.length];
                    for (int i = 0; i < p.length; i++) {
                        x[i] = p[i] + alpha * d[i];
                    }
                    return optimizer.computeObjectiveValue(x);
                }
            };
            return optimize(new MaxEval(100), f, goalType, 0.0, 1.0, 0.5)[1];
        }
    }
}