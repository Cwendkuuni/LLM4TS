package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NewtonRaphsonOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.preconditioner.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.solvers.LineSearchFunctions.BrentLineSearchFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.solvers.LineSearchFunctions.GoldenSectionSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.solvers.LineSearchFunctions.QuadraticInterpolationPolakRibiereLineSearch;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NonLinearConjugateGradientOptimizerTest extends AbstractTest {

    private UnivariateFunction f = f -> Math.pow(f, 2);
    private double[] startPoint = {1.0, 1.0};
    private double[] expectedOptimum = {0.0, 0.0};

    @Test
    public void testConjugateGradientWithFletcherReeves() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithPolakRibiere() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithCustomPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        return new double[]{r[0] * 2, r[1] * 2};
                    }
                });

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(new double[]{0.0, 0.0}), Arrays.toString(optimizer.getOptimum()));
    }

    @Test(expected = MathIllegalStateException.class)
    public void testConjugateGradientWithoutGoalType() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker());

        optimizer.optimize(f, startPoint);

        assertNull(optimizer.getOptimum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConjugateGradientWithInvalidUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.INVALID,
                new SimpleValueChecker());

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, startPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConjugateGradientWithNullChecker() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                null);

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, startPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConjugateGradientWithNullSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                null);

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, startPoint));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConjugateGradientWithNullPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                null);

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(f, startPoint));
    }

    @Test
    public void testConjugateGradientWithCustomSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new LevenbergMarquardtIterativeSolver(),
                new IdentityPreconditioner());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithCustomSolverAndCustomPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new NewtonRaphsonOptimizer(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        return new double[]{r[0] * 2, r[1] * 2};
                    }
                });

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(new double[]{0.0, 0.0}), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithLineSearchFunctionBrent() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithLineSearchFunctionGoldenSection() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new GoldenSectionSearch());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithLineSearchFunctionQuadraticInterpolationPolakRibiere() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new QuadraticInterpolationPolakRibiereLineSearch());

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);

        optimizer.optimize(f, startPoint);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }

    @Test
    public void testConjugateGradientWithConstraint() {
        NonLinearConstraintSet constraints = new NonLinearConstraintSet();
        constraints.addConstraint(new NonLinearConstraint(f, 2.0));

        NonLinearObjectiveFunction objectiveFunction = new NonLinearObjectiveFunction(f, startPoint);

        NonLinearSolver optimizer = new GaussNewtonOptimizer(new SimpleValueChecker(), new IdentityPreconditioner(),
                GoalType.MINIMIZE, MaxEval.UNLIMITED, constraints);

        optimizer.optimize(objectiveFunction);

        assertNotNull(optimizer.getOptimum());
        assertEquals(Arrays.toString(expectedOptimum), Arrays.toString(optimizer.getOptimum()));
    }
}