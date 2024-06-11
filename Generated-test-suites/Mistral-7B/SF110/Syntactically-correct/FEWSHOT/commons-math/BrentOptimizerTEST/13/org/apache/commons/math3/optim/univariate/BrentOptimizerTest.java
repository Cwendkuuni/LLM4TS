package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        final double relTol = 0.01;
        final double absTol = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, null);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRelativeTolerance() {
        new BrentOptimizer(0.0, 0.001, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteTolerance() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test
    public void testOptimizeWithMinimizationGoal() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0);
        double x = optimizer.optimize(function, 0.5, 1.5).getPoint();
        assertEquals(0.0, x, 0.001);
    }

    @Test
    public void testOptimizeWithMaximizationGoal() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0).inverse();
        double x = optimizer.optimize(function, 0.5, 1.5).getPoint();
        assertEquals(1.5, x, 0.001);
    }

    @Test
    public void testOptimizeWithConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MaxCountConvergenceChecker(100));
        final UnivariateFunction function = new QuadraticFunction(1.0);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 1.5);
        assertTrue(result != null);
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeWithMaxCountConvergenceCheckerAndNoSolution() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MaxCountConvergenceChecker(1));
        final UnivariateFunction function = new SinFunction();
        assertNull(optimizer.optimize(function, -10.0, 10.0));
    }

    @Test
    public void testOptimizeWithGoalTypeSetToNull() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0);
        assertNull(optimizer.optimize(function, 0.5, 1.5));
    }

    @Test
    public void testOptimizeWithMinAndMaxValuesEqual() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new ConstFunction(0.0);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 0.0);
        assertEquals(0.0, result.getPoint(), 0.0);
    }

    @Test
    public void testOptimizeWithInflectionPoint() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new CubicFunction();
        assertNull(optimizer.optimize(function, -1.0, 1.0));
    }

    @Test
    public void testOptimizeWithBracketing() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new BracketingFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(-3.0, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeWithBracketingAndConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MaxCountConvergenceChecker(100));
        final UnivariateFunction function = new BracketingFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(-3.0, result.getPoint(), 0.001);
        assertFalse(result.isBracketed());
    }

    @Test
    public void testOptimizeWithCustomBestMethod() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0);
        double x1 = optimizer.optimize(function, 0.5, 1.5).getPoint();
        double x2 = optimizer.optimize(function, 1.5, 2.5).getPoint();
        double x3 = optimizer.optimize(function, 2.5, 3.5).getPoint();
        UnivariatePointValuePair best = optimizer.best(optimizer.best(null, new UnivariatePointValuePair(x1, 0.0), true), optimizer.best(null, new UnivariatePointValuePair(x2, 0.0), true), true);
        assertEquals(x1, best.getPoint(), 0.001);
    }

    @Test
    public void testComputeObjectiveValue() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        double x = 0.0;
        double y = optimizer.computeObjectiveValue(x);
        assertEquals(0.0, y, 0.001);
    }

    @Test
    public void testIsMinim() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertTrue(optimizer.isMinim());
    }

    @Test
    public void testDoOptimizeWithMinimum() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(-1.0, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMaximum() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new QuadraticFunction(1.0).inverse();
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(1.0, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMaxCountConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MaxCountConvergenceChecker(100));
        final UnivariateFunction function = new QuadraticFunction(1.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithBracketingFunction() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        final UnivariateFunction function = new BracketingFunction();
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(-3.0, result.getPoint(), 0.001);
    }

    @Test
    public void testDoOptimizeWithBracketingFunctionAndConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MaxCountConvergenceChecker(100));
        final UnivariateFunction function = new BracketingFunction();
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(-3.0, result.getPoint(), 0.001);
        assertFalse(result.isBracketed());
    }

    @Test
    public void testBest() {
        final UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        final UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        final UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);
        UnivariatePointValuePair best = BrentOptimizer.this.best(null, a, true);
        assertEquals(a, best);
        best = BrentOptimizer.this.best(best, b, true);
        assertEquals(b, best);
        best = BrentOptimizer.this.best(best, c, true);
        assertEquals(c, best);
    }

    @Test
    public void testGetGoalType() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertTrue(optimizer.getGoalType() == GoalType.MINIMIZE);
    }

    @Test
    public void testGetMin() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNull(optimizer.getMin());
    }

    @Test
    public void testGetMax() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNull(optimizer.getMax());
    }

    @Test
    public void testGetConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        OptimizationConvergenceChecker convergenceChecker = new MaxCountConvergenceChecker(100);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    private static class BracketingFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < -3.0) {
                return Math.pow(x + 3.0, 2.0) - 1.0;
            }
            else if (x > -3.0 && x < 3.0) {
                return Math.pow(x - 3.0, 2.0) + 1.0;
            }
            else {
                return Math.pow(x - 3.0, 2.0) - 1.0;
            }
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }
    }

    private static class CubicFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3.0) - 1.0;
        }

        @Override
        public double derivative(double x) {
            return 3.0 * Math.pow(x, 2.0);
        }
    }

    private static class QuadraticFunction implements UnivariateFunction {
        private final double a;

        public QuadraticFunction(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return a * Math.pow(x, 2.0);
        }

        @Override
        public double derivative(double x) {
            return 2.0 * a * x;
        }
    }

    private static class ConstFunction implements UnivariateFunction {
        private final double value;

        public ConstFunction(double value) {
            this.value = value;
        }

        @Override
        public double value(double x) {
            return value;
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }
    }

    private static class SinFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    private static class MaxCountConvergenceChecker implements OptimizationConvergenceChecker {
        private final int maxIterations;

        public MaxCountConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return iteration >= maxIterations;
        }
    }
}