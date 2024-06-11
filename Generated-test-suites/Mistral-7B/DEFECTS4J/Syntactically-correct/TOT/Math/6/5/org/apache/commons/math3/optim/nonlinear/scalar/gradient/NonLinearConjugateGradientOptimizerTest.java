package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LBFGSBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.StoppingCondition;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradientOptimizer;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.Preconditioner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** The optimizer to test. */
    private ConjugateGradientOptimizer<PointValuePair> optimizer;

    /** The optimizer with Fletcher-Reeves update formula. */
    private ConjugateGradientOptimizer<PointValuePair> optimizerFR;

    /** The optimizer with Polak-Ribi&egrave;re update formula. */
    private ConjugateGradientOptimizer<PointValuePair> optimizerPR;

    /** The identity preconditioner. */
    private Preconditioner preconditioner;

    /** The no-op preconditioner. */
    private Preconditioner noopPreconditioner;

    /** The line solver. */
    private UnivariateSolver lineSolver;

    /** The Brent line solver. */
    private UnivariateSolver brentLineSolver;

    /** The L-BFGS-Brent line solver. */
    private UnivariateSolver lbfsbrentLineSolver;

    /** The convergence checker. */
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    /** The max evaluations. */
    private int maxEvaluations;

    /** The initial point. */
    private double[] initialPoint;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                                                           new SimpleValueChecker(),
                                                           new BrentSolver(),
                                                           new IdentityPreconditioner());
        optimizerFR = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                                                             new SimpleValueChecker(),
                                                             new BrentSolver(),
                                                             new IdentityPreconditioner());
        optimizerPR = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                                                             new SimpleValueChecker(),
                                                             new BrentSolver(),
                                                             new IdentityPreconditioner());
        preconditioner = new IdentityPreconditioner();
        noopPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                return r.clone();
            }
        };
        lineSolver = new BrentSolver();
        brentLineSolver = new LBFGSBrentSolver();
        lbfsbrentLineSolver = new LBFGSBrentSolver();
        convergenceChecker = new SimpleValueChecker();
        maxEvaluations = 50;
        initialPoint = new double[] { 1.0, 2.0 };
    }

    /**
     * Tests optimization of a simple function with identity preconditioner.
     */
    @Test
    public void testIdentityPreconditioner() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final PointValuePair result = optimizer.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(0, result.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -1.0, -1.0 }, result.getPoint(), 1e-12);
    }

    /**
     * Tests optimization of a simple function with no-op preconditioner.
     */
    @Test
    public void testNoopPreconditioner() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.setPreconditioner(noopPreconditioner);
        final PointValuePair result = optimizer.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(0, result.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -1.0, -1.0 }, result.getPoint(), 1e-12);
    }

    /**
     * Tests optimization of a simple function with different line solvers.
     */
    @Test
    public void testLineSolvers() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        final PointValuePair resultBrent = optimizer.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(0, resultBrent.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -1.0, -1.0 }, resultBrent.getPoint(), 1e-12);

        optimizer.setSolver(brentLineSolver);
        final PointValuePair resultLBFGSBrent = optimizer.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(0, resultLBFGSBrent.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -1.0, -1.0 }, resultLBFGSBrent.getPoint(), 1e-12);

        optimizer.setSolver(lbfsbrentLineSolver);
        final PointValuePair resultLBFGSBrentWithLineSearch = optimizer.optimize(f, initialPoint,
                                                                                 maxEvaluations,
                                                                                 new MaxEval(maxEvaluations),
                                                                                 new StoppingCondition(
                                                                                         convergenceChecker,
                                                                                         1e-12));
        Assert.assertEquals(0, resultLBFGSBrentWithLineSearch.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { -1.0, -1.0 }, resultLBFGSBrentWithLineSearch.getPoint(),
                               1e-12);
    }

    /**
     * Tests optimization with Fletcher-Reeves formula.
     */
    @Test
    public void testFletcherReevesFormula() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3 * x * x + 2;
            }
        };
        final PointValuePair result = optimizerFR.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(1.0, result.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 1e-12);
    }

    /**
     * Tests optimization with Polak-Ribi&egrave;re formula.
     */
    @Test
    public void testPolakRibiereFormula() {
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3 * x * x + 2;
            }
        };
        final PointValuePair result = optimizerPR.optimize(f, initialPoint, maxEvaluations);
        Assert.assertEquals(1.0, result.getValue(), 1e-12);
        Assert.assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 1e-12);
    }

    /**
     * Tests optimization with invalid goal type.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testInvalidGoalType() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        optimizer.optimize(f, initialPoint, maxEvaluations);
    }
}