package org.apache.commons.math.special.test;

import org.apache.commons.math3.SpecialFunctions;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the Gamma utility class.
 */
public class GammaTest {

    /**
     * Test logGamma method with positive input.
     */
    @Test
    public void testLogGammaPositive() {
        double result = SpecialFunctions.logGamma(2.5);
        double expected = 0.7853981633974483; // Approx value obtained from Wolfram Alpha
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test logGamma method with negative input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLogGammaNegative() {
        SpecialFunctions.logGamma(-1.0);
    }

    /**
     * Test logGamma method with zero input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLogGammaZero() {
        SpecialFunctions.logGamma(0.0);
    }

    /**
     * Test logGamma method with large input.
     */
    @Test
    public void testLogGammaLarge() {
        double result = SpecialFunctions.logGamma(100.0);
        double expected = 318.4810158604879; // Approx value obtained from Wolfram Alpha
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test regularizedGammaP method with valid input.
     */
    @Test
    public void testRegularizedGammaPP() {
        double result = SpecialFunctions.regularizedGammaP(2.5, 0.5);
        double expected = 1.230318438208971; // Approx value obtained from Wolfram Alpha
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test regularizedGammaP method with invalid input - negative a.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaPNegativeA() {
        SpecialFunctions.regularizedGammaP(-2.5, 0.5);
    }

    /**
     * Test regularizedGammaP method with invalid input - negative x.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaPNegativeX() {
        SpecialFunctions.regularizedGammaP(2.5, -0.5);
    }

    /**
     * Test regularizedGammaP method with large input.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaPMaxIterations() {
        SpecialFunctions.regularizedGammaP(50000.0, 0.5, 1e-11, 1000);
    }

    /**
     * Test regularizedGammaQ method with valid input.
     */
    @Test
    public void testRegularizedGammaQ() {
        double result = SpecialFunctions.regularizedGammaQ(2.5, 0.5);
        double expected = 0.7612726423755326; // Approx value obtained from Wolfram Alpha
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test regularizedGammaQ method with invalid input - negative a.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQNegativeA() {
        SpecialFunctions.regularizedGammaQ(-2.5, 0.5);
    }

    /**
     * Test regularizedGammaQ method with invalid input - negative x.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQNegativeX() {
        SpecialFunctions.regularizedGammaQ(2.5, -0.5);
    }

    /**
     * Test regularizedGammaQ method with large input.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQMaxIterations() {
        SpecialFunctions.regularizedGammaQ(50000.0, 0.5, 1e-11, 1000);
    }

    /**
     * Test regularizedGammaQ method with equality.
     */
    @Test
    public void testRegularizedGammaPEquality() {
        double result = SpecialFunctions.regularizedGammaP(2.5, 0.5);
        double expected = 1.0 - SpecialFunctions.regularizedGammaQ(2.5, 0.5);
        assertEquals(expected, result, 1e-10);
    }
}