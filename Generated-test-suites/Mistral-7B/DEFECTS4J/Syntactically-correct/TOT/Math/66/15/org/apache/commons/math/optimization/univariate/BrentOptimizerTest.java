package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2) + Math.pow(x - 3, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1.0);
        assertEquals(-2.0, result.getPoint(), 1e-6);
        assertEquals(4.0, result.getValue(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws NotStrictlyPositiveException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(10);
        optimizer.optimize(function, GoalType.MINIMIZE, -10, 10, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAccuracy() throws NotStrictlyPositiveException {
        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1e-6);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, GoalType.MINIMIZE, -1, 1, 0.0);
    }

    @Test
    public void testMaximization() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10, 10, 1.0);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    public void testMinimumAtBoundary() throws NotStrictlyPositiveException, MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, -5, 1.0);
        assertEquals(-5.0, result.getPoint(), 1e-6);
        assertEquals(25.0, result.getValue(), 1e-6);
    }
}