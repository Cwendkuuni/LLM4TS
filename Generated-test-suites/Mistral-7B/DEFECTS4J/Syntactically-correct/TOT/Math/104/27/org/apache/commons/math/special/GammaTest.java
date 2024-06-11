package org.apache.commons.math.special.test;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;

public class GammaTest extends TestCase {

    /**
     * Test of logGamma method, regular case.
     */
    public void testLogGamma() {
        assertEquals("log gamma(1) should be 0", 0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals("log gamma(2) should be log(sqrt(pi))", 0.918295834, Gamma.logGamma(2.0), 1e-10);
        assertEquals("log gamma(3.5) should be ~0.8989", 0.898969711, Gamma.logGamma(3.5), 1e-10);
    }

    /**
     * Test of logGamma method, negative argument.
     */
    public void testLogGammaNegative() {
        try {
            Gamma.logGamma(-1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of logGamma method, non-numeric argument.
     */
    public void testLogGammaNaN() {
        try {
            Gamma.logGamma(Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of logGamma method, very small argument.
     */
    public void testLogGammaSmall() {
        double result = Gamma.logGamma(1e-10);
        double expected = -0.042059833 + logGamma(1.0) - log(1e-10);
        assertEquals("log gamma(1e-10) should be " + expected, expected, result, 1e-12);
    }

    /**
     * Test of regularizedGammaP method, regular case.
     */
    public void testRegularizedGammaP() {
        assertEquals("P(1, 1) = gamma(1) = 1", 1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals("P(0.5, 0.5) = sqrt(pi) / 2", 0.612743283, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals("P(3, 2) = gamma(3) / gamma(2) = 2", 2.0, Gamma.regularizedGammaP(3.0, 2.0), 1e-10);
        assertEquals("P(2, 2.5) = ~1.5875", 1.587493837, Gamma.regularizedGammaP(2.0, 2.5), 1e-10);
    }

    /**
     * Test of regularizedGammaP method, negative argument.
     */
    public void testRegularizedGammaPNegative() {
        try {
            Gamma.regularizedGammaP(-1.0, 1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaP(1.0, -1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaP method, non-numeric arguments.
     */
    public void testRegularizedGammaPNan() {
        try {
            Gamma.regularizedGammaP(Double.NaN, Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaP(1.0, Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaP(Double.NaN, 1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaP method, very large x.
     */
    public void testRegularizedGammaPLarge() {
        double x = 1e10;
        assertEquals("P(2, 1e10) should be ~1.225e-3", 1.224744871e-3, Gamma.regularizedGammaP(2.0, x), 1e-12);
    }

    /**
     * Test of regularizedGammaP method, very small a.
     */
    public void testRegularizedGammaPSmall() {
        double a = 1e-10;
        double x = 1.0;
        assertEquals("P(1e-10, 1) should be ~1.243e-11", 1.242857138e-11, Gamma.regularizedGammaP(a, x), 1e-13);
    }

    /**
     * Test of regularizedGammaQ method, regular case.
     */
    public void testRegularizedGammaQ() {
        assertEquals("Q(1, 1) = 1 - P(1, 1) = 0", 0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals("Q(0.5, 0.5) = 1 - sqrt(pi) / 2 = 0.66666667", 0.666666667, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals("Q(3, 2) = gamma(2) / gamma(3) = 1 / 2", 0.5, Gamma.regularizedGammaQ(3.0, 2.0), 1e-10);
        assertEquals("Q(2, 2.5) = ~0.4125", 0.412506163, Gamma.regularizedGammaQ(2.0, 2.5), 1e-10);
    }

    /**
     * Test of regularizedGammaQ method, negative argument.
     */
    public void testRegularizedGammaQNegative() {
        try {
            Gamma.regularizedGammaQ(-1.0, 1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaQ(1.0, -1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaQ method, non-numeric arguments.
     */
    public void testRegularizedGammaQNaN() {
        try {
            Gamma.regularizedGammaQ(Double.NaN, Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaQ(1.0, Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaQ(Double.NaN, 1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGammaQ method, very large x.
     */
    public void testRegularizedGammaQLarge() {
        double x = 1e10;
        assertEquals("Q(2, 1e10) should be ~1.225e-3", 1.224744871e-3, Gamma.regularizedGammaQ(2.0, x), 1e-12);
    }

    /**
     * Test of regularizedGammaQ method, very small a.
     */
    public void testRegularizedGammaQSmall() {
        double a = 1e-10;
        double x = 1.0;
        assertEquals("Q(1e-10, 1) should be ~1.243e-11", 1.242857138e-11, Gamma.regularizedGammaQ(a, x), 1e-13);
    }

    /**
     * Test of regularizedGammaQ method, x = a.
     */
    public void testRegularizedGammaQEqual() {
        for (double a = 0.5; a <= 5.0; a += 0.5) {
            assertEquals("Q(" + a + ", " + a + ") = 0", 0.0, Gamma.regularizedGammaQ(a, a), 1e-10);
        }
    }

    /**
     * Test of regularizedGamma method, regular case.
     */
    public void testRegularizedGamma() {
        assertEquals("gamma(1) = 1", 1.0, Gamma.gamma(1.0), 1e-10);
        assertEquals("gamma(2) = sqrt(pi)", Math.sqrt(Math.PI), Gamma.gamma(2.0), 1e-10);
        assertEquals("gamma(3) = 2!", 2.0, Gamma.gamma(3.0), 1e-10);
        assertEquals("gamma(3.5) = 3.5!", 3.589825013, Gamma.gamma(3.5), 1e-10);
    }

    /**
     * Test of regularizedGamma method, negative argument.
     */
    public void testRegularizedGammaNegative() {
        try {
            Gamma.gamma(-1.0);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGamma method, non-numeric argument.
     */
    public void testRegularizedGammaNaN() {
        try {
            Gamma.gamma(Double.NaN);
            fail("Exception expected");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test of regularizedGamma method, very large argument.
     */
    public void testRegularizedGammaLarge() {
        double x = 1e10;
        assertEquals("gamma(1000, 1e10) should be ~1.54e-48", 1.538159805e-48, Gamma.gamma(1000.0, x), 1e-51);
    }

    /**
     * Test of regularizedGamma method, very small argument.
     */
    public void testRegularizedGammaSmall() {
        double x = 1e-10;
        assertEquals("gamma(0.5, 1e-10) should be ~8.31e-9", 8.312384611e-9, Gamma.gamma(0.5, x), 1e-12);
    }

    /**
     * Test of regularizedGamma method, x = a.
     */
    public void testRegularizedGammaEqual() {
        for (double a = 0.5; a <= 5.0; a += 0.5) {
            assertEquals("gamma(" + a + ", " + a + ") = infinity", Double.POSITIVE_INFINITY, Gamma.gamma(a, a), Double.POSITIVE_INFINITY);
        }
    }

    /**
     * Test of regularizedGamma method, max iterations exceeded.
     */
    public void testRegularizedGammaMaxIterations() {
        try {
            Gamma.regularizedGammaP(1.5, 10.0, 1e-15, 10);
            fail("MaxIterationsExceededException expected");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaQ(1.5, 10.0, 1e-15, 10);
            fail("MaxIterationsExceededException expected");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }
}