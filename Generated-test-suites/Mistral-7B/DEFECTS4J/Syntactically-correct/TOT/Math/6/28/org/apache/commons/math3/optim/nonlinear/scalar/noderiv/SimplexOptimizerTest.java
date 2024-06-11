package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {
    /**
     * Test function for optimization.
     */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] point) {
            return Math.pow(Math.sin(point[0]) + 1.5 * Math.pow(point[1], 2), 2)
                 + Math.cos(2 * point[0]) + Math.pow(point[1], 3) - 5;
        }
    };

    /**
     * Test optimization with a simplex of 3 dimensions.
     */
    @Test
    public void testOptimizeSimplex3() {
        // Given
        final double[] startPoint = new double[] {-1.2, -1.2, -1.2};
        SimpleSimplex simplex = new SimpleSimplex(3);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-5, 5.0e-5);
        MultivariateOptimizer optimizer = new SimplexOptimizer(checker);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(startPoint);
        optimizationData.setFunction(TEST_FUNCTION);
        optimizationData.setMaxEvaluations(100);
        optimizationData.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, optimizationData);

        // When
        PointValuePair result = optimizer.getOptimum();

        // Then
        Assert.assertArrayEquals(new double[] {-1.092156782293671, -0.730473357855192, -0.641828292531931}, result.getPoint(), 0.0001);
        Assert.assertEquals(-2.219258321256182, result.getValue(), 0.0001);
    }

    /**
     * Test optimization with a simplex of 4 dimensions.
     */
    @Test
    public void testOptimizeSimplex4() {
        // Given
        final double[] startPoint = new double[] {-1.2, -1.2, -1.2, -1.2};
        SimpleSimplex simplex = new SimpleSimplex(4);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-5, 5.0e-5);
        MultivariateOptimizer optimizer = new SimplexOptimizer(checker);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setInitialGuess(startPoint);
        optimizationData.setFunction(TEST_FUNCTION);
        optimizationData.setMaxEvaluations(100);
        optimizationData.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, optimizationData);

        // When
        PointValuePair result = optimizer.getOptimum();

        // Then
        Assert.assertArrayEquals(new double[] {-1.235025935529973, -1.373121899022759, -1.203632133934103, -1.150222070231864}, result.getPoint(), 0.0001);
        Assert.assertEquals(-2.31115553695664, result.getValue(), 0.0001);
    }

    /**
     * Test optimization with an invalid simplex.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullSimplex() {
        // Given
        SimpleSimplex simplex = null;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-5, 5.0e-5);
        MultivariateOptimizer optimizer = new SimplexOptimizer(checker);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setFunction(TEST_FUNCTION);
        optimizationData.setMaxEvaluations(100);
        optimizationData.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, optimizationData);
    }

    /**
     * Test optimization with an empty simplex.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeEmptySimplex() {
        // Given
        SimpleSimplex simplex = new SimpleSimplex(0);
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-5, 5.0e-5);
        MultivariateOptimizer optimizer = new SimplexOptimizer(checker);
        OptimizationData optimizationData = new OptimizationData();
        optimizationData.setFunction(TEST_FUNCTION);
        optimizationData.setMaxEvaluations(100);
        optimizationData.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(100, TEST_FUNCTION, GoalType.MINIMIZE, optimizationData);
    }
}