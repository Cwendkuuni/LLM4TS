package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.ConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.NonLinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {
    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}
     * constructor.
     */
    @Test
    public void testConstructor() {
        // Test with Fletcher-Reeves formula
        ConjugateGradientOptimizer opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, opt.getUpdateFormula());
        // Test with Polak-Ribiere formula
        opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, opt.getUpdateFormula());
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}
     * method.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());
        opt.setInitialStep(2.0);
        assertEquals(2.0, opt.getInitialStep(), 0.0);
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#optimize(double[], double[], double[], double[], double[], double[], double[], double[], ConvergenceChecker)}
     * method.
     */
    @Test
    public void testOptimize() {
        // Test with a linear objective function and no constraints
        NonLinearConjugateGradientOptimizer opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        double[] startPoint = {1.0, 2.0};
        double[] expectedSolution = {3.0, 4.0};
        double[] gradient = {5.0, 6.0};

        opt.optimize(startPoint, gradient, null, null, null, null, null, null, new SimpleValueChecker());

        assertArrayEquals(expectedSolution, opt.getPoint(), 0.0);

        // Test with a non-linear objective function and no constraints
        UnivariateFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{3.0, 4.0});
        LinearConstraints constraints = new LinearConstraints();
        opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        startPoint = new double[]{1.0, 2.0};

        opt.optimize(startPoint, null, objective, null, null, null, null, null, new SimpleValueChecker());

        assertArrayEquals(new double[]{3.0, 4.0}, opt.getPoint(), 0.0);

        // Test with a linear objective function and a constraint
        UnivariateFunction objectiveWithConstraint = new LinearObjectiveFunction(new double[]{1.0, 0.0}, new double[]{3.0, 4.0});
        LinearConstraints constraintsWithBound = new LinearConstraints(new double[]{1.0, 2.0}, new double[]{-1.0, 5.0});
        opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        startPoint = new double[]{1.0, 2.0};

        PointValuePair solution = opt.optimize(startPoint, null, objectiveWithConstraint, new SimpleBounds(0.0, 5.0), null, null, null, null, new SimpleValueChecker());

        assertArrayEquals(new double[]{3.0, 2.0}, solution.getPoint(), 0.0);
        assertEquals(GoalType.MINIMIZE, solution.getGoalType());
    }

    /**
     * Test for {@link NonLinearConjugateGradientOptimizer#optimize(double[], double[], double[], double[], double[], double[], double[], double[], ConvergenceChecker)}
     * method with an error thrown when the optimization fails.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeException() {
        NonLinearConjugateGradientOptimizer opt = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        double[] startPoint = {1.0, 2.0};
        double[] gradient = {5.0, 6.0};

        // Test with an objective function that has no roots
        UnivariateFunction objective = new LinearObjectiveFunction(new double[]{-1.0, -1.0}, new double[]{3.0, 4.0});
        opt.optimize(startPoint, gradient, null, null, null, null, null, null, new SimpleValueChecker());
    }
}