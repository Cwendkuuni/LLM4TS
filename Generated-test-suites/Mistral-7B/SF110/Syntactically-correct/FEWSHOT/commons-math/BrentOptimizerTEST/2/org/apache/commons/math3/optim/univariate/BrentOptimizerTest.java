package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.1, -1.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithSmallRelativeThreshold() {
        new BrentOptimizer(0.001, 1.0, null);
    }

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, null);
        GoalType goalType = GoalType.MINIMIZE;
        FunctionF1 function = new FunctionF1();

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, goalType);

        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(-(Math.PI * Math.PI) / 4.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, null);
        GoalType goalType = GoalType.MAXIMIZE;
        FunctionF1 function = new FunctionF1();

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, goalType);

        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals((Math.PI * Math.PI) / 4.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerConvergence() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 1.0, null);
        GoalType goalType = GoalType.MINIMIZE;
        FunctionF1 function = new FunctionF1();
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < 0.00001;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, goalType, convergenceChecker);

        assertEquals(0.5, result.getPoint(), 0.0001);
        assertEquals(-(Math.PI * Math.PI) / 4.0, result.getValue(), 0.0001);
    }
}

class FunctionF1 implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(Math.PI - x, 2.0);
    }
}

interface ConvergenceChecker<T> {
    boolean converged(int iter, T previous, T current);
}