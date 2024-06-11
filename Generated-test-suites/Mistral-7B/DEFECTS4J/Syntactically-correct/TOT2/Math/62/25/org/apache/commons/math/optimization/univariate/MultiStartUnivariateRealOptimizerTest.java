package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.Polynomials;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testMinimizeSimpleFunction() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final UnivariateRealPointValuePair result = mOptimizer.optimize(f, goal, min, max);
        assertEquals(min, result.getX(), 1e-8);
        assertEquals(f.value(min), result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testMaximizeSimpleFunction() {
        final UnivariateRealFunction f = new PolynomialFunction(2, 1.0);
        final GoalType goal = GoalType.MAXIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.NelderMeadSimplex() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final UnivariateRealPointValuePair result = mOptimizer.optimize(f, goal, min, max);
        assertEquals(max, result.getX(), 1e-8);
        assertEquals(f.value(max), result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer does not converge with any start point
     * when the supplied function is non-convex.
     */
    @Test
    public void testNonConvexFunction() {
        final UnivariateRealFunction f = new PolynomialFunction(-2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.BroydenFletcherGoldfarbShanno() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final UnivariateRealPointValuePair[] optima = mOptimizer.optimize(f, goal, min, max, new MaxEval(100));
        assertArrayEquals(new UnivariateRealPointValuePair[3], optima);
        for (final UnivariateRealPointValuePair op : optima) {
            assertNull(op);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer throws an exception when the supplied function is not a valid instance of UnivariateRealFunction.
     */
    @Test
    public void testInvalidFunctionType() {
        final GoalType goal = GoalType.MINIMIZE;
        final double min = 0.0;
        final double max = 1.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<Object> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final FunctionEvaluationException exception = assertThrows(FunctionEvaluationException.class, () -> mOptimizer.optimize(new Object() {}, goal, min, max));
        assertEquals("Supplied function is not a valid instance of UnivariateRealFunction.", exception.getMessage());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer throws an exception when the supplied goal type is not valid.
     */
    @Test
    public void testInvalidGoalType() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final int invalidGoal = 5;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final MathIllegalStateException exception = assertThrows(MathIllegalStateException.class, () -> mOptimizer.optimize(f, GoalType.values()[invalidGoal], min, max));
        assertEquals("Invalid goal type: " + invalidGoal, exception.getMessage());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer throws an exception when the supplied bounds are not valid.
     */
    @Test
    public void testInvalidBounds() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = Double.POSITIVE_INFINITY;
        final double max = Double.NEGATIVE_INFINITY;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final MathIllegalStateException exception = assertThrows(MathIllegalStateException.class, () -> mOptimizer.optimize(f, goal, min, max));
        assertEquals("Invalid bounds: lower bound is greater than upper bound.", exception.getMessage());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer throws an exception when the supplied maximum number of evaluations is negative.
     */
    @Test
    public void testNegativeMaxEvaluations() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;
        final int maxEvaluations = -1;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0));

        final MathIllegalStateException exception = assertThrows(MathIllegalStateException.class, () -> mOptimizer.optimize(f, goal, min, max, maxEvaluations));
        assertEquals("Maximum number of evaluations cannot be negative.", exception.getMessage());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer correctly returns the best solution found among multiple starts.
     */
    @Test
    public void testMultipleStarts() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, RandomData.nextGaussianGenerator(1.0));

        final UnivariateRealPointValuePair[] optima = mOptimizer.optimize(f, goal, min, max, new MaxEval(100));

        final UnivariateRealPointValuePair best = optima[0];
        final double bestValue = best.getValue();
        for (int i = 1; i < optima.length; ++i) {
            final UnivariateRealPointValuePair current = optima[i];
            assertTrue(bestValue <= current.getValue());
        }

        final double tolerance = 1e-8;
        assertEquals(best.getX(), Precision.round(best.getX(), tolerance), tolerance);
        assertEquals(bestValue, best.getValue(), tolerance);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * Verify that the optimizer correctly throws an exception when it does not converge within the maximum number of evaluations.
     */
    @Test
    public void testNoConvergence() {
        final UnivariateRealFunction f = new PolynomialFunction(2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.Powell() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, RandomData.nextGaussianGenerator(1.0));

        final ConvergenceException exception = assertThrows(ConvergenceException.class, () -> mOptimizer.optimize(f, goal, min, max, new MaxEval(5)));
        assertTrue(exception.getMessage().contains("no convergence with any start point"));
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with custom initial guesses
     */
    @Test
    public void testCustomInitialGuesses() {
        final UnivariateRealFunction f = new PolynomialFunction(-2, -1.0);
        final GoalType goal = GoalType.MINIMIZE;
        final double min = -10.0;
        final double max = 10.0;
        final double[] initialGuesses = {-5.0, 5.0};

        final BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new org.apache.commons.math3.optim.fmin.BroydenFletcherGoldfarbShanno() {};
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> mOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, RandomData.nextGaussianGenerator(1.0), InitialGuess.VALUES[0]);

        final UnivariateRealPointValuePair[] optima = mOptimizer.optimize(f, goal, min, max, new MaxEval(100));

        for (final UnivariateRealPointValuePair op : optima) {
            assertNotNull(op);
        }

        final UnivariateRealPointValuePair best = optima[0];
        final double bestValue = best.getValue();
        for (int i = 1; i < optima.length; ++i) {
            final UnivariateRealPointValuePair current = optima[i];
            assertTrue(bestValue <= current.getValue());
        }

        final double tolerance = 1e-8;
        assertEquals(best.getX(), Precision.round(best.getX(), tolerance), tolerance);
        assertEquals(bestValue, best.getValue(), tolerance);
    }
}