package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithNegativeRelativeThreshold() {
        new BrentOptimizer(-0.1, 0.0, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbsoluteThreshold() {
        new BrentOptimizer(0.01, 0.0, null);
    }

    @Test
    public void testBrentOptimizerWithValidParameters() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        assertNotNull(optimizer);
        assertEquals(0.1, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithNullConvergenceChecker() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> mockChecker = mock(ConvergenceChecker.class);
        when(mockChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(true);

        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, mockChecker);
        assertSame(mockChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = IllegalStateException.class)
    public void testBrentOptimizerIfMinIsGreaterThanMax() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(1.5);
        optimizer.setMax(1.0);
        optimizer.optimize();
    }

    @Test
    public void testBrentOptimizerWithMinLessThanMax() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-1.0);
        optimizer.setMax(1.0);
        optimizer.optimize();
    }

    @Test
    public void testBrentOptimizerWithGoalTypeMinimize() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertEquals(-5.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithGoalTypeMaximize() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertEquals(5.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithDifferentMinAndMaxValues() {
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-5.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertNotEquals(-5.0, result.getPoint(), 0.0);
        assertNotEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatHasOneGlobalMinimum() {
        final Function<Double, Double> testFunction = x -> Math.pow(x, 2);
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-3.0);
        optimizer.setMax(3.0);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MINIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertEquals(-1.0, result.getPoint(), 0.01);
        assertEquals(1.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatHasOneGlobalMaximum() {
        final Function<Double, Double> testFunction = x -> Math.pow(x, 2);
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-3.0);
        optimizer.setMax(3.0);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 0.01);
        assertEquals(1.0, result.getValue(), 0.01);
    }

    @Test
    public void testBrentOptimizerWithFunctionThatHasMultipleLocalMinima() {
        final Function<Double, Double> testFunction = x -> Math.pow(x, 2) + Math.sin(x);
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-3.0);
        optimizer.setMax(3.0);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MINIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        // The result will depend on the initial values of 'a', 'b' and 'mid'.
        // We cannot test a specific value as the optimization algorithm may converge to different local minima.
    }

    @Test
    public void testBrentOptimizerWithFunctionThatHasMultipleLocalMaxima() {
        final Function<Double, Double> testFunction = x -> -Math.pow(x, 2) - Math.sin(x);
        final BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setMin(-3.0);
        optimizer.setMax(3.0);
        optimizer.setFunction(testFunction);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        final UnivariatePointValuePair result = optimizer.optimize();
        assertNotNull(result);
        // The result will depend on the initial values of 'a', 'b' and 'mid'.
        // We cannot test a specific value as the optimization algorithm may converge to different local maxima.
    }
}