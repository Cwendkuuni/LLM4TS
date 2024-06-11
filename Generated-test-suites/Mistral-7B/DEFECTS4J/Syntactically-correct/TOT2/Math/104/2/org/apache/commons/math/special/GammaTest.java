package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;

/**
 * Test class for Gamma utility class.
 */
public class GammaTest {

    /**
     * Test for logGamma method.
     */
    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
        assertEquals(Math.log(1.0) + Math.log(2.0) * 0.5,
                Gamma.logGamma(1.5), 1e-12);

        // Test with edge cases
        assertEquals(1.0, Gamma.logGamma(0.5), 1e-12); // half
        assertEquals(-Math.log(1.0 / 2.0) - Math.log(2.0) * 0.5,
                Gamma.logGamma(0.5), 1e-12); // negative half
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(1.0), 1e-12); // positive infinity
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(0.0), 1e-12); // negative infinity
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-12); // negative number

        // Test with exceptions
        assertThrows(MathException.class, () -> Gamma.logGamma(-0.5)); // negative argument
        assertThrows(MathException.class, () -> Gamma.logGamma(0.0)); // zero argument
        assertThrows(MathException.class, () -> Gamma.logGamma(1.5, new MathContext(1))); // low precision
    }

    /**
     * Test for regularizedGammaP method.
     */
    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-12);
        assertEquals(0.6656756988511657, Gamma.regularizedGammaP(1.1, 0.5), 1e-12);

        // Test with edge cases
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12); // same argument
        assertEquals(1.0 - Math.exp(-1.0 + Math.log(1.0) * 0.5),
                Gamma.regularizedGammaP(0.5, 1.0), 1e-12); // large x
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 0.5), 1e-12); // same a and x

        // Test with exceptions
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.0, -0.5)); // negative argument
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(0.0, 0.5)); // zero a
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.1, -0.5)); // negative x
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(0.5, Double.NEGATIVE_INFINITY)); // negative x
        assertThrows(MaxIterationsExceededException.class, () -> Gamma.regularizedGammaP(0.5, 0.5, 1, 1)); // low iterations
    }

    /**
     * Test for regularizedGammaQ method.
     */
    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        assertEquals(0.5, 1.0 - Gamma.regularizedGammaP(0.5, 0.5), 1e-12);
        assertEquals(1.0 - 0.6656756988511657,
                Gamma.regularizedGammaQ(1.1, 0.5), 1e-12);

        // Test with edge cases
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12); // same argument
        assertEquals(1.0 - Math.exp(-1.0 + Math.log(1.0) * 0.5),
                Gamma.regularizedGammaQ(0.5, 1.0), 1e-12); // large x
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(0.5, 0.5), 1e-12); // same a and x

        // Test with exceptions
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.0, -0.5)); // negative argument
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(0.0, 0.5)); // zero a
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.1, -0.5)); // negative x
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(0.5, Double.NEGATIVE_INFINITY)); // negative x
        assertThrows(MaxIterationsExceededException.class, () -> Gamma.regularizedGammaQ(0.5, 0.5, 1, 1)); // low iterations
    }
}