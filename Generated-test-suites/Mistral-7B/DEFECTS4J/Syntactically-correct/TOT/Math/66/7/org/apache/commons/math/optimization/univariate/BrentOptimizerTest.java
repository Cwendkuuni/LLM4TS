package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeMin() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x * Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
        assertEquals(-1.7426957, result, 1e-8);
    }

    @Test
    public void testOptimizeMax() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) + 4 * x * Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(function, GoalType.MAXIMIZE, -5.0, 5.0, 0.0);
        assertEquals(3.532583, result, 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1e-8);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStepSize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, -0.1);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, GoalType.MINIMIZE, -1.0, 1.0, 0.0);
    }
}