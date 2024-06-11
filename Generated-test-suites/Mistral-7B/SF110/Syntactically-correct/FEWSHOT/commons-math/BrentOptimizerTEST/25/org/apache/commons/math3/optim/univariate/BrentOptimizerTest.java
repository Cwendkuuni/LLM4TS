package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.cli.BrentOptimizer;
import org.apache.commons.cli.GoalType;
import org.apache.commons.cli.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeThreshold() {
        new BrentOptimizer(0.0001, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1.0, -1.0);
    }

    @Test
    public void testBrentOptimizerWithMinimizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -3.0, 3.0);
        assertEquals(0.0, result.getValue(), 1.0E-6);
        assertEquals(-1.0, result.getPoint(), 1.0E-6);
    }

    @Test
    public void testBrentOptimizerWithMaximizeGoal() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, -3.0, 3.0);
        assertEquals(9.0, result.getValue(), 1.0E-6);
        assertEquals(3.0, result.getPoint(), 1.0E-6);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue() == previous.getValue();
            }
        });
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(0.0, result.getValue(), 1.0E-6);
        assertEquals(0.0, result.getPoint(), 1.0E-6);
    }
}