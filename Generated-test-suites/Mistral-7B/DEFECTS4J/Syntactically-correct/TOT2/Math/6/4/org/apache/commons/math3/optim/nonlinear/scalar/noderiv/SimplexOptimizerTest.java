package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.apache.commons.math3.optimization.test.MoreAsserts;
import org.junit.Assert;

import java.util.Random;

/**
 * Test cases for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Basic test of SimplexOptimizer.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testBasic() throws Exception {
        // Testing a simple function
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[]{3.0, 3.0};
        OptimizationData data = new OptimizationData() {
            public OptimizationData setInitialGuess(double[] value) {
                return this;
            }

            public AbstractSimplex setSimplex(AbstractSimplex simplex) {
                return new SimpleSimplex(new double[][]{
                        {0.0, 0.0},
                        {1.0, 1.0},
                        {startPoint[0], startPoint[1]}
                }, 3);
            }
        };

        PointValuePair result = optimizer.optimize(100, f, goalType, data);
        MoreAsserts.assertLessOrEqual(0.0, result.getValue(), 1e-12);
        MoreAsserts.assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-12);
    }

    /**
     * Test of SimplexOptimizer for non-convergence.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testNonConvergence() throws Exception {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]);
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        double[] startPoint = new double[]{0.1, 0.1};
        OptimizationData data = new OptimizationData() {
            public OptimizationData setInitialGuess(double[] value) {
                return this;
            }

            public AbstractSimplex setSimplex(AbstractSimplex simplex) {
                return new SimpleSimplex(new double[][]{
                        {0.0, 0.0},
                        {1.0, 1.0},
                        {startPoint[0], startPoint[1]}
                }, 3);
            }
        };

        try {
            PointValuePair result = optimizer.optimize(100, f, goalType, data);
            Assert.fail("Expected exception");
        } catch (Exception e) {
            Assert.assertEquals("Maximum number of iterations or function evaluations reached", e.getMessage());
        }
    }

    /**
     * Test of SimplexOptimizer with invalid input.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testInvalidInput() throws Exception {
        MultivariateFunction f = null;
        try {
            GoalType goalType = GoalType.MINIMIZE;
            ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-10, 1e-10);
            SimplexOptimizer optimizer = new SimplexOptimizer(checker);

            double[] startPoint = new double[]{3.0, 3.0};
            OptimizationData data = new OptimizationData() {
                public OptimizationData setInitialGuess(double[] value) {
                    return this;
                }

                public AbstractSimplex setSimplex(AbstractSimplex simplex) {
                    return new SimpleSimplex(new double[][]{
                            {0.0, 0.0},
                            {1.0, 1.0},
                            {startPoint[0], startPoint[1]}
                    }, 3);
                }
            };

            PointValuePair result = optimizer.optimize(100, f, goalType, data);
            Assert.fail("Expected exception");
        } catch (NullArgumentException e) {
            // expected
        }
    }
}