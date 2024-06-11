package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidPopulationSize() {
        new HypergeometricDistribution(0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(1, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithInvalidSampleSize() {
        new HypergeometricDistribution(1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 1, 2);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbability() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double probability = distribution.cumulativeProbability(x);
            double expectedProbability = 0;
            if (x == domain[0]) {
                expectedProbability = 0;
            } else if (x == domain[1]) {
                expectedProbability = 1;
            } else {
                expectedProbability = distribution.probability(x);
            }
            assertEquals(expectedProbability, probability, 1e-10);
        }
    }

    @Test
    public void testProbability() {
        RandomGenerator rng = new Well19937c();
        HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 5);

        int[] domain = distribution.getDomain();

        for (int x = domain[0]; x <= domain[1]; x++) {
            double probability = distribution.probability(x);
            double binomialProbability = SaddlePointExpansion.binomialProbability(x, 10, 5.0 / 10.0, 5.0 / 10.0);
            assertEquals(binomialProbability, probability, 1e-10);
        }
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertEquals(10.0 / 101.0, distribution.getNumericalMean(), 1e-6);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertEquals(1.0 / (101.0 * 100.0 * (100.0 - 1.0)), distribution.getNumericalVariance(), 1e-6);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertEquals(1.0 / (101.0 * 100.0 * (100.0 - 1.0)), distribution.calculateNumericalVariance(), 1e-6);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertEquals(101, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertEquals(10, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(1, 10, 100);
        assertTrue(distribution.isSupportConnected());
    }
}