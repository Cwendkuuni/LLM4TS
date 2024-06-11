package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private static final double TOL = 1e-8;
    private static final double ACC = 1e-8;
    private static final double MAX_ITERATIONS = 100;

    @Test
    public void testMinimizeSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setAbsoluteAccuracy(ACC);
        optimizer.setRelativeAccuracy(ACC);
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -5, 5, 1.0);
        assertEquals(2.0, result, TOL);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, -5, 5, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeArguments() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -1, 1, 1.0);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2);
            }
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setAbsoluteAccuracy(ACC);
        optimizer.setRelativeAccuracy(ACC);
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -5, 5, 1.0);
        assertEquals(2.0, result, TOL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        UnivariateRealOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.NEUTRAL, -5, 5, 1.0);
    }
}