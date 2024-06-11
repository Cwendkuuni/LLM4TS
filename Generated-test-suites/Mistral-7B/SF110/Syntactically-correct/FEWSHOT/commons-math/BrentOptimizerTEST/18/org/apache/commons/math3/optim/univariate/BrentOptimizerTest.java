package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMethod;
import org.apache.commons.math3.optim.simple.SimpleValuesProvider;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.MathConstants;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getMin());
        assertNull(optimizer.getStartValue());
        assertNull(optimizer.getMax());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 0.01, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test
    public void testBrentOptimizerWithMinValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        optimizer.setMin(-MathConstants.PI);
        optimizer.setStartValue(0.0);
        optimizer.setMax(MathConstants.PI);

        PointValuePair solution = optimizer.optimize(new SimpleValuesProvider() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        });
        assertEquals(-Math.PI, solution.getPoint(), 0.0);
        assertEquals(-Math.sin(Math.PI), solution.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithMaxValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        optimizer.setMin(-MathConstants.PI);
        optimizer.setStartValue(MathConstants.PI);
        optimizer.setMax(MathConstants.PI);

        PointValuePair solution = optimizer.optimize(new SimpleValuesProvider() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        });
        assertEquals(Math.PI, solution.getPoint(), 0.0);
        assertEquals(Math.sin(Math.PI), solution.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, new ConvergenceChecker() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return current.getValue() == previous.getValue();
            }
        });
        optimizer.setMin(-MathConstants.PI);
        optimizer.setStartValue(0.0);
        optimizer.setMax(MathConstants.PI);

        PointValuePair solution = optimizer.optimize(new SimpleValuesProvider() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        });
        assertEquals(-Math.PI, solution.getPoint(), 0.0);
        assertEquals(-Math.sin(Math.PI), solution.getValue(), 0.0);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerWithFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        optimizer.setMin(-3.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(5.0);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x) * Math.sin(x);
            }
        };

        PointValuePair solution = optimizer.optimize(function);
        assertEquals(-1.41371588555739, solution.getPoint(), 0.0001);
        assertEquals(0.13535532551234, solution.getValue(), 0.0001);
        assertTrue(optimizer.isConverged());
    }

    @Test
    public void testBrentOptimizerWithFunctionAndConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, new ConvergenceChecker() {
            @Override
            public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.001;
            }
        });
        optimizer.setMin(-3.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(5.0);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x) * Math.sin(x);
            }
        };

        PointValuePair solution = optimizer.optimize(function);
        assertEquals(-1.41371588555739, solution.getPoint(), 0.0001);
        assertEquals(0.13535532551234, solution.getValue(), 0.0001);
        assertTrue(optimizer.isConverged());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithNullFunction() {
        new BrentOptimizer(0.01, 0.01, null).optimize(null);
    }

    interface ConvergenceChecker {
        boolean converged(int iter, PointValuePair current, PointValuePair previous);
    }
}