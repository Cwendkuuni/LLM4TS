package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest extends AbstractTest {

    @Test
    public void testOptimizer() {
        UnivariateFunction f = new PointValueFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair(2 * x - 4, 2);
            }
        };
        UnivariatePointValuePair result = new BrentOptimizer(1e-6, 1e-6, null)
                .optimize(f, new SimpleBounds(1, 3), GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-3.0, result.getValue(), 1e-6);

        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(f, null, GoalType.MINIMIZE));
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(f, new SimpleBounds(-1, 0), GoalType.MINIMIZE));
        assertThrows(NumberIsTooSmallException.class, () -> optimizer.optimize(f, new SimpleBounds(1, 1e-12), GoalType.MINIMIZE));
        assertThrows(MaxEvalException.class, () -> optimizer.optimize(f, new SimpleBounds(1, 3), GoalType.MINIMIZE, 1));

        UnivariateFunction notDefined = new PointValueFunction() {
            @Override
            public double value(double x) {
                throw new AssertionError();
            }
        };
        assertThrows(AssertionError.class, () -> optimizer.optimize(notDefined, new SimpleBounds(1, 3), GoalType.MINIMIZE));

        UnivariateFunction quadratic = new PointValueFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair(2 * x, null);
            }
        };
        UnivariatePointValuePair wrongInitialGuess = new BrentOptimizer(1e-6, 1e-6, null)
                .optimize(quadratic, new SimpleBounds(-1, 1), GoalType.MINIMIZE);
        assertNull(wrongInitialGuess);

        UnivariatePointValuePair initialGuess = new BrentOptimizer(1e-6, 1e-6, new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 1;
            }
        })
                .optimize(quadratic, new SimpleBounds(-1, 1), GoalType.MINIMIZE);
        assertEquals(0.0, initialGuess.getPoint(), 1e-6);
        assertEquals(0.0, initialGuess.getValue(), 1e-6);
    }

    @Test
    public void testConvergenceChecker() {
        UnivariateFunction f = new PointValueFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4 * x + 4;
            }

            @Override
            public PointValuePair gradient(double x) {
                return new PointValuePair(2 * x - 4, 2);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6) {
            @Override
            protected boolean checkConvergence(int iter, UnivariatePointValuePair p1, UnivariatePointValuePair p2) {
                return Math.abs(p1.getValue() - p2.getValue()) < 1e-8;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(f, new SimpleBounds(1, 3), GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-3.0, result.getValue(), 1e-6);
    }

}