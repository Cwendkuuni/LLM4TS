package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        GoalType goalType = GoalType.MINIMIZE;
        double relTol = 1.0e-6;
        double absTol = 1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, null);
        BrentOptimizer optimizer2 = new BrentOptimizer(relTol, absTol);
        assertNotNull(optimizer);
        assertNotNull(optimizer2);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(relTol, optimizer2.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer2.getAbsoluteThreshold(), 0.0);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(goalType, optimizer2.getGoalType());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithInvalidRelativeTolerance() {
        double relTol = 0.0;
        double absTol = 1.0e-6;
        new BrentOptimizer(relTol, absTol, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsoluteTolerance() {
        double relTol = 1.0e-6;
        double absTol = 0.0;
        new BrentOptimizer(relTol, absTol, null);
    }

    @Test
    public void testDoOptimizeWithSimpleFunction() {
        double min = -3.0;
        double max = 3.0;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.doOptimize(() -> Math.pow(Math.sin(x), 2), min, max);
        assertEquals(-1.0, result.getX(), 1.0e-6);
        assertEquals(-1.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testDoOptimizeWithMonotonicFunction() {
        double min = 0.0;
        double max = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.doOptimize(x -> x, min, max);
        assertEquals(min, result.getX(), 1.0e-6);
        assertEquals(min, result.getValue(), 1.0e-6);
    }

    @Test
    public void testDoOptimizeWithNonMonotonicFunction() {
        double min = 0.0;
        double max = 1.0;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.doOptimize(x -> Math.pow(Math.sin(x), 3), min, max);
        assertEquals(0.5, result.getX(), 1.0e-6);
        assertEquals(-Math.pow(Math.sin(0.5), 3), result.getValue(), 1.0e-6);
    }

    @Test
    public void testDoOptimizeWithFunctionThatTransversesBoundaries() {
        double min = -5.0;
        double max = 5.0;
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.doOptimize(x -> Math.pow(x, 2) - Math.pow(Math.sin(x), 2), min, max);
        assertEquals(-2.4495, result.getX(), 1.0e-6);
        assertEquals(-4.6868, result.getValue(), 1.0e-6);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);
        UnivariatePointValuePair d = new UnivariatePointValuePair(4.0, 16.0);

        GoalType goalType = GoalType.MINIMIZE;
        UnivariatePointValuePair best = optimizer.best(a, b, goalType);
        assertEquals(a, best);
        best = optimizer.best(b, c, goalType);
        assertEquals(b, best);
        best = optimizer.best(c, d, goalType);
        assertEquals(c, best);

        goalType = GoalType.MAXIMIZE;
        best = optimizer.best(a, b, goalType);
        assertEquals(d, best);
        best = optimizer.best(b, c, goalType);
        assertEquals(c, best);
        best = optimizer.best(c, a, goalType);
        assertEquals(d, best);
    }
}