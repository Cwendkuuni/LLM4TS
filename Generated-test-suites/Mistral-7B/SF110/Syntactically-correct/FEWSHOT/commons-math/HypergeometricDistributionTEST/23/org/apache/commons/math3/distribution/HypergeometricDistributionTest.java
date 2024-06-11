package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import static org.apache.commons.math3.util.Precision.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.random.*;
import org.apache.commons.math3.special.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_whenPopulationSizeIsNonPositive_thenThrowsException() {
        new HypergeometricDistribution(0, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_whenNumberOfSuccessesIsNonPositive_thenThrowsException() {
        new HypergeometricDistribution(1, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructor_whenSampleSizeIsNonPositive_thenThrowsException() {
        new HypergeometricDistribution(1, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_whenNumberOfSuccessesIsLargerThanPopulationSize_thenThrowsException() {
        new HypergeometricDistribution(1, 2, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructor_whenSampleSizeIsLargerThanPopulationSize_thenThrowsException() {
        new HypergeometricDistribution(1, 1, 2);
    }

    @Test
    public void testConstructor() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 100);
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(100, distribution.getSampleSize());
    }

    @Test
    public void testGetDomain() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        final int[] domain = distribution.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(10, domain[1]);
    }

    @Test
    public void testCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(0.0, distribution.cumulativeProbability(0), 1e-10);
        assertEquals(0.04545454545454545, distribution.cumulativeProbability(1), 1e-10);
        assertEquals(0.4545454545454545, distribution.cumulativeProbability(5), 1e-10);
        assertEquals(1.0, distribution.cumulativeProbability(10), 1e-10);
    }

    @Test
    public void testProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(0.009787671232876381, distribution.probability(0), 1e-10);
        assertEquals(0.04761904545454545, distribution.probability(1), 1e-10);
        assertEquals(0.05789473684210526, distribution.probability(2), 1e-10);
        assertEquals(0.05789473684210526, distribution.probability(5), 1e-10);
        assertEquals(0.009787671232876381, distribution.probability(10), 1e-10);
    }

    @Test
    public void testUpperCumulativeProbability() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(1.0, distribution.upperCumulativeProbability(10), 1e-10);
        assertEquals(0.9523809523809523, distribution.upperCumulativeProbability(9), 1e-10);
        assertEquals(0.4545454545454545, distribution.upperCumulativeProbability(5), 1e-10);
        assertEquals(0.04545454545454545, distribution.upperCumulativeProbability(1), 1e-10);
    }

    @Test
    public void testNumericalMean() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(5.0, distribution.getNumericalMean(), 1e-10);
    }

    @Test
    public void testNumericalVariance() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(0.015428571428571429, distribution.getNumericalVariance(), 1e-10);
    }

    @Test
    public void testGetSupportLowerBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(-5, distribution.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        final HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 100);
        assertEquals(5, distribution.getSupportUpperBound());
    }
}