package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LBFGS;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.ScalarDifferentiableOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.util.FastMath;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Function to optimize. */
    private static final UnivariateFunction function = new RidgedMultiwellFunction();

    /** Optimizer under test. */
    private NonLinearConjugateGradientOptimizer optimizer;

    /** Convergence checker. */
    private SimpleValueChecker checker;

    /** Solver for line search. */
    private UnivariateSolver lineSearchSolver;

    /** Preconditioner. */
    private Preconditioner preconditioner;

    /**
     * Sets the up of the test.
     * @throws Exception if initialization fails
     */
    @Before
    public void setUp() throws Exception {
        checker = new SimpleValueChecker(1e-6);
        lineSearchSolver = new BrentSolver();
        preconditioner = new IdentityPreconditioner();
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
    }

    /**
     * Test optimization with the default configuration.
     */
    @Test
    public void testOptimize() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a custom goal type.
     */
    @Test
    public void testOptimizeGoalType() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner, GoalType.MAXIMIZE);
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MAXIMIZE, result.getGoalType());
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom convergence checker.
     */
    @Test
    public void testOptimizeChecker() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        final SimpleValueChecker checker = new SimpleValueChecker(1e-9);
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom solver for line search.
     */
    @Test
    public void testOptimizeSolver() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, new LBFGS(), preconditioner);
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom preconditioner.
     */
    @Test
    public void testOptimizePreconditioner() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, new DiagonalPreconditioner());
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom initial step.
     */
    @Test
    public void testOptimizeInitialStep() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner, 0.1);
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom function.
     */
    @Test
    public void testOptimizeCustomFunction() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        final UnivariateFunction function = new CustomFunction();
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final PointValuePair result = optimizer.optimize(function, startPoint);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 0.0);
    }

    /**
     * Test optimization with a custom constraint.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeConstraint() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final UnivariateFunction function = new RidgedMultiwellFunctionWithConstraint();
        final LinearConstraint constraint = new LinearConstraint(new double[] { 1.0 }, 0.0);
        optimizer.subjectToConstraints(constraint);
        optimizer.optimize(function, startPoint);
    }

    /**
     * Test optimization with a non-differentiable function.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNondifferentiableFunction() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final UnivariateFunction function = new StepFunction();
        optimizer.optimize(function, startPoint);
    }

    /**
     * Test optimization with a non-convex function.
     */
    @Test(expected = NoBracketingException.class)
    public void testOptimizeNonconvexFunction() {
        final double[] startPoint = new double[] { 1.0, 1.0 };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, checker, lineSearchSolver, preconditioner);
        final UnivariateFunction function = new RidgedMultiwellFunctionWithSaddlePoint();
        optimizer.optimize(function, startPoint);
    }

}

/**
 * Custom function to optimize.
 */
class CustomFunction implements UnivariateFunction {

    /** {@inheritDoc} */
    @Override
    public double value(double x) {
        return Math.sin(x) * Math.cos(x);
    }

    /** {@inheritDoc} */
    @Override
    public double[] gradient(double x) {
        final double sinX = Math.sin(x);
        final double cosX = Math.cos(x);
        return new double[] { cosX * (1.0 - sinX) - sinX * Math.cos(x), -sinX * (1.0 + sinX) - cosX * Math.sin(x) };
    }
}

/**
 * Diagonal preconditioner.
 */
class DiagonalPreconditioner implements Preconditioner {

    /** {@inheritDoc} */
    @Override
    public double[] precondition(double[] variables, double[] r) {
        final double[] preconditioned = new double[variables.length];
        for (int i = 0; i < variables.length; ++i) {
            preconditioned[i] = r[i] / FastMath.sqrt(variables[i] + 1e-15);
        }
        return preconditioned;
    }
}