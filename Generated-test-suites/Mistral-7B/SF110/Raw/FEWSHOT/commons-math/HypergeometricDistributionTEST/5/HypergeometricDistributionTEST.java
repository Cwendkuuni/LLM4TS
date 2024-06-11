package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.RandomUtils;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 10, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, -1, 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 10, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 11, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 10, 11);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int[] domain = hDist.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double p = hDist.cumulativeProbability(x);
            double pExpected = hDist.probability(x);
            assertEquals(pExpected, p, 0.0001);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);

        for (int x = 0; x <= 15; x++) {
            double p = hDist.probability(x);
            double pExpected = hDist.cumulativeProbability(x + 1) - hDist.cumulativeProbability(x);
            assertEquals(pExpected, p, 0.0001);
        }
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        assertEquals(5, hDist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        assertEquals(10, hDist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        assertEquals(15, hDist.getSampleSize());
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int[] domain = hDist.getDomain();

        assertEquals(5, domain[0]);
        assertEquals(15, domain[1]);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int lowerDomain = hDist.getLowerDomain(10, 5, 15);

        assertEquals(5, lowerDomain);
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int upperDomain = hDist.getUpperDomain(10, 5);

        assertEquals(15, upperDomain);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        double mean = hDist.getNumericalMean();

        assertEquals(5.0, mean, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        double variance = hDist.getNumericalVariance();

        double expectedVariance = 5.0 * 15.0 * (10.0 - 15.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0));
        assertEquals(expectedVariance, variance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int lowerBound = hDist.getSupportLowerBound();

        assertEquals(-5, lowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        int upperBound = hDist.getSupportUpperBound();

        assertEquals(15, upperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);
        boolean isConnected = hDist.isSupportConnected();

        assertTrue(isConnected);
    }

    @Test
    public void testGenerateValues() {
        HypergeometricDistribution hDist = new HypergeometricDistribution(new Well19937c(), 10, 5, 15);

        int numberOfValues = 10000;
        int[] values = new int[numberOfValues];

        for (int i = 0; i < numberOfValues; i++) {
            values[i] = hDist.nextInt();
        }

        int countSuccess = 0;
        for (int value : values) {
            if (value >= 0 && value <= 15) {
                countSuccess++;
            }
        }

        double percentageSuccess = ((double) countSuccess / numberOfValues) * 100.0;
        System.out.println("Percentage of success: " + percentageSuccess + "%");
        assertTrue(percentageSuccess >= 95.0);
    }
}