package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for the {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization of a simple quadratic function using the default
     * solver, checker and preconditioner.
     */
    @Test
    public void testSimpleQuadraticFunction() {
        final UnivariateFunction f = (x) -> Math.pow(x, 2);
        final double[] startPoint = {1.0};
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(0.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }

    /**
     * Test optimization of a simple quadratic function with negative start point.
     */
    @Test
    public void testSimpleQuadraticFunctionNegativeStartPoint() {
        final UnivariateFunction f = (x) -> Math.pow(x, 2);
        final double[] startPoint = {-1.0};
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
            fail("IllegalArgumentException expected");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test optimization of a simple quadratic function with a preconditioner.
     */
    @Test
    public void testSimpleQuadraticFunctionWithPreconditioner() {
        final UnivariateFunction f = (x) -> Math.pow(x, 2);
        final double[] startPoint = {1.0};
        final Preconditioner preconditioner = (variables, r) -> {
            for (int i = 0; i < variables.length; ++i) {
                variables[i] += r[i];
            }
            return r.clone();
        };
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner);
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(0.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }

    /**
     * Test optimization of a rosenbrock function using the default
     * solver, checker and preconditioner.
     */
    @Test
    public void testRosenbrockFunction() {
        final UnivariateFunction f = (x) -> 100.0 * (Math.pow(x[0] - 1.0, 2) + Math.pow(Math.pow(x[1], 2) - x[0], 2));
        final double[] startPoint = {2.0, 2.0};
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(-1.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getPoint()[1], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }

    /**
     * Test optimization of a rosenbrock function with a negative start point.
     */
    @Test
    public void testRosenbrockFunctionNegativeStartPoint() {
        final UnivariateFunction f = (x) -> 100.0 * (Math.pow(x[0] - 1.0, 2) + Math.pow(Math.pow(x[1], 2) - x[0], 2));
        final double[] startPoint = {-2.0, 2.0};
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
            fail("IllegalArgumentException expected");
        } catch (MathIllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test optimization of a rosenbrock function with a custom solver.
     */
    @Test
    public void testRosenbrockFunctionCustomSolver() {
        final UnivariateFunction f = (x) -> 100.0 * (Math.pow(x[0] - 1.0, 2) + Math.pow(Math.pow(x[1], 2) - x[0], 2));
        final double[] startPoint = {2.0, 2.0};
        final UnivariateSolver customSolver = new BrentSolver();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                customSolver,
                new IdentityPreconditioner());
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(-1.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getPoint()[1], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }

    /**
     * Test optimization of a simple function with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        final UnivariateFunction f = (x) -> Math.pow(x - 1.0, 2);
        final double[] startPoint = {1.1};
        final ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 1e-5;
            }
        };
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                customChecker,
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(1.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }

    /**
     * Test optimization of a simple function with a custom value checker.
     */
    @Test
    public void testCustomValueChecker() {
        final UnivariateFunction f = (x) -> Math.pow(x - 1.0, 2);
        final double[] startPoint = {1.1};
        final SimpleValueChecker customChecker = new SimpleValueChecker() {
            @Override
            public boolean isZero(double value, double tolerance) {
                return Math.abs(value) < 1e-5;
            }
        };
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                customChecker,
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize(f, startPoint, GoalType.MINIMIZE);
        assertEquals(1.0, optimizer.getPoint()[0], 1e-6);
        assertEquals(0.0, optimizer.getValue(), 1e-6);
    }
}