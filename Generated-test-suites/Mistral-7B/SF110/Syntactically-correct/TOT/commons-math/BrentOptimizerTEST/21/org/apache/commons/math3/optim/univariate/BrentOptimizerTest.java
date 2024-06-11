package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithGoalTypeAndConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-8, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return (iter > 1000) || (FastMath.abs(current.getValue() - previous.getValue()) < 1.0e-8);
            }
        });
        UnivariateOptimizable univariateOptimizable = new UnivariateOptimizable() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double getMin() {
                return -3.0;
            }

            @Override
            public double getMax() {
                return 3.0;
            }

            @Override
            public Function getFunction() {
                return f;
            }
        };
        UnivariatePointValuePair optimum = optimizer.optimize(univariateOptimizable);
        assertTrue(FastMath.abs(optimum.getValue()) < 1.0e-8);
        assertTrue(FastMath.abs(optimum.getX() - Math.PI) < 1.0e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeToleranceTooSmall() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-10, 1.0e-8, null);
        UnivariateOptimizable univariateOptimizable = new UnivariateOptimizable() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double getMin() {
                return -3.0;
            }

            @Override
            public double getMax() {
                return 3.0;
            }

            @Override
            public Function getFunction() {
                return f;
            }
        };
        optimizer.optimize(univariateOptimizable);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithAbsoluteToleranceZero() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 0.0, null);
        UnivariateOptimizable univariateOptimizable = new UnivariateOptimizable() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double getMin() {
                return -3.0;
            }

            @Override
            public double getMax() {
                return 3.0;
            }

            @Override
            public Function getFunction() {
                return f;
            }
        };
        optimizer.optimize(univariateOptimizable);
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-8, null);
        UnivariateOptimizable univariateOptimizable = new UnivariateOptimizable() {
            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }

            @Override
            public double getMin() {
                return -3.0;
            }

            @Override
            public double getMax() {
                return 3.0;
            }

            @Override
            public Function getFunction() {
                return f;
            }
        };
        UnivariatePointValuePair optimum = optimizer.optimize(univariateOptimizable);
        assertTrue(FastMath.abs(optimum.getValue()) < 1.0e-8);
        assertTrue(FastMath.abs(optimum.getX() - Math.PI) < 1.0e-8);
    }
}