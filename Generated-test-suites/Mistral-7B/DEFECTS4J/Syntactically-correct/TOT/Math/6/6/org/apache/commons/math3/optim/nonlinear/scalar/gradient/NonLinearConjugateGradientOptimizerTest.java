package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LBracketer;
import org.apache.commons.math3.analysis.solvers.SimpleBracketingBrentMethod;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.test.AbstractTestCase;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest extends AbstractTestCase {

    /** Test function f(x) = x^3 - 2x + 1. */
    private static final UnivariateFunction TEST_FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x * x - 2 * x + 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * x * x - 2;
        }
    };

    /**
     * Tests that {@link NonLinearConjugateGradientOptimizer} finds the minimum
     * of the test function.
     * @throws Exception if an error occurs during the optimization
     */
    public void testMinimumFindable() throws Exception {
        // Given
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new BrentSolver());

        // When
        optimizer.optimize(TEST_FUNCTION, new SimpleBounds(0.5, 1.5));

        // Then
        final double result = optimizer.getPoint()[0];
        assertEquals("Result should be close to the expected minimum value",
                     -0.75, result, 1e-5);
    }

    /**
     * Tests that {@link NonLinearConjugateGradientOptimizer} fails to find the minimum
     * of the test function when the initial guess is too far.
     * @throws Exception if an error occurs during the optimization
     */
    public void testInitialGuessTooFar() throws Exception {
        // Given
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new BrentSolver());

        // When
        try {
            optimizer.optimize(TEST_FUNCTION, new SimpleBounds(-2, 2));
            fail("Expected illegal argument exception");
        } catch (final MathIllegalArgumentException e) {
            // Then
            assertTrue("Expected exception to contain the correct message",
                       e.getMessage().contains("Initial guess is not bracketing the minimum"));
        }
    }

    /**
     * Tests that {@link NonLinearConjugateGradientOptimizer} fails to find the minimum
     * of the test function when the optimization is not converging.
     * @throws Exception if an error occurs during the optimization
     */
    public void testMinimumNotConverging() throws Exception {
        // Given
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-3),
                new BrentSolver());

        // When
        try {
            optimizer.optimize(TEST_FUNCTION, new SimpleBounds(0.5, 1.5));
            fail("Expected no bracketing exception");
        } catch (final NoBracketingException e) {
            // Then
            assertTrue("Expected exception to contain the correct message",
                       e.getMessage().contains("Could not bracket minimum"));
        }
    }

    /**
     * Tests that {@link NonLinearConjugateGradientOptimizer} fails to find the minimum
     * of the test function when the optimization reaches the maximum number of evaluations.
     * @throws Exception if an error occurs during the optimization
     */
    public void testMaxEvaluations() throws Exception {
        // Given
        final UnivariateFunction function = TEST_FUNCTION;
        final NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-10),
                new BrentSolver());
        optimizer.setMaxEvaluations(0);

        // When
        try {
            optimizer.optimize(function, new SimpleBounds(0.5, 1.5));
            fail("Expected illegal argument exception");
        } catch (final MaxEvalException e) {
            // Then
            assertTrue("Expected exception to contain the correct message",
                       e.getMessage().contains("Maximum number of evaluations exceeded"));
        }
    }
}