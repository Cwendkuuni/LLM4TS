package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.PolakRibiereFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.Preconditioner;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Fletcher-Reeves update formula, SimpleValueChecker, BrentSolver and IdentityPreconditioner.
     */
    @Test
    public void testSimpleFletcherReeves() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return -2 * (Math.sin(x) * Math.cos(x) + Math.cos(x) * Math.sin(x));
            }
        };
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6, 1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            PointValuePair result = optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
            Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
            Assert.assertEquals(0.0, result.getValue(), 1e-6);
        } catch (MaxEvalException e) {
            Assert.fail("Test should not throw MaxEvalException");
        }
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Polak-Ribiére update formula, SimpleValueChecker, BrentSolver and IdentityPreconditioner.
     */
    @Test
    public void testSimplePolakRibiere() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return -2 * (Math.sin(x) * Math.cos(x) + Math.cos(x) * Math.sin(x));
            }
        };
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6, 1e-6),
                new BrentSolver(),
                new IdentityPreconditioner());
        try {
            PointValuePair result = optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
            Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
            Assert.assertEquals(0.0, result.getValue(), 1e-6);
        } catch (MaxEvalException e) {
            Assert.fail("Test should not throw MaxEvalException");
        }
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Fletcher-Reeves update formula, SimpleValueChecker, LevenbergMarquardtIterativeSolver and IdentityPreconditioner.
     */
    @Test
    public void testComplexFletcherReeves() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6, 1e-6),
                new LevenbergMarquardtIterativeSolver(),
                new IdentityPreconditioner());
        try {
            PointValuePair result = optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
            Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
            Assert.assertEquals(0.0, result.getValue(), 1e-6);
        } catch (MaxEvalException e) {
            Assert.fail("Test should not throw MaxEvalException");
        }
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Polak-Ribiére update formula, SimpleValueChecker, LevenbergMarquardtIterativeSolver and IdentityPreconditioner.
     */
    @Test
    public void testComplexPolakRibiere() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6, 1e-6),
                new LevenbergMarquardtIterativeSolver(),
                new IdentityPreconditioner());
        try {
            PointValuePair result = optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
            Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
            Assert.assertEquals(0.0, result.getValue(), 1e-6);
        } catch (MaxEvalException e) {
            Assert.fail("Test should not throw MaxEvalException");
        }
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Fletcher-Reeves update formula, SimpleValueChecker, BrentSolver and null preconditioner.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNoPreconditioner() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6, 1e-6),
                new BrentSolver(),
                null);
        try {
            optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
        } finally {
            // test should throw an exception
        }
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * with Polak-Ribiére update formula, SimpleValueChecker, BrentSolver and null preconditioner.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testNoPreconditionerPolakRibiere() {
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        NonlinearOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6, 1e-6),
                new BrentSolver(),
                null);
        try {
            optimizer.optimize(new UnconstrainedOptimizationProblem(function, new double[]{0.0}));
        } finally {
            // test should throw an exception
        }
    }
}