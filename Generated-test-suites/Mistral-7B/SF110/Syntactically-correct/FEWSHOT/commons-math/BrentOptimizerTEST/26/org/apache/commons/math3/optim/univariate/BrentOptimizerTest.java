package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRelativeThreshold() {
        new BrentOptimizer(0.0001, 0.001, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test
    public void testDoOptimizeWithMinimization() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(f, 1.0, 3.0);
        assertEquals(Math.sqrt(2.0), result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMaximization() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, 1.0, 3.0);
        assertEquals(Math.sqrt(2.0), result.getPoint(), 0.001);
        assertEquals(-0.0, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMaxRange() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(f, -1000.0, 1000.0);
        assertEquals(1.349685125272485, result.getPoint(), 0.001);
        assertEquals(1.349685125272485, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithMinRange() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariatePointValuePair result = optimizer.optimize(f, -1000.0, -1.0);
        assertTrue(result.getValue() > 0.0);
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(convergenceChecker.converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class))).thenReturn(true);

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(f, 1.0, 3.0);
        verify(convergenceChecker, times(1)).converged(anyInt(), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
        assertEquals(Math.sqrt(2.0), result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
}