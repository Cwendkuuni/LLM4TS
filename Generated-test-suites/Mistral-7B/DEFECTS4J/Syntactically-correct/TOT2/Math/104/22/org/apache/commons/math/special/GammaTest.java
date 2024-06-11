package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.special.Gamma;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.junit.Test;

/**
 * This class tests the methods in the Gamma utility class.
 */
public class GammaTest {

    /**
     * Test of logGamma method, regular case.
     */
    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
        assertEquals(-0.176268652646908111, Gamma.logGamma(0.5), 1e-12);
        assertEquals(0.261727471573625541, Gamma.logGamma(2.0), 1e-12);
        assertEquals(1.421356237309552535, Gamma.logGamma(3.0), 1e-12);
        assertEquals(1.67943798635548352, Gamma.logGamma(4.0), 1e-12);
        assertEquals(1.91977728192102118, Gamma.logGamma(5.0), 1e-12);
    }

    /**
     * Test of logGamma method, edge cases.
     */
    @Test
    public void testLogGammaEdgeCases() {
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(1.0e-223), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(-1.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
    }

    /**
     * Test of logGamma method, error cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLogGammaError() {
        // Gamma function is not defined for complex numbers
        Gamma.logGamma(new Complex(1.0, 2.0));
    }

    /**
     * Test of regularizedGammaP method, regular case.
     */
    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.5), 1e-12);
        assertEquals(0.886226925452758017, Gamma.regularizedGammaP(1.5), 1e-12);
        assertEquals(0.664857213716172345, Gamma.regularizedGammaP(2.5), 1e-12);
        assertEquals(0.332251950366171490, Gamma.regularizedGammaP(3.5), 1e-12);
        assertEquals(0.226145268525268558, Gamma.regularizedGammaP(4.5), 1e-12);
    }

    /**
     * Test of regularizedGammaP method, edge cases.
     */
    @Test
    public void testRegularizedGammaPEdgeCases() {
        try {
            // throw an exception for negative 'a'
            Gamma.regularizedGammaP(-0.5, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for complex 'x'
            Gamma.regularizedGammaP(1.0, new Complex(1.0, 2.0));
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for 'x' < 0.0
            Gamma.regularizedGammaP(1.0, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for 'a' <= 0.0
            Gamma.regularizedGammaP(0.0, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaP method, error cases.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaPError() {
        // throw an exception for too many iterations
        Gamma.regularizedGammaP(0.5, 1.0, 1e-16, 1000000);
    }

    /**
     * Test of regularizedGammaQ method, regular case.
     */
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5), 1e-12);
        assertEquals(0.115833228196331144, Gamma.regularizedGammaQ(1.5), 1e-12);
        assertEquals(0.153626433633729814, Gamma.regularizedGammaQ(2.5), 1e-12);
        assertEquals(0.182160324423310234, Gamma.regularizedGammaQ(3.5), 1e-12);
        assertEquals(0.213552496204348307, Gamma.regularizedGammaQ(4.5), 1e-12);
    }

    /**
     * Test of regularizedGammaQ method, edge cases.
     */
    @Test
    public void testRegularizedGammaQEdgeCases() {
        try {
            // throw an exception for negative 'a'
            Gamma.regularizedGammaQ(-0.5, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for complex 'x'
            Gamma.regularizedGammaQ(1.0, new Complex(1.0, 2.0));
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for 'x' < 0.0
            Gamma.regularizedGammaQ(1.0, -1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for 'a' <= 0.0
            Gamma.regularizedGammaQ(0.0, 1.0);
            assertTrue(false);
        } catch (MathIllegalArgumentException e) {
            // expected
        }

        try {
            // throw an exception for 'x' > 'a'
            Gamma.regularizedGammaQ(1.0, 2.0);
            assertTrue(false);
        } catch (MathRuntimeError e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaQ method, error cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQError() {
        // throw an exception for too many iterations
        Gamma.regularizedGammaQ(0.5, 1.0, 1e-16, 1000000);
    }
}