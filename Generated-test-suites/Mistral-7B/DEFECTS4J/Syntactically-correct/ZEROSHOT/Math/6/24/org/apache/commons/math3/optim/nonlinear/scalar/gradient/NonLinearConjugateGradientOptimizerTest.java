package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optimization.nonlinear.SimpleNonlinearSolver;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /** Function for testing. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.exp(x) - E.E0.doubleValue() - x;
        }

        @Override
        public double derivative(double x) {
            return Math.exp(x) - E.E1.doubleValue();
        }
    };

    /**
     * Test constructor with defaults.
     */
    @Test
    public void testDefaultConstruction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(SimpleValueChecker.class, optimizer.getConvergenceChecker().getClass());
        assertEquals(BrentSolver.class, optimizer.getLineSearchSolver().getClass());
        assertEquals(IdentityPreconditioner.class, optimizer.getPreconditioner().getClass());
    }

    /**
     * Test constructor with custom solver, checker, and preconditioner.
     */
    @Test
    public void testCustomConstruction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = ConvergenceCheckerFactory.createConvergenceChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = x -> x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, lineSearchSolver, preconditioner);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
        assertEquals(lineSearchSolver.getClass(), optimizer.getLineSearchSolver().getClass());
        assertEquals(preconditioner.getClass(), optimizer.getPreconditioner().getClass());
    }

    /**
     * Test optimization with identity preconditioner and default settings.
     */
    @Test
    public void testIdentityPreconditioner() throws UnconstrainedOptimizationException, NoBracketingException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        PointValuePair solution = optimizer.optimize(FUNCTION, 1.0);
        double[] expectedSolution = new double[] { 0.3626865, 1.7182818 };
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        assertEquals(-1.1628607, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization with custom preconditioner.
     */
    @Test
    public void testCustomPreconditioner() throws UnconstrainedOptimizationException, NoBracketingException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = ConvergenceCheckerFactory.createConvergenceChecker(1e-6, 100);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = x -> x + 1;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, lineSearchSolver, preconditioner);
        PointValuePair solution = optimizer.optimize(FUNCTION, 1.0);
        double[] expectedSolution = new double[] { 0.6418518, 2.3561945 };
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        assertEquals(-1.6487181, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization with custom line search solver.
     */
    @Test
    public void testCustomLineSearchSolver() throws UnconstrainedOptimizationException, NoBracketingException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = ConvergenceCheckerFactory.createConvergenceChecker(1e-6, 100);
        Preconditioner preconditioner = x -> x;
        SimpleNonlinearSolver lineSearchSolver = new SimpleNonlinearSolver(checker) {
            @Override
            public double solve(UnivariateFunction f, double bracketEnd, double initialStep)
                    throws UnconstrainedOptimizationException, NoBracketingException {
                // Custom line search implementation
                // ...
                return 0.5;
            }
        };
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, lineSearchSolver, preconditioner);
        PointValuePair solution = optimizer.optimize(FUNCTION, 1.0);
        double[] expectedSolution = new double[] { 0.3816317, 1.6664594 };
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        assertEquals(-1.3673423, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testCustomConvergenceChecker() throws UnconstrainedOptimizationException, NoBracketingException {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair start, PointValuePair current) {
                return false;
            }
        };
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = x -> x;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, checker, lineSearchSolver, preconditioner);
        optimizer.optimize(FUNCTION, 1.0);
    }

    /**
     * Test setting initial search step.
     */
    @Test
    public void testSetInitialSearchStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 1e-6);
    }
}