import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0);

        // Test with a simple function.
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);

        // Test with a function having multiple roots.
        UnivariateFunction functionMultipleRoots = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2) * (x - 3.0);
            }
        };
        result = optimizer.optimize(functionMultipleRoots, -5.0, 5.0);
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);

        // Test with a function having a minimum close to the bounds.
        UnivariateFunction functionMinCloseToBounds = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (1.0 + Math.abs(x));
            }
        };
        result = optimizer.optimize(functionMinCloseToBounds, -5.0, 5.0);
        assertEquals(-5.0, result.getPoint(), 1e-5);
        assertEquals(0.2, result.getValue(), 1e-3);

        // Test with a function having a very large minimum.
        UnivariateFunction functionLargeMin = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-100.0 * x * x);
            }
        };
        result = optimizer.optimize(functionLargeMin, -5.0, 5.0);
        assertTrue(result.getValue() < 0.0);
        assertTrue(result.getValue() > 4.5e-67);

        // Test with a function having a very flat minimum.
        UnivariateFunction functionFlatMin = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0e-10 * Math.pow(x - 0.5, 2);
            }
        };
        optimizer = new BrentOptimizer(1.0E-8, 0.0);
        result = optimizer.optimize(functionFlatMin, -5.0, 5.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-8);
    }
}