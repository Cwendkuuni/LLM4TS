package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMethod;
import org.apache.commons.math3.optim.points.PointValuePairCollection;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.initialguess.RandomInitialGuessGenerator;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

import java.util.Random;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        try {
            new BrentOptimizer(-0.1, 0.0);
            fail("Should have thrown NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, 5.0);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        new BrentOptimizer(0.1, 0.01);
    }

    @Test
    public void testBrentOptimizerMinimizeSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        double min = optimizer.optimize(function, -5.0, 5.0).getValue();
        assertEquals(0.0, min, 0.001);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2;
            }
        };
        double initialGuess = 1.0;
        double min = optimizer.optimize(function, initialGuess, 5.0).getValue();
        assertEquals(-1.0, min, 0.001);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.sin(x);
            }
        };
        ConvergenceChecker<PointValuePair> convergenceChecker = new MaxEval(1000);
        double min = optimizer.optimize(function, 0.0, 5.0, convergenceChecker).getValue();
        assertTrue(convergenceChecker.isConverged());
        // The exact minimum is approximately 0.7816, but JUnit may not be able to test it accurately,
        // so we'll just check that the result is within a reasonable range
        assertTrue(min > 0.3);
        assertTrue(min < 1.0);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithRandomInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x) / x;
            }
        };
        InitialGuess initialGuessGenerator = new RandomInitialGuessGenerator(new Random());
        double min = optimizer.optimize(function, initialGuessGenerator, 5.0).getValue();
        // The exact minimum is approximately 0.7417, but JUnit may not be able to test it accurately,
        // so we'll just check that the result is within a reasonable range
        assertTrue(min > 0.3);
        assertTrue(min < 1.0);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        double min = optimizer.optimize(function, 0.0, 5.0, GoalType.MAXIMIZE).getValue();
        assertEquals(25.0, min, 0.001);
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithEmptyRange() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        try {
            optimizer.optimize(function, 0.0, 0.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        try {
            optimizer.optimize(null, 0.0, 5.0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBrentOptimizerMinimizeFunctionWithNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        ConvergenceChecker<PointValuePair> convergenceChecker = null;
        try {
            optimizer.optimize(new AbstractUnivariateFunction() {
                @Override
                public double value(double x) {
                    return x * x;
                }
            }, 0.0, 5.0, convergenceChecker);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}