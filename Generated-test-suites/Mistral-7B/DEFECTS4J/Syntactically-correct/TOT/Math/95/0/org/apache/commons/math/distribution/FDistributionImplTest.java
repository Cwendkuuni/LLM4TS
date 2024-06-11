package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FServiceRegistry;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Tests {@link FDistributionImpl#FDistributionImpl(double, double)}.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // Typical use case
        double x = 1.5;
        double p = distribution.cumulativeProbability(x);
        double expected = Beta.regularizedBeta(3.0 * x / 7.0, 1.0, 2.0);
        assertEquals(expected, p, 1e-6);
        // Edge case: x = 0
        x = 0.0;
        p = distribution.cumulativeProbability(x);
        expected = 0.0;
        assertEquals(expected, p);
        // Error case: negative x
        x = -1.0;
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(x));
    }

    /**
     * Tests {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // Typical use case
        double p = 0.95;
        double x = distribution.inverseCumulativeProbability(p);
        // Manually calculate x using inverse of the cumulative distribution function
        double t = (Beta.digamma(1.0 + 0.5 * 2.0)
                - Beta.digamma(1.0 + 0.5 * 4.0))
                / (Beta.digamma(1.0 + 0.5 * 2.0 + 0.5 * 4.0)
                        - Beta.digamma(1.0 + 0.5 * 2.0));
        x *= (2.0 * 4.0) / (2.0 * 4.0 + t * (2.0 * 4.0));
        assertEquals(x, p, 1e-6);
        // Edge case: p = 0
        p = 0.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.0, x);
        // Edge case: p = 1
        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, x);
        // Error case: invalid probability
        p = 1.1;
        assertThrows(MathException.class,
                () -> distribution.inverseCumulativeProbability(p));
    }

    /**
     * Initializes the FServiceRegistry to register the FDistributionImpl.
     */
    static {
        FServiceRegistry.registerDistribution(FDistribution.class,
                FDistributionImpl.class);
    }
}