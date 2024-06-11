package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with positive mean.
     */
    @Test
    public void testPoissonDistributionWithPositiveMean() {
        final double mean = 2.5;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(distribution.probability(0), 0.06784362341961121, 1e-6);
        assertEquals(distribution.probability(1), 0.2035325122688322, 1e-6);
        assertEquals(distribution.probability(2), 0.2706760268738582, 1e-6);
        assertEquals(distribution.probability(3), 0.2035325122688322, 1e-6);
        assertEquals(distribution.probability(4), 0.1135363357327377, 1e-6);
        assertEquals(distribution.probability(5), 0.06784362341961121, 1e-6);
        assertEquals(distribution.cumulativeProbability(0), 0.06784362341961121, 1e-6);
        assertEquals(distribution.cumulativeProbability(1), 0.3013793460251013, 1e-6);
        assertEquals(distribution.cumulativeProbability(2), 0.5752158645897524, 1e-6);
        assertEquals(distribution.cumulativeProbability(3), 0.8418335238576136, 1e-6);
        assertEquals(distribution.cumulativeProbability(4), 0.9754675337262854, 1e-6);
        assertEquals(distribution.cumulativeProbability(5), 0.999999999968328, 1e-6);

        // Test normal approximation
        assertEquals(distribution.normalApproximateProbability(0), 0.06784362341961121, 1e-6);
        assertEquals(distribution.normalApproximateProbability(1), 0.2035325122688322, 1e-6);
        assertEquals(distribution.normalApproximateProbability(2), 0.2706760268738582, 1e-6);
        assertEquals(distribution.normalApproximateProbability(3), 0.2035325122688322, 1e-6);
        assertEquals(distribution.normalApproximateProbability(4), 0.1135363357327377, 1e-6);
        assertEquals(distribution.normalApproximateProbability(5), 0.06784362341961121, 1e-6);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testPoissonDistributionWithNegativeMean() {
        final double mean = -2.5;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test constructor with zero mean.
     */
    @Test
    public void testPoissonDistributionWithZeroMean() {
        final double mean = 0.0;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
        assertEquals(distribution.probability(0), 0.3678794411714423, 1e-6);
        assertEquals(distribution.probability(1), 0.3678794411714423, 1e-6);
        assertEquals(distribution.probability(2), 0.1834397205857212, 1e-6);
        assertEquals(distribution.probability(3), 0.07589686652751314, 1e-6);
        assertEquals(distribution.probability(4), 0.01821278879441171, 1e-6);
        assertEquals(distribution.probability(5), 0.00360563157894339, 1e-6);
        assertEquals(distribution.cumulativeProbability(0), 0.3678794411714423, 1e-6);
        assertEquals(distribution.cumulativeProbability(1), 0.7357128823428546, 1e-6);
        assertEquals(distribution.cumulativeProbability(2), 0.9189660119143158, 1e-6);
        assertEquals(distribution.cumulativeProbability(3), 0.9817497261219539, 1e-6);
        assertEquals(distribution.cumulativeProbability(4), 0.9963274335805365, 1e-6);
        assertEquals(distribution.cumulativeProbability(5), 0.9998334154930383, 1e-6);

        // Test normal approximation
        assertEquals(distribution.normalApproximateProbability(0), 0.3678794411714423, 1e-6);
        assertEquals(distribution.normalApproximateProbability(1), 0.3678794411714423, 1e-6);
        assertEquals(distribution.normalApproximateProbability(2), 0.1834397205857212, 1e-6);
        assertEquals(distribution.normalApproximateProbability(3), 0.07589686652751314, 1e-6);
        assertEquals(distribution.normalApproximateProbability(4), 0.01821278879441171, 1e-6);
        assertEquals(distribution.normalApproximateProbability(5), 0.00360563157894339, 1e-6);
    }

    /**
     * Test cumulative probability with edge cases.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() {
        final double mean = 2.5;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        assertEquals(distribution.cumulativeProbability(Integer.MIN_VALUE), 0.0, 1e-6);
        assertEquals(distribution.cumulativeProbability(Integer.MAX_VALUE), 1.0, 1e-6);
    }

    /**
     * Test sample method with normal approximation.
     */
    @Test
    public void testSampleWithNormalApproximation() throws MathException {
        final double mean = 2.5;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i < 100_000; i++) {
            int sample = distribution.sample();
            int expected = distribution.sample();
            assertEquals(sample, expected);
            assertEquals(distribution.probability(sample), distribution.normalApproximateProbability(sample), 1e-6);
        }
    }

    /**
     * Test sample method with small mean.
     */
    @Test
    public void testSampleWithSmallMean() throws MathException {
        final double mean = 0.5;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i < 100_000; i++) {
            int sample = distribution.sample();
            assertEquals(sample, Math.round(mean), 0);
            assertEquals(distribution.probability(sample), 1.0 / Math.pow(2.71828, mean), 1e-6);
        }
    }

    /**
     * Test sample method with large mean.
     */
    @Test
    public void testSampleWithLargeMean() throws MathException {
        final double mean = 100.0;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        for (int i = 0; i < 100_000; i++) {
            int sample = distribution.sample();
            assertTrue(sample <= 100 * mean);
            assertEquals(distribution.probability(sample), FastMath.exp(-mean) / (FastMath.pow(FastMath.E, sample) * FastMath.sqrt(FastMath.TWO_PI * sample)), 1e-6);
        }
    }

    /**
     * Test sample method with error cases.
     */
    @Test(expected = MathException.class)
    public void testSampleWithErrorCases() throws MathException {
        final double mean = 2.5;
        PoissonDistribution<Integer> distribution = new PoissonDistributionImpl(mean);
        distribution.sample(); // this is fine
        distribution.sample(); // but the second call throws an exception
    }
}