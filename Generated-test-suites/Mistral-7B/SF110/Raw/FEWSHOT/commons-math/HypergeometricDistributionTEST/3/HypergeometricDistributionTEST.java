package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructors() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd1 = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        HypergeometricDistribution hd2 = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, hd1.getPopulationSize());
        assertEquals(numberOfSuccesses, hd1.getNumberOfSuccesses());
        assertEquals(sampleSize, hd1.getSampleSize());

        assertEquals(hd1.getPopulationSize(), hd2.getPopulationSize());
        assertEquals(hd1.getNumberOfSuccesses(), hd2.getNumberOfSuccesses());
        assertEquals(hd1.getSampleSize(), hd2.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 3, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 0, 3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, -1, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorZeroNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 10, 0, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 3, -1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorZeroSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 3, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 3, 11);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();

        for (int i = 0; i < domain.length; i++) {
            double expected = hd.cumulativeProbability(domain[i]);
            double actual = hd.sample();
            assertEquals("Error in cumulativeProbability for x = " + domain[i], expected, actual, 1e-10);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();
        double p = sampleSize / (double) populationSize;
        double q = (populationSize - sampleSize) / (double) populationSize;

        for (int i = 0; i < domain.length; i++) {
            double expected = hd.probability(domain[i]);
            double actual = hd.sampleProbability(domain[i]);
            assertEquals("Error in probability for x = " + domain[i], expected, actual, 1e-10);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();

        for (int i = 0; i < domain.length; i++) {
            double expected = hd.upperCumulativeProbability(domain[i]);
            double actual = 1.0 - hd.cumulativeProbability(domain[i]);
            assertEquals("Error in upperCumulativeProbability for x = " + domain[i], expected, actual, 1e-10);
        }
    }

    @Test
    public void testGetNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        assertEquals(populationSize, hd.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        assertEquals(sampleSize, hd.getSampleSize());
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int expected = populationSize + numberOfSuccesses - sampleSize;
        int actual = hd.getSupportLowerBound();

        assertEquals("Error in getSupportLowerBound", expected, actual);
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int expected = Math.min(numberOfSuccesses, sampleSize);
        int actual = hd.getSupportUpperBound();

        assertEquals("Error in getSupportUpperBound", expected, actual);
    }

    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        assertTrue(hd.isSupportConnected());
    }

    @Test
    public void testNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) sampleSize * numberOfSuccesses / populationSize;
        double actual = hd.getNumericalMean();

        assertEquals("Error in getNumericalMean", expected, actual, 1e-10);
    }

    @Test
    public void testNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        double expected = sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1));
        double actual = hd.getNumericalVariance();

        assertEquals("Error in getNumericalVariance", expected, actual, 1e-10);
    }

    @Test
    public void testSample() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();
        int sample = 0;
        int trials = 10000;

        int count = 0;

        for (int i = 0; i < trials; i++) {
            sample = hd.nextInt();
            if (sample >= domain[0] && sample <= domain[1]) {
                count++;
            }
        }

        double probability = hd.probability(domain[0]);
        double expected = (int) (trials * probability);

        assertEquals("Error in sample", expected, count);
    }
}