package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        int populationSize = 10;
        int numberOfSuccesses = 5;
        int sampleSize = 5;

        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(populationSize, hd.getPopulationSize());
        assertEquals(numberOfSuccesses, hd.getNumberOfSuccesses());
        assertEquals(sampleSize, hd.getSampleSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNegativePopulationSize() {
        new HypergeometricDistribution(-1, 5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(10, -5, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructionWithNegativeSampleSize() {
        new HypergeometricDistribution(10, 5, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructionWithNumberOfSuccessesGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 11, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructionWithSampleSizeGreaterThanPopulationSize() {
        new HypergeometricDistribution(10, 5, 11);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionWithZeroPopulationSize() {
        new HypergeometricDistribution(0, 5, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionWithZeroNumberOfSuccesses() {
        new HypergeometricDistribution(10, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructionWithZeroSampleSize() {
        new HypergeometricDistribution(10, 5, 0);
    }

    @Test
    public void testCumulativeProbability() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int[] domain = hd.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double expectedCumulativeProbability = (double) x / sampleSize;
            double actualCumulativeProbability = hd.cumulativeProbability(x);
            assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
        }
    }

    @Test
    public void testProbability() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        for (int x = 0; x <= sampleSize; x++) {
            double expectedProbability = Math.comb(populationSize, numberOfSuccesses) * Math.pow(sampleSize / populationSize, x) * Math.pow((populationSize - sampleSize) / populationSize, sampleSize - x) / (Math.pow(populationSize, sampleSize) * Math.comb(sampleSize, x));
            double actualProbability = hd.probability(x);
            assertEquals(expectedProbability, actualProbability, 0.001);
        }
    }

    @Test
    public void testGetNumericalMean() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        double expectedNumericalMean = (double) numberOfSuccesses * sampleSize / populationSize;
        double actualNumericalMean = hd.getNumericalMean();
        assertEquals(expectedNumericalMean, actualNumericalMean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        doubleexpectedNumericalVariance = Math.pow(populationSize, 2) * (numberOfSuccesses * (sampleSize - numberOfSuccesses) / (populationSize * populationSize) + sampleSize * (populationSize - sampleSize) / (populationSize * populationSize * (populationSize - 1)));
        double actualNumericalVariance = hd.getNumericalVariance();
        assertEquals(expectedNumericalVariance, actualNumericalVariance, 0.001);
    }

    @Test
    public void testGenerateValues() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 10000;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
        MersenneTwister rng = new MersenneTwister();

        int[] generatedValues = new int[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            generatedValues[i] = hd.nextInt();
        }

        int sum = 0;
        for (int value : generatedValues) {
            sum += value;
        }

        double expectedNumericalMean = (double) numberOfSuccesses * sampleSize / populationSize;
        double actualNumericalMean = (double) sum / sampleSize;
        assertEquals(expectedNumericalMean, actualNumericalMean, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expectedSupportLowerBound = populationSize + numberOfSuccesses - sampleSize;
        int actualSupportLowerBound = hd.getSupportLowerBound();
        assertEquals(expectedSupportLowerBound, actualSupportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        int populationSize = 15;
        int numberOfSuccesses = 5;
        int sampleSize = 7;
        HypergeometricDistribution hd = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);

        int expectedSupportUpperBound = numberOfSuccesses;
        int actualSupportUpperBound = hd.getSupportUpperBound();
        assertEquals(expectedSupportUpperBound, actualSupportUpperBound);
    }
}