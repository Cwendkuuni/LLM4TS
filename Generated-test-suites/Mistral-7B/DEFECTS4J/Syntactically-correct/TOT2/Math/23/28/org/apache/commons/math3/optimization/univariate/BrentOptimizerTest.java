package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionTest;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test the constructor
        double relTolerance = 1e-6;
        double absTolerance = 1e-6;
        UnivariateOptimizer optimizer = new BrentOptimizer(relTolerance, absTolerance);

        // Test the optimize method with a simple function
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0, 10), null);
        assertEquals(Math.sqrt(2), result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test the optimize method with a function with multiple roots
        Function f2 = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x);
            }
        };

        result = optimizer.optimize(f2, new SimpleBounds(-10, 10), null);
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(-2.414213771152621, result.getValue(), 1e-6);

        // Test the optimize method with a function with a maximum
        Function f3 = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * (x - 2) * (x - 1);
            }
        };

        result = optimizer.optimize(f3, new SimpleBounds(1, 3), null);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-1.648721269609065E-10, result.getValue(), 1e-10);

        // Test the optimize method with a function with a minimum
        Function f4 = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(x) * (x - 3) * (x - 2);
            }
        };

        result = optimizer.optimize(f4, new SimpleBounds(1, 5), null);
        assertEquals(3.0, result.getPoint(), 1e-6);
        assertEquals(1.082732702642689E-10, result.getValue(), 1e-10);

        // Test the optimize method with a non-monotonic function
        Function f5 = new Function() {
            @Override
            public double value(double x) {
                return x * x * (1 - x);
            }
        };

        result = optimizer.optimize(f5, new SimpleBounds(0, 1), null);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(0.25, result.getValue(), 1e-6);

        // Test the optimize method with a constrained function
        Function f6 = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1, 1, 2));

        result = optimizer.optimize(f6, new SimpleBounds(0, 10), constraints);
        assertEquals(1.5, result.getPoint(), 1e-6);
        assertEquals(1.5, result.getValue(), 1e-6);

        // Test the optimize method with a function having a singularity
        Function f7 = new Function() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new IllegalArgumentException("x cannot be zero");
                }
                return Math.log(1 / x);
            }
        };

        try {
            optimizer.optimize(f7, new SimpleBounds(-1, 1), null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the optimize method with a function having an infinite value
        Function f8 = new Function() {
            @Override
            public double value(double x) {
                return 1 / (x - 1);
            }
        };

        try {
            optimizer.optimize(f8, new SimpleBounds(0, 2), null);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test the optimize method with a goal type
        Function f9 = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };

        UnivariateOptimizer optimizerGoal = new BrentOptimizer(relTolerance, absTolerance, GoalType.MAXIMIZE);
        result = optimizerGoal.optimize(f9, new SimpleBounds(-10, 10), null);
        assertEquals(2.5, result.getPoint(), 1e-6);
        assertEquals(2, result.getValue(), 1e-6);

        // Test the optimize method with custom initial guess
        Function f10 = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariateOptimizer optimizerInitial = new BrentOptimizer(relTolerance, absTolerance, null, new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 5.0;
            }
        });
        result = optimizerInitial.optimize(f10, new SimpleBounds(0, 10), null);
        assertEquals(25.0, result.getPoint(), 1e-6);
        assertEquals(250.0, result.getValue(), 1e-6);
    }

    @FunctionalInterface
    private interface Function {
        double value(double x);
    }
}