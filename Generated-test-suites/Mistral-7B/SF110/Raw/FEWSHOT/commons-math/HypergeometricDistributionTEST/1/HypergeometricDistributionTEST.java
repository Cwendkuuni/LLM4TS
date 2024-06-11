package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorThrowExceptionWithNegativePopulationSize() {
        new HypergeometricDistribution(1, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorThrowExceptionWithNotStrictlyPositiveNumberOfSuccessesOrSampleSize() {
        new HypergeometricDistribution(1, 0, 3);
        new HypergeometricDistribution(1, 2, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorThrowExceptionWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 3, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorThrowExceptionWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 1, 3);
    }

    @Test
    public void testHypergeometricDistributionConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        assertEquals(1, dist.getPopulationSize());
        assertEquals(2, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testGetCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        int[] expectedDomain = dist.getDomain();
        for (int i = expectedDomain[0]; i <= expectedDomain[1]; i++) {
            double expected = dist.cumulativeProbability(i);
            double observed = dist.cumulativeProbability(i);
            assertEquals(expected, observed, 1e-8);
        }
    }

    @Test
    public void testGetProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        int[] expectedDomain = dist.getDomain();
        for (int i = expectedDomain[0]; i <= expectedDomain[1]; i++) {
            double expected = dist.probability(i);
            double observed = dist.probability(i);
            assertEquals(expected, observed, 1e-8);
        }
    }

    @Test
    public void testGetUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        int[] expectedDomain = dist.getDomain();
        for (int i = expectedDomain[0]; i <= expectedDomain[1]; i++) {
            double expected = dist.upperCumulativeProbability(i);
            double observed = dist.upperCumulativeProbability(i);
            assertEquals(expected, observed, 1e-8);
        }
    }

    @Test
    public void testGetSupportBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();
        assertTrue(lowerBound >= 0);
        assertTrue(upperBound >= 0);
        assertTrue(upperBound <= 2);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        assertTrue(dist.isSupportConnected());
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        double expectedMean = 2.0 * 3.0 / 1.0;
        double observedMean = dist.getNumericalMean();
        assertEquals(expectedMean, observedMean, 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        double expectedVariance = 2.0 * 3.0 * (1.0 - 3.0) * (1.0 - 2.0) / (1.0 * 1.0 * (1.0 - 1.0));
        double observedVariance = dist.getNumericalVariance();
        assertEquals(expectedVariance, observedVariance, 1e-8);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        double expectedVariance = 2.0 * 3.0 * (1.0 - 3.0) * (1.0 - 2.0) / (1.0 * 1.0 * (1.0 - 1.0));
        dist.calculateNumericalVariance();
        double observedVariance = dist.getNumericalVariance();
        assertEquals(expectedVariance, observedVariance, 1e-8);
    }

    @Test
    public void testGetDistribution() {
        HypergeometricDistribution dist = new HypergeometricDistribution(1, 2, 3);
        int numberOfTrials = 100_000;
        double[] observedValues = new double[numberOfTrials];
        for (int i = 0; i < numberOfTrials; i++) {
            observedValues[i] = dist.sample();
        }
        StandardDeviation stdDev = new StandardDeviation(observedValues);
        double observedVariance = stdDev.getResult();
        double expectedVariance = 2.0 * 3.0 * (1.0 - 3.0) * (1.0 - 2.0) / (1.0 * 1.0 * (1.0 - 1.0));
        assertEquals(expectedVariance, observedVariance, 1e-3);
    }
}