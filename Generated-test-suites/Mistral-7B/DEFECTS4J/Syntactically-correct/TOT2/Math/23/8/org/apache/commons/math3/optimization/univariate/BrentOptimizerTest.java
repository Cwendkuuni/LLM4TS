package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePair;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        ObjectiveFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 0.0);
        PointValuePair result = optimizer.optimize(func, -5.0, 5.0);
        assertEquals(Math.PI, result.getPoint(), 1.0E-6);
        assertEquals(0.0, result.getValue(), 1.0E-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInitialGuess() {
        ObjectiveFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 0.0, new ValueInitializer(-3.0));
        PointValuePair result = optimizer.optimize(func, -5.0, 5.0);
        assertEquals(-3.0, result.getPoint(), 1.0E-6);
        assertEquals(9.0 * Math.PI_2 * Math.PI_2, result.getValue(), 1.0E-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0E-6, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(0.0, 0.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1.0E-6, 0.0, null);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        ObjectiveFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -Math.exp(x);
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 0.0, null);
        PointValuePair result = optimizer.optimize(func, -5.0, 5.0, goalType);
        assertEquals(E.EULER_NUM.doubleValue(), result.getPoint(), 1.0E-6);
        assertEquals(-E.E, result.getValue(), 1.0E-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        ObjectiveFunction func = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return previous != null && previous.getValue() - current.getValue() < 1.0E-8;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-8, 0.0, convergenceChecker);
        PointValuePair result = optimizer.optimize(func, -5.0, 5.0);
        assertEquals(Math.PI, result.getPoint(), 1.0E-8);
        assertEquals(0.0, result.getValue(), 1.0E-8);
    }
}