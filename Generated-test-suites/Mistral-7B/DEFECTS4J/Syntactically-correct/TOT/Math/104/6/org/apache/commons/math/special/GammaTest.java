package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid input
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-12);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-12);
        assertEquals(0.17006444, Gamma.logGamma(2.0), 1e-12);
        assertEquals(1.04284854, Gamma.logGamma(3.0), 1e-12);
        assertEquals(1.31131721, Gamma.logGamma(4.0), 1e-12);
        assertEquals(1.42142741, Gamma.logGamma(5.0), 1e-12);

        // Test with negative input
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));

        // Test with large input
        assertEquals(1.70146256, Gamma.logGamma(100.0), 1e-12);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid input
        assertEquals(0.0, Gamma.regularizedGammaP(0.5, 0.0), 1e-12);
        assertEquals(0.5, Gamma.regularizedGammaP(1.5, 1.0), 1e-12);
        assertEquals(0.83276149, Gamma.regularizedGammaP(2.5, 2.0), 1e-12);
        assertEquals(0.91828714, Gamma.regularizedGammaP(3.5, 3.0), 1e-12);
        assertEquals(0.99217776, Gamma.regularizedGammaP(4.5, 4.0), 1e-12);

        // Test with negative input
        assertNaN(Gamma.regularizedGammaP(-1.5, 1.0));
        assertNaN(Gamma.regularizedGammaP(0.5, -1.0));

        // Test with complex input
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1.5, 1.5)));
        assertEquals(0.91828714, Gamma.regularizedGammaP(3.5, 3.0), 1e-12);

        // Test with large input
        assertEquals(0.9999999999999984, Gamma.regularizedGammaP(100.5, 100.0), 1e-12);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPConvergenceFailure() {
        Gamma.regularizedGammaP(0.5, 0.5, 1e-15, 10000);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid input
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.0), 1e-12);
        assertEquals(0.5, Gamma.regularizedGammaQ(1.5, 1.0), 1e-12);
        assertEquals(0.16726755, Gamma.regularizedGammaQ(2.5, 2.0), 1e-12);
        assertEquals(0.11111111, Gamma.regularizedGammaQ(3.5, 3.0), 1e-12);
        assertEquals(0.099999999856, Gamma.regularizedGammaQ(4.5, 4.0), 1e-12);

        // Test with negative input
        assertNaN(Gamma.regularizedGammaQ(-1.5, 1.0));
        assertNaN(Gamma.regularizedGammaQ(0.5, -1.0));

        // Test with complex input
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1.5, 1.5)));
        assertEquals(0.88888888, Gamma.regularizedGammaQ(3.5, 3.0), 1e-12);

        // Test with large input
        assertEquals(1.0000000000000003, Gamma.regularizedGammaQ(100.5, 100.0), 1e-12);
    }
}