package org.apache.commons.math.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = new NelderMeadSimplex();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
    }

    @Test
    public void testBasic() {
        final UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0, 1.0);
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;

        final double result = multiStartOptimizer.optimize(function, goalType, min, max);
        assertEquals(result, 1.0, 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[] { 1.0, 1.0, 1.0, 1.0, 1.0 }, 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 }, 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 5 * optimizer.getMaxIterations());
        assertEquals(multiStartOptimizer.getEvaluations(), 5 * optimizer.getMaxIterations() + optimizer.getMaxEvaluations());
    }

    @Test
    public void testConvergenceException() {
        final UnivariateRealFunction function = new PolynomialFunction(0.0, 1.0, 1.0);
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;

        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        assertNotNull(multiStartOptimizer.getOptima());
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[0], 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[0], 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 0);
        assertEquals(multiStartOptimizer.getEvaluations(), 0);
    }

    @Test
    public void testFunctionEvaluationException() {
        final UnivariateRealFunction function = new PolynomialFunction(Double.NEGATIVE_INFINITY, 1.0, 1.0) {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("test");
            }
        };
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;

        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        assertNotNull(multiStartOptimizer.getOptima());
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[0], 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[0], 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 0);
        assertEquals(multiStartOptimizer.getEvaluations(), 0);
    }

    @Test
    public void testMaxEvalExceeded() {
        final UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0, 1.0);
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;
        final int maxEvaluations = 1;

        try {
            multiStartOptimizer.optimize(function, goalType, min, max, maxEvaluations);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        assertNotNull(multiStartOptimizer.getOptima());
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[0], 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[0], 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 0);
        assertEquals(multiStartOptimizer.getEvaluations(), 0);
    }

    @Test
    public void testIterationExceeded() {
        final UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0, 1.0);
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;
        final int maxIterations = 0;

        try {
            multiStartOptimizer.optimize(function, goalType, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        assertNotNull(multiStartOptimizer.getOptima());
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[0], 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[0], 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 0);
        assertEquals(multiStartOptimizer.getEvaluations(), 0);
    }

    @Test
    public void testMultipleStarts() {
        final UnivariateRealFunction function = new PolynomialFunction(-1.0, 2.0, 1.0);
        final double min = 0.0;
        final double max = 10.0;
        final GoalType goalType = GoalType.MINIMIZE;

        final double[] expectedOptima = new double[] { 1.0, 2.5, 3.0, 4.0, 5.0 };
        final double[] expectedOptimaValues = new double[] { 0.0, 1.25, 2.25, 3.75, 5.0 };

        final PointValuePair result = multiStartOptimizer.optimize(function, goalType, min, max, MaxEval.UNLIMITED);
        assertEquals(result.getValue(), 1.0, 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptima(), expectedOptima, 1e-6);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), expectedOptimaValues, 1e-6);
        assertEquals(multiStartOptimizer.getIterationCount(), 5 * optimizer.getMaxIterations());
        assertEquals(multiStartOptimizer.getEvaluations(), 5 * optimizer.getMaxIterations() + optimizer.getMaxEvaluations());
    }

}