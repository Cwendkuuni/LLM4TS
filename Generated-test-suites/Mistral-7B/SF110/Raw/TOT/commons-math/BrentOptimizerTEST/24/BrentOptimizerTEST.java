package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-2.25, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerMinRelTolTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-10, 10.0);
        UnivariateFunction f = (x -> x * x - 4.0);
        optimizer.optimize(f, -5.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsThresholdNegative() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, -10.0);
        UnivariateFunction f = (x -> x * x - 4.0);
        optimizer.optimize(f, -5.0, 5.0);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(0.0, result.getValue(), 1e-6);
        // This checker will cause the optimization to stop early, so the result is not guaranteed to be correct.
        optimizer = new BrentOptimizer(1.0e-5, 10.0, new MyConvergenceChecker());
        result = optimizer.optimize(f, -5.0, 5.0);
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerNoConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerGoalTypeMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerNullFunctions() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(null, -5.0, 5.0);
        assertNull(result);
    }

    @Test
    public void testBrentOptimizerNullMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        }, null);
        assertEquals(Double.NaN, result.getPoint(), 0.0);
        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerNullMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        }, 5.0);
        assertEquals(Double.NaN, result.getPoint(), 0.0);
        assertEquals(Double.NaN, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerEmptyBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        }, -5.0, 5.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-2.25, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerZeroBounds() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        }, 0.0, 0.0);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerZeroStart() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, 0.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-2.25, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerNegativeStart() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, -5.0);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-2.25, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerInvalidInput() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException();
            }
        };
        try {
            optimizer.optimize(f, -5.0, 5.0);
            fail();
        } catch (OptimizationException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerNaNFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.NaN;
            }
        };
        try {
            optimizer.optimize(f, -5.0, 5.0);
            fail();
        } catch (OptimizationException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerNaNStart() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        try {
            UnivariatePointValuePair result = optimizer.optimize(f, FastMath.NaN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerNaNMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        try {
            UnivariatePointValuePair result = optimizer.optimize(f, -5.0, FastMath.NaN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerNaNMax() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 10.0);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        try {
            UnivariatePointValuePair result = optimizer.optimize(f, FastMath.NaN, 5.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            return iter > 10;
        }
    }
}