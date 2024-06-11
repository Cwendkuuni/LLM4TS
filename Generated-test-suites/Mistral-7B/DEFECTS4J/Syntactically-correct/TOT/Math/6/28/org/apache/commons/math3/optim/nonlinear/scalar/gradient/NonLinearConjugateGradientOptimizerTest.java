package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization with Fletcher-Reeves update formula and default settings.
     */
    @Test
    public void testFletcherReevesOptimization() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, identityPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);

        assertEquals("Optimization failed", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("Optimum not found", -1.0, result.getValue(), 1e-6);
        assertArrayEquals("Start point not equal to the result", startPoint, optimizer.getPoint());
    }

    /**
     * Test optimization with Polak-Ribiére update formula and default settings.
     */
    @Test
    public void testPolakRibiereOptimization() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, identityPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);

        assertEquals("Optimization failed", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("Optimum not found", -1.0, result.getValue(), 1e-6);
        assertArrayEquals("Start point not equal to the result", startPoint, optimizer.getPoint());
    }

    /**
     * Test optimization with custom convergence checker and default settings.
     */
    @Test
    public void testCustomConvergenceChecker() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> customConvergenceChecker = new CustomConvergenceChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, customConvergenceChecker, lineSearchSolver, identityPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);

        assertEquals("Optimization failed", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("Optimum not found", -1.0, result.getValue(), 1e-6);
        assertArrayEquals("Start point not equal to the result", startPoint, optimizer.getPoint());
    }

    /**
     * Test optimization with custom preconditioner and default settings.
     */
    @Test
    public void testCustomPreconditioner() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner customPreconditioner = new CustomPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, customPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);

        assertEquals("Optimization failed", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("Optimum not found", -1.0, result.getValue(), 1e-6);
        assertArrayEquals("Start point not equal to the result", startPoint, optimizer.getPoint());
    }

    /**
     * Test optimization with custom line search solver and default settings.
     */
    @Test
    public void testCustomLineSearchSolver() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver customLineSearchSolver = new CustomLineSearchSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, customLineSearchSolver, identityPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);

        assertEquals("Optimization failed", GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals("Optimum not found", -1.0, result.getValue(), 1e-6);
        assertArrayEquals("Start point not equal to the result", startPoint, optimizer.getPoint());
    }

    /**
     * Test optimization with initial step set to zero and default settings.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testZeroInitialStep() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, identityPreconditioner);
        optimizer.setInitialStep(0);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);
    }

    /**
     * Test optimization with a custom goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCustomGoalType() {
        UnivariateFunction testFunction = new RooSquaredFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, identityPreconditioner);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);
    }

    /**
     * Test optimization with a non-differentiable function.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNonDifferentiableFunction() {
        UnivariateFunction testFunction = new AbsFunction();
        Preconditioner identityPreconditioner = new IdentityPreconditioner();
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(1e-6);
        UnivariateSolver lineSearchSolver = new BrentSolver();
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                updateFormula, convergenceChecker, lineSearchSolver, identityPreconditioner);

        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(testFunction, startPoint);
    }
}

class RooSquaredFunction implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        double[] startPoint = new double[]{0.0, 0.0};
        double[] gradient = computeObjectiveGradient(startPoint);
        double sum = 0;
        for (int i = 0; i < gradient.length; ++i) {
            sum += gradient[i] * gradient[i];
        }
        return sum;
    }

    /** {@inheritDoc} */
    public double[] valuePartials(double x) {
        double[] startPoint = new double[]{0.0, 0.0};
        return computeObjectiveGradient(startPoint);
    }

    private double[] computeObjectiveGradient(double[] point) {
        double x = point[0];
        double y = point[1];
        double[] gradient = new double[2];
        gradient[0] = 2 * x;
        gradient[1] = 2 * y;
        return gradient;
    }
}

class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    /** {@inheritDoc} */
    public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
        double difference = 0;
        for (int i = 0; i < current.getPoint().length; ++i) {
            difference += Math.pow(current.getPoint()[i] - previous.getPoint()[i], 2);
        }
        return difference < 1e-12;
    }
}

class CustomPreconditioner implements Preconditioner {
    /** {@inheritDoc} */
    public double[] precondition(double[] variables, double[] r) {
        for (int i = 0; i < variables.length; ++i) {
            variables[i] += r[i];
        }
        return r;
    }
}

class CustomLineSearchSolver implements UnivariateSolver {
    /** {@inheritDoc} */
    public double solve(int maxEvaluations, UnivariateFunction f, double start, double end, double stepMin) {
        throw new UnsupportedOperationException("Custom line search solver is not implemented");
    }
}