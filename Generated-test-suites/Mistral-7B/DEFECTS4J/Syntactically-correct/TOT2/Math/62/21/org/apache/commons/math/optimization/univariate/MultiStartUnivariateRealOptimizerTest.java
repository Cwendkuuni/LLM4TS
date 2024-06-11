package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization with {@link GoalType#MINIMIZE} and a simple quadratic function.
     */
    @Test
    public void testMinimizeQuadraticFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getPoint(), 1e-12);
        Assert.assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with {@link GoalType#MAXIMIZE} and a simple quadratic function.
     */
    @Test
    public void testMaximizeQuadraticFunction() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(10.0, result.getPoint(), 1e-12);
        Assert.assertEquals(100.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with {@link GoalType#MINIMIZE} and a function with multiple local minima.
     */
    @Test
    public void testMinimizeFunctionWithMultipleLocalMinima() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2) + Math.pow(Math.sin(x), 2) - 1.0;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.0, result.getPoint(), 1e-12);
        Assert.assertEquals(0.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with {@link GoalType#MAXIMIZE} and a function with multiple local maxima.
     */
    @Test
    public void testMaximizeFunctionWithMultipleLocalMaxima() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2) + Math.pow(Math.sin(x), 2) - 1.0;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0, result.getPoint(), 1e-12);
        Assert.assertEquals(2.0, result.getValue(), 1e-12);
    }

    /**
     * Test optimization with a function throwing an exception during evaluation.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test exception");
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with a function throwing a convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ConvergenceException("Test convergence exception");
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FibonacciSearchOptimizer(f), 10, new RandomDataGenerator());
        optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test optimization with an optimizer that does not support {@code setMaxEvaluations}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedSetMaxEvaluations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {};
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new GoldenSectionSearch(f);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 10, new RandomDataGenerator());
        multiOptimizer.setMaxEvaluations(100);
    }
}