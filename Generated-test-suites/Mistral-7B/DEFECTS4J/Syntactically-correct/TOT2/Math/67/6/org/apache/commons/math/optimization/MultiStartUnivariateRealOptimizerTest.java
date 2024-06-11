package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testBasicFunctionality() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(Math.sqrt(5.0) / 2.0, result, 1e-10);
    }

    @Test
    public void testEdgeCases() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 3);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(f, goalType, min, max));
        assertEquals(Double.NaN, optimizer.getOptima()[0]);
        assertEquals(Double.NaN, optimizer.getOptimaValues()[0]);
    }

    @Test
    public void testExceptionHandling() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Function evaluation failed");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(f, goalType, min, max));
        assertArrayEquals(new double[]{}, optimizer.getOptima());
        assertArrayEquals(new double[]{}, optimizer.getOptimaValues());
    }

    @Test
    public void testFunctionWithMultipleLocalMinima() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.5, 2) + Math.pow(x - 3.5, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{-1.5, -1.5, -3.5, -3.5}, optima);
        assertArrayEquals(new double[]{0.00781182, 0.00781182, 0.0276236, 0.0276236}, optimaValues);
    }

    @Test
    public void testZeroFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(0.0, result, 1e-10);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{min, min, min, min, min}, optima);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0}, optimaValues);
    }

    @Test
    public void testMaximization() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 10.0;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(new SimpleBounds(min, max), new InitialGuess(min), MaxEval.ITERATIONS, goalType),
                5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(Math.pow(10.0, 2), result, 1e-10);
    }
}