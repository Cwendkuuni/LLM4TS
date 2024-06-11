package org.apache.commons.math3.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;

public class HypergeometricDistributionTest {

    @org.junit.Test
    public void testConstructor_validParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(5, distribution.getSampleSize());
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructor_populationSizeLessThanZero() {
        new HypergeometricDistribution(-1, 5, 5);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_numberOfSuccessesLessThanZero() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructor_sampleSizeLessThanZero() {
        new HypergeometricDistribution(10, 5, -1);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_numberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_sampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @org.junit.Test
    public void testCumulativeProbability_validParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int[] domain = distribution.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = i * 1.0 / 10.0;
            double actual = distribution.cumulativeProbability(i);
            assertEquals(expected, actual, 1e-13);
        }
    }

    @org.junit.Test
    public void testCumulativeProbability_outOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int[] domain = distribution.getDomain();
        assertEquals(0.0, distribution.cumulativeProbability(domain[0] - 1), 1e-13);
        assertEquals(1.0, distribution.cumulativeProbability(domain[1] + 1), 1e-13);
    }

    @org.junit.Test
    public void testProbability_validParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        for (int i = 0; i <= 10; i++) {
            double expected = FastMath.binomialProbability(5, 5.0 / 10.0);
            double actual = distribution.probability(i);
            assertEquals(expected, actual, 1e-13);
        }
    }

    @org.junit.Test
    public void testProbability_outOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.0, distribution.probability(-1), 1e-13);
        assertEquals(0.0, distribution.probability(11), 1e-13);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability_validParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int[] domain = distribution.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = 1.0 - FastMath.binomialProbability(5, 5.0 / 10.0);
            double actual = distribution.upperCumulativeProbability(i);
            assertEquals(expected, actual, 1e-13);
        }
    }

    @org.junit.Test
    public void testUpperCumulativeProbability_outOfRange() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int[] domain = distribution.getDomain();
        assertEquals(0.0, distribution.upperCumulativeProbability(domain[0] - 1), 1e-13);
        assertEquals(1.0, distribution.upperCumulativeProbability(domain[1] + 1), 1e-13);
    }

    @org.junit.Test
    public void testGetLowerDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int lowerDomain = distribution.getLowerDomain(10, 5, 5);
        assertEquals(5, lowerDomain);
    }

    @org.junit.Test
    public void testGetUpperDomain() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int upperDomain = distribution.getUpperDomain(10, 5);
        assertEquals(5, upperDomain);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        double numericalMean = distribution.getNumericalMean();
        assertEquals(5.0, numericalMean, 1e-13);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        double numericalVariance = distribution.getNumericalVariance();
        assertEquals(0.02916666666666667, numericalVariance, 1e-13);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int supportLowerBound = distribution.getSupportLowerBound();
        assertEquals(0, supportLowerBound);
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        int supportUpperBound = distribution.getSupportUpperBound();
        assertEquals(5, supportUpperBound);
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertTrue(distribution.isSupportConnected());
    }

    @org.junit.Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        double numericalVariance = distribution.calculateNumericalVariance();
        assertEquals(0.02916666666666667, numericalVariance, 1e-13);
    }

    @org.junit.Test
    public void testSample() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        MersenneTwister rng = new MersenneTwister();
        int sampleValue = distribution.sample(rng);
        int[] domain = distribution.getDomain();
        assertTrue(domain[0] <= sampleValue && sampleValue <= domain[1]);
    }

}