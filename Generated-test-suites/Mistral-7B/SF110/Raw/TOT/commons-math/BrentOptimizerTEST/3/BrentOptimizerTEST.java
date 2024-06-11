package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithMinimizationGoal() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2) + x - 1;
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertEquals(-1.0, result.getPoint(), 1.0e-5);
        assertEquals(-2.166666666666667, result.getValue(), 1.0e-5);
    }

    @Test
    public void testBrentOptimizerWithMaximizationGoal() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * Math.pow(x, 2);
            }
        };
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        UnivariateOptimizationProblem problem = new UnivariateOptimizationProblem(f, -5.0, 5.0);
        problem.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertEquals(2.0, result.getPoint(), 1.0e-5);
        assertEquals(11.0, result.getValue(), 1.0e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNegativeRelativeThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(-1.0e-5, 1.0e-5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, -1.0e-5, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullConvergenceChecker() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5, null);
        optimizer.setConvergenceChecker(null);
    }
}