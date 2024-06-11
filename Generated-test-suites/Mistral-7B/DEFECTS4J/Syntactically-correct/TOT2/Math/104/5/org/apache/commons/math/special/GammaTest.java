package org.junit.tests;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double result = Gamma.logGamma(0.5);
        double expected = -0.577215664901532861;
        assertEquals(expected, result, 0.0001); // Allow small error
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        double result = Gamma.regularizedGammaP(1.5, 1.0);
        double expected = 0.906261287243461414;
        assertEquals(expected, result, 0.001); // Allow small error
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        double result = Gamma.regularizedGammaQ(1.5, 1.0);
        double expected = 0.093738712756538593;
        assertEquals(expected, result, 0.001); // Allow small error
    }

    @Test(expected = MathException.class)
    public void testLogGammaNegativeX() {
        try {
            Gamma.logGamma(-1.0);
        } catch (MathException e) {
            // Expected MathException
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaNegativeA() throws MathException {
        double x = 1.0;
        try {
            Gamma.regularizedGammaP(-1.0, x);
        } catch (MathException e) {
            // Expected MathException
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeX() throws MathException {
        double a = 1.0;
        try {
            Gamma.regularizedGammaP(a, -1.0);
        } catch (MathException e) {
            // Expected MathException
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaZeroOrNegativeA() throws MathException {
        double x = 1.0;
        try {
            Gamma.regularizedGammaP(0.0, x);
        } catch (MathException e) {
            // Expected MathException
        }

        try {
            Gamma.regularizedGammaP(-1.0, x);
        } catch (MathException e) {
            // Expected MathException
        }
    }

    @Test
    public void testRegularizedGammaPLargeAAndX() throws MathException {
        double a = 1000.0;
        double x = 1000.0;
        double result = Gamma.regularizedGammaP(a, x);
        double expected = 0.999999999999999980; // Approximate result
        assertEquals(expected, result, 0.0001); // Allow small error
    }

    @Test
    public void testRegularizedGammaPEqualAAndX() throws MathException {
        double a = 1.0;
        double x = 1.0;
        double result = Gamma.regularizedGammaP(a, x);
        double expected = 0.5;
        assertEquals(expected, result, 0.001); // Allow small error
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPXGreaterThanA() throws MathException {
        double a = 1.0;
        double x = 2.0;
        try {
            Gamma.regularizedGammaP(a, x);
        } catch (MathException e) {
            // Expected MathException
        }
    }
}