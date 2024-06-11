package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunctions;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for SimplexOptimizer class.
 */
public class SimplexOptimizerTest {
    /** The optimizer to test. */
    private SimplexOptimizer optimizer;

    /** The function to optimize. */
    private MultivariateFunction function;

    /** The convergence checker. */
    private ConvergenceChecker<PointValuePair> checker;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        function = new MultivariateFunctions.RosenbrockFunction();
        checker = optimizer.getConvergenceChecker();
    }

    /**
     * Test optimization with a simple goal type and default parameters.
     *
     * @throws MaxEvalException if the optimization does not converge.
     */
    @Test
    public void testOptimization()
            throws MaxEvalException {
        PointValuePair result = optimizer.optimize(50, function, GoalType.MINIMIZE,
                                                 new double[] { 1.0, 1.0 });
        Assert.assertEquals(new PointValuePair(new double[] { 0.0, 0.0 }, 0.0), result, 0.0);
    }

    /**
     * Test optimization with a large number of parameters.
     *
     * @throws MaxEvalException if the optimization does not converge.
     */
    @Test
    public void testOptimizationWithLargeNumberOfParameters()
            throws MaxEvalException {
        PointValuePair result = optimizer.optimize(50, function, GoalType.MINIMIZE,
                                                 new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 });
        Assert.assertEquals(new PointValuePair(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 }, 0.0),
                           result, 0.0);
    }

    /**
     * Test optimization with a custom convergence checker.
     *
     * @throws MaxEvalException if the optimization does not converge.
     */
    @Test
    public void testOptimizationWithCustomConvergenceChecker()
            throws MaxEvalException {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous,
                                     PointValuePair current) {
                return current.getValue() < 1e-4;
            }
        };
        optimizer = new SimplexOptimizer(customChecker);
        PointValuePair result = optimizer.optimize(50, function, GoalType.MINIMIZE,
                                                 new double[] { 1.0, 1.0 });
        Assert.assertEquals(new PointValuePair(new double[] { 0.0, 0.0 }, 0.0), result, 1e-4);
    }

    /**
     * Test optimization with an incorrect number of dimensions.
     *
     * @throws MaxEvalException if the optimization does not converge.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithIncorrectNumberOfDimensions()
            throws MaxEvalException {
        optimizer.optimize(50, function, GoalType.MINIMIZE, new double[3]);
    }
}