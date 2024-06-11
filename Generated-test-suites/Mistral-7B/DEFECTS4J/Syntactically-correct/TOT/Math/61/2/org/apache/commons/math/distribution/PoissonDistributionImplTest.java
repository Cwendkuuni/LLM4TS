package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.util.Precision;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            double prob = dist.probability(i);
            double expected = Math.exp(-1.0) * Math.pow(1.0, i) / Math.sqrt(2.0 * Math.PI);
            assertEquals("Probability test failed for i = " + i, expected, prob, 1e-6);
        }
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 5; i++) {
            double cdf = dist.cumulativeProbability(i);
            double expected = Gamma.regularizedGammaP(i + 1, 1.0);
            assertEquals("Cumulative probability test failed for i = " + i, Precision.round(expected, 7), cdf, 1e-6);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityNegativeInput() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        dist.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityMaxInput() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        dist.cumulativeProbability(Integer.MAX_VALUE);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i < 10; i++) {
            int sample = dist.sample();
            assertEquals("Sample test failed for i = " + i, i, sample);
        }
    }

    @Test
    public void testMean() {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        assertEquals("Mean test failed", 1.0, dist.getMean(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1.0);
        for (int i = 0; i <= 5; i++) {
            double normalProb = dist.normalApproximateProbability(i);
            double expected = Math.exp(-1.0) * Math.sqrt(1.0 / Math.PI) * Math.pow(1.0 / Math.sqrt(2.0), i + 0.5) * Math.exp(i + 0.5 * Math.log(1.0 / Math.sqrt(2.0)) - (i + 0.5) * Math.log(i + 0.5) + 0.5 * Math.log(Math.PI) - 0.5);
            assertEquals("Normal approximate probability test failed for i = " + i, Precision.round(expected, 7), normalProb, 1e-6);
        }
    }
}