package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBoundingMethodOptimizer;
import org.apache.commons.math3.optim.nonlinear.NelderMeadSimplexOptimizer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new NelderMeadSimplexOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new SimpleBoundingMethodOptimizer());
    }

    @After
    public void tearDown() {
        optimizer = null;
        multiStartOptimizer = null;
    }

    @Test
    public void testMultiStartMinimization() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, -1.0}, true);
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double expected = -2.0;
        double result = multiStartOptimizer.optimize(function, goalType, min, max);
        assertEquals(expected, result, 1e-12);
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, 1e-12);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[]{-2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0}, 1e-12);
    }

    @Test
    public void testMultiStartMaximization() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{1.0, 1.0, 1.0}, true);
        GoalType goalType = GoalType.MAXIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double expected = 2.0;
        double result = multiStartOptimizer.optimize(function, goalType, min, max);
        assertEquals(expected, result, 1e-12);
        assertArrayEquals(multiStartOptimizer.getOptima(), new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, 1e-12);
        assertArrayEquals(multiStartOptimizer.getOptimaValues(), new double[]{2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0}, 1e-12);
    }

    @Test
    public void testErrorCases() {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, -1.0}, true);
        GoalType goalType = GoalType.MINIMIZE;
        try {
            multiStartOptimizer.optimize(function, goalType, Double.NaN, Double.POSITIVE_INFINITY);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(function, goalType, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(function, goalType, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(function, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }

        try {
            multiStartOptimizer.optimize(null, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSingleStart() throws Exception {
        UnivariateRealFunction function = new PolynomialFunction(3, new double[]{1.0, 1.0, 1.0}, true);
        GoalType goalType = GoalType.MAXIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double expected = 2.0;
        UnivariateRealOptimizer optimizer = new NelderMeadSimplexOptimizer();
        double result = optimizer.optimize(function, goalType, min, max);
        assertEquals(expected, result, 1e-12);
    }
}