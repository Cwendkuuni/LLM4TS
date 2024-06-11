import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction function = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, new ValueInitializer(2.0));
        Assert.assertEquals(0.0, result.getValue(), 1e-6);

        // Test with a function with multiple local minima
        UnivariateFunction functionMultipleMinima = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x * Math.pow(Math.E, -x);
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-6);
        result = optimizer.optimize(functionMultipleMinima, new ValueInitializer(1.0));
        Assert.assertEquals(-1.934695594159452, result.getValue(), 1e-6);

        // Test with a function that is not monotonic
        UnivariateFunction functionNotMonotonic = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 0.5) * Math.sin(Math.PI * x);
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-6);
        result = optimizer.optimize(functionNotMonotonic, new ValueInitializer(-2.0));
        Assert.assertEquals(0.479425538604203, result.getValue(), 1e-6);

        // Test with a function that is not defined on the interval [lo, hi]
        UnivariateFunction functionNotDefined = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        optimizer = new BrentOptimizer(0.0, 1.0);
        try {
            optimizer.optimize(functionNotDefined, new ValueInitializer(0.5));
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test with a function that throws an exception
        UnivariateFunction functionThrowingException = new AbstractUnivariateFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new Exception("Something went wrong!");
            }
        };

        optimizer = new BrentOptimizer(1e-6, 1e-6);
        try {
            optimizer.optimize(functionThrowingException, new ValueInitializer(1.0));
            Assert.fail();
        } catch (OptimizationException e) {
            // Expected
        }
    }
}