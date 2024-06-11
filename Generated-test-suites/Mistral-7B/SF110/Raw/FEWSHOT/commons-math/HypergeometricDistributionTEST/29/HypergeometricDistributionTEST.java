package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(-1, 1, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNonPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(1, -1, 2);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionConstructorWithNonPositiveSampleSize() {
        new HypergeometricDistribution(1, 2, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 3, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(1, 2, 3);
    }

    @Test
    public void testHypergeometricDistributionConstructor() {
        final RandomGenerator rng = new Well19937c();
        final HypergeometricDistribution distribution = new HypergeometricDistribution(rng, 10, 5, 100);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(100, distribution.getSampleSize());
    }

    @Test
    public void testGetDomain() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        final int[] domain = distribution.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(10, domain[1]);
    }

    @Test
    public void testCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.001);
        assertEquals(0.045, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.176, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.405, distribution.cumulativeProbability(3), 0.001);
        assertEquals(0.652, distribution.cumulativeProbability(4), 0.001);
        assertEquals(0.885, distribution.cumulativeProbability(5), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.001);
    }

    @Test
    public void testProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(0.045, distribution.probability(1), 0.001);
        assertEquals(0.176, distribution.probability(2), 0.001);
        assertEquals(0.278, distribution.probability(3), 0.001);
        assertEquals(0.203, distribution.probability(4), 0.001);
        assertEquals(0.139, distribution.probability(5), 0.001);
        assertEquals(0.097, distribution.probability(6), 0.001);
        assertEquals(0.059, distribution.probability(7), 0.001);
        assertEquals(0.038, distribution.probability(8), 0.001);
        assertEquals(0.025, distribution.probability(9), 0.001);
        assertEquals(0.016, distribution.probability(10), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(0.955, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.995, distribution.upperCumulativeProbability(6), 0.001);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(5.0, distribution.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(0.012, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(1, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 10);
        assertTrue(distribution.isSupportConnected());
    }
}