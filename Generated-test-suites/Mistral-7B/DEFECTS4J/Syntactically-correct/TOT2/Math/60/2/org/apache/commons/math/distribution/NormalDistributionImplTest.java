package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerators;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test constructor with given mean and standard deviation.
     */
    @Test
    public void testNormalDistributionImplWithGivenMeanAndStandardDeviation() {
        final double mean = 0.5;
        final double standardDeviation = 1.0;
        final NormalDistribution normalDist = new NormalDistributionImpl(mean, standardDeviation);
        assertEquals(mean, normalDist.getMean(), 0.0);
        assertEquals(standardDeviation, normalDist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with given mean, standard deviation, and inverse cumulative probability accuracy.
     */
    @Test
    public void testNormalDistributionImplWithGivenMeanStandardDeviationAndInverseCumulativeProbabilityAccuracy() {
        final double mean = 0.5;
        final double standardDeviation = 1.0;
        final double inverseCumulativeProbabilityAccuracy = 0.0001;
        new NormalDistributionImpl(mean, standardDeviation, inverseCumulativeProbabilityAccuracy);
    }

    /**
     * Test constructor with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplWithNegativeStandardDeviation() {
        final double mean = 0.5;
        final double standardDeviation = -1.0;
        new NormalDistributionImpl(mean, standardDeviation);
    }

    /**
     * Test method {@link NormalDistributionImpl#density(double)}.
     */
    @Test
    public void testDensity() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double expectedDensity = 0.398942280401;
        assertEquals(expectedDensity, normalDist.density(0.0), 0.00001);
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)} with valid input.
     */
    @Test
    public void testCumulativeProbabilityWithValidInput() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double expectedCumulativeProbability = 0.5;
        assertEquals(expectedCumulativeProbability, normalDist.cumulativeProbability(0.0), 0.00001);
    }

    /**
     * Test method {@link NormalDistributionImpl#cumulativeProbability(double)} with extreme input.
     */
    @Test
    public void testCumulativeProbabilityWithExtremeInput() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double expectedCumulativeProbability = 1;
        assertEquals(expectedCumulativeProbability, normalDist.cumulativeProbability(100), 0.00001);
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)} with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityWithValidInput() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double expectedValue = 0.0;
        assertEquals(expectedValue, normalDist.inverseCumulativeProbability(0.0), 0.00001);
    }

    /**
     * Test method {@link NormalDistributionImpl#inverseCumulativeProbability(double)} with extreme input.
     */
    @Test
    public void testInverseCumulativeProbabilityWithExtremeInput() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double expectedValue = Double.POSITIVE_INFINITY;
        assertEquals(expectedValue, normalDist.inverseCumulativeProbability(1.0), 0.00001);
    }

    /**
     * Test method {@link NormalDistributionImpl#sample()} with valid input.
     */
    @Test
    public void testSampleWithValidInput() {
        final NormalDistribution normalDist = new NormalDistributionImpl(0.0, 1.0);
        double sample = normalDist.sample();
        assert(sample > -3.0);
        assert(sample < 3.0);
    }
}