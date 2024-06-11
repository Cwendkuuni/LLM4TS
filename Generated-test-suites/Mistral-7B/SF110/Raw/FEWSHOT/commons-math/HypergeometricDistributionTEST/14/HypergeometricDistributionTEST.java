package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), -2, 1, 1);
            fail();
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), 0, 1, 1);
            fail();
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), 2, -1, 1);
            fail();
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionZeroNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), 2, 0, 1);
            fail();
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), 2, 1, -1);
            fail();
        } catch (NotPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), 2, 1, 3);
            fail();
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test
    public void testHypergeometricDistributionValidConstruction() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(3, dist.getNumberOfSuccesses());
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        int[] domain = dist.getDomain();
        assertEquals(0, domain[0]);
        assertEquals(3, domain[1]);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.03691136676572189, dist.cumulativeProbability(1), 0.001);
        assertEquals(0.1715726938422171, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.3593336590123533, dist.cumulativeProbability(3), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        int[] domain = dist.getDomain();
        assertEquals(0.03691136676572189, dist.probability(1), 0.001);
        assertEquals(0.1715726938422171, dist.probability(2), 0.001);
        assertEquals(0.3593336590123533, dist.probability(3), 0.001);
        assertEquals(0.0, dist.probability(4), 0.001);
        assertEquals(0.0, dist.probability(6), 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        int[] domain = dist.getDomain();
        assertEquals(1.0, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.001);
        assertEquals(0.0, dist.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(2, dist.getSupportLowerBound());
        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 3, 5);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(0.01122216033183669, numericalVariance, 0.001);
        assertEquals(numericalVariance, dist.getNumericalVariance(), 0.001);
    }
}