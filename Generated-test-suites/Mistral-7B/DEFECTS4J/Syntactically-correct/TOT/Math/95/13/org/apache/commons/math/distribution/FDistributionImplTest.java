package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;

/**
 * Test class for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test of {@link FDistributionImpl#FDistributionImpl(double, double)} constructor.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test of {@link FDistributionImpl#cumulativeProbability(double)} method.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);

        // typical use case
        double x = 1.5;
        double p = distribution.cumulativeProbability(x);
        assertEquals(0.9173366884803812, p, 0.0001);

        // edge case: minimum value
        x = 0.0;
        p = distribution.cumulativeProbability(x);
        assertEquals(0.0, p, 0.0);

        // edge case: maximum value
        x = Double.POSITIVE_INFINITY;
        p = distribution.cumulativeProbability(x);
        assertEquals(1.0, p, 0.0);

        // error scenario: negative degrees of freedom
        try {
            FDistributionImpl dist = new FDistributionImpl(-5.0, 10.0);
        } catch (IllegalArgumentException e) {
            // expected exception
            assertEquals("degrees of freedom must be positive.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException.");
    }

    /**
     * Test of {@link FDistributionImpl#inverseCumulativeProbability(double)} method.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);

        // typical use case
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        assertEquals(3.583123758270541, x, 0.0001);

        // edge case: minimum value
        p = 0.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 0.0);

        // edge case: maximum value
        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x, 0.0);

        // error scenario: invalid probability
        try {
            distribution.inverseCumulativeProbability(-0.1);
        } catch (IllegalArgumentException e) {
            // expected exception
            assertEquals("p must be between 0 and 1 inclusive.", e.getMessage());
            return;
        }
        fail("Expected IllegalArgumentException.");
    }
}