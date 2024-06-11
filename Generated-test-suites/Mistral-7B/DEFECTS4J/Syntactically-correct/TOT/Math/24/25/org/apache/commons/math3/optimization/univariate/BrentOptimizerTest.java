package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.magic.TestFunctions;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalings.PolynomialScaling;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function for optimization.
     */
    private UnivariateFunction testFunction;

    /**
     * Convergence checker.
     */
    private ConvergenceChecker<PointValuePair> convergenceChecker
        = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration,
                                      PointValuePair previous,
                                      PointValuePair current) {
                return iteration > 1000;
            }
        };

    /**
     * Sets up the test.
     */
    @Before
    public void setup() {
        testFunction = new TestFunctions.Sine();
    }

    /**
     * Tests optimization of a simple function where the minimum is known.
     */
    @Test
    public void testOptimization() {
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-8, 1e-8, convergenceChecker);
        SimpleBounds bounds = new SimpleBounds(0, 10);
        PointValuePair solution = optimizer.optimize(testFunction, bounds);
        Assert.assertEquals(0.5, solution.getPoint(), 1e-6);
    }

    /**
     * Tests optimization of a function where the minimum is not known (button
     * function).
     */
    @Test
    public void testOptimizationUnknownMinimum() {
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-8, 1e-8, convergenceChecker);
        SimpleBounds bounds = new SimpleBounds(-10, 10);
        PointValuePair solution = optimizer.optimize(testFunction, bounds);
        Assert.assertTrue(solution.getValue() < 0);
    }

    /**
     * Tests optimization of a function with a non-strictly positive absolute
     * threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-8, -1e-8, convergenceChecker);
        SimpleBounds bounds = new SimpleBounds(0, 10);
        optimizer.optimize(testFunction, bounds);
    }

    /**
     * Tests optimization of a function with a non-positive relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationNegativeRelativeThreshold() {
        BrentOptimizer optimizer
            = new BrentOptimizer(-1e-8, 1e-8, convergenceChecker);
        SimpleBounds bounds = new SimpleBounds(0, 10);
        optimizer.optimize(testFunction, bounds);
    }

    /**
     * Tests optimization of a function with a non-strictly positive scaling.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationNonPositiveScaling() {
        BrentOptimizer optimizer
            = new BrentOptimizer(1e-8, 1e-8, convergenceChecker);
        PolynomialScaling scaling = new PolynomialScaling(-1);
        SimpleBounds bounds = new SimpleBounds(0, 10);
        optimizer.setScaling(scaling);
        optimizer.optimize(testFunction, bounds);
    }
}