package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HypergeometricDistributionTest {

    private final int populationSize;
    private final int numberOfSuccesses;
    private final int sampleSize;
    private HypergeometricDistribution distribution;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {10, 2, 4},
                {100, 30, 10},
                {1000, 500, 50},
                {Integer.MAX_VALUE, Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2},
                {Integer.MAX_VALUE, Integer.MAX_VALUE / 2, Integer.MAX_VALUE - 1},
                {-5, 3, 2},
                {-5, 3, 5},
                {0, 3, 2},
                {0, 3, 5},
                {0, 0, 5}
        });
    }

    public HypergeometricDistributionTest(int populationSize, int numberOfSuccesses, int sampleSize) {
        this.populationSize = populationSize;
        this.numberOfSuccesses = numberOfSuccesses;
        this.sampleSize = sampleSize;
    }

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
            assertNotNull(distribution);
        } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
            throw new AssertionError("Should not throw exception", e);
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionZeroPopulationSize() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
    }

    @Test
    public void testGetDomain() {
        int[] domain = distribution.getDomain();
        int lowerBound = domain[0];
        int upperBound = domain[1];
        int expectedLowerBound = getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        int expectedUpperBound = getUpperDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expectedLowerBound, lowerBound);
        assertEquals(expectedUpperBound, upperBound);
    }

    @Test
    public void testCumulativeProbability() {
        int x = 0;
        double probability = distribution.cumulativeProbability(x);
        double expectedProbability = 0.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = numberOfSuccesses;
        probability = distribution.cumulativeProbability(x);
        expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = numberOfSuccesses + 1;
        probability = distribution.cumulativeProbability(x);
        expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = populationSize;
        probability = distribution.cumulativeProbability(x);
        expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);
    }

    @Test
    public void testGetProbability() {
        for (int x = 0; x <= sampleSize; x++) {
            double probability = distribution.probability(x);
            double expectedProbability = innerProbability(x, populationSize, numberOfSuccesses, sampleSize);
            assertEquals(expectedProbability, probability, 1e-12);
        }
    }

    @Test
    public void testGetUpperCumulativeProbability() {
        int x = 0;
        double probability = distribution.upperCumulativeProbability(x);
        double expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = populationSize + 1;
        probability = distribution.upperCumulativeProbability(x);
        expectedProbability = 0.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = numberOfSuccesses;
        probability = distribution.upperCumulativeProbability(x);
        expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);

        x = numberOfSuccesses + 1;
        probability = distribution.upperCumulativeProbability(x);
        expectedProbability = 1.0;
        assertEquals(expectedProbability, probability, 1e-10);
    }

    @Test
    public void testGetNumericalMean() {
        double numericalMean = distribution.getNumericalMean();
        double expectedNumericalMean = populationSize * numberOfSuccesses / sampleSize;
        assertEquals(expectedNumericalMean, numericalMean, 1e-12);
    }

    @Test
    public void testGetNumericalVariance() {
        double numericalVariance = distribution.getNumericalVariance();
        double expectedNumericalVariance = calculateNumericalVariance();
        assertEquals(expectedNumericalVariance, numericalVariance, 1e-12);
    }

    @Test
    public void testIsSupportConnected() {
        boolean isSupportConnected = distribution.isSupportConnected();
        assertTrue(isSupportConnected);
    }

    @Test
    public void testGetSupportLowerBound() {
        int supportLowerBound = distribution.getSupportLowerBound();
        int expectedSupportLowerBound = getSupportLowerBound();
        assertEquals(expectedSupportLowerBound, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        int supportUpperBound = distribution.getSupportUpperBound();
        int expectedSupportUpperBound = getSupportUpperBound();
        assertEquals(expectedSupportUpperBound, supportUpperBound);
    }

    private int getLowerDomain(int n, int m, int k) {
        return FastMath.max(0, m - (n - k));
    }

    private int getUpperDomain(int m, int k) {
        return FastMath.min(k, m);
    }

    private double innerProbability(int x, int n, int m, int k) {
        double p = sampleSize / (double) populationSize;
        double q = (populationSize - sampleSize) / (double) populationSize;
        return SaddlePointExpansion.logBinomialProbability(x, m, p, q) +
                SaddlePointExpansion.logBinomialProbability(sampleSize - x, populationSize - m, p, q) -
                SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize, p, q);
    }

    private double calculateNumericalVariance() {
        double N = (double) populationSize;
        double m = (double) numberOfSuccesses;
        double n = (double) sampleSize;
        return n * m * (N - n) * (N - m) / (N * N * (N - 1.0));
    }

    private int getSupportLowerBound() {
        return FastMath.max(0, sampleSize + numberOfSuccesses - populationSize);
    }
}