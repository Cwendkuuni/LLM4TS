package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.25, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(4), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(0.166666666666667, distribution.probability(2), 0.001);
        assertEquals(0.272727272727273, distribution.probability(3), 0.001);
        assertEquals(0.111111111111111, distribution.probability(4), 0.001);
        assertEquals(0.0384615384615385, distribution.probability(5), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(1.0, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(3.5, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(1.111111111111111, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(3, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 5);
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testNextInt() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        final int[] sample = new int[1000];

        for (int i = 0; i < sample.length; i++) {
            sample[i] = distribution.nextInt();
        }

        int successes = 0;
        int total = 0;
        for (int value : sample) {
            total++;
            if (value <= 4) {
                successes++;
            }
        }

        assertEquals(sample.length * 0.25, successes, 1);
        assertEquals(sample.length, total);
    }
}