package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.function.Functions;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimization with Fletcher-Reeves formula and no preconditioning.
     */
    @Test
    public void testFletcherReevesNoPreconditioning() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver()) {
            @Override
            public double[] getStartPoint() {
                return new double[] {1.0, 1.0};
            }
        };
        optimizer.setMaxEvaluations(100);

        PointValuePair result = optimizer.optimize(Functions.ROSENBROCK, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getValue1(), 1e-9);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getValue2(), 1e-9);
    }

    /**
     * Test optimization with Polak-Ribiére formula and identity preconditioning.
     */
    @Test
    public void testPolakRibiereIdentityPreconditioning() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()) {
            @Override
            public double[] getStartPoint() {
                return new double[] {1.0, 1.0};
            }
        };
        optimizer.setMaxEvaluations(100);

        PointValuePair result = optimizer.optimize(Functions.ROSENBROCK, GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getValue1(), 1e-9);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getValue2(), 1e-9);
    }

    /**
     * Test optimization with Fletcher-Reeves formula, custom preconditioning,
     * and maximum number of evaluations reached.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testFletcherReevesCustomPreconditioningMaxEvaluations() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new CustomPreconditioner()) {
            @Override
            public double[] getStartPoint() {
                return new double[] {1.0, 1.0};
            }
        };
        optimizer.setMaxEvaluations(1);

        optimizer.optimize(Functions.ROSENBROCK, GoalType.MINIMIZE);
    }

    /**
     * Test optimization with Polak-Ribiére formula, no preconditioning,
     * and line search solver not set.
     */
    @Test(expected = IllegalStateException.class)
    public void testPolakRibiereNoPreconditioningNoLineSearchSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker()) {
            @Override
            public double[] getStartPoint() {
                return new double[] {1.0, 1.0};
            }
        };
        optimizer.optimize(Functions.ROSENBROCK, GoalType.MINIMIZE);
    }
}

class CustomPreconditioner implements Preconditioner {

    /** {@inheritDoc} */
    public double[] precondition(double[] variables, double[] r) {
        // Custom preconditioning implementation
        // ...

        return r.clone();
    }
}