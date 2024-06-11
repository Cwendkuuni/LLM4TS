package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        try {
            new BrentOptimizer(-1.0, 1.0, null);
            assert false;
        } catch (NotStrictlyPositiveException | NumberIsTooSmallException e) {
            // expected
        }

        new BrentOptimizer(0.1, 1.0, null);
        new BrentOptimizer(1.0, 1.0, null);
        new BrentOptimizer(0.01, 1.0, null);

        new BrentOptimizer(0.1, 0.0, null);
        new BrentOptimizer(0.1, 1.0, new GoalType[] { GoalType.MINIMIZE, GoalType.MAXIMIZE });
        new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iterationNumber, UnivariatePointValuePair currentValue, UnivariatePointValuePair previousValue) {
                return false;
            }
        });
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 1.0, null);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.pow(x, 2);
            }
        };

        OptimizationResult result = optimizer.optimize(function, 0.0, 1.0);
        assertTrue(result.isSuccess());
        assertTrue(Precision.isClose(result.getPoint().getValue(), 1.0, 0.0001));

        optimizer = new BrentOptimizer(0.1, 1.0, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iterationNumber, UnivariatePointValuePair currentValue, UnivariatePointValuePair previousValue) {
                return FastMath.abs(currentValue.getValue() - previousValue.getValue()) < 0.0001;
            }
        });

        result = optimizer.optimize(function, 0.0, 1.0);
        assertTrue(result.isSuccess());
        assertTrue(Precision.isClose(result.getPoint().getValue(), 1.0, 0.0001));

        optimizer = new BrentOptimizer(0.1, 1.0, new GoalType[] { GoalType.MAXIMIZE });
        result = optimizer.optimize(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FastMath.log(FastMath.abs(x) + 1.0);
            }
        }, -10.0, 10.0);
        assertTrue(result.isSuccess());
        assertTrue(Precision.isClose(result.getPoint().getValue(), 0.0, 0.0001));
        assertTrue(result.getValue().isNegative());

        optimizer = new BrentOptimizer(0.1, 1.0, null);
        try {
            result = optimizer.optimize(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    throw new RuntimeException();
                }
            }, 0.0, 1.0);
            assert false;
        } catch (OptimizationException e) {
            // expected
        }

        optimizer = new BrentOptimizer(0.1, 1.0, null);
        UnivariatePointValuePair initialPoint = new UnivariatePointValuePair(2.0, 4.0);
        result = optimizer.optimize(function, initialPoint, null);
        assertTrue(result.isSuccess());
        assertTrue(Precision.isClose(result.getPoint().getValue(), 1.0, 0.0001));
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);

        UnivariatePointValuePair best = BrentOptimizer.this.best(a, b, true);
        assertTrue(best == a || best == b);

        best = BrentOptimizer.this.best(null, b, true);
        assertTrue(best == b);

        best = BrentOptimizer.this.best(a, c, false);
        assertTrue(best == c || best == a);

        best = BrentOptimizer.this.best(null, c, false);
        assertTrue(best == c);
    }
}