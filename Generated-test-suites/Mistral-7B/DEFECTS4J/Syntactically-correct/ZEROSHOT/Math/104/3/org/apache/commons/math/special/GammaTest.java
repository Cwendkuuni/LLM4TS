package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.MathConstants;

import org.junit.Test;

/**
 * JUnit 4 test file for the Gamma class.
 */
public class GammaTest {

    /**
     * Test of logGamma method, of class Gamma.
     */
    @Test
    public void testLogGamma() {
        for (double x = 0.0; x <= 5.0; x += 0.5) {
            double expected = MathConstants.LN_GAMMA[((int) Math.ceil(x))];
            double result = Gamma.logGamma(x);
            assertEquals(expected, result, 1e-7);
        }

        assertEquals(0.0, Gamma.logGamma(0.0), 1e-7);
        assertEquals(Double.POSITIVE_INFINITY, Gamma.logGamma(Double.POSITIVE_INFINITY), 1e-7);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(Double.NEGATIVE_INFINITY), 1e-7);
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 1e-7);
    }

    /**
     * Test of regularizedGammaP method, of class Gamma.
     */
    @Test
    public void testRegularizedGammaP() {
        for (double a = 0.1; a <= 5.0; a += 0.5) {
            for (double x = 0.0; x <= 5.0; x += 0.5) {
                double expected = MathConstants.INCOMPLETE_GAUSSIAN_PI[((int) Math.ceil(a))][((int) Math.ceil(x))];
                double result;

                try {
                    result = Gamma.regularizedGammaP(a, x);
                } catch (MathException e) {
                    assertTrue(false);
                    result = Double.NaN;
                    break;
                }

                assertEquals(expected, result, 1e-7);
            }
        }

        for (double a = 0.0; a <= 0.0; a += -0.1) {
            for (double x = 0.0; x <= a; x += 0.1) {
                double expected = MathConstants.PSI[((int) Math.ceil(a))][((int) Math.ceil(x))];
                double result;

                try {
                    result = Gamma.regularizedGammaP(a, x);
                } catch (MathException e) {
                    assertTrue(false);
                    result = Double.NaN;
                    break;
                }

                assertEquals(expected, result, 1e-7);
            }
        }
    }

    /**
     * Test of regularizedGammaQ method, of class Gamma.
     */
    @Test
    public void testRegularizedGammaQ() {
        for (double a = 0.1; a <= 5.0; a += 0.5) {
            for (double x = 0.0; x <= 5.0; x += 0.5) {
                double expected = 1.0 - MathConstants.INCOMPLETE_GAUSSIAN_PI[((int) Math.ceil(a))][((int) Math.ceil(x))];
                double result;

                try {
                    result = Gamma.regularizedGammaQ(a, x);
                } catch (MathException e) {
                    assertTrue(false);
                    result = Double.NaN;
                    break;
                }

                assertEquals(expected, result, 1e-7);
            }
        }

        for (double a = 0.0; a <= 0.0; a -= -0.1) {
            for (double x = a; x <= 5.0; x += 0.1) {
                double expected = 1.0 - MathConstants.PSI[((int) Math.ceil(a))][((int) Math.ceil(x))];
                double result;

                try {
                    result = Gamma.regularizedGammaQ(a, x);
                } catch (MathException e) {
                    assertTrue(false);
                    result = Double.NaN;
                    break;
                }

                assertEquals(expected, result, 1e-7);
            }
        }
    }
}