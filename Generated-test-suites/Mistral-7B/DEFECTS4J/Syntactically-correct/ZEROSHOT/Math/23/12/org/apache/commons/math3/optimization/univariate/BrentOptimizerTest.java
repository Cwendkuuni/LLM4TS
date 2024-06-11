package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer.Minimizer;
import org.apache.commons.math3.special.Erfc;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function: f(x) = erfc(x).
     */
    private static class ErfcFunction implements UnivariateFunction {

        @Override
        public double value(double arg0) {
            return Erfc.erfc(arg0);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    /**
     * Test function: f(x) = x^3 - 4 * x^2 + 6.
     */
    private static class CubicFunction implements UnivariateFunction {

        @Override
        public double value(double arg0) {
            return Math.pow(arg0, 3) - 4 * Math.pow(arg0, 2) + 6;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    /**
     * Test function: f(x) = x^2.
     */
    private static class SquareFunction implements UnivariateFunction {

        @Override
        public double value(double arg0) {
            return Math.pow(arg0, 2);
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    @org.junit.Test
    public void testBrentOptimizer() {
        // Test with no tolerance.
        BrentOptimizer optimizer = new BrentOptimizer(0, 0);
        UnivariateFunction function = new ErfcFunction();
        PointValuePair result = optimizer.minimize(function, -3, 3);
        assertNotNull(result);
        double expected = -1.202056903159594;
        assertEquals(expected, result.getPoint(), 0.0);
        double actual = result.getValue();
        assertEquals(expected, actual, 0.0);

        // Test with tolerance.
        optimizer = new BrentOptimizer(0.00001d, 0.01d);
        result = optimizer.minimize(function, -3, 3);
        assertNotNull(result);
        expected = -1.202056903159594;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test with custom ConvergenceChecker.
        optimizer = new BrentOptimizer(0.00001d, 0.01d);
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (previous != null && current.getValue() <= previous.getValue());
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
        result = optimizer.minimize(function, -3, 3);
        assertNotNull(result);
        assertEquals(-1.202056903159594, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(-1.202056903159594, actual, 0.00001d);

        // Test when the minimum is at the end of the interval.
        optimizer = new BrentOptimizer(0.00001d, 0.01d);
        result = optimizer.minimize(new SquareFunction(), -10, 10);
        assertNotNull(result);
        expected = 0;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test when the minimum is at the start of the interval.
        optimizer = new BrentOptimizer(0.00001d, 0.01d);
        result = optimizer.minimize(new SquareFunction(), -20, -10);
        assertNotNull(result);
        expected = 100;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test when the minimum is inside the interval.
        optimizer = new BrentOptimizer(0.00001d, 0.01d);
        result = optimizer.minimize(new CubicFunction(), -5, 5);
        assertNotNull(result);
        expected = 1.324717957244746;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test for a negative absolute tolerance.
        BrentOptimizer optimizerNegative = new BrentOptimizer(0.00001d, -0.01d);
        result = optimizerNegative.minimize(function, -3, 3);
        assertNull(result);

        // Test for a zero relative tolerance.
        BrentOptimizer optimizerZeroRel = new BrentOptimizer(0d, 0.00001d);
        result = optimizerZeroRel.minimize(function, -3, 3);
        // The optimization algorithm does not terminate with zero relative tolerance.
        assertNull(result);

        // Test when the interval is too small.
        BrentOptimizer optimizerSmall = new BrentOptimizer(0.00001d, 0.00001d);
        result = optimizerSmall.minimize(function, -3, 3);
        // The optimization algorithm does not work with an interval too small.
        assertNull(result);

        // Test with the default tolerance.
        BrentOptimizer optimizerDefault = new BrentOptimizer(0.00001d);
        result = optimizerDefault.minimize(function, -3, 3);
        assertNotNull(result);
        expected = -1.202056903159594;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test with a very large interval.
        optimizerDefault = new BrentOptimizer(0.00001d);
        result = optimizerDefault.minimize(function, -1000, 1000);
        assertNotNull(result);
        expected = -1.202056903159594;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);

        // Test with a custom minimizer.
        Minimizer minimizer = new Minimizer() {
            @Override
            public PointValuePair minimize(UnivariateFunction function, double start, double end) {
                BrentOptimizer optimizer = new BrentOptimizer(0.00001d, 0.01d);
                return optimizer.minimize(function, start, end);
            }
        };
        result = minimizer.minimize(function, -3, 3);
        assertNotNull(result);
        expected = -1.202056903159594;
        assertEquals(expected, result.getPoint(), 0.00001d);
        actual = result.getValue();
        assertEquals(expected, actual, 0.00001d);
    }
}