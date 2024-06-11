package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerConstructionWithNullChecker() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
        assertEquals(relTol, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absTol, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        final double relTol = -1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        new BrentOptimizer(relTol, absTol, checker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        final double relTol = 1e-5;
        final double absTol = -1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        new BrentOptimizer(relTol, absTol, checker);
    }

    @Test(expected = NullPointerException.class)
    public void testBrentOptimizerNullCheckConvergence() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol);
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testBrentOptimizerZeroRelativeThreshold() {
        final double relTol = 0.0;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        try {
            new BrentOptimizer(relTol, absTol, checker);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertEquals(relTol, e.getAllowedValue(), 0.0);
            assertEquals(relTol, e.getMinimalValue(), 0.0);
            assertEquals(true, e.isExceptionThrown());
        }
    }

    @Test
    public void testBrentOptimizerZeroAbsoluteThreshold() {
        final double relTol = 1e-5;
        final double absTol = 0.0;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        try {
            new BrentOptimizer(relTol, absTol, checker);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(absTol, e.getValue(), 0.0);
        }
    }

    @Test
    public void testBrentOptimizerNullFunction() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = null;
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        try {
            optimizer.optimize();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Function is not set.", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerMinimization() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> x * x - 2.0 * x + 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.optimize();
        assertEquals(1.0, optimizer.getSolution().getPoint(), 0.0);
        assertEquals(-1.0, optimizer.getSolution().getValue(), 0.0);
        verify(checker, times(1)).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerMaximization() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> -(x * x - 2.0 * x + 1.0));
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(false);
        optimizer.optimize();
        assertEquals(-1.0, optimizer.getSolution().getPoint(), 0.0);
        assertEquals(1.0, optimizer.getSolution().getValue(), 0.0);
        verify(checker, times(1)).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerInvalidInitialBounds() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.pow(x, 2.0) - 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        try {
            optimizer.optimize(1.0, -1.0);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Invalid bounds.", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerConvergenceChecking() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.pow(x, 2.0) - 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.setInitialBounds(1.0, -1.0);
        optimizer.optimize();
        verify(checker, times(1)).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerNoConvergence() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.sin(Math.PI / 2.0 + x) * Math.pow(Math.abs(x), 0.5));
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.setInitialBounds(-10.0, 10.0);
        optimizer.optimize();
        verify(checker, never()).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerConvergenceCheckingWithCustomCheck() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.pow(x, 2.0) - 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.setInitialBounds(-10.0, 10.0);
        optimizer.setConvergenceChecker((iter, previous, current) -> previous == null && current == null);
        optimizer.optimize();
        verify(checker, never()).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerCustomCheckConvergence() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.pow(x, 2.0) - 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.setInitialBounds(-10.0, 10.0);
        optimizer.setConvergenceChecker((iter, previous, current) -> Math.abs(previous.getValue() - current.getValue()) < relTol);
        optimizer.optimize();
        verify(checker, times(1)).converged(ArgumentMatchers.anyInt(), ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
    }

    @Test
    public void testBrentOptimizerCustomCheckConvergenceWithFailure() {
        final double relTol = 1e-5;
        final double absTol = 1e-3;
        final ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        final ScalarFunction function = mockFunction((x) -> Math.pow(x, 2.0) - 1.0);
        final BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        optimizer.setFunction(function);
        optimizer.setMinimizer(true);
        optimizer.setInitialBounds(-10.0, 10.0);
        optimizer.setConvergenceChecker((iter, previous, current) -> false);
        try {
            optimizer.optimize();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Convergence checking failed.", e.getMessage());
        }
    }

    private ScalarFunction mockFunction(Function<Double, Double> function) {
        final ScalarFunction scalarFunction = mock(ScalarFunction.class);
        when(scalarFunction.value(ArgumentMatchers.anyDouble())).then((invocation) -> function.apply(invocation.getArgument(0)));
        return scalarFunction;
    }

    private void verify(ConvergenceChecker<UnivariatePointValuePair> checker, int times, UnivariatePointValuePair... expectedArguments) {
        verify(checker, times(times)).converged(ArgumentMatchers.eq(ArgumentMatchers.isA(UnivariatePointValuePair.class)),
                ArgumentMatchers.isNull(), ArgumentMatchers.isNull());
        if (expectedArguments != null) {
            for (UnivariatePointValuePair expectedArgument : expectedArguments) {
                verify(checker, times(1)).converged(ArgumentMatchers.eq(expectedArgument.getValue()), expectedArgument, expectedArgument);
            }
        }
    }
}