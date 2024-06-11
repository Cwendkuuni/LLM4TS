package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMinimization() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, -1, 1, 1);
        GoalType goalType = GoalType.MINIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchOptimizer(), 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        PointValuePair result = optimizer.optimize(f, goalType, bounds);
        double x = result.getPoint();
        double fx = result.getValue();
        assertEquals(0.61803398875, x, 1e-5);
        assertEquals(-1.64872127132, fx, 1e-5);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{0.61803398875, 0.61803398875, 0.61803398875}, optima, 1e-5);
        assertArrayEquals(new double[]{-1.64872127132, -1.64872127132, -1.64872127132}, optimaValues, 1e-5);
    }

    @Test
    public void testMaximization() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1);
        GoalType goalType = GoalType.MAXIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new GoldenSectionSearchOptimizer(), 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        PointValuePair result = optimizer.optimize(f, goalType, bounds);
        double x = result.getPoint();
        double fx = result.getValue();
        assertEquals(9.381966011255, x, 1e-5);
        assertEquals(8.485281397616, fx, 1e-5);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{9.381966011255, 9.381966011255, 9.381966011255}, optima, 1e-5);
        assertArrayEquals(new double[]{8.485281397616, 8.485281397616, 8.485281397616}, optimaValues, 1e-5);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, -1);
        GoalType goalType = GoalType.MINIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new RegulaFalsiOptimizer(), 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        optimizer.optimize(f, goalType, bounds);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1) {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("TestFunctionEvaluationException");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        optimizer.optimize(f, goalType, bounds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1);
        GoalType goalType = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                null, 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        optimizer.optimize(f, goalType, new SimpleBounds(0.0, 10.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1);
        GoalType goalType = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), -3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        optimizer.optimize(f, goalType, new SimpleBounds(0.0, 10.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException3() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1);
        GoalType goalType = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, null);
        optimizer.optimize(f, goalType, new SimpleBounds(0.0, 10.0));
    }

    @Test
    public void testNoOptimaFound() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(1, 1, 1, 1);
        GoalType goalType = GoalType.MINIMIZE;
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new RegulaFalsiOptimizer(), 3, new RandomGenerator() {
                    @Override
                    public double nextDouble() {
                        return Math.random();
                    }
                });
        PointValuePair result = optimizer.optimize(f, goalType, bounds);
        double x = result.getPoint();
        double fx = result.getValue();
        assertFalse(Double.isFinite(x));
        assertFalse(Double.isFinite(fx));
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertTrue(Arrays.stream(optima).allMatch(d -> Double.isNaN(d)));
        assertTrue(Arrays.stream(optimaValues).allMatch(d -> Double.isNaN(d)));
    }
}