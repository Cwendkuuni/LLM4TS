package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class HypergeometricDistributionTest {
    @Test(expected = NotPositiveException.class)
    public void testInstanciationWithNegativePopulationSize() {
        new HypergeometricDistribution(new RandomGenerator(), -1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInstanciationWithNonStrictlyPositivePopulationSize() {
        new HypergeometricDistribution(new RandomGenerator(), 0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testInstanciationWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new RandomGenerator(), 1, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInstanciationWithNonStrictlyPositiveNumberOfSuccesses() {
        new HypergeometricDistribution(new RandomGenerator(), 1, 0, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testInstanciationWithNegativeSampleSize() {
        new HypergeometricDistribution(new RandomGenerator(), 1, 1, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInstanciationWithNonStrictlyPositiveSampleSize() {
        new HypergeometricDistribution(new RandomGenerator(), 1, 1, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInstanciationWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new RandomGenerator(), 1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testInstanciationWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new RandomGenerator(), 1, 1, 2);
    }

    @Test
    public void testInstanciationWithValidParameters() {
        new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
    }

    @Test
    public void testCumulativeProbabilityZero() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.0, test.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilitySuccess() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.3605634327357602, test.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testCumulativeProbabilityFailure() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.6394365672642398, test.cumulativeProbability(7), 0.001);
    }

    @Test
    public void testProbabilityZero() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.0, test.probability(0), 0.001);
    }

    @Test
    public void testProbabilitySuccess() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.1761085895822606, test.probability(3), 0.001);
    }

    @Test
    public void testProbabilityFailure() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.0588379781856346, test.probability(7), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityZero() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(1.0, test.upperCumulativeProbability(10), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilitySuccess() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.3605634327357602, test.upperCumulativeProbability(3), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityFailure() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.6394365672642398, test.upperCumulativeProbability(7), 0.001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(5, test.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(10, test.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(5, test.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(5.0, test.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0.1555208593183698, test.getNumericalVariance(), 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(0, test.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertEquals(5, test.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution test = new HypergeometricDistribution(new RandomGenerator(), 10, 5, 5);
        assertTrue(test.isSupportConnected());
    }
}