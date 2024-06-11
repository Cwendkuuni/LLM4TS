package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -5, 2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 0, 2, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, -2, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 2, -5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 2, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 5, 6);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 2, 5);
        assertEquals(2, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 2, 5);
        assertEquals(5, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 2, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        for (int i = 0; i <= 13; i++) {
            double prob = dist.cumulativeProbability(i);
            double expectedProb = FastMath.comb(12, i) * Math.pow(0.3, i) * Math.pow(0.7, 12 - i) / Math.pow(1.0, 12.0);
            assertEquals(expectedProb, prob, 0.001);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        for (int i = 0; i <= 13; i++) {
            double prob = dist.probability(i);
            double expectedProb = FastMath.comb(12, i) * Math.pow(0.3, i) * Math.pow(0.7, 12 - i) / Math.pow(1.0, 12.0);
            assertEquals(expectedProb, prob, 0.001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        for (int i = 0; i <= 13; i++) {
            double prob = dist.upperCumulativeProbability(i);
            double expectedProb = FastMath.comb(12, i + 1) * Math.pow(0.3, i) * Math.pow(0.7, 12 - i) / Math.pow(1.0, 12.0);
            assertEquals(expectedProb, prob, 0.001);
        }
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        double expectedMean = 3.0;
        double actualMean = dist.getNumericalMean();
        assertEquals(expectedMean, actualMean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        double expectedVariance = 1.481394156136;
        double actualVariance = dist.getNumericalVariance();
        assertEquals(expectedVariance, actualVariance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        int expectedLowerBound = 1;
        int actualLowerBound = dist.getSupportLowerBound();
        assertEquals(expectedLowerBound, actualLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        int expectedUpperBound = 5;
        int actualUpperBound = dist.getSupportUpperBound();
        assertEquals(expectedUpperBound, actualUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 15);
        assertTrue(dist.isSupportConnected());
    }
}