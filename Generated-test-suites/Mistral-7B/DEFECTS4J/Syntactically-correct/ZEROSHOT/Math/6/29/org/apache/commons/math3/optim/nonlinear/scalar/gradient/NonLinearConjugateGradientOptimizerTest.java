package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.NonLinearConstraint;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with identity preconditioner and {@link SimpleValueChecker} convergence checker.
     */
    @Test
    public void testConstructorWithSimpleValueCheckerAndIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with custom preconditioner and {@link SimpleValueChecker} convergence checker.
     */
    @Test
    public void testConstructorWithCustomPreconditionerAndSimpleValueChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver)}
     * with identity preconditioner and {@link SimpleValueChecker} convergence checker.
     */
    @Test
    public void testConstructorWithSimpleValueCheckerAndIdentityPreconditionerInOptimizer() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker());
        Assert.assertNotNull(optimizer);
        Assert.assertNull(optimizer.solver);
        Assert.assertNull(optimizer.preconditioner);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker)}
     * with default solver and preconditioner.
     */
    @Test
    public void testConstructorWithSimpleValueChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker());
        Assert.assertNotNull(optimizer);
        Assert.assertNull(optimizer.solver);
        Assert.assertNull(optimizer.preconditioner);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.setInitialStep(2.0);
        Assert.assertEquals(2.0, optimizer.initialStep, 0.0);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#optimize()}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithoutFunction() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize();
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#optimize()}.
     */
    @Test
    public void testOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                new RootFunction(),
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize();
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#optimize()}.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithConvergenceFailure() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                new RootFunction(),
                new ConvergenceChecker() {
                    @Override
                    public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                        return false;
                    }
                },
                new BrentSolver(),
                new IdentityPreconditioner());
        optimizer.optimize();
    }

    /**
     * Test function for root finding.
     */
    private static class RootFunction implements UnivariateFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return Math.pow(x - 1.0, 2.0) + 0.1 * Math.sin(Math.PI * x);
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            return 2.0 * (x - 1.0) + 0.1 * Math.cos(Math.PI * x);
        }
    }
}