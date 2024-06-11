import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalings.SquaredSimpleScaling;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-8;
    private static final double MINIMUM = 3.0;
    private static final double MAXIMUM = 5.0;
    private static final Function F = new FunctionWithRemainder() {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 4 * x + MINIMUM;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 4;
        }

        @Override
        public double secondDerivative(double x) {
            return 6 * Math.pow(x, 1);
        }
    };

    /**
     * Basic test for the constructor with default threshold values.
     */
    @Test
    public void testDefaultConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(F);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(MINIMUM, result.getPoint(), 0.0);
        assertEquals(MINIMUM, result.getValue(), 0.0);
    }

    /**
     * Test for the constructor with specified threshold values.
     */
    @Test
    public void testCustomConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, F);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(MINIMUM, result.getPoint(), RELATIVE_TOLERANCE);
        assertEquals(MINIMUM, result.getValue(), Precision.safeEpsilon(MINIMUM));
    }

    /**
     * Test for finding minimum of a function.
     */
    @Test
    public void testFindMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(F);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(MINIMUM, result.getPoint(), 0.0);
        assertEquals(MINIMUM, result.getValue(), 0.0);
    }

    /**
     * Test for finding minimum of a function with specified threshold values.
     */
    @Test
    public void testFindMinimumWithCustomThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE, F);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(MINIMUM, result.getPoint(), RELATIVE_TOLERANCE);
        assertEquals(MINIMUM, result.getValue(), Precision.safeEpsilon(MINIMUM));
    }

    /**
     * Test for the optimization process with a function that doesn't have a minimum.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNoMinimum() {
        FunctionWithRemainder function = new FunctionWithRemainder() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }

            @Override
            public double secondDerivative(double x) {
                return -Math.sin(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(function);
        optimizer.optimize();
    }

    /**
     * Test for scaling with SquaredSimpleScaling.
     */
    @Test
    public void testScaling() {
        FunctionWithRemainder function = new FunctionWithRemainder() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x + 2 * Math.cos(x);
            }

            @Override
            public double secondDerivative(double x) {
                return 2 + 2 * Math.sin(x) * Math.cos(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(new SquaredSimpleScaling(), F);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(MINIMUM, result.getPoint(), 0.0);
        assertEquals(MINIMUM, result.getValue(), 0.0);
    }
}