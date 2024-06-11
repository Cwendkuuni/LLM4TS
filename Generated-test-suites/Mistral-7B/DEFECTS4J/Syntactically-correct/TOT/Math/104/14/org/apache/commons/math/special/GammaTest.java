package org.apache.commons.math.special.test;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with positive values
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-15);
        assertEquals(1.0, Gamma.logGamma(2.0), 1e-15);
        assertEquals(1.2020566643034941, Gamma.logGamma(3.0), 1e-15);
        assertEquals(1.4523850909490555, Gamma.logGamma(4.0), 1e-15);
        assertEquals(1.6414599623132253, Gamma.logGamma(5.0), 1e-15);

        // Test with large values
        assertEquals(2.9658482433787132, Gamma.logGamma(10.0), 1e-15);
        assertEquals(4.0142535122220853, Gamma.logGamma(20.0), 1e-15);

        // Test error scenarios
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(1.0, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(1.0, -1.0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with small positive values
        assertEquals(0.9932972903565517, Gamma.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(0.9737591375281432, Gamma.regularizedGammaP(1.0, 2.0), 1e-8);
        assertEquals(0.9991038776862648, Gamma.regularizedGammaP(1.0, 5.0), 1e-8);

        // Test with large positive values
        assertEquals(0.9182866161521574, Gamma.regularizedGammaP(1.0, 10.0), 1e-8);
        assertEquals(0.9999999990163987, Gamma.regularizedGammaP(1.0, 100.0), 1e-12);

        // Test with complex values
        assertEquals(0.831914233745208, Gamma.regularizedGammaP(1.5, 2.5), 1e-8);

        // Test error scenarios
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -1.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, 1.0, 1e-11), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, 1.0, 0), 0.0);
        try {
            Gamma.regularizedGammaP(1.0, 1.0, 1e-11, -1);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with small positive values
        assertEquals(0.0267029735026186, 1.0 - Gamma.regularizedGammaP(1.0, 1.0), 1e-8);
        assertEquals(0.036786956242197, 1.0 - Gamma.regularizedGammaP(1.0, 2.0), 1e-8);
        assertEquals(0.0, 1.0 - Gamma.regularizedGammaP(1.0, 5.0), 1e-8);

        // Test with large positive values
        assertEquals(0.0817978897969192, 1.0 - Gamma.regularizedGammaP(1.0, 10.0), 1e-8);
        assertEquals(0.999921134873935, 1.0 - Gamma.regularizedGammaP(1.0, 100.0), 1e-12);

        // Test with complex values
        assertEquals(0.1686243293333429, 1.0 - Gamma.regularizedGammaP(1.5, 2.5), 1e-8);

        // Test error scenarios
        assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP(-1.0, 1.0), 0.0);
        assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(Double.NaN, 1.0 - Gamma.regularizedGammaP(1.0, -1.0), 0.0);
        try {
            Gamma.regularizedGammaQ(1.0, 1.0, 1e-11, -1);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}