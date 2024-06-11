package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AverageStartingPoint;
import org.apache.commons.math3.optim.initializer.MinBoundStartingPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link MultiStartUnivariateRealOptimizer} class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new PolynomialFunction(new double[]{-1d, 1d, 1d}, new double[]{0d, 1d, -1d});
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new AverageStartingPoint());
    }

    /**
     * Test optimization with simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        final double min = -5.0;
        final double max = 5.0;
        final GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = multiStartOptimizer.optimize(optimizer, goalType, min, max);
        Assert.assertNotNull(result.getValue());
        Assert.assertEquals(result.getValue(), -2.5, 1e-6);
        Assert.assertEquals(result.getPoint(), -2.5, 1e-6);
        Assert.assertArrayEquals(multiStartOptimizer.getOptima(), new double[]{ -2.5, -1.5, 0.5 }, 1e-6);
        Assert.assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[]{-3.0, -1.5, 0.25}, 1e-6);
    }

    /**
     * Test optimization with boundary constraints.
     */
    @Test(expected = NonConvergenceException.class)
    public void testOptimizeBoundedFunction() {
        final double min = 0.0;
        final double max = 1.0;
        final GoalType goalType = GoalType.MINIMIZE;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new MinBoundStartingPoint(min, max));

        multiStartOptimizer.optimize(optimizer, goalType, min, max);
    }

    /**
     * Test optimization with MaxEval limit.
     */
    @Test(expected = MaxEval.class)
    public void testOptimizeFunctionWithMaxEvalLimit() {
        final double min = 0.0;
        final double max = 1.0;
        final GoalType goalType = GoalType.MINIMIZE;
        final int maxEval = 10;

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new AverageStartingPoint());
        multiStartOptimizer.setMaxEvaluations(maxEval);

        multiStartOptimizer.optimize(optimizer, goalType, min, max);
    }
}