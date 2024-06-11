package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            new HypergeometricDistribution(-5, 3, 10);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, -3, 10);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 3, -10);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 3, 10.5F);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 3, 10, new MERSenneTwister());
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 3, 10, new MersenneTwisterFast());
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(5, 3, 10, new XORShift64Star());
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        HypergeometricDistribution hd = new HypergeometricDistribution(5, 3, 10);
        assertEquals(5, hd.getPopulationSize());
        assertEquals(3, hd.getNumberOfSuccesses());
        assertEquals(10, hd.getSampleSize());
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        for (int i = 0; i <= 10; i++) {
            double p = hd.probability(i);
            double pExpected = SaddlePointExpansion.logBinomialProbability(i, 3, hd.getSampleSize() / (double) hd.getPopulationSize(), (hd.getPopulationSize() - hd.getSampleSize()) / (double) hd.getPopulationSize());
            assertEquals(pExpected, p, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        for (int i = 0; i <= 10; i++) {
            double cpd = hd.cumulativeProbability(i);
            double cpExpected = SaddlePointExpansion.logCumulProbability(i, 3, hd.getSampleSize() / (double) hd.getPopulationSize(), (hd.getPopulationSize() - hd.getSampleSize()) / (double) hd.getPopulationSize());
            assertEquals(cpExpected, cpd, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionUpperCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        for (int i = 0; i <= 10; i++) {
            double ucp = hd.upperCumulativeProbability(i);
            double ucptExpected = SaddlePointExpansion.logCumulProbability(i + 1, 3, hd.getSampleSize() / (double) hd.getPopulationSize(), (hd.getPopulationSize() - hd.getSampleSize()) / (double) hd.getPopulationSize());
            assertEquals(ucptExpected, ucp, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionMean() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double mean = hd.getNumericalMean();
        double expectedMean = hd.getSampleSize() * hd.getNumberOfSuccesses() / hd.getPopulationSize();
        assertEquals(expectedMean, mean, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        double variance = hd.getNumericalVariance();
        double expectedVariance = hd.getSampleSize() * hd.getNumberOfSuccesses() * (hd.getPopulationSize() - hd.getSampleSize()) * (hd.getPopulationSize() - hd.getNumberOfSuccesses()) / (hd.getPopulationSize() * hd.getPopulationSize() * (hd.getPopulationSize() - 1));
        assertEquals(expectedVariance, variance, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        int lowerBound = hd.getSupportLowerBound();
        int upperBound = hd.getSupportUpperBound();
        assertEquals(-2, lowerBound);
        assertEquals(3, upperBound);
    }

    @Test
    public void testHypergeometricDistributionIsSupportConnected() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        boolean isConnected = hd.isSupportConnected();
        assertTrue(isConnected);
    }

    @Test
    public void testHypergeometricDistributionSampling() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 3, 5);
        int sampleSize = 100000;
        int[] sample = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = hd.nextInt();
        }
        int numberOfSuccessesInSample = 0;
        for (int i = 0; i < sampleSize; i++) {
            if (sample[i] == 1) {
                numberOfSuccessesInSample++;
            }
        }
        int expectedNumberOfSuccessesInSample = (int) Math.round(sampleSize * hd.getProbability(1));
        assertEquals(expectedNumberOfSuccessesInSample, numberOfSuccessesInSample);
    }
}