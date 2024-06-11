package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.Method.ILLINOIS;
import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.Method.PEGASUS;
import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.Method.REGULA_FALSI;

/**
 * Test {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Function f(x) = x^3 - 2.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {

        @Override
        public double value(double x) {
            return x * x * x - 2.0;
        }

        @Override
        public double derivative(double x) {
            return 3.0 * x * x - 2.0;
        }

        @Override
        public double root() {
            return Precision.sqrt(2.0);
        }
    };

    /**
     * Test solver with Regula Falsi method.
     */
    @Test
    public void testRegulaFalsi() {
        final double absAccuracy = 1e-8;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, REGULA_FALSI);
        try {
            double root = solver.solve(maxEval, TEST_FUNCTION, -5.0, 5.0);
            Assert.assertEquals("Root found is not correct.", TEST_FUNCTION.root(), root, absAccuracy);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations was reached.");
        } catch (TooManyIterationsException e) {
            Assert.fail("Too many iterations were performed.");
        }
    }

    /**
     * Test solver with Illinois method.
     */
    @Test
    public void testIllinois() {
        final double absAccuracy = 1e-8;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, ILLINOIS);
        try {
            double root = solver.solve(maxEval, TEST_FUNCTION, -5.0, 5.0);
            Assert.assertEquals("Root found is not correct.", TEST_FUNCTION.root(), root, absAccuracy);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations was reached.");
        } catch (TooManyIterationsException e) {
            Assert.fail("Too many iterations were performed.");
        }
    }

    /**
     * Test solver with Pegasus method.
     */
    @Test
    public void testPegasus() {
        final double absAccuracy = 1e-8;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(absAccuracy, PEGASUS);
        try {
            double root = solver.solve(maxEval, TEST_FUNCTION, -5.0, 5.0);
            Assert.assertEquals("Root found is not correct.", TEST_FUNCTION.root(), root, absAccuracy);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations was reached.");
        } catch (TooManyIterationsException e) {
            Assert.fail("Too many iterations were performed.");
        }
    }

    /**
     * Test solver with relative accuracy and function value accuracy.
     */
    @Test
    public void testRelativeAndFunctionValueAccuracy() {
        final double relAccuracy = 1e-4;
        final double absAccuracy = 1e-8;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, REGULA_FALSI);
        try {
            double root = solver.solve(maxEval, TEST_FUNCTION, -5.0, 5.0);
            Assert.assertEquals("Root found is not correct.", TEST_FUNCTION.root(), root, absAccuracy);
            Assert.assertTrue("Relative error is not within the tolerance.",
                    Precision.abs(TEST_FUNCTION.value(root) / TEST_FUNCTION.value(0.0) - 1.0) < relAccuracy);
        } catch (MaxEvalException e) {
            Assert.fail("Maximum number of evaluations was reached.");
        } catch (TooManyIterationsException e) {
            Assert.fail("Too many iterations were performed.");
        }
    }
}