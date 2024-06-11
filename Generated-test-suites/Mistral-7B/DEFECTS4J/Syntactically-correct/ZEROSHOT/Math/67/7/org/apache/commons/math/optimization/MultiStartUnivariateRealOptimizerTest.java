package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test optimization of a simple quadratic function.
     */
    @Test
    public void testSimpleQuadraticFunction() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, -1.0});
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10,  new CyclicRandomGenerator(1));

        try {
            double result = multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
            assertEquals(1.5, result, 0.001);
            assertArrayEquals(new double[] {1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5}, multiStartOptimizer.getOptima(), 0.001);
            assertArrayEquals(new double[] {1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5}, multiStartOptimizer.getOptimaValues(), 0.001);
        } catch (OptimizationException e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization of a simple quadratic function with multiple local minima.
     */
    @Test
    public void testMultipleLocalMinima() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 12.0, -1.0, 2.0});
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        try {
            double result = multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
            assertEquals(2.0, result, 0.001);
            assertArrayEquals(new double[] {2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.5}, multiStartOptimizer.getOptima(), 0.001);
            assertArrayEquals(new double[] {2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.5}, multiStartOptimizer.getOptimaValues(), 0.001);
        } catch (OptimizationException e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization of a function with a local maximum.
     */
    @Test
    public void testLocalMaximum() {
        UnivariateRealFunction function = new PolynomialFunction(new double[] {-1.0, -4.0, 1.0});
        GoalType goalType = GoalType.MAXIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        try {
            double result = multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
            assertEquals(1.0, result, 0.001);
            assertArrayEquals(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, multiStartOptimizer.getOptima(), 0.001);
            assertArrayEquals(new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, multiStartOptimizer.getOptimaValues(), 0.001);
        } catch (OptimizationException e) {
            fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test optimization of a discontinuous function.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testDiscontinuousFunction() {
        UnivariateRealFunction function = new DiscontinuousFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
    }

    /**
     * Test optimization of a non-differentiable function.
     */
    @Test(expected = MathRuntimeException.class)
    public void testNonDifferentiableFunction() {
        UnivariateRealFunction function = new WeierstrassFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
    }

    /**
     * Test optimization of a function with infinite solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testInfiniteSolution() {
        UnivariateRealFunction function = new FunctionWithInfiniteSolution();
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
    }

    /**
     * Test optimization of a function with a solution outside the bounds.
     */
    @Test(expected = OptimizationException.class)
    public void testOutOfBoundsSolution() {
        UnivariateRealFunction function = new FunctionWithOutOfBoundsSolution();
        GoalType goalType = GoalType.MINIMIZE;
        double[] bounds = {-5.0, 5.0};
        UnivariateRealOptimizer optimizer = new FibonacciSearchUnivariateOptimizer();
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 10, new CyclicRandomGenerator(1));

        multiStartOptimizer.optimize(function, goalType, bounds[0], bounds[1]);
    }
}

class FunctionWithInfiniteSolution implements UnivariateRealFunction {
    public double value(double x) {
        return 1.0 / Math.sqrt(Math.abs(x));
    }
}

class FunctionWithOutOfBoundsSolution implements UnivariateRealFunction {
    public double value(double x) {
        return Math.sqrt(Math.abs(x - 10.0));
    }
}

class DiscontinuousFunction implements UnivariateRealFunction {
    public double value(double x) {
        if (x > 0.5) {
            throw new FunctionEvaluationException("Function is not defined for x > 0.5");
        }
        return Math.pow(x, 2.0);
    }
}

class WeierstrassFunction implements UnivariateRealFunction {
    public double value(double x) {
        double sum = 0.0;
        for (int i = 0; i < 1000; i++) {
            double term = Math.sin(Math.pow(Math.PI, i) * x);
            sum += term * Math.pow(Math.sin(Math.PI * i), 10.0);
        }
        return sum;
    }
}