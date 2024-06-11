package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonUnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciMinimizer;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimization() {
        UnivariateRealFunction f = new PolynomialFunction(3, -1.0, 1.0);
        UnivariateRealOptimizer optimizer = new FibonacciMinimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomGenerator());

        // Test with valid inputs
        double[] results = new double[5];
        for (int i = 0; i < 5; i++) {
            results[i] = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0)[0];
        }
        PointValuePair best = multiOptimizer.getOptima();
        double bestValue = best.getValue();
        Assert.assertArrayEquals(new double[]{-1.61803398875, -1.61803398875, -1.61803398875, -1.61803398875, -1.61803398875}, results, 0.0001);
        Assert.assertEquals(-2.44948974278, bestValue, 0.0001);
        
        // Test with different initial points
        double[] results2 = new double[5];
        for (int i = 0; i < 5; i++) {
            multiOptimizer.reset();
            results2[i] = multiOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0)[0];
        }
        Assert.assertNotEquals(results, results2);
        
        // Test with invalid input (non-optimizable function)
        try {
            UnivariateRealFunction fNaN = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            multiOptimizer.optimize(fNaN, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test with invalid input (negative number of starts)
        MultiStartUnivariateRealOptimizer negativeStartsOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, -1, new RandomGenerator());
        try {
            negativeStartsOptimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMultiStartMaximization() {
        UnivariateRealFunction f = new PolynomialFunction(3, 1.0, 10.0);
        UnivariateRealOptimizer optimizer = new FibonacciMaximizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomGenerator());

        // Test with valid inputs
        double[] results = new double[5];
        for (int i = 0; i < 5; i++) {
            results[i] = multiOptimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0)[0];
        }
        PointValuePair best = multiOptimizer.getOptima();
        double bestValue = best.getValue();
        Assert.assertArrayEquals(new double[]{9.61803398875, 9.61803398875, 9.61803398875, 9.61803398875, 9.61803398875}, results, 0.0001);
        Assert.assertEquals(7.55051123722, bestValue, 0.0001);
        
        // Test with different initial points
        double[] results2 = new double[5];
        for (int i = 0; i < 5; i++) {
            multiOptimizer.reset();
            results2[i] = multiOptimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0)[0];
        }
        Assert.assertNotEquals(results, results2);
        
        // Test with invalid input (non-optimizable function)
        try {
            UnivariateRealFunction fNaN = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            multiOptimizer.optimize(fNaN, GoalType.MAXIMIZE, -10.0, 10.0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test with invalid input (negative number of starts)
        MultiStartUnivariateRealOptimizer negativeStartsOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, -1, new RandomGenerator());
        try {
            negativeStartsOptimizer.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMultiStartWithCustomBounds() {
        UnivariateRealFunction f = new PolynomialFunction(3, 0.5, 2.5);
        UnivariateRealOptimizer optimizer = new FibonacciMinimizer();
        MultiStartUnivariateRealOptimizer multiOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, new RandomGenerator());

        // Test with valid inputs
        SimpleBounds bounds = new SimpleBounds(0.75, 1.5);
        double[] results = new double[5];
        for (int i = 0; i < 5; i++) {
            results[i] = multiOptimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound())[0];
        }
        PointValuePair best = multiOptimizer.getOptima();
        double bestValue = best.getValue();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, results, 0.0001);
        Assert.assertEquals(0.25, bestValue, 0.0001);
        
        // Test with different initial bounds
        SimpleBounds bounds2 = new SimpleBounds(0.25, 1.25);
        multiOptimizer.reset();
        double[] results2 = new double[5];
        for (int i = 0; i < 5; i++) {
            results2[i] = multiOptimizer.optimize(f, GoalType.MINIMIZE, bounds2.getLowerBound(), bounds2.getUpperBound())[0];
        }
        Assert.assertNotEquals(results, results2);
        
        // Test with invalid input (non-optimizable function)
        try {
            UnivariateRealFunction fNaN = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            multiOptimizer.optimize(fNaN, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test with invalid input (negative number of starts)
        MultiStartUnivariateRealOptimizer negativeStartsOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, -1, new RandomGenerator());
        try {
            negativeStartsOptimizer.optimize(f, GoalType.MINIMIZE, bounds.getLowerBound(), bounds.getUpperBound());
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}