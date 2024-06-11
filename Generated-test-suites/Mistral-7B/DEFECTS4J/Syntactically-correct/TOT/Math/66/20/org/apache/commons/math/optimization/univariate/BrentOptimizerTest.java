package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testSimpleMinimization() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, 1.0);
        Assert.assertEquals(2.0, result.getPoint(), 1E-6);
        Assert.assertEquals(0.000333066907387, result.getValue(), 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / x);
            }
        };
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAccuracy() throws MaxIterationsExceededException {
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        }, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }

    @Test
    public void testZeroFunction() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1E-6);
        Assert.assertEquals(0.0, result.getValue(), 1E-6);
    }

    @Test
    public void testMaximization() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x);
            }
        };
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(3.0, result.getPoint(), 1E-6);
        Assert.assertEquals(23.14069263274357, result.getValue(), 1E-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testDivisionByZero() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return 1.0 / x;
            }
        };
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 0.0, 0.0);
    }
}