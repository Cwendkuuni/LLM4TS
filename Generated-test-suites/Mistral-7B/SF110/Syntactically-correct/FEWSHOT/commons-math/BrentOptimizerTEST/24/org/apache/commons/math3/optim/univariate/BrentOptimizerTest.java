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
        try {
            new BrentOptimizer(-0.1, 0.0);
            fail("Exception expected: NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // pass
        }

        try {
            new BrentOptimizer(0.0, -0.1);
            fail("Exception expected: NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // pass
        }

        try {
            new BrentOptimizer(0.0, 0.0);
            fail("Exception expected: NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // pass
        }

        try {
            new BrentOptimizer(1.0, -1.0);
            fail("Exception expected: NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // pass
        }

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);

        // Test with a simple function
        AbstractFunction1D testFunction = new AbstractFunction1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(testFunction, 0.0, 1.0);
        assertEquals(0.0, result.getX(), 0.01);
        assertEquals(0.0, result.getValue(), 0.001);

        // Test with a more complex function
        AbstractFunction1D testFunction2 = new AbstractFunction1D() {
            @Override
            public double value(double x) {
                return Math.log(1 + Math.pow(Math.E, -Math.pow(x, 2)));
            }
        };

        result = optimizer.optimize(testFunction2, -5.0, 5.0);
        assertEquals(-1.3117336646651255, result.getX(), 0.001);
        assertEquals(-1.311729141347188, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyConvergenceChecker());

        AbstractFunction1D testFunction = new AbstractFunction1D() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        optimizer.optimize(testFunction, 0.0, 1.0);

        // The convergence checker should have been called at least once
        assertTrue(myConvergenceCheckerCalled);
    }

    private boolean myConvergenceCheckerCalled = false;

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iter, UnivariatePointValuePair current, UnivariatePointValuePair previous) {
            myConvergenceCheckerCalled = true;
            return true;
        }
    }
}