package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;

import java.util.Arrays;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer1 = new BrentOptimizer(0.1, 1.0);
        BrentOptimizer optimizer2 = new BrentOptimizer(0.1, 1.0, null);
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.001);
        BrentOptimizer optimizer4 = new BrentOptimizer(0.001, 0.0);

        assertEquals(0.1, optimizer1.getRelativeThreshold(), 0.001);
        assertEquals(1.0, optimizer1.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer1.getConvergenceChecker());

        assertEquals(0.1, optimizer2.getRelativeThreshold(), 0.001);
        assertEquals(1.0, optimizer2.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer2.getConvergenceChecker());

        assertEquals(0.01, optimizer3.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer3.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer3.getConvergenceChecker());

        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-0.1, 0.0));
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(0.1, -0.0));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.0, 0.0));
    }

    @Test
    public void testEmptyMinAndMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, null);
        assertThrows(IllegalArgumentException.class, () -> optimizer.doOptimize());
    }

    @Test
    public void testBrentOptimizerMinAndMax() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker() {
            @Override
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 0.1;
            }
        });

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-1.0);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.6931471805599453, result.getPoint(), 0.001);
        assertEquals(-0.6931471805599453, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new CustomConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithDifferentGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithNonConvergentFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0.5) {
                    return Math.pow(x, 2);
                } else {
                    return -Math.pow(x, 2);
                }
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertNull(result);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithConvergentFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(1.312383173784137, result.getPoint(), 0.001);
        assertEquals(-1.312383173784137, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithConvergentFunctionWithCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, new CustomConvergenceChecker());

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(x);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(1.312383173784137, result.getPoint(), 0.001);
        assertEquals(-1.312383173784137, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithFunctionThrowsException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) throws MaxEvalException {
                throw new MaxEvalException("Exception message");
            }
        };

        assertThrows(MaxEvalException.class, () -> optimizer.optimize(function, 0.0, 1.0));
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithFunctionThrowsExceptionAtPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) throws MaxEvalException {
                if (x == 0.5) {
                    throw new MaxEvalException("Exception message");
                }
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertNull(result);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithOutOfBoundsFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new IllegalArgumentException("x is negative");
                }
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerMinAndMaxWithOutOfBoundsFunctionAtPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x < 0.0) {
                    throw new IllegalArgumentException("x is negative");
                }
                if (x == 1.0) {
                    throw new IllegalArgumentException("x is out of bounds");
                }
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);

        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(0.25, result.getValue(), 0.001);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return (previous == null) || (Math.abs(current.getValue() - previous.getValue()) < 0.001);
        }
    }
}