package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionIllegalPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionIllegalNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionIllegalSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, 2);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int[] domain = distribution.getDomain();

        // Test for values outside domain
        assertEquals(0.0, distribution.cumulativeProbability(-1), 0.0);
        assertEquals(1.0, distribution.cumulativeProbability(domain[1] + 1), 0.0);

        // Test for values inside domain
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = (double) i / populationSize;
            double actual = distribution.cumulativeProbability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionProbability() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        // Test for values outside domain
        assertEquals(0.0, distribution.probability(-1), 0.0);
        assertEquals(0.0, distribution.probability(populationSize + 1), 0.0);

        // Test for values inside domain
        for (int i = 0; i <= populationSize; i++) {
            double p = (double) i / populationSize;
            double q = (populationSize - i) / populationSize;
            double expected = SaddlePointExpansion.logBinomialProbability(sampleSize, populationSize - numberOfSuccesses, p, q);
            double actual = distribution.probability(i);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testHypergeometricDistributionNumericalMean() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        double expected = (double) numberOfSuccesses * sampleSize / populationSize;
        double actual = distribution.getNumericalMean();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionNumericalVariance() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        double expected = populationSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses) / (populationSize * populationSize * (populationSize - 1));
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testHypergeometricDistributionSupportBounds() {
        int populationSize = 10;
        int numberOfSuccesses = 3;
        int sampleSize = 5;

        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);

        int lowerBound = distribution.getSupportLowerBound();
        int upperBound = distribution.getSupportUpperBound();

        // Test for lower bound
        assertEquals(populationSize + numberOfSuccesses - sampleSize, lowerBound);

        // Test for upper bound
        assertEquals(FastMath.min(numberOfSuccesses, sampleSize), upperBound);
    }
}