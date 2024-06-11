package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * Test for method optimize with minimum goal and positive bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMinimumPositive() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 1, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.1, 1.0, 0.5);
        assertEquals(0.362487534, result.getPoint(), 1e-5);
        assertEquals(-0.636147403, result.getValue(), 1e-5);
    }

    /**
     * Test for method optimize with minimum goal and negative bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMinimumNegative() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, 1, 2, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, SimpleGoalType.MINIMIZE, -1.0, -0.1, 0.5);
        assertEquals(-1.0, result.getPoint(), 1e-5);
        assertEquals(0.5, result.getValue(), 1e-5);
    }

    /**
     * Test for method optimize with maximum goal and positive bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMaximumPositive() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, 1, 2, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, SimpleGoalType.MAXIMIZE, 0.1, 1.0, 0.5);
        assertEquals(1.0, result.getPoint(), 1e-5);
        assertEquals(0.5, result.getValue(), 1e-5);
    }

    /**
     * Test for method optimize with maximum goal and negative bounds.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMaximumNegative() throws Exception {
        UnivariateFunction f = new PolynomialFunction(2, -1, 1, 0.5);
        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, SimpleGoalType.MAXIMIZE, -1.0, -0.1, 0.5);
        assertEquals(-1.0, result.getPoint(), 1e-5);
        assertEquals(-0.5, result.getValue(), 1e-5);
    }

    /**
     * Test for method optimize with minimum goal, positive bounds and no
     * convergence.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMinimumNoConvergence() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3, 1, 2, 3, -1);
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, SimpleGoalType.MINIMIZE, 0.1, 1.0, 0.5));
    }

    /**
     * Test for method optimize with maximum goal, positive bounds and no
     * convergence.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMaximumNoConvergence() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3, 1, 2, 3, 1);
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, SimpleGoalType.MAXIMIZE, 0.1, 1.0, 0.5));
    }

    /**
     * Test for method optimize with minimum goal, negative bounds and no
     * convergence.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMinimumNoConvergenceNegative() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3, -1, -2, -3, -1);
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, SimpleGoalType.MINIMIZE, -1.0, -0.1, 0.5));
    }

    /**
     * Test for method optimize with maximum goal, negative bounds and no
     * convergence.
     *
     * @throws Exception If an error occurs.
     */
    @Test
    public void testOptimizeMaximumNoConvergenceNegative() throws Exception {
        UnivariateFunction f = new PolynomialFunction(3, -1, -2, -3, 1);
        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(f, SimpleGoalType.MAXIMIZE, -1.0, -0.1, 0.5));
    }

    /**
     * Test for method optimize with negative relative accuracy.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeRelativeAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-0.1);
    }

    /**
     * Test for method optimize with negative absolute accuracy.
     *
     * @throws Exception If an error occurs.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeAbsoluteAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.1);
    }
}