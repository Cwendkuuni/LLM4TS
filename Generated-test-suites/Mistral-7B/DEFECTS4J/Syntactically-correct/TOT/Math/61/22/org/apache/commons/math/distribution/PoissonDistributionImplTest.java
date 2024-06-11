package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double prob = dist.probability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 1.0);
            assertEquals(Precision.round(prob, 14), expected, 1e-14);
        }
    }

    @Test
    public void testCumulativeProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 10; i++) {
            double cdf = 0.0;
            try {
                cdf = dist.cumulativeProbability(i);
            } catch (MathException e) {
                fail("Unexpected MathException: " + e.getMessage());
            }
            double expected = Gamma.regularizedGammaQ(i + 1, 1.0);
            assertEquals(Precision.round(cdf, 14), expected, 1e-14);
        }
    }

    @Test
    public void testSample() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 100; i++) {
            int sample = dist.sample();
            assertEquals(sample, dist.inverseCumulativeProbability(FastMath.random()));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testMaxIterationsAndEpsilon() {
        PoissonDistribution dist = new PoissonDistributionImpl(1E12, 1.0e-256, 1);
        assertThrows(MathException.class, () -> dist.cumulativeProbability(1000000000));
        assertEquals(Precision.round(dist.cumulativeProbability(Integer.MAX_VALUE), 14), 1.0, 1e-14);
    }
}