package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.preconditioners.JacobianPreconditioner;
import org.apache.commons.math3.optim.nonlinear.solvers.BrentSolver;
import org.apache.commons.math3.optim.nonlinear.solvers.LBFGS;
import org.apache.commons.math3.optim.nonlinear.solvers.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.testfunctions.RosenbrockFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 *
 * @see NonLinearConjugateGradientOptimizer
 */
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    private UnconstrainedOptimizationProblem problem;

    @Before
    public void setUp() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-9, 1e-9);
        UnivariateSolver solver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        problem = new UnconstrainedOptimizationProblem(new RosenbrockFunction(), new double[] {2.0, 2.0});
    }

    /**
     * Test the optimization with default settings.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimization() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom initialization point.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomInitialPoint() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        optimizer.setInitialPoint(new double[] {3.0, 3.0});
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom goal type.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomGoalType() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(3.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom convergence checker.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomConvergenceChecker() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean hasConverged(int iteration, PointValuePair current, PointValuePair previous) {
                return current.getValue() < 1e-8;
            }
        };
        optimizer.setConvergenceChecker(customChecker);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom line search solver.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomLineSearchSolver() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        UnivariateSolver customSolver = new LBFGS();
        optimizer.setSolver(customSolver);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom preconditioner.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomPreconditioner() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        Preconditioner customPreconditioner = new JacobianPreconditioner();
        optimizer.setPreconditioner(customPreconditioner);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom optimization algorithm (ConjugateGradient).
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomOptimizer() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        NonlinearOptimizer customOptimizer = new ConjugateGradient(ConjugateGradientFormula.POLAK_RIBIERE);
        customOptimizer.setMaxIterations(1000);
        customOptimizer.setTolerance(1e-12);
        customOptimizer.setPreconditioner(new JacobianPreconditioner());
        customOptimizer.setLineSearch(new SimpleLineSearch());
        optimizer = new NonLinearConjugateGradientOptimizer(customOptimizer);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom initial step.
     * @throws ConvergenceException
     * @throws MaxEvalException
     * @throws MathIllegalStateException
     */
    @Test
    public void testOptimizationCustomInitialStep() throws ConvergenceException, MaxEvalException, MathIllegalStateException {
        optimizer.setInitialStep(0.1);
        optimizer.optimize(problem);
        PointValuePair solution = optimizer.getSolution();
        double[] expectedSolution = new double[] {1.0, 1.0};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-9);
        assertEquals(0.0, solution.getValue(), 1e-9);
    }
}