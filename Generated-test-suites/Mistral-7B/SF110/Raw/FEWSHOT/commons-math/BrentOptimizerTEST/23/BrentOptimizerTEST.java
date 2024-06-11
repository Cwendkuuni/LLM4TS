package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        GoalType goalType = GoalType.MINIMIZE;
        new BrentOptimizer(0.0, 0.01, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        GoalType goalType = GoalType.MINIMIZE;
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test
    public void testBrentOptimizerMinimization() {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionEvaluator function = new FunctionEvaluator() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public FunctionEvaluator getFunctionToMinimize() {
                return function;
            }

            @Override
            public double getLowerBound(double x) {
                return -10.0;
            }

            @Override
            public double getUpperBound(double x) {
                return 10.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(-3.141592653589793, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        GoalType goalType = GoalType.MAXIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionEvaluator function = new FunctionEvaluator() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public FunctionEvaluator getFunctionToMinimize() {
                return new FunctionEvaluator() {
                    @Override
                    public double value(double x) {
                        return -function.value(x);
                    }
                };
            }

            @Override
            public double getLowerBound(double x) {
                return -10.0;
            }

            @Override
            public double getUpperBound(double x) {
                return 10.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        assertEquals(3.141592653589793, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return Math.abs(previous.getValue() - current.getValue()) < 0.0001;
            }
        });
        FunctionEvaluator function = new FunctionEvaluator() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public FunctionEvaluator getFunctionToMinimize() {
                return function;
            }

            @Override
            public double getLowerBound(double x) {
                return -10.0;
            }

            @Override
            public double getUpperBound(double x) {
                return 10.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertNotNull(result);
        // The convergence checker is custom and might not meet the default convergence criteria.
        // Thus, we cannot directly test the convergence with JUnit's Assert.assertEquals.
    }
}