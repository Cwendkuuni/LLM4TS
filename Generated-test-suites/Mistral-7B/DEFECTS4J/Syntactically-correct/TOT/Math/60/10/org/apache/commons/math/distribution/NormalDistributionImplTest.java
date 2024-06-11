package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.gauss.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test construction with positive mean and standard deviation.
     */
    @Test
    public void testConstructionPositiveParameters() {
        final double mean = 1.2;
        final double stdDev = 0.5;
        final NormalDistribution dist = new NormalDistributionImpl(mean, stdDev);
        assertEquals(mean, dist.getMean(), 0.0);
        assertEquals(stdDev, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test construction with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionNegativeStandardDeviation() {
        final double mean = 0.0;
        final double stdDev = -1.5;
        new NormalDistributionImpl(mean, stdDev);
    }

    /**
     * Test {@link NormalDistribution#density(double)}.
     */
    @Test
    public void testDensity() {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final double densityMean = dist.density(0.0);
        final double densityStd = dist.density(0.5);
        final double densityOutlier = dist.density(10.0);
        assertEquals(0.39894228040143271, densityMean, 1e-8);
        assertEquals(0.24200045782785513, densityStd, 1e-8);
        assertTrue(densityOutlier < 1e-300);
    }

    /**
     * Test {@link NormalDistribution#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final double p = 0.25;
        final double x = dist.inverseCumulativeProbability(p);
        assertEquals(0.0, x, 1e-8);
        final double p2 = dist.cumulativeProbability(x);
        assertEquals(p, p2, 1e-8);

        final double p3 = 0.99;
        final double x3 = dist.inverseCumulativeProbability(p3);
        assertEquals(3.0901639344259465, x, 1e-8);
        final double p4 = dist.cumulativeProbability(x3);
        assertEquals(p3, p4, 1e-8);
    }

    /**
     * Test {@link NormalDistribution#inverseCumulativeProbability(double)} with
     * extreme values.
     */
    @Test
    public void testInverseCumulativeProbabilityExtremeValues() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final double pMin = 0.0;
        final double xMin = dist.inverseCumulativeProbability(pMin);
        assertEquals(Double.NEGATIVE_INFINITY, xMin, 0.0);
        final double pMax = 1.0;
        final double xMax = dist.inverseCumulativeProbability(pMax);
        assertEquals(Double.POSITIVE_INFINITY, xMax, 0.0);
    }

    /**
     * Test {@link NormalDistribution#sample()} against cumulative probability.
     */
    @Test
    public void testSample() throws MathException {
        final NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
        final RandomGenerator rg = dist.getRandomGenerator();
        final double p = 0.5;
        final int numSamples = 10000;
        int count = 0;
        for (int i = 0; i < numSamples; ++i) {
            final double x = dist.sample();
            final double pX = dist.cumulativeProbability(x);
            if (pX >= p) {
                count++;
            }
        }
        final double fraction = (double) count / numSamples;
        assertTrue(fraction > p - 0.01);
        assertTrue(fraction < p + 0.01);
    }
}