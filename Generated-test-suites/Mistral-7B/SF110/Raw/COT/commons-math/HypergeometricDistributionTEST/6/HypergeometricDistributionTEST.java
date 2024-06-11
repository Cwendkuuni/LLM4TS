package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 5, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, 0, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 5, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 11, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(2, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbabilityWithValidX() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.cumulativeProbability(1);
        assertTrue(prob > 0.0 && prob <= 0.1);
    }

    @Test
    public void testCumulativeProbabilityWithInvalidXBelowLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.cumulativeProbability(-1);
        assertEquals(0.0, prob, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithInvalidXAboveUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.cumulativeProbability(7);
        assertEquals(1.0, prob, 0.001);
    }

    @Test
    public void testProbabilityWithValidX() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.probability(1);
        assertTrue(prob > 0.0 && prob <= 0.1);
    }

    @Test
    public void testProbabilityWithInvalidXBelowLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.probability(0);
        assertEquals(0.0, prob, 0.001);
    }

    @Test
    public void testProbabilityWithInvalidXAboveUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.probability(3);
        assertEquals(0.0, prob, 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityWithValidX() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.upperCumulativeProbability(1);
        assertTrue(prob > 0.0 && prob <= 0.1);
    }

    @Test
    public void testUpperCumulativeProbabilityWithInvalidXBelowLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.upperCumulativeProbability(0);
        assertEquals(1.0, prob, 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityWithInvalidXAboveUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        double prob = distribution.upperCumulativeProbability(3);
        assertEquals(0.0, prob, 0.001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(0.1111111111111111, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(-3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 2);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testCalculateProbabilitiesUsingRandomSampling() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 1000;
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
        int[] probabilities = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            probabilities[i] = distribution.nextInt();
        }

        double expectedProbability = numberOfSuccesses * 1.0 / populationSize;
        for (int i = 0; i < sampleSize; i++) {
            double calculatedProbability = (i + 1) * 1.0 / sampleSize;
            double difference = Math.abs(calculatedProbability - expectedProbability);
            assertTrue(difference < 0.01);
        }
    }
}