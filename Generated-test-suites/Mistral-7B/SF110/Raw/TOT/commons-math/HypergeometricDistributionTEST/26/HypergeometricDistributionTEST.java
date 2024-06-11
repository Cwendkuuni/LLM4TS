package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 5, 6);
    }

    @Test
    public void testGetters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
        assertEquals(5, distribution.getPopulationSize());
        assertEquals(3, distribution.getNumberOfSuccesses());
        assertEquals(5, distribution.getSampleSize());
    }

    @Test
    public void testCumulativeProbabilityZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilitySuccess() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testCumulativeProbabilityFailure() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(1.0, distribution.cumulativeProbability(6), 0.001);
    }

    @Test
    public void testProbabilityZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.0, distribution.probability(0), 0.001);
    }

    @Test
    public void testProbabilitySuccess() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.333, distribution.probability(3), 0.001);
    }

    @Test
    public void testProbabilityFailure() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.333, distribution.probability(4), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityZero() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilitySuccess() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.5, distribution.upperCumulativeProbability(5), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityFailure() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.0, distribution.upperCumulativeProbability(6), 0.001);
    }

    @Test
    public void testNumericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(0.00078125, distribution.getNumericalVariance(), 0.001);
    }

    @Test
    public void testSupportLowerBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(-1, distribution.getSupportLowerBound());
    }

    @Test
    public void testSupportUpperBound() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(5, distribution.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertTrue(distribution.isSupportConnected());
    }

}