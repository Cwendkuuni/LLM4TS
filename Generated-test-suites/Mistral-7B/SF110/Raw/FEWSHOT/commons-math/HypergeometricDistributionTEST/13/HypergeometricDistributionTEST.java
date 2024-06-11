package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.random.Well19937c;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testCtorPopulationSizeNegative() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCtorNumberOfSuccessesNegative() {
        new HypergeometricDistribution(new Well19937c(), 1, -1, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCtorSampleSizeNegative() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCtorNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testCtorSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 1, 1, 2);
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
    public void testCumulativeProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
    }

    @Test
    public void testCumulativeProbabilityBoundary() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.001);
    }

    @Test
    public void testCumulativeProbabilityIntermediate() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.6169, dist.cumulativeProbability(2), 0.001);
    }

    @Test
    public void testProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.probability(0), 0.001);
    }

    @Test
    public void testProbabilityBoundary() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.1539, dist.probability(3), 0.001);
    }

    @Test
    public void testProbabilityIntermediate() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.1183, dist.probability(1), 0.001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(3.0, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.1111, dist.getNumericalVariance(), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityZero() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityBoundary() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.0, dist.upperCumulativeProbability(3), 0.001);
    }

    @Test
    public void testUpperCumulativeProbabilityIntermediate() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
        assertEquals(0.3831, dist.upperCumulativeProbability(2), 0.001);
    }
}