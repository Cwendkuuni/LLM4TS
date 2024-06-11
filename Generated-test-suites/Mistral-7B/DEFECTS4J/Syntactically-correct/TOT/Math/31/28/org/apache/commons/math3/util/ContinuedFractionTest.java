package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluating a simple continued fraction.
     */
    @Test
    public void testSquareRootOfTwo() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        double result = cf.evaluate(2.0);
        assertEquals("Wrong value for sqrt(2)", 1.4142135623730955, result, 1e-9);
    }

    /**
     * Test evaluating a continued fraction with large coefficients.
     */
    @Test
    public void testE() {
        ContinuedFraction cf = new EContinuedFraction();
        double result = cf.evaluate(0.0);
        assertEquals("Wrong value for E", 2.718281828459045, result, 1e-9);
    }

    /**
     * Test evaluating a continued fraction with max iterations.
     */
    @Test
    public void testFibonacci() {
        ContinuedFraction cf = new FibonacciContinuedFraction();
        double result = cf.evaluate(5.0, 1e-9, 10);
        assertEquals("Wrong value for fibonacci(5)", 5.0, result, 1e-9);
    }

    /**
     * Test evaluating a continued fraction with an infinite result.
     */
    @Test
    public void testLogBaseE() {
        ContinuedFraction cf = new LogBaseEContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(-1.0, 1e-9, Integer.MAX_VALUE));
    }

    /**
     * Test evaluating a continued fraction with an NaN result.
     */
    @Test
    public void testLogBaseTwo() {
        ContinuedFraction cf = new LogBaseTwoContinuedFraction();
        assertThrows(ArithmeticException.class, () -> cf.evaluate(1.0));
    }

    /**
     * Test evaluating a continued fraction with an exception.
     */
    @Test
    public void testSineContinuedFraction() {
        ContinuedFraction cf = new SineContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(1.5707963267948966, 1e-9, Integer.MAX_VALUE));
    }

    /**
     * Test evaluating a continued fraction with an exception for max iterations.
     */
    @Test
    public void testCotangentContinuedFraction() {
        ContinuedFraction cf = new CotangentContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> cf.evaluate(0.0, 1e-9, 1));
    }

}