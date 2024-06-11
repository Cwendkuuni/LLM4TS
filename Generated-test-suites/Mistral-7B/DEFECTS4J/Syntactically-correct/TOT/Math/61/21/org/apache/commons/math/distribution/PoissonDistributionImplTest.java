package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test constructor with valid mean.
     */
    @Test
    public void testConstructorValidMean() {
        final double mean = 1.5;
        PoissonDistribution distribution = new PoissonDistributionImpl(mean);
        assertEquals(mean, distribution.getMean(), 0.0);
    }

    /**
     * Test constructor with negative mean.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNegativeMean() {
        final double mean = -2.0;
        new PoissonDistributionImpl(mean);
    }

    /**
     * Test probability method.
     */
    @Test
    public void testProbability() {
        final PoissonDistribution distribution = new PoissonDistributionImpl(2.0);

        // Test with valid input
        assertEquals(0.1353352832306648, distribution.probability(1), 0.0000001);

        // Test with edge case
        assertEquals(0.1353352832306648, distribution.probability(Integer.MAX_VALUE / 2), 0.0000001);

        // Test with non-positive input
        assertEquals(0.0, distribution.probability(-1), 0.0);
    }

    /**
     * Test cumulative probability method.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        final PoissonDistribution distribution = new PoissonDistributionImpl(3.0);

        // Test with valid input
        assertEquals(0.04986580252651427, distribution.cumulativeProbability(1), 0.0000001);

        // Test with edge case
        assertEquals(0.9993121455865897, distribution.cumulativeProbability(Integer.MAX_VALUE / 2), 0.0000001);

        // Test with negative input
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(-1));

        // Test with non-integer input
        assertThrows(MathException.class, () -> distribution.cumulativeProbability(3.5));

        // Test convergence criterion
        PoissonDistribution distributionSmallEpsilon = new PoissonDistributionImpl(3.0, 1e-16);
        assertEquals(0.04986580252651427, distributionSmallEpsilon.cumulativeProbability(1), 1e-16);

        // Test maximum iterations
        PoissonDistribution distributionMaxIterations = new PoissonDistributionImpl(3.0, 1);
        assertEquals(0.04986580252651427, distributionMaxIterations.cumulativeProbability(1), 0.0000001);
        assertThrows(MathException.class, () -> distributionMaxIterations.cumulativeProbability(1000000));
    }

    /**
     * Test normalApproximateProbability method.
     */
    @Test
    public void testNormalApproximateProbability() throws Exception {
        final PoissonDistribution distribution = new PoissonDistributionImpl(3.0);

        // Test with valid input
        assertEquals(0.04986580252651427, distribution.normalApproximateProbability(1), 0.0000001);

        // Test with edge case
        assertEquals(0.9993121455865896, distribution.normalApproximateProbability(Integer.MAX_VALUE / 2), 0.0000001);

        // Test with non-positive input
        assertEquals(0.0, distribution.normalApproximateProbability(-1), 0.0);

        // Test with non-integer input
        assertThrows(MathRuntimeException.class, () -> distribution.normalApproximateProbability(3.5));
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws Exception {
        final PoissonDistribution distribution = new PoissonDistributionImpl(2.0);
        final int[] samples = new int[1000];

        for (int i = 0; i < 1000; i++) {
            samples[i] = distribution.sample();
        }

        for (int sample : samples) {
            assertEquals(true, distribution.probability(sample) > 0.0);
        }

        // Test convergence criterion
        PoissonDistribution distributionSmallEpsilon = new PoissonDistributionImpl(3.0, 1e-16);
        int sampleSmallEpsilon = distributionSmallEpsilon.sample();
        assertEquals(Precision.isClose(3.0, distributionSmallEpsilon.getMean(), 1e-16), true);
        assertEquals(Precision.isClose(distributionSmallEpsilon.probability(sampleSmallEpsilon), 1.0, 1e-16), true);
    }
}