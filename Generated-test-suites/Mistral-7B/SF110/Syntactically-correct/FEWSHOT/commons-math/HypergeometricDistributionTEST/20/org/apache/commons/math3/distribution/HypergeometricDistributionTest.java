package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationSizeThrowsException() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroPopulationSizeThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccessesThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroNumberOfSuccessesThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 1, 0, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeSampleSizeThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSizeThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSizeThrowsException() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, 2);
    }

    @Test
    public void testCumulativeProbabilityWithValidArgumentReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = (double) x / 20.0;
            double actual = distribution.cumulativeProbability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testProbabilityWithValidArgumentsReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int[] domain = distribution.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = FastMath.comb(10, x) * FastMath.pow(10.0 / 20.0, x) * FastMath.pow(15.0 / 20.0, 10 - x) / FastMath.pow(16.0 / 20.0, 20);
            double actual = distribution.probability(x);
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testGetDomainMethodReturnsExpectedArray() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int[] expected = { 0, 10 };
        int[] actual = distribution.getDomain();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetLowerDomainMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(),
                5, 10, 20);
        int populationSize = distribution.getPopulationSize();
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        int sampleSize = distribution.getSampleSize();
        int expected = FastMath.max(0, populationSize - (numberOfSuccesses + sampleSize));
        int actual = distribution.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperDomainMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(),
                5, 10, 20);
        int populationSize = distribution.getPopulationSize();
        int numberOfSuccesses = distribution.getNumberOfSuccesses();
        int sampleSize = distribution.getSampleSize();
        int expected = FastMath.min(numberOfSuccesses, sampleSize);
        int actual = distribution.getUpperDomain(populationSize, numberOfSuccesses);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNumberOfSuccessesMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int expected = 10;
        int actual = distribution.getNumberOfSuccesses();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPopulationSizeMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int expected = 20;
        int actual = distribution.getPopulationSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSampleSizeMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int expected = 20;
        int actual = distribution.getSampleSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNumericalVarianceMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        double expected = 11.11111111111111;
        double actual = distribution.getNumericalVariance();
        assertEquals(expected, actual, 0.00000000000001);
    }

    @Test
    public void testNumericalVarianceIsCalculatedFlagIsTrue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        assertTrue(distribution.isNumericalVarianceCalculated());
    }

    @Test
    public void testGetSupportLowerBoundMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int expected = FastMath.max(0, 15 - 10 - 5);
        int actual = distribution.getSupportLowerBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSupportUpperBoundMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        int expected = FastMath.min(10, 20);
        int actual = distribution.getSupportUpperBound();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSupportConnectedMethodReturnsExpectedValue() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 5, 10, 20);
        assertTrue(distribution.isSupportConnected());
    }
}