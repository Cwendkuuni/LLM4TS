package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    /**
     * Test log gamma function
     */
    @Test
    public void testLogGamma() {
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double result = Gamma.logGamma(x);
            double expected = Math.log(fact(x));
            assertEquals("Error in logGamma for x = " + x, expected, result, 1e-5);
        }

        // test with negative and non-numeric values
        assertEquals("Error in logGamma for x = -1.0", Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals("Error in logGamma for x = NaN", Double.NaN, Gamma.logGamma(Double.NaN), 0.0);

        try {
            Gamma.logGamma(0.0);
            fail("Expected MathException for logGamma(0.0)");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.logGamma(-5.0);
            fail("Expected MathException for logGamma(-5.0)");
        } catch (MathException e) {
            // expected
        }

        try {
            Gamma.logGamma("test");
            fail("Expected MathException for logGamma(String)");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma P function
     */
    @Test
    public void testRegularizedGammaP() {
        for (double a = 0.5; a <= 5.0; a += 0.5) {
            for (double x = 0.0; x <= 3.0; x += 0.1) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = pFunction(a, x);
                assertEquals("Error in regularizedGammaP for a = " + a + " and x = " + x, expected, result, 1e-5);
            }
        }

        // test with negative a and non-numeric values
        for (double a = -1.0; a <= 0.0; a += -0.1) {
            try {
                Gamma.regularizedGammaP(a, 0.5);
                fail("Expected MathException for regularizedGammaP(a, x) with negative a");
            } catch (MathException e) {
                // expected
            }
        }

        for (double x = -0.1; x <= 0.0; x += -0.1) {
            try {
                Gamma.regularizedGammaP(1.0, x);
                fail("Expected MathException for regularizedGammaP(a, x) with x <= 0.0");
            } catch (MathException e) {
                // expected
            }
        }

        for (double a = 0.0; a <= 5.0; a += 0.5) {
            try {
                Gamma.regularizedGammaP(a, -0.5);
                fail("Expected MathException for regularizedGammaP(a, x) with negative x");
            } catch (MathException e) {
                // expected
            }
        }

        try {
            Gamma.regularizedGammaP("test", 0.5);
            fail("Expected MathException for regularizedGammaP(String, double)");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test regularized gamma Q function
     */
    @Test
    public void testRegularizedGammaQ() {
        for (double a = 0.5; a <= 5.0; a += 0.5) {
            for (double x = 0.0; x <= 3.0; x += 0.1) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = qFunction(a, x);
                assertEquals("Error in regularizedGammaQ for a = " + a + " and x = " + x, expected, result, 1e-5);
            }
        }

        // test with negative a and non-numeric values
        for (double a = -1.0; a <= 0.0; a += -0.1) {
            try {
                Gamma.regularizedGammaQ(a, 0.5);
                fail("Expected MathException for regularizedGammaQ(a, x) with negative a");
            } catch (MathException e) {
                // expected
            }
        }

        for (double x = -0.1; x <= 0.0; x += -0.1) {
            try {
                Gamma.regularizedGammaQ(1.0, x);
                fail("Expected MathException for regularizedGammaQ(a, x) with x <= 0.0");
            } catch (MathException e) {
                // expected
            }
        }

        for (double a = 0.0; a <= 5.0; a += 0.5) {
            try {
                Gamma.regularizedGammaQ(a, -0.5);
                fail("Expected MathException for regularizedGammaQ(a, x) with negative x");
            } catch (MathException e) {
                // expected
            }
        }

        try {
            Gamma.regularizedGammaQ("test", 0.5);
            fail("Expected MathException for regularizedGammaQ(String, double)");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Helper function for log gamma test
     *
     * @param x value
     * @return factorial value
     */
    private double fact(double x) {
        double result = 1.0;
        for (int i = 1; i <= (int) x; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Helper function for regularized gamma P test
     *
     * @param a a parameter
     * @param x value
     * @return function value
     */
    private double pFunction(double a, double x) {
        double gamma = fact(2 * a);
        double num = Math.gamma(a + x);
        double den = Math.pow(Math.PI, a) * Math.gamma(a);
        return gamma / (num * den);
    }

    /**
     * Helper function for regularized gamma Q test
     *
     * @param a a parameter
     * @param x value
     * @return function value
     */
    private double qFunction(double a, double x) {
        return 1.0 - pFunction(a, x);
    }
}