package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorValidParameters() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0, convergenceChecker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(1.0, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorInvalidRelativeThreshold() {
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(-0.01, 1.0));
    }

    @Test
    public void testConstructorInvalidAbsoluteThreshold() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(0.01, -1.0));
    }

    @Test
    public void testDoOptimize_Minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariateFunction function = mock(UnivariateFunction.class);
        double min = optimizer.optimize(function, -10.0, 10.0)[0];
        assertEquals(-1.23, min, 0.001);
    }

    @Test
    public void testDoOptimize_Maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariateFunction function = mock(UnivariateFunction.class);
        double max = optimizer.optimize(function, -10.0, 10.0)[0];
        assertEquals(1.23, max, 0.001);
    }

    @Test
    public void testDoOptimize_OutOfRange() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariateFunction function = mock(UnivariateFunction.class);
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, -10.0, -5.0));
    }

    @Test
    public void testDoOptimize_Convergence() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(function.value(1.5)).thenReturn(0.5);
        when(function.value(2.0)).thenReturn(1.2);
        when(function.value(2.5)).thenReturn(2.0);
        when(function.value(3.0)).thenReturn(3.5);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(true);
        optimizer = new BrentOptimizer(0.01, 1.0, convergenceChecker);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertEquals(1.5, result.getPoint(), 0.001);
        assertEquals(0.5, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimize_NoConvergence() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 1.0);
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(0.0)).thenReturn(0.0);
        when(function.value(1.0)).thenReturn(1.0);
        when(function.value(1.5)).thenReturn(0.5);
        when(function.value(2.0)).thenReturn(1.2);
        when(function.value(2.5)).thenReturn(2.0);
        when(function.value(3.0)).thenReturn(3.5);
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(false);
        optimizer = new BrentOptimizer(0.01, 1.0, convergenceChecker);

        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertNull(result);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(2.0, 1.0);

        assertEquals(a, optimizer.best(null, b, true));
        assertEquals(b, optimizer.best(a, null, true));
        assertEquals(c, optimizer.best(a, b, false));
        assertEquals(b, optimizer.best(a, b, false));
    }
}