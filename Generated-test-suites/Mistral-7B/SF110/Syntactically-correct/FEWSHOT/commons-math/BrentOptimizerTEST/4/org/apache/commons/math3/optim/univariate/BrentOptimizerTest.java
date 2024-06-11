package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(-3.9166636028555555E-15, result.getValue(), 1e-15);

        optimizer = new BrentOptimizer(0.01, 0.0, null);
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(0.01, -0.01, null));

        optimizer = new BrentOptimizer(0.0, 0.01, null);
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.0001, 0.001, null));

        optimizer = new BrentOptimizer(0.01, 0.01, new NullConvergenceChecker());
        assertThrows(NullPointerException.class, () -> optimizer.optimize(null, 0.0, 0.0));

        optimizer = new BrentOptimizer(0.01, 0.01, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return false;
            }
        });
        assertThrows(IterationLimitExceededException.class, () -> optimizer.optimize(f, -5.0, 5.0));
    }

    @Test
    public void testBrentOptimizerMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(-3.9166636028555555E-15, result.getValue(), 1e-15);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return -(Math.pow(x, 2) - 4);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(3.9166636028555555E-15, result.getValue(), 1e-15);
    }

    @Test
    public void testBrentOptimizerNullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new NullConvergenceChecker());
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        assertThrows(IllegalStateException.class, () -> optimizer.optimize(f, -5.0, 5.0));
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
                return current.getValue() == 0;
            }
        });
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(f, -5.0, 5.0);
        assertEquals(0.0, result.getValue(), 1e-15);
    }

    @Test
    public void testBrentOptimizerMinRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001, null);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            optimizer.optimize(f, -5.0, 5.0);
            fail("NumberIsTooSmallException expected.");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001, null);
        FunctionFunction f = new FunctionFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        try {
            optimizer.optimize(f, -5.0, 5.0);
            fail("NotStrictlyPositiveException expected.");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
}