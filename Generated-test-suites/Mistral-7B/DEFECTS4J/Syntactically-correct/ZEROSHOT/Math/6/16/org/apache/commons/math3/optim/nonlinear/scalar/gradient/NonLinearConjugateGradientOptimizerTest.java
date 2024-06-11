package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.UnivariateSolver.BracketingStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test file for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test for constructor with default parameters.
     */
    @Test
    public void testConstructorDefault() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer.getPreconditioner());
        assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.getUpdateFormula());
        assertInstanceOf(SimpleValueChecker.class, optimizer.getConvergenceChecker());
        assertInstanceOf(BrentSolver.class, optimizer.getSolver());
        assertInstanceOf(IdentityPreconditioner.class, optimizer.getPreconditioner());
    }

    /**
     * Test for constructor with custom parameters.
     */
    @Test
    public void testConstructorCustom() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }

            public double derivative(double x) {
                return 2 * x;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return (iter > 100);
            }
        };

        Preconditioner preconditioner = new Preconditioner() {
            public double[] precondition(double[] variables, double[] r) {
                double[] result = new double[variables.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = r[i];
                }
                return result;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                convergenceChecker,
                new BrentSolver(BracketingStrategy.DECREASING, 0.01, 10),
                preconditioner);

        assertNotNull(optimizer);
        assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.getUpdateFormula());
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
        assertInstanceOf(BrentSolver.class, optimizer.getSolver());
        assertSame(preconditioner, optimizer.getPreconditioner());
    }

    /**
     * Test for setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(-1);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test for optimize method with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }

            public double derivative(double x) {
                return 2.0 * x;
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for optimize method with a non-convex function.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeNonConvexFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                if (x > 0) {
                    return Math.pow(x, 2);
                } else {
                    return -Math.pow(-x, 2);
                }
            }

            public double derivative(double x) {
                if (x > 0) {
                    return 2.0 * x;
                } else {
                    return -2.0 * (-x);
                }
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function);
        fail("Optimization should fail with a MathIllegalStateException");
    }

}