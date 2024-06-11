
package org.apache.commons.math3.distribution;

import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {
    @Test
    public void testHypergeometricDistributionConstructor() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(5, dist.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativePopulationSize() {
        try {
            new HypergeometricDistribution(-5, 5, 5);
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroPopulationSize() {
        try {
            new HypergeometricDistribution(0, 5, 5);
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, -5, 5);
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(10, 0, 5);
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testNegativeSampleSize() {
        try {
            new HypergeometricDistribution(10, 5, -5);
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testZeroSampleSize() {
        try {
            new HypergeometricDistribution(10, 5, 0);
        } catch (NotPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberOfSuccessesLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 11, 5);
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSampleSizeLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(10, 5, 11);
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double prob = dist.cumulativeProbability(3);
        assertEquals(0.4011, Precision.round(prob, 4));
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double prob = dist.probability(3);
        assertEquals(0.2051, Precision.round(prob, 4));
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double prob = dist.upperCumulativeProbability(3);
        assertEquals(0.5989, Precision.round(prob, 4));
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double mean = dist.getNumericalMean();
        assertEquals(5.5, Precision.round(mean, 2));
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
        double variance = dist.getNumericalVariance();
        assertEquals(3.0, Precision.round(variance, 2));
    }
}

