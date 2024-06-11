package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with a simple function, f(x) = x^2
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6);
        PointValuePair result = optimizer.optimize(function, 2.0, null);
        Assert.assertEquals(0.0, result.getPoint(), 1.0E-6);

        // Test with a function that has a minimum at a negative value
        ObjectiveFunction functionNegative = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return -Math.exp(x) + E.E;
            }
        };

        BrentOptimizer optimizerNegative = new BrentOptimizer(1.0E-6, 1.0E-6);
        PointValuePair resultNegative = optimizerNegative.optimize(functionNegative, 0.0, null);
        Assert.assertEquals(-E.E, resultNegative.getPoint(), 1.0E-6);

        // Test with a function that has a minimum at a large positive value
        ObjectiveFunction functionLarge = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.log(x);
            }
        };

        BrentOptimizer optimizerLarge = new BrentOptimizer(1.0E-6, 1.0E12);
        PointValuePair resultLarge = optimizerLarge.optimize(functionLarge, 0.0, null);
        Assert.assertTrue(resultLarge.getPoint() > 0.0);

        // Test with a function that is not monotonic
        ObjectiveFunction functionNotMonotonic = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };

        BrentOptimizer optimizerNotMonotonic = new BrentOptimizer(1.0E-6, 1.0E-6);
        try {
            optimizerNotMonotonic.optimize(functionNotMonotonic, 0.0, null);
            Assert.fail("Expected exception NotStrictlyMonotonicFunctionException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a function that is not defined for negative values
        ObjectiveFunction functionNegativeDefined = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new IllegalArgumentException("Function is not defined for negative arguments");
                }
                return x * x;
            }
        };

        BrentOptimizer optimizerNegativeDefined = new BrentOptimizer(1.0E-6, 1.0E-6);
        try {
            optimizerNegativeDefined.optimize(functionNegativeDefined, -1.0, null);
            Assert.fail("Expected exception IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with a function that is not differentiable at the minimum
        ObjectiveFunction functionNotDifferentiable = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) * Math.log(Math.abs(x - 1.0));
            }
        };

        BrentOptimizer optimizerNotDifferentiable = new BrentOptimizer(1.0E-6, 1.0E-6);
        PointValuePair resultNotDifferentiable = optimizerNotDifferentiable.optimize(functionNotDifferentiable, 1.0, null);
        Assert.assertEquals(1.0, resultNotDifferentiable.getPoint(), 1.0E-6);

        // Test with a function that has a minimum at a very large value
        ObjectiveFunction functionLargeMin = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.log(x) + 1000000.0;
            }
        };

        BrentOptimizer optimizerLargeMin = new BrentOptimizer(1.0E-6, 1.0E12);
        PointValuePair resultLargeMin = optimizerLargeMin.optimize(functionLargeMin, 0.0, null);
        Assert.assertTrue(resultLargeMin.getPoint() > 0.0);

        // Test with a function that has a very sharp minimum
        ObjectiveFunction functionSharp = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1.0), 2) * Math.sin(Math.PI / x);
            }
        };

        BrentOptimizer optimizerSharp = new BrentOptimizer(1.0E-12, 1.0E-12);
        PointValuePair resultSharp = optimizerSharp.optimize(functionSharp, 1.0, null);
        Assert.assertEquals(1.0, resultSharp.getPoint(), 1.0E-12);

        // Test with a function that has a minimum at a very small value
        ObjectiveFunction functionSmall = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1.0), 2) * Math.pow(1.0E-6, 2);
            }
        };

        BrentOptimizer optimizerSmall = new BrentOptimizer(1.0E-12, 1.0E-12);
        PointValuePair resultSmall = optimizerSmall.optimize(functionSmall, 1.0, null);
        Assert.assertTrue(resultSmall.getPoint() < 1.0);

        // Test with a function that has a maximum at a very small value
        ObjectiveFunction functionSmallMax = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x - 1.0), 2) * Math.pow(1.0E-6, 2);
            }
        };

        BrentOptimizer optimizerSmallMax = new BrentOptimizer(1.0E-12, 1.0E-12);
        PointValuePair resultSmallMax = optimizerSmallMax.optimize(functionSmallMax, 1.0, null);
        Assert.assertTrue(resultSmallMax.getPoint() > 1.0);

        // Test with a function that has a maximum at a very large value
        ObjectiveFunction functionLargeMax = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.log(x) - 1000000.0;
            }
        };

        BrentOptimizer optimizerLargeMax = new BrentOptimizer(1.0E-6, 1.0E12);
        PointValuePair resultLargeMax = optimizerLargeMax.optimize(functionLargeMax, 0.0, null);
        Assert.assertTrue(resultLargeMax.getPoint() < 0.0);

        // Test with a function that has a maximum at a negative value
        ObjectiveFunction functionNegativeMax = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return -Math.exp(x) + E.E;
            }
        };

        BrentOptimizer optimizerNegativeMax = new BrentOptimizer(1.0E-6, 1.0E-6);
        PointValuePair resultNegativeMax = optimizerNegativeMax.optimize(functionNegativeMax, 0.0, null);
        Assert.assertEquals(-E.E, resultNegativeMax.getPoint(), 1.0E-6);

        // Test with a custom initializer
        ValueInitializer initializer = new ValueInitializer() {
            @Override
            public double getInitialValue() {
                return 1.5;
            }
        };

        BrentOptimizer optimizerCustomInitializer = new BrentOptimizer(1.0E-6, 1.0E-6, initializer);
        PointValuePair resultCustomInitializer = optimizerCustomInitializer.optimize(function, 1.5, null);
        Assert.assertEquals(0.0, resultCustomInitializer.getPoint(), 1.0E-6);
    }
}