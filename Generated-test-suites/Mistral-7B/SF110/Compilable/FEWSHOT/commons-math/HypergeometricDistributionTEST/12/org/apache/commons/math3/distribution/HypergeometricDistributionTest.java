package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionWithNegativePopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), -7, 3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionWithNonPositiveSampleSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 3, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 3, 6);
    }

    @Test
    public void testHypergeometricDistributionConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(6, dist.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(0.3846153846153846, dist.probability(3), 0.0000001);
        assertEquals(0.182254037844386, dist.probability(5), 0.0000001);
        assertEquals(0.0304347826086957, dist.probability(7), 0.0000001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0000001);
        assertEquals(0.03846153846153846, dist.cumulativeProbability(1), 0.0000001);
        assertEquals(0.11764705882352942, dist.cumulativeProbability(3), 0.0000001);
        assertEquals(0.4807317158596045, dist.cumulativeProbability(5), 0.0000001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0000001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(0.9615384615384616, dist.upperCumulativeProbability(2), 0.0000001);
        assertEquals(0.9999999999999999, dist.upperCumulativeProbability(5), 0.0000001);
        assertEquals(1.0, dist.upperCumulativeProbability(6), 0.0000001);
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(5.0, dist.getNumericalMean(), 0.0000001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(0.024777777777777777, dist.getNumericalVariance(), 0.0000001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertEquals(1, dist.getSupportUpperBound());
        assertEquals(-1, dist.getSupportLowerBound());
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 6);
        assertTrue(dist.isSupportConnected());
    }
}