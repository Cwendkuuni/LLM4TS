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
        UnivariateFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 3.0);
        assertEquals(-1.0, result.getPoint(), 1.0e-6);
        assertEquals(-2.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerWithMaximizationGoal() {
        UnivariateFunction function = new QuadraticFunction(1.0, 2.0, 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 3.0);
        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(2.0, result.getValue(), 1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativelySmallRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-9, 1.0e-6);
        throw new AssertionError("Test should have thrown NumberIsTooSmallException");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNonPositiveAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0);
        throw new AssertionError("Test should have thrown NotStrictlyPositiveException");
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        UnivariateFunction function = new QuadraticFunction(-1.0, 2.0, 1.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(function, 1.0, 3.0);
        assertEquals(-1.0, result.getPoint(), 1.0e-6);
        assertEquals(-2.0, result.getValue(), 1.0e-6);
    }

    interface UnivariateFunction {
        double value(double x);
    }

    static class QuadraticFunction implements UnivariateFunction {
        double a;
        double b;
        double c;

        public QuadraticFunction(double coefficientA, double coefficientB, double constantTerm) {
            a = coefficientA;
            b = coefficientB;
            c = constantTerm;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }

    static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return current.getValue() == previous.getValue();
        }
    }
}