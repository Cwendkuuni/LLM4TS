package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import org.junit.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /** A simple test case for the golden ratio continued fraction. */
    @Test
    public void testGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        assertEquals(1.618051265366785028841368226953447758217501793652881,
                     goldenRatio.evaluate(1.0), 1e-10);
    }

    /** A test case for the evaluation of the continued fraction at the negative point. */
    @Test
    public void testNegativePoint() {
        ContinuedFraction sqrt2ContinuedFraction = new Sqrt2ContinuedFraction();
        assertThrows(ConvergenceException.class, () -> sqrt2ContinuedFraction.evaluate(-1.0, 1e-10, Integer.MAX_VALUE));
    }

    /** A test case for the evaluation of the continued fraction with epsilon too small. */
    @Test
    public void testEpsilonTooSmall() {
        ContinuedFraction sqrt2ContinuedFraction = new Sqrt2ContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> sqrt2ContinuedFraction.evaluate(1.0, 1e-15, Integer.MAX_VALUE));
    }

    /** A test case for the evaluation of the continued fraction with maxIterations too small. */
    @Test
    public void testMaxIterationsTooSmall() {
        ContinuedFraction sqrt2ContinuedFraction = new Sqrt2ContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> sqrt2ContinuedFraction.evaluate(1.0, 1e-10, 10));
    }

    /** A test case for the evaluation of the continued fraction with infinite coefficients. */
    @Test
    public void testInfiniteCoefficients() {
        ContinuedFraction log2ContinuedFraction = new Log2ContinuedFraction();
        assertThrows(ConvergenceException.class, () -> log2ContinuedFraction.evaluate(1.5, 1e-10, Integer.MAX_VALUE));
    }
}