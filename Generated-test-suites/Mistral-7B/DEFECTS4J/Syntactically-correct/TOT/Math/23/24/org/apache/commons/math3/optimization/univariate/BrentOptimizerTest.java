package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /** A simple test function. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }
    };

    /** The optimizer to be tested. */
    private BrentOptimizer optimizer;

    /** The test function. */
    private UnivariateFunction function;

    /** The optimizer's goal type. */
    private GoalType goalType;

    /** The relative tolerance. */
    private double relativeTolerance;

    /** The absolute tolerance. */
    private double absoluteTolerance;

    /** The convergence checker. */
    private ConvergenceChecker<UnivariatePointValuePair> checker;

    /**
     * Sets up the test environment.
     *
     * @throws NumberIsTooSmallException if the tolerance values are too small.
     */
    @Before
    public void setUp() throws NumberIsTooSmallException {
        this.function = TEST_FUNCTION;
        this.goalType = GoalType.MINIMIZE;
        this.relativeTolerance = 0.001;
        this.absoluteTolerance = 0.1;
        this.checker = null;
        this.optimizer = new BrentOptimizer(this.relativeTolerance, this.absoluteTolerance, this.checker);
    }

    /**
     * Tests the optimization of a simple function using the default initial
     * guess.
     */
    @Test
    public void testSimpleFunction() {
        UnivariatePointValuePair result = this.optimizer.optimize(this.function, this.goalType);
        double expectedX = -1.0;
        double expectedValue = 0.0;
        Assert.assertEquals(expectedX, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }

    /**
     * Tests the optimization of a simple function with a given initial guess.
     */
    @Test
    public void testSimpleFunctionWithInitialGuess() {
        double initialGuess = 1.0;
        UnivariatePointValuePair result = this.optimizer.optimize(this.function, this.goalType, initialGuess);
        double expectedX = -1.0;
        double expectedValue = 0.0;
        Assert.assertEquals(expectedX, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }

    /**
     * Tests the optimization of a simple function with user-defined convergence
     * checker.
     */
    @Test
    public void testSimpleFunctionWithCustomCheck() {
        MyConvergenceChecker checker = new MyConvergenceChecker(10);
        UnivariatePointValuePair result = this.optimizer.optimize(this.function, this.goalType, checker);
        double expectedX = -1.0;
        double expectedValue = 0.0;
        Assert.assertEquals(expectedX, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }

    /**
     * Tests the optimization of a simple function with a large tolerance.
     */
    @Test
    public void testLargeTolerance() {
        this.relativeTolerance = 1.0;
        this.absoluteTolerance = 10.0;
        UnivariatePointValuePair result = this.optimizer.optimize(this.function, this.goalType);
        double expectedX = -1.0;
        double expectedValue = 0.0;
        Assert.assertEquals(expectedX, result.getPoint(), 0.1);
        Assert.assertEquals(expectedValue, result.getValue(), 0.1);
    }

    /**
     * Tests the optimization of a simple function with a very small tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testVerySmallTolerance() {
        this.relativeTolerance = 0.0;
        this.absoluteTolerance = 0.0;
        this.optimizer = new BrentOptimizer(this.relativeTolerance, this.absoluteTolerance, this.checker);
        this.optimizer.optimize(this.function, this.goalType);
    }

    /**
     * Test custom convergence checker.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

        /** Number of iterations. */
        private final int maxIterations;

        /**
         * Constructor.
         *
         * @param maxIterations The maximum number of iterations.
         */
        public MyConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iter >= this.maxIterations;
        }
    }
}