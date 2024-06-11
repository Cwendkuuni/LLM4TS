package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private static final double TOLERANCE = 1e-6;
    private static final double EPSILON = 1e-9;

    @Test
    public void testBrentOptimizerWithMinimizationGoal() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.sin(Math.PI * x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(TOLERANCE, 0.0);
        UnivariateOptimizerResult result = optimizer.optimize(f, -1.0, 1.0);
        assertEquals(1.224744971575546, result.getPoint().getValue(), TOLERANCE);
    }

    @Test
    public void testBrentOptimizerWithMaximizationGoal() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-Math.pow(x, 2)) * Math.cos(Math.PI * x / 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(TOLERANCE, 0.0);
        UnivariateOptimizerResult result = optimizer.optimize(f.negate(), -1.0, 1.0);
        assertEquals(1.157059510330531, result.getPoint().getValue(), TOLERANCE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001);
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(TOLERANCE, 0.0);
        UnivariateOptimizerResult result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(0.0, result.getPoint().getValue(), TOLERANCE);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.sin(Math.PI * x);
            }
        };
        ConvergenceChecker<UnivariatePointValuePair> customChecker = (iter, last, current) -> Math.abs(last.getValue() - current.getValue()) < EPSILON;
        BrentOptimizer optimizer = new BrentOptimizer(TOLERANCE, 0.0, customChecker);
        UnivariateOptimizerResult result = optimizer.optimize(f, -1.0, 1.0);
        assertEquals(1.224744971575546, result.getPoint().getValue(), TOLERANCE);
    }
}