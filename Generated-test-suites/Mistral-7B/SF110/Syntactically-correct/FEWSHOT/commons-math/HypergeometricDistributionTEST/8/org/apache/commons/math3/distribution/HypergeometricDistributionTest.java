package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.SaddlePointExpansion;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        assertEquals(10, hd.getPopulationSize());
        assertEquals(3, hd.getNumberOfSuccesses());
        assertEquals(5, hd.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(-5, 3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        new HypergeometricDistribution(10, 3, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(10, 3, 11);
    }

    @Test
    public void testHypergeometricDistributionDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        int[] domain = hd.getDomain();
        assertEquals(5, domain[1] - domain[0] + 1);
        assertEquals(0, domain[0]);
        assertEquals(5, domain[1]);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbabilityZero() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double cp = hd.cumulativeProbability(0);
        assertEquals(0.0, cp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbabilityLastSuccess() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double cp = hd.cumulativeProbability(3);
        double p = hd.getSampleSize() / hd.getPopulationSize();
        double q = (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize();
        double expResult = SaddlePointExpansion.logBinomialProbability(3, hd.getNumberOfSuccesses(), p, q);
        assertEquals(expResult, cp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbabilityLastFailure() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double cp = hd.cumulativeProbability(4);
        double p = hd.getSampleSize() / hd.getPopulationSize();
        double q = (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize();
        double expResult = SaddlePointExpansion.logBinomialProbability(hd.getSampleSize() - 1, hd.getPopulationSize() - hd.getNumberOfSuccesses(), p, q);
        assertEquals(expResult, cp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbabilityZero() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double p = hd.probability(0);
        assertEquals(0.0, p, 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbabilityLastSuccess() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double p = hd.probability(3);
        double p2 = SaddlePointExpansion.logBinomialProbability(3, hd.getNumberOfSuccesses(), hd.getSampleSize() / hd.getPopulationSize(), (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize());
        double expResult = Math.exp(p2);
        assertEquals(expResult, p, 0.001);
    }

    @Test
    public void testHypergeometricDistributionProbabilityLastFailure() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double p = hd.probability(4);
        double p3 = SaddlePointExpansion.logBinomialProbability(hd.getSampleSize() - 1, hd.getPopulationSize() - hd.getNumberOfSuccesses(), hd.getSampleSize() / hd.getPopulationSize(), (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize());
        double expResult = Math.exp(p3);
        assertEquals(expResult, p, 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbabilityZero() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double ucp = hd.upperCumulativeProbability(0);
        assertEquals(1.0, ucp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbabilityLastSuccess() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double ucp = hd.upperCumulativeProbability(3);
        double p = hd.getSampleSize() / hd.getPopulationSize();
        double q = (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize();
        double expResult = SaddlePointExpansion.logBinomialProbability(3, hd.getPopulationSize() - hd.getNumberOfSuccesses(), p, q);
        assertEquals(-expResult, ucp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbabilityLastFailure() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double ucp = hd.upperCumulativeProbability(5);
        double p = hd.getSampleSize() / hd.getPopulationSize();
        double q = (hd.getPopulationSize() - hd.getSampleSize()) / hd.getPopulationSize();
        double expResult = SaddlePointExpansion.logBinomialProbability(hd.getNumberOfSuccesses(), hd.getSampleSize(), p, q);
        assertEquals(-expResult, ucp, 0.001);
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double mean = hd.getNumericalMean();
        double expectedMean = hd.getSampleSize() * hd.getNumberOfSuccesses() / hd.getPopulationSize();
        assertEquals(expectedMean, mean, 0.001);
    }

    @Test
    public void testHypergeometricDistributionVarianceCalculation() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double variance = hd.getNumericalVariance();
        double expectedVariance = hd.calculateNumericalVariance();
        assertEquals(expectedVariance, variance, 0.001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        int lowerBound = hd.getSupportLowerBound();
        int upperBound = hd.getSupportUpperBound();
        int expectedLowerBound = hd.getSampleSize() + hd.getNumberOfSuccesses() - hd.getPopulationSize();
        int expectedUpperBound = hd.getNumberOfSuccesses();
        assertEquals(expectedLowerBound, lowerBound);
        assertEquals(expectedUpperBound, upperBound);
    }

    @Test
    public void testHypergeometricDistributionSupportIsConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        boolean isConnected = hd.isSupportConnected();
        assertTrue(isConnected);
    }

    @Test
    public void testHypergeometricDistributionSample() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        MersenneTwister rng = new MersenneTwister();
        for (int i = 0; i < 10000; i++) {
            int sample = hd.sample(rng, 1)[0];
            int domain[] = hd.getDomain();
            assertTrue(domain[0] <= sample && sample <= domain[1]);
        }
    }
}