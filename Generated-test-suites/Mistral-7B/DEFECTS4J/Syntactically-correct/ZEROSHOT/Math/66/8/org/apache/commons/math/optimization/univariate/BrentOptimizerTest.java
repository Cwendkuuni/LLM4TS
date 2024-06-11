import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonUnaryOperators;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCompare;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private UnivariateRealFunction testFunction;
    private UnivariateRealOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testMinimizeSimpleFunction() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1e-16);
        Assert.assertEquals(0.0, result.getValue(), 1e-16);
    }

    @Test
    public void testMinimizeFunctionWithMaxIterations() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.cos(x) * Math.exp(-x);
            }
        };
        optimizer.setMaxIterations(10);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0));
    }

    @Test
    public void testMinimizeFunctionWithInvalidAccuracy() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new BrentOptimizer(0.0));
    }

    @Test
    public void testMinimizeFunctionWithInvalidStartValue() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 0.5);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 1.0, Double.NaN));
    }

    @Test
    public void testMaximizeSimpleFunction() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -x * x;
            }
        };
        PointValuePair result = optimizer.optimize(NonUnaryOperators.NEGATE, testFunction, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1e-16);
        Assert.assertEquals(0.0, result.getValue(), 1e-16);
    }

    @Test
    public void testMinimizeNonDifferentiableFunction() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.log(Math.abs(x));
                } else {
                    return -Math.log(Math.abs(x));
                }
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1e-16);
        Assert.assertEquals(0.0, result.getValue(), 1e-16);
    }

    @Test
    public void testMaximizeNonDifferentiableFunction() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return -Math.log(Math.abs(x));
                } else {
                    return Math.log(Math.abs(x));
                }
            }
        };
        PointValuePair result = optimizer.optimize(NonUnaryOperators.NEGATE, testFunction, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        Assert.assertEquals(0.0, result.getPoint(), 1e-16);
        Assert.assertEquals(0.0, result.getValue(), 1e-16);
    }

    @Test
    public void testMinimizeFunctionWithGoalTypeMAXIMIZE() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MAXIMIZE, -10.0, 10.0, 0.0));
    }

    @Test
    public void testMinimizeFunctionWithInvalidBounds() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, 10.0, -10.0, 0.0));
    }

    @Test
    public void testMinimizeFunctionWithInvalidStartValueOutsideBounds() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(testFunction, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, 10.0, 0.0));
    }

    @Test
    public void testMinimizeFunctionWithInvalidStartValueInsideBounds() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -10.0, 10.0, -5.0);
        Assert.assertTrue(result.getValue() > 0);
    }

    @Test
    public void testMinimizeFunctionWithSameMinimaAtBothBounds() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x) - 1, 2);
            }
        };
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, -2.0, 2.0, 0.0);
        Assert.assertEquals(1.0, result.getPoint(), 1e-16);
        Assert.assertEquals(0.0, result.getValue(), 1e-16);
    }
}