package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterativeRootFinder;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.ScaledVectorFunction;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.PolakRibiereUpdate;
import org.apache.commons.math3.optim.nonlinear.conjugate.FletcherReevesUpdate;
import org.apache.commons.math3.optim.nonlinear.conjugate.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.JacobiPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.GaussSeidelPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.NormalEquationPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugate.LevenbergMarquardtPreconditioner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test optimizing a quadratic function with identity preconditioner and
     * Fletcher-Reeves update formula.
     */
    @Test
    public void testQuadraticIdentityFletcherReeves() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test optimizing a quadratic function with identity preconditioner and
     * Polak-Ribiére update formula.
     */
    @Test
    public void testQuadraticIdentityPolakRibiere() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new IdentityPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.POLAK_RIBIERE;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test optimizing a quadratic function with Jacobi preconditioner and
     * Fletcher-Reeves update formula.
     */
    @Test
    public void testQuadraticJacobiFletcherReeves() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new JacobiPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test optimizing a quadratic function with Gauss-Seidel preconditioner and
     * Fletcher-Reeves update formula.
     */
    @Test
    public void testQuadraticGaussSeidelFletcherReeves() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new GaussSeidelPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test optimizing a quadratic function with normal equation preconditioner and
     * Fletcher-Reeves update formula.
     */
    @Test
    public void testQuadraticNormalEquationFletcherReeves() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new NormalEquationPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setMaxEvaluations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test optimizing a quadratic function with Levenberg-Marquardt preconditioner and
     * Fletcher-Reeves update formula.
     */
    @Test
    public void testQuadraticLevenbergMarquardtFletcherReeves() {
        final UnivariateFunction func = new ScaledVectorFunction(new QuadraticFunction(), 10);
        final Preconditioner preconditioner = new LevenbergMarquardtPreconditioner();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new LevenbergMarquardtIterativeRootFinder(), preconditioner);
        optimizer.setMaxIterations(100);
        optimizer.optimize(func, 0.5);
        final double expectedOptimum = 0.0;
        final double tolerance = 1e-6;
        assertEquals(expectedOptimum, optimizer.getMinimum(), tolerance);
    }

    /**
     * Test exception handling: optimization of a function that does not meet the
     * requirements.
     */
    @Test
    public void testExceptionHandling() {
        final UnivariateFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Unsupported operation");
            }

            @Override
            public double derivative(double x) {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), null, new IdentityPreconditioner());
        assertThrows(UnconstrainedOptimizationException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Test exception handling: optimization of a function with an illegal initial
     * step.
     */
    @Test
    public void testExceptionHandlingInitialStep() {
        final UnivariateFunction func = new QuadraticFunction();
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setInitialStep(-0.5);
        assertThrows(MathIllegalStateException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Test optimization of a function with null pointer.
     */
    @Test
    public void testExceptionHandlingNullPointer() {
        final UnivariateFunction func = null;
        final ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        assertThrows(NullArgumentException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Test optimization of a function with an illegal goal type.
     */
    @Test
    public void testExceptionHandlingIllegalGoalType() {
        final UnivariateFunction func = new QuadraticFunction();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner()) {
            @Override
            public PointValuePair doOptimize() {
                throw new UnsupportedOperationException("Unsupported method");
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Test optimization of a function with an illegal initial step and illegal goal type.
     */
    @Test
    public void testExceptionHandlingIllegalStepAndGoalType() {
        final UnivariateFunction func = new QuadraticFunction();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner()) {
            @Override
            public PointValuePair doOptimize() {
                throw new UnsupportedOperationException("Unsupported method");
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setInitialStep(-0.5);
        assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Test optimization of a function with an illegal number of evaluations.
     */
    @Test
    public void testExceptionHandlingMaxEvaluations() {
        final UnivariateFunction func = new QuadraticFunction();
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
        optimizer.setMaxEvaluations(-10);
        assertThrows(NumberIsTooLargeException.class, () -> optimizer.optimize(func, 0.5));
    }

    /**
     * Quadratic function used for testing.
     */
    private static class QuadraticFunction implements UnivariateFunction {

        /** {@inheritDoc} */
        @Override
        public double value(double x) {
            return x * x;
        }

        /** {@inheritDoc} */
        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }
}