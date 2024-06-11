package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.OptimizationDirection;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleGoalTypeChecker;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.UnivariateFunctionUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        double relTol = 1.0e-6;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, goalType);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(goalType, optimizer.getGoalType());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelTolLowerThanMin() {
        double relTol = 1.0e-8;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        new BrentOptimizer(relTol, absTol, goalType);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsTolNotPositive() {
        double relTol = 1.0e-6;
        double absTol = -1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        new BrentOptimizer(relTol, absTol, goalType);
    }

    @Test
    public void testBrentOptimizerConstructionWithoutChecker() {
        double relTol = 1.0e-6;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(goalType, optimizer.getGoalType());
    }

    @Test
    public void testBrentOptimizerConstructionWithChecker() {
        double relTol = 1.0e-6;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        GoalTypeChecker checker = mock(SimpleGoalTypeChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, goalType, checker);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithFunction() {
        double relTol = 1.0e-6;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
        UnivariateFunction testFunction = mock(UnivariateFunction.class);
        when(testFunction.value(0.0)).thenReturn(1.0);
        when(testFunction.value(1.0)).thenReturn(0.0);
        when(testFunction.value(2.0)).thenReturn(1.0);
        when(testFunction.value(3.0)).thenReturn(1.0);

        UnivariatePointValuePair result = optimizer.optimize(testFunction, new UnivariatePointValuePair(2.0, 1.0), null);

        ArgumentCaptor<UnivariatePointValuePair> bestCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        ArgumentCaptor<UnivariatePointValuePair> previousCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        ArgumentCaptor<Boolean> isMinimCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(optimizer).best(bestCaptor.capture(), previousCaptor.capture(), isMinimCaptor.capture());

        assertEquals(new UnivariatePointValuePair(1.548882275247145, 0.0), result);
        assertEquals(new UnivariatePointValuePair(2.0, 1.0), bestCaptor.getValue());
        assertTrue(isMinimCaptor.getValue());
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithFunctionAndChecker() {
        double relTol = 1.0e-6;
        double absTol = 1.0e-5;
        GoalType goalType = GoalType.MINIMIZE;
        GoalTypeChecker checker = mock(SimpleGoalTypeChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, goalType, checker);
        UnivariateFunction testFunction = mock(UnivariateFunction.class);
        when(testFunction.value(0.0)).thenReturn(1.0);
        when(testFunction.value(1.0)).thenReturn(0.0);
        when(testFunction.value(2.0)).thenReturn(1.0);
        when(testFunction.value(3.0)).thenReturn(1.0);

        UnivariatePointValuePair result = optimizer.optimize(testFunction, new UnivariatePointValuePair(2.0, 1.0), null);

        ArgumentCaptor<UnivariatePointValuePair> bestCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        ArgumentCaptor<UnivariatePointValuePair> previousCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        ArgumentCaptor<Boolean> isMinimCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Integer> iterationCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<UnivariatePointValuePair> previousBestCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        ArgumentCaptor<UnivariatePointValuePair> currentBestCaptor = ArgumentCaptor.forClass(UnivariatePointValuePair.class);
        verify(checker).converged(iterationCaptor.capture(), previousCaptor.capture(), currentBestCaptor.capture());

        assertEquals(new UnivariatePointValuePair(1.548882275247145, 0.0), result);
        assertEquals(new UnivariatePointValuePair(2.0, 1.0), bestCaptor.getValue());
        assertTrue(isMinimCaptor.getValue());
        assertEquals(1, iterationCaptor.getValue());
    }

    @Test
    public void testGetMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-5, GoalType.MINIMIZE);
        assertEquals(-Double.MAX_VALUE, optimizer.getMin(), 0.0);
    }

    @Test
    public void testGetMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-5, GoalType.MINIMIZE);
        assertEquals(Double.MAX_VALUE, optimizer.getMax(), 0.0);
    }

    @Test
    public void testComputeObjectiveValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-5, GoalType.MINIMIZE);
        UnivariateFunction testFunction = mock(UnivariateFunction.class);
        when(testFunction.value(0.0)).thenReturn(Double.POSITIVE_INFINITY);
        when(testFunction.value(1.0)).thenReturn(0.0);
        when(testFunction.value(2.0)).thenReturn(1.0);
        when(testFunction.value(3.0)).thenReturn(Double.NEGATIVE_INFINITY);

        double x = 1.5;
        double objectiveValue = optimizer.computeObjectiveValue(x, testFunction);

        ArgumentCaptor<Double> xCaptor = ArgumentCaptor.forClass(Double.class);
        verify(testFunction).value(xCaptor.capture());
        assertEquals(-optimizer.isMinim() * testFunction.value(xCaptor.getValue()), objectiveValue, 0.0);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 3.0);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-5, GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);

        result = optimizer.best(null, b, true);
        assertEquals(b, result);

        result = optimizer.best(a, null, true);
        assertEquals(a, result);

        result = optimizer.best(a, b, false);
        assertEquals(b, result);

        result = optimizer.best(null, null, false);
        assertNull(result);
    }
}