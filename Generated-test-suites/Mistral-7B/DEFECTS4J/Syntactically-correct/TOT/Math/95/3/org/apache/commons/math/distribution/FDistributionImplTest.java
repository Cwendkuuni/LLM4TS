package org.apache.commons.math.distribution;

import junit.framework.TestCase;
import org.apache.commons.math3.special.Beta;

public class FDistributionImplTest extends TestCase {

    private static final double DELTA = 1e-12;

    private FDistributionImpl distribution;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        distribution = new FDistributionImpl(2.0, 4.0);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * with valid degrees of freedom.
     */
    public void testFDistributionImpl() {
        assertEquals("Unexpected degrees of freedom.", 2.0, distribution.getNumeratorDegreesOfFreedom());
        assertEquals("Unexpected degrees of freedom.", 4.0, distribution.getDenominatorDegreesOfFreedom());
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with valid x.
     */
    public void testCumulativeProbabilityValidX() throws MathException {
        double x = 1.5;
        double cdf = distribution.cumulativeProbability(x);
        double expectedCdf = Beta.regularizedBeta((2.0 * x) / (4.0 + 2.0 * x), 1.0, 2.0);
        assertEquals("Unexpected CDF value for x = " + x, expectedCdf, cdf, DELTA);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with x <= 0.
     */
    public void testCumulativeProbabilityXLE0() throws MathException {
        double x = -1.0;
        double cdf = distribution.cumulativeProbability(x);
        assertEquals("Unexpected CDF value for x = " + x, 0.0, cdf, DELTA);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with x > 0 and x is too large.
     */
    public void testCumulativeProbabilityXTooLarge() throws MathException {
        double x = 100.0;
        try {
            distribution.cumulativeProbability(x);
            fail("Expected MathException.");
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with valid p.
     */
    public void testInverseCumulativeProbabilityValidP() throws MathException {
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        double expectedX = Beta.incompleteBeta(0.95, 2.0) / 2.0;
        assertEquals("Unexpected inverse CDF value for p = " + p, expectedX, x, DELTA);
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with p < 0.
     */
    public void testInverseCumulativeProbabilityPLessThan0() {
        double p = -1.0;
        try {
            distribution.inverseCumulativeProbability(p);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with p > 1.
     */
    public void testInverseCumulativeProbabilityPGreaterThan1() throws MathException {
        double p = 1.1;
        try {
            distribution.inverseCumulativeProbability(p);
            fail("Expected MathException.");
        } catch (MathException e) {
            // expected
        }
    }
}