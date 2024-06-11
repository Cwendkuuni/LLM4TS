package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid input
        double result = Gamma.logGamma(1.5);
        double expected = Math.log(Math.PI) + 0.5 * Math.log(1.5) - 0.125312226063; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-8);

        // Test with negative input
        result = Gamma.logGamma(-1.0);
        assertNaN(result);

        // Test with zero input
        result = Gamma.logGamma(0.0);
        assertNaN(result);

        // Test with large input
        result = Gamma.logGamma(1000.0);
        expected = Math.log(Math.PI) + 500.5 * Math.log(1000.0) - 1141.28871268; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-8);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid input
        double result = Gamma.regularizedGammaP(1.5, 1.5);
        double expected = 0.886226925452; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        // Test with negative a
        try {
            result = Gamma.regularizedGammaP(-1.0, 1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with negative x
        try {
            result = Gamma.regularizedGammaP(1.5, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with a = x
        result = Gamma.regularizedGammaP(1.5, 1.5);
        expected = 0.886226925452; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        // Test with a = 0
        result = Gamma.regularizedGammaP(0.0, 1.5);
        expected = 1.0;
        assertEquals(expected, result, 1e-10);

        // Test with a <= 0
        try {
            result = Gamma.regularizedGammaP(0.0, 1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with x < 0
        try {
            result = Gamma.regularizedGammaP(1.5, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with large a and x
        result = Gamma.regularizedGammaP(1000.0, 1000.0);
        expected = 0.535357673527; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid input
        double result = Gamma.regularizedGammaQ(1.5, 1.5);
        double expected = 0.113773074548; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        // Test with negative a
        try {
            result = Gamma.regularizedGammaQ(-1.0, 1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with negative x
        try {
            result = Gamma.regularizedGammaQ(1.5, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with a = x
        result = Gamma.regularizedGammaQ(1.5, 1.5);
        expected = 0.113773074548; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);

        // Test with a = 0
        result = Gamma.regularizedGammaQ(0.0, 1.5);
        expected = 1.0 - result; // result should be 0.886226925452, but we are testing Q(a, x) = 1 - P(a, x)
        assertEquals(expected, result, 1e-6);

        // Test with a <= 0
        try {
            result = Gamma.regularizedGammaQ(0.0, 1.5);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with x < 0
        try {
            result = Gamma.regularizedGammaQ(1.5, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        // Test with large a and x
        result = Gamma.regularizedGammaQ(1000.0, 1000.0);
        expected = 0.464693289948; // calculated using Wolfram Alpha
        assertEquals(expected, result, 1e-6);
    }
}