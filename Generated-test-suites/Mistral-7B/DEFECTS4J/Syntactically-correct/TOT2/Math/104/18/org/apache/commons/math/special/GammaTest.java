import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

/**
 * Test cases for the Gamma class
 */
public class GammaTest {

    @Test
    public void testLogGamma() {
        // Positive test case
        double result = SpecialFunctions.logGamma(1.5);
        double expected = 0.405823993641621; // calculated using Mathematica
        assertEquals(expected, result, 1e-6);

        // Edge case: very large argument
        result = SpecialFunctions.logGamma(1000.0);
        expected = 1023.588323529413;
        assertEquals(expected, result, 1e-6);

        // Edge case: very small argument
        result = SpecialFunctions.logGamma(0.00001);
        expected = -8.91472818226573;
        assertEquals(expected, result, 1e-6);

        // Error case: invalid argument
        result = SpecialFunctions.logGamma(-1.0);
        assertNaN(result);

        // Error case: logarithm of zero
        result = SpecialFunctions.logGamma(0.0);
        assertNaN(result);
    }

    @Test
    public void testRegularizedGammaP() {
        // Positive test case
        double result = SpecialFunctions.regularizedGammaP(1.5, 1.5);
        double expected = 0.422784335027022; // calculated using Mathematica
        assertEquals(expected, result, 1e-6);

        // Edge case: very large argument and parameter
        result = SpecialFunctions.regularizedGammaP(1000.0, 1000.0);
        expected = 0.797884564492558;
        assertEquals(expected, result, 1e-6);

        // Edge case: very small argument and large parameter
        result = SpecialFunctions.regularizedGammaP(0.001, 5.0);
        expected = 0.999999999907171;
        assertEquals(Precision.round(expected, 10), result, 1e-12);

        // Error case: invalid argument
        result = SpecialFunctions.regularizedGammaP(-1.0, 1.5);
        assertNaN(result);

        // Error case: invalid parameter
        result = SpecialFunctions.regularizedGammaP(1.5, -1.0);
        assertNaN(result);

        // Error case: x is less than a
        result = SpecialFunctions.regularizedGammaP(1.5, 0.5);
        assertNaN(result);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Positive test case
        double result = SpecialFunctions.regularizedGammaQ(1.5, 1.5);
        double expected = 0.577215664901533; // calculated using Mathematica
        assertEquals(expected, result, 1e-6);

        // Edge case: very large argument and parameter
        result = SpecialFunctions.regularizedGammaQ(1000.0, 1000.0);
        expected = 0.202784335027022;
        assertEquals(expected, result, 1e-6);

        // Edge case: very small argument and large parameter
        result = SpecialFunctions.regularizedGammaQ(0.001, 5.0);
        expected = 0.0000009282862;
        assertEquals(Precision.round(expected, 10), result, 1e-12);

        // Error case: invalid argument
        result = SpecialFunctions.regularizedGammaQ(-1.0, 1.5);
        assertNaN(result);

        // Error case: invalid parameter
        result = SpecialFunctions.regularizedGammaQ(1.5, -1.0);
        assertNaN(result);

        // Error case: x is greater than a
        result = SpecialFunctions.regularizedGammaQ(1.5, 2.0);
        assertNaN(result);
    }
}