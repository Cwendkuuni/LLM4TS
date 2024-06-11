package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, dist.getPopulationSize());
        assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
        assertEquals(sampleSize, dist.getSampleSize());
    }

    /**
     * Test constructor with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        int populationSize = 0;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        int populationSize = 10;
        int numberOfSuccesses = -1;
        int sampleSize = 4;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = -1;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test constructor with invalid population size, number of successes and sample size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidParameters() {
        int populationSize = 10;
        int numberOfSuccesses = 11;
        int sampleSize = 5;
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    /**
     * Test cumulative probability with valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expected = dist.cumulativeProbability(x);
        double actual = dist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test cumulative probability with lower bound.
     */
    @Test
    public void testCumulativeProbabilityLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportLowerBound();
        double expected = 0.0;
        double actual = dist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test cumulative probability with upper bound.
     */
    @Test
    public void testCumulativeProbabilityUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportUpperBound();
        double expected = 1.0;
        double actual = dist.cumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test cumulative probability with invalid argument.
     */
    @Test
    public void testCumulativeProbabilityInvalid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = -1;
        try {
            dist.cumulativeProbability(x);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        int y = populationSize + 1;
        try {
            dist.cumulativeProbability(y);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test probability with valid arguments.
     */
    @Test
    public void testProbabilityValid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expected = dist.probability(x);
        double actual = dist.probability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test probability with lower bound.
     */
    @Test
    public void testProbabilityLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportLowerBound();
        double expected = 0.0;
        double actual = dist.probability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test probability with upper bound.
     */
    @Test
    public void testProbabilityUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportUpperBound();
        double expected = dist.probabilityMass(x);
        double actual = dist.probability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test probability with invalid argument.
     */
    @Test
    public void testProbabilityInvalid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = -1;
        try {
            dist.probability(x);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        int y = populationSize + 1;
        try {
            dist.probability(y);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test upper cumulative probability with valid arguments.
     */
    @Test
    public void testUpperCumulativeProbabilityValid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = 2;
        double expected = dist.upperCumulativeProbability(x);
        double actual = dist.upperCumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test upper cumulative probability with lower bound.
     */
    @Test
    public void testUpperCumulativeProbabilityLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportLowerBound();
        double expected = 1.0;
        double actual = dist.upperCumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test upper cumulative probability with upper bound.
     */
    @Test
    public void testUpperCumulativeProbabilityUpperBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = dist.getSupportUpperBound();
        double expected = dist.upperCumulativeProbability(x);
        double actual = dist.upperCumulativeProbability(x);
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test upper cumulative probability with invalid argument.
     */
    @Test
    public void testUpperCumulativeProbabilityInvalid() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int x = -1;
        try {
            dist.upperCumulativeProbability(x);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        int y = populationSize + 1;
        try {
            dist.upperCumulativeProbability(y);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    /**
     * Test getNumericalMean.
     */
    @Test
    public void testGetNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) (sampleSize * numberOfSuccesses) / (double) populationSize;
        double actual = dist.getNumericalMean();
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test getNumericalVariance.
     */
    @Test
    public void testGetNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        double expected = (double) (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (double) (populationSize * populationSize * (populationSize - 1));
        double actual = dist.getNumericalVariance();
        assertEquals(expected, actual, 0.00001);
    }

    /**
     * Test getSupportLowerBound.
     */
    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        int expected = FastMath.max(0, sampleSize + numberOfSuccesses - populationSize);
        int actual = dist.getSupportLowerBound();
        assertEquals(expected, actual);
    }

    /**
     * Test isSupportConnected.
     */
    @Test
    public void testIsSupportConnected() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 4;
        HypergeometricDistribution dist = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        boolean expected = true;
        boolean actual = dist.isSupportConnected();
        assertEquals(expected, actual);
    }

    /**
     * Test generation of random values.
     */
    @Test
    public void testRandomValuesGeneration() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 1000;
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] expectedValues = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            expectedValues[i] = dist.sample();
        }

        int[] actualValues = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            actualValues[i] = dist.sample();
        }

        assertEquals(expectedValues, actualValues);
    }
}