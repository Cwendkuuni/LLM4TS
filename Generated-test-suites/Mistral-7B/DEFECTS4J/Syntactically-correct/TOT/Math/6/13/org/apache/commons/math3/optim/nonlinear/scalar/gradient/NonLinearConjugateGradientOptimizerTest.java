package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ArrayInitializer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.NoPreconditioner;
import org.apache.commons.math3.optim.preconditions.SimplePreconditioner;
import org.apache.commons.math3.optim.search.*;
import org.apache.commons.math3.optim.test.AbstractUnivariateOptimizationTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest extends AbstractUnivariateOptimizationTest {

    /** Function to test. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) {
            return -2 * Math.sin(x) * Math.cos(x);
        }
    };

    /** Optimizer to test. */
    private Optimizer optimizer;

    /**
     * Set up test.
     */
    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(1e-6),
                new IdentityPreconditioner());
    }

    /**
     * Test optimization with identity preconditioner.
     */
    @Test
    public void testOptimizationIdentityPreconditioner() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };
        final PointValuePair result = optimizer.optimize(FUNCTION, x, null);

        assertNotNull(result);
        assertEquals(1.182111363723545, result.getPoint()[0], 1e-6);
        assertEquals(-1.175201112712023e-7, result.getValue(), 1e-7);
    }

    /**
     * Test optimization with no preconditioner.
     */
    @Test
    public void testOptimizationNoPreconditioner() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };
        final Optimizer optimizerNoPreconditioner = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(1e-6),
                NoPreconditioner.INSTANCE);

        final PointValuePair result = optimizerNoPreconditioner.optimize(FUNCTION, x, null);

        assertNotNull(result);
        assertEquals(1.182111363723545, result.getPoint()[0], 1e-6);
        assertEquals(-1.175201112712023e-7, result.getValue(), 1e-7);
    }

    /**
     * Test optimization with simple preconditioner.
     */
    @Test
    public void testOptimizationSimplePreconditioner() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };
        final Optimizer optimizerSimplePreconditioner = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(1e-6),
                new SimplePreconditioner());

        final PointValuePair result = optimizerSimplePreconditioner.optimize(FUNCTION, x, null);

        assertNotNull(result);
        assertEquals(1.182111363723545, result.getPoint()[0], 1e-6);
        assertEquals(-1.175201112712023e-7, result.getValue(), 1e-7);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test
    public void testOptimizationInvalidGoalType() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };

        assertThrows(IllegalArgumentException.class, () -> {
            optimizer = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.POLAK_RIBIERE,
                    new SimpleValueChecker(1e-6),
                    new BrentSolver(1e-6),
                    new IdentityPreconditioner(),
                    GoalType.MAXIMIZE);
        });
    }

    /**
     * Test optimization with invalid solver.
     */
    @Test
    public void testOptimizationInvalidSolver() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };

        assertThrows(NullPointerException.class, () -> {
            optimizer = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.POLAK_RIBIERE,
                    new SimpleValueChecker(1e-6),
                    null,
                    new IdentityPreconditioner());
        });
    }

    /**
     * Test optimization with invalid preconditioner.
     */
    @Test
    public void testOptimizationInvalidPreconditioner() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };

        assertThrows(NullPointerException.class, () -> {
            optimizer = new NonLinearConjugateGradientOptimizer(
                    ConjugateGradientFormula.POLAK_RIBIERE,
                    new SimpleValueChecker(1e-6),
                    new BrentSolver(1e-6),
                    null);
        });
    }

    /**
     * Test optimization with converged flag.
     */
    @Test
    public void testOptimizationConvergedFlag() {
        final double x0 = 3.0;
        final double[] x = new double[] { x0 };

        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(1e-6),
                new BrentSolver(1e-6),
                new IdentityPreconditioner()) {
                    @Override
                    protected boolean isConverged(PointValuePair current, PointValuePair previous) {
                        return current.getValue() == previous.getValue();
                    }
                };

        final PointValuePair result = optimizer.optimize(FUNCTION, x, null);

        assertNotNull(result);
        assertEquals(3.0, result.getPoint()[0], 1e-6);
        assertEquals(Double.NaN, result.getValue(), 1e-6);
    }
}