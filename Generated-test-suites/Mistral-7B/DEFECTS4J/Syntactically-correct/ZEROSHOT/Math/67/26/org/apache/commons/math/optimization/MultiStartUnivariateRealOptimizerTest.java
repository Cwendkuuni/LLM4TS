package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealOptimizer underlyingOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        PolynomialFunction func = new PolynomialFunction(1, new double[]{-1.0, 1.0, -1.0, 1.0});
        underlyingOptimizer = new NelderMeadSimplexOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer(underlyingOptimizer, 10, new MersenneTwister());
    }

    @Test
    public void testOptimize() throws Exception {
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;

        double optimum = optimizer.optimize(func, goalType, min, max);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(1.0, optimum, 0.001);
        assertEquals(10, optima.length);
        for (double d : optima) {
            assertNotEquals(Double.NaN, d);
            assertTrue(min <= d && d <= max);
        }
        for (double d : optimaValues) {
            assertEquals(1.0, d, 0.001);
        }
    }

    @Test
    public void testOptimize_Maximize() throws Exception {
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MAXIMIZE;

        double optimum = optimizer.optimize(func, goalType, min, max);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();

        assertEquals(1.0, optimum, 0.001);
        assertEquals(10, optima.length);
        for (double d : optima) {
            assertNotEquals(Double.NaN, d);
            assertTrue(min <= d && d <= max);
        }
        for (double d : optimaValues) {
            assertEquals(-1.0, d, 0.001);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testNoConvergence() throws Exception {
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction func = new PolynomialFunction(1, new double[]{1.0, 1.0, -1.0, 1.0});

        optimizer.optimize(func, goalType, min, max);

        fail("expected OptimizationException not thrown");
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction func = new PolynomialFunction(1, new double[]{Double.POSITIVE_INFINITY});

        optimizer.optimize(func, goalType, min, max);

        fail("expected FunctionEvaluationException not thrown");
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        double min = 0.0;
        double max = 2.0;
        GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction func = new PolynomialFunction(1, new double[]{-1.0, 1.0, -1.0, 1.0});

        optimizer.optimize(func, goalType, min, max);

        fail("expected ConvergenceException not thrown");
    }
}