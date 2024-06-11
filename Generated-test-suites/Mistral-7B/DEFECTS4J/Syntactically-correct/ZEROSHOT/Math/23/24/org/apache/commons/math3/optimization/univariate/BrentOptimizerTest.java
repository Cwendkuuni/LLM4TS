import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);

        // Test optimization of simple function f(x) = x^2
        Function Function = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        PointValuePair result = optimizer.optimize(Function, -5.0, 5.0);
        Assert.assertEquals(0.0, result.getPoint(), 1.0e-5);

        // Test optimization of function f(x) = |x - 2|
        Function absFunction = new Function() {
            @Override
            public double value(double x) {
                return Math.abs(x - 2.0);
            }
        };

        result = optimizer.optimize(absFunction, -5.0, 5.0);
        Assert.assertEquals(2.0, result.getPoint(), 1.0e-5);

        // Test optimization of function f(x) = exp(-x)
        Function expFunction = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x);
            }
        };

        result = optimizer.optimize(expFunction, -10.0, -1.0);
        Assert.assertEquals(-1.0, result.getPoint(), 1.0e-5);

        // Test optimization of function f(x) = sin(x)
        Function sinFunction = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        result = optimizer.optimize(sinFunction, -2.0 * Math.PI, 2.0 * Math.PI);
        Assert.assertEquals(0.0, result.getPoint(), 1.0e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0e-5, -1.0e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        new BrentOptimizer(0.0, 1.0e-5);
    }

    interface Function {
        double value(double x);
    }
}