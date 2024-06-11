package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NotFoundException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the MultiDirectional optimizer.
 */
public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction function;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        function = new PointValuePairFunction(new FibonacciSearchOptimizer()) {
            @Override
            public PointValuePair value(final double[] x) {
                return new PointValuePair(x[0], Math.pow(x[0], 2));
            }
        };
    }

    /**
     * Tests the optimizer with default coefficients.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefault() throws Exception {
        final double[] xMin = new double[] { 0.0 };
        final double[] xMax = new double[] { 10.0 };
        final SimpleBounds bounds = new SimpleBounds(xMin, xMax);
        optimizer.setBounds(bounds);
        optimizer.optimize(function, GoalType.MINIMIZE);

        // check result
        final PointValuePair result = optimizer.getOptimum();
        Assert.assertEquals("Wrong minimum found", 2.0, result.getValue(), 1e-6);

        // check convergence
        Assert.assertTrue("Convergence not detected", optimizer.isConverged());
    }

    /**
     * Tests the optimizer with specified coefficients.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomized() throws Exception {
        final double[] xMin = new double[] { 0.0 };
        final double[] xMax = new double[] { 10.0 };
        final SimpleBounds bounds = new SimpleBounds(xMin, xMax);
        optimizer = new MultiDirectional(3.0, 0.8);
        optimizer.setBounds(bounds);
        optimizer.optimize(function, GoalType.MINIMIZE);

        // check result
        final PointValuePair result = optimizer.getOptimum();
        Assert.assertEquals("Wrong minimum found", 2.0, result.getValue(), 1e-6);

        // check convergence
        Assert.assertTrue("Convergence not detected", optimizer.isConverged());
    }

    /**
     * Tests the optimizer with a non-converging function.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testNonConverging() throws Exception {
        final double[] xMin = new double[] { 0.0 };
        final double[] xMax = new double[] { 10.0 };
        final SimpleBounds bounds = new SimpleBounds(xMin, xMax);
        optimizer.setBounds(bounds);
        optimizer.optimize(new PointValuePairFunction(new GoalTypeRaiseFunction()) {
            @Override
            public PointValuePair value(final double[] x) {
                return new PointValuePair(x[0], 1.0 + Math.pow(x[0], 2));
            }
        }, GoalType.MINIMIZE);
    }

    /**
     * Tests the optimizer with a non-bracketing function.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoBracketingException.class)
    public void testNonBracketing() throws Exception {
        final double[] xMin = new double[] { -10.0 };
        final double[] xMax = new double[] { 10.0 };
        final SimpleBounds bounds = new SimpleBounds(xMin, xMax);
        optimizer.setBounds(bounds);
        optimizer.optimize(new PointValuePairFunction(new IdentityFunction()) {
            @Override
            public PointValuePair value(final double[] x) {
                if (x[0] < 0.0) {
                    return new PointValuePair(x, -Math.pow(x[0], 2));
                } else {
                    return new PointValuePair(x, Math.pow(x[0], 2));
                }
            }
        }, GoalType.MINIMIZE);
    }

    /**
     * Tests the optimizer with an invalid initial guess.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialGuess() throws Exception {
        final double[] xMin = new double[] { 0.0 };
        final double[] xMax = new double[] { 10.0 };
        final SimpleBounds bounds = new SimpleBounds(xMin, xMax);
        final InitialGuess initialGuess = new InitialGuess() {
            @Override
            public PointValuePair getInitialPoint() {
                throw new IllegalArgumentException("Invalid initial guess");
            }
        };
        optimizer.setBounds(bounds);
        optimizer.setInitialGuess(initialGuess);
        optimizer.optimize(function, GoalType.MINIMIZE);
    }

}