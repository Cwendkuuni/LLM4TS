package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test construction.
     */
    @Test
    public void testConstruction() {
        final double mean = 1.5;
        PoissonDistributionImpl dist = new PoissonDistributionImpl(mean);
        assertNotNull(dist);
        assertEquals(mean, dist.getMean(), 1e-13);
    }

    /**
     * Test probabilities for small mean.
     */
    @Test
    public void testSmallMeanProbabilities() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        assertFalse(dist.hasDensity());
        assertEquals(0.367978124274619, dist.probability(0), 1e-6);
        assertEquals(0.367924314593374, dist.probability(1), 1e-6);
        assertEquals(0.183935863123691, dist.probability(2), 1e-6);
        assertEquals(0.118216436265558, dist.probability(3), 1e-6);
        assertEquals(0.065692785386191, dist.probability(4), 1e-6);
        assertEquals(0.037311342378038, dist.probability(5), 1e-6);
    }

    /**
     * Test probabilities for large mean.
     */
    @Test
    public void testLargeMeanProbabilities() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(50);
        assertEquals(FastMath.exp(-50), dist.probability(0), 1e-13);
        assertEquals(FastMath.exp(-50) * FastMath.pow((FastMath.E), 50), dist.probability(50), 1e-13);
        for (int i = 1; i < 50; i++) {
            assertEquals(FastMath.exp(-50) * FastMath.pow((FastMath.E), 50) / Gamma.gamma(i + 1), dist.probability(i),
                    1e-7);
        }
    }

    /**
     * Test cumulative probability with small mean.
     */
    @Test
    public void testSmallMeanCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        assertEquals(0, dist.cumulativeProbability(0), 1e-13);
        assertEquals(0.367978124274619, dist.cumulativeProbability(0), 1e-13);
        assertEquals(0.632021875725381, dist.cumulativeProbability(1), 1e-13);
        assertEquals(0.999963205275501, dist.cumulativeProbability(2), 1e-13);
        assertEquals(1, dist.cumulativeProbability(3), 1e-13);
    }

    /**
     * Test cumulative probability with large mean.
     */
    @Test
    public void testLargeMeanCumulativeProbability() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(50);
        assertEquals(0, dist.cumulativeProbability(0), 1e-13);
        assertEquals(1.12033559514789e-12, dist.cumulativeProbability(1), 1e-13);
        assertEquals(0.00135103616888955, dist.cumulativeProbability(5), 1e-13);
        assertEquals(0.021792042885127, dist.cumulativeProbability(10), 1e-13);
        assertEquals(0.070608384578234, dist.cumulativeProbability(20), 1e-13);
        assertEquals(0.216718729225554, dist.cumulativeProbability(30), 1e-13);
        assertEquals(0.516540050157213, dist.cumulativeProbability(40), 1e-13);
        assertEquals(0.808864683514581, dist.cumulativeProbability(50), 1e-13);
    }

    /**
     * Test normal approximation probabilities with small mean.
     */
    @Test
    public void testSmallMeanNormalApproxProbabilities() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        NormalDistributionImpl normalDist = new NormalDistributionImpl(1, 1);
        for (int x = 0; x <= 5; x++) {
            double p = dist.probability(x);
            double approxP = normalDist.cumulativeProbability(x + 0.5);
            assertEquals(p, approxP, 1e-5);
        }
    }

    /**
     * Test normal approximation probabilities with large mean.
     */
    @Test
    public void testLargeMeanNormalApproxProbabilities() {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(50);
        NormalDistributionImpl normalDist = new NormalDistributionImpl(50, Math.sqrt(50));
        for (int x = 0; x <= 50; x++) {
            double p = dist.probability(x);
            double approxP = normalDist.cumulativeProbability(x + 0.5);
            assertEquals(p, approxP, 1e-5);
        }
    }

    /**
     * Test sample with small mean.
     */
    @Test
    public void testSmallMeanSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(1);
        for (int i = 0; i < 10000; i++) {
            int x = dist.sample();
            assertEquals(Math.min(x, 1000 * dist.getMean()), x, 1e-3);
        }
    }

    /**
     * Test sample with large mean.
     */
    @Test
    public void testLargeMeanSample() throws MathException {
        PoissonDistributionImpl dist = new PoissonDistributionImpl(50);
        for (int i = 0; i < 1000; i++) {
            int x = dist.sample();
            assertTrue(x >= 0);
            assertTrue(x <= 1000 * dist.getMean());
        }
    }

    /**
     * Test sample with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMeanSample() {
        new PoissonDistributionImpl(-1);
    }
}