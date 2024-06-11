package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, -1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(-2.25, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMin() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(-1.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(2.25, result.getValue(), 0.01);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNonPositiveAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBrentOptimizerNoConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
    }

    @Test
    public void testBrentOptimizerCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyCustomConvergenceChecker());
        UnivariateFunction function = new PolynomialFunction(3, new double[]{-1.0, 1.0, 1.0});
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(-1.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.01);
    }
}

class MyCustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    @Override
    public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
        return current.getValue() == previous.getValue();
    }
}