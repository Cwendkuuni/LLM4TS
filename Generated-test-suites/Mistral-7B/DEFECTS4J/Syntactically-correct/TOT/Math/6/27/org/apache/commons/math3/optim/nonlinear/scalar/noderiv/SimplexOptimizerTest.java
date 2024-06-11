package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintFactory;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSetFixer;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction.LinearConstraintSensitivity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** Optimizer to test. */
    private SimplexOptimizer optimizer;

    /** Objective function for testing. */
    private MultivariateFunction f;

    /** Initial point for testing. */
    private double[] startPoint = {1.0, 1.0};

    /** Constraints for testing. */
    private LinearConstraintSet constraints;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        // Test with a simple objective function: f(x,y) = x^2 + y^2
        f = new LinearObjectiveFunction(2, new double[]{-2.0, -2.0}, new double[]{1.0, 1.0});

        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
    }

    /**
     * Tests the optimization of a simple objective function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        double[] expected = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, startPoint);
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    /**
     * Tests the optimization of a simple objective function with initial guess.
     */
    @Test
    public void testOptimizeSimpleFunctionWithInitialGuess() {
        double[] expected = new double[]{0.0, 0.0};
        InitialGuess initialGuess = new InitialGuess() {
            public double[] getInitialPoint() {
                return expected;
            }
        };
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, initialGuess);
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-6);
    }

    /**
     * Tests the optimization of a simple objective function with a custom convergence checker.
     */
    @Test
    public void testOptimizeSimpleFunctionWithCustomChecker() {
        SimpleValueChecker checker = new SimpleValueChecker(1e-7, 1e-7);
        optimizer = new SimplexOptimizer(checker);
        double[] expected = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, startPoint);
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-7);
    }

    /**
     * Tests the optimization of a simple objective function with constraints.
     */
    @Test
    public void testOptimizeSimpleFunctionWithConstraints() {
        // Define constraints: x + y <= 1
        LinearConstraint xPlusY = LinearConstraintFactory.createLessThanOrEqual(1.0, new double[]{1.0, 1.0});
        constraints = new LinearConstraintSet(xPlusY);

        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        LinearConstraintSetFixer fixer = new LinearConstraintSetFixer(optimizer, constraints);
        optimizer.setObjectiveFunction(new LinearObjectiveFunction(2, new double[]{-2.0, -2.0}, new double[]{1.0, 1.0}, LinearConstraintSensitivity.PRIMAL));

        double[] expected = new double[]{0.5, 0.5};
        PointValuePair result = optimizer.optimize(100, (MultivariateFunction) fixer.getObjectiveFunction(), GoalType.MINIMIZE, startPoint);
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-6);
    }
}