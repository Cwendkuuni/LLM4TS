package org.apache.commons.math3.distribution.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @org.junit.Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new Well19937c(), 5, -1, 10);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositivePopulationSize() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 10);
    }

    @org.junit.Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositivePopulationSizeOrSampleSize() {
        new HypergeometricDistribution(new Well19937c(), 10, 1, -1);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 7, 3);
    }

    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 5, 7);
    }

    @org.junit.Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @org.junit.Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @org.junit.Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @org.junit.Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();

        assertEquals(0.0, dist.cumulativeProbability(domain[0] - 1), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(domain[0]), 0.001);
        assertEquals(0.75, dist.cumulativeProbability(domain[1] - 1), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(domain[1]), 0.001);
    }

    @org.junit.Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        int[] domain = dist.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(5, domain[1]);
    }

    @org.junit.Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();

        assertEquals(0.1, dist.probability(0), 0.001);
        assertEquals(0.16666666666666668, dist.probability(1), 0.001);
        assertEquals(0.3333333333333333, dist.probability(2), 0.001);
        assertEquals(0.16666666666666668, dist.probability(4), 0.001);
        assertEquals(0.0, dist.probability(6), 0.001);
    }

    @org.junit.Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();

        assertEquals(0.5, dist.upperCumulativeProbability(domain[0] - 1), 0.001);
        assertEquals(0.6666666666666666, dist.upperCumulativeProbability(domain[0]), 0.001);
        assertEquals(0.8333333333333333, dist.upperCumulativeProbability(domain[1] - 1), 0.001);
        assertEquals(1.0, dist.upperCumulativeProbability(domain[1]), 0.001);
    }

    @org.junit.Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5.0, dist.getNumericalMean(), 0.001);
    }

    @org.junit.Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(2.380231683531975, dist.getNumericalVariance(), 0.001);
    }

    @org.junit.Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(-1, dist.getSupportLowerBound());
    }

    @org.junit.Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @org.junit.Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
        assertTrue(dist.isSupportConnected());
    }

}