package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testConstructor_populationSizeNegative() {
        new HypergeometricDistribution(new Well19937c(), -2, 2, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_numberOfSuccessesNotStrictlyPositive() {
        new HypergeometricDistribution(new Well19937c(), 5, 0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_sampleSizeNotStrictlyPositive() {
        new HypergeometricDistribution(new Well19937c(), 5, 3, -2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_numberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 7, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_sampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 5, 5, 7);
    }

    @Test
    public void testCumulativeProbability_xLessThanLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testCumulativeProbability_xGreaterThanUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(1.0, dist.cumulativeProbability(15), 0.001);
    }

    @Test
    public void testCumulativeProbability_xInDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.681805584369883, dist.cumulativeProbability(5), 0.001);
    }

    @Test
    public void testProbability_xLessThanLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.probability(2), 0.001);
    }

    @Test
    public void testProbability_xGreaterThanUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.probability(15), 0.001);
    }

    @Test
    public void testProbability_xInDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.243108344460634, dist.probability(5), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability_xLessThanLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(1.0, dist.upperCumulativeProbability(2), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability_xGreaterThanUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.upperCumulativeProbability(15), 0.001);
    }

    @Test
    public void testUpperCumulativeProbability_xInDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.318191655539366, dist.upperCumulativeProbability(5), 0.001);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(3, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(5, dist.getSampleSize());
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0142857142857143, dist.getNumericalVariance(), 0.0000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(-2, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(5, dist.getSupportUpperBound());
    }
}