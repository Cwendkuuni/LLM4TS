package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula)}.
     */
    @Deprecated
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerWithDefaultCheckerAndBrentSolverAndIdentityPreconditioner() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerWithCustomCheckerAndBrentSolverAndIdentityPreconditioner() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10));

        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver)}.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerWithCustomCheckerAndBrentSolverAndCustomPreconditioner() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final Preconditioner customPreconditioner = x -> new double[]{x[0], x[1] + x[0]};

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver());
        optimizer.setPreconditioner(customPreconditioner);

        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}.
     */
    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizerWithCustomCheckerAndBrentSolverAndCustomPreconditioner() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final Preconditioner customPreconditioner = x -> new double[]{x[0], x[1] + x[0]};

        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), customPreconditioner);

        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @org.junit.Test
    public void testSetInitialStep() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());

        optimizer.setInitialStep(0.5);
        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithoutFunction() {
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());
        optimizer.optimize(null, null, GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithInvalidGoalType() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());

        optimizer.optimize(rosenbrock, null, GoalType.MAXIMIZE);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithNullStartPoint() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());

        optimizer.optimize(rosenbrock, null, GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithNonVectorStartPoint() {
        final UnivariateFunction rosenbrock = x -> x * x;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());

        optimizer.optimize(rosenbrock, 3, GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @org.junit.Test
    public void testDoOptimize() {
        final UnivariateFunction rosenbrock = x -> x[0] * x[0] + 100 * (x[1] - x[0] * x[0] + 1) * (x[1] - x[0] * x[0] + 1);
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(1e-10), new BrentSolver(), new IdentityPreconditioner());

        final double[] startPoint = new double[]{3, 3};
        final PointValuePair result = optimizer.optimize(rosenbrock, startPoint, GoalType.MINIMIZE);
        final double[] expectedResult = new double[]{1.233141551356736E-10, 2.753333311318132E-5};
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(result.getPoint()));
        assertEquals(1.232752787321983E-10, result.getValue(), 1e-10);
    }
}