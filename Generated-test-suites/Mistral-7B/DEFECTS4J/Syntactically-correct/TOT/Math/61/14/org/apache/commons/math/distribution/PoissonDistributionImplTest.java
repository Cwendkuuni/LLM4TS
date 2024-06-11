package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

/**
 * Tests for {@link PoissonDistributionImpl}.
 */
public class PoissonDistributionImplTest {

    /**
     * Test the probabilities for mean=1.
     */
    @Test
    public void testProbabilitiesMean1() {
        PoissonDistribution dist = new PoissonDistributionImpl(1);
        double[] expectedProbs = {0.36792434, 0.36792434, 0.18396915, 0.13598776, 0.06796845, 0.02231331, 0.00533482, 0.00093128, 0.00014488, 0.00002193, 0.00000316, 0.0, 0.0, 0.0, 0.0, 0.0};
        for (int i = 0; i < expectedProbs.length; i++) {
            assertEquals(expectedProbs[i], dist.probability(i), 1e-12);
        }
    }

    /**
     * Test the cumulative probabilities for mean=1.
     */
    @Test
    public void testCumulativeProbabilitiesMean1() {
        PoissonDistribution dist = new PoissonDistributionImpl(1);
        double[] expectedCumProbs = {0.0, 0.36792434, 0.73585068, 0.94178492, 0.99730020, 0.99993463, 0.99999955, 0.99999999, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        for (int i = 0; i < expectedCumProbs.length; i++) {
            assertEquals(expectedCumProbs[i], dist.cumulativeProbability(i), 1e-12);
        }
    }

    /**
     * Test the normal approximation for mean=1.
     */
    @Test
    public void testNormalApproximationMean1() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1);
        NormalDistribution normalDist = new NormalDistributionImpl(1, Math.sqrt(1));
        for (int i = 0; i < 10; i++) {
            double x = i + 0.5;
            double probDist = dist.cumulativeProbability(i);
            double probNorm = normalDist.cumulativeProbability(x);
            double probNormCorr = dist.normalApproximateProbability(i);
            assertEquals(probDist, probNorm, 1e-12);
            assertEquals(probDist, probNormCorr, 1e-12);
        }
    }

    /**
     * Test the sampling for mean=1.
     */
    @Test
    public void testSamplingMean1() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(1);
        RandomGenerator rgen = new UniformRealDistribution(0, 1);
        int sample;
        double[] expectedSamples = {0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5};
        for (int i = 0; i < expectedSamples.length; i++) {
            sample = dist.sample();
            assertEquals(expectedSamples[i], sample);
        }
    }

    /**
     * Test the probabilities for mean=100.
     */
    @Test
    public void testProbabilitiesMean100() {
        PoissonDistribution dist = new PoissonDistributionImpl(100);
        double[] expectedProbs = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.13986683, 0.22313310, 0.25876831, 0.21616514, 0.13896256, 0.08151661, 0.04158775, 0.01652188, 0.00512229, 0.00114322, 0.00019296, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        for (int i = 0; i < expectedProbs.length; i++) {
            assertEquals(expectedProbs[i], dist.probability(i), 1e-12);
        }
    }

    /**
     * Test the cumulative probabilities for mean=100.
     */
    @Test
    public void testCumulativeProbabilitiesMean100() {
        PoissonDistribution dist = new PoissonDistributionImpl(100);
        double[] expectedCumProbs = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.13986683, 0.22313310, 0.31126253, 0.36353424, 0.38735192, 0.39451432, 0.39693945, 0.39833777, 0.39914381, 0.39955857, 0.39982521, 0.40006190, 0.40025408, 0.40039806, 0.40050134, 0.40056376, 0.40060139, 0.40062187, 0.40063525, 0.40064204, 0.40064342, 0.40064052, 0.40063258, 0.40061992, 0.40059345, 0.40055553, 0.40049873, 0.40042405, 0.40033472, 0.40023270, 0.40011905, 0.39998564, 0.39983329, 0.39966713, 0.39947913, 0.39926314, 0.39899292, 0.39867277, 0.39830709, 0.39788951, 0.39741372, 0.39687478, 0.39626544, 0.39557045, 0.39477999, 0.39388705, 0.39287651, 0.39172116, 0.39040255, 0.38890510, 0.38721299, 0.38531189, 0.38318311, 0.37981604, 0.37513543, 0.36922913, 0.36206275, 0.35297877, 0.34178295, 0.32858740, 0.30741103, 0.28132878, 0.24518129, 0.20125857, 0.15321835, 0.10324248, 0.05557524, 0.02749738, 0.01032326, 0.00322960, 0.00062529, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        for (int i = 0; i < expectedCumProbs.length; i++) {
            assertEquals(expectedCumProbs[i], dist.cumulativeProbability(i), 1e-12);
        }
    }

    /**
     * Test the sampling for mean=100.
     */
    @Test
    public void testSamplingMean100() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(100);
        RandomGenerator rgen = new UniformRealDistribution(0, 1);
        int sample;
        for (int i = 0; i < 100; i++) {
            sample = dist.sample();
            assertTrue(sample >= 0 && sample <= 199);
        }
    }

    /**
     * Test the exception when the mean is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionNegativeMean() {
        new PoissonDistributionImpl(-1);
    }
}