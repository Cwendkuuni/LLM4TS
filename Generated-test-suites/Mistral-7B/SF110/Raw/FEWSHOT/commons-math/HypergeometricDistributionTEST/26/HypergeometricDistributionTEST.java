package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            new HypergeometricDistribution(new MersenneTwister(), -1, 10, 10);
            fail("Should have thrown exception for negative population size");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(new MersenneTwister(), 10, -1, 10);
            fail("Should have thrown exception for negative number of successes");
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(new MersenneTwister(), 10, 10, -1);
            fail("Should have thrown exception for negative sample size");
        } catch (NotPositiveException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(new MersenneTwister(), 10, 11, 10);
            fail("Should have thrown exception for number of successes larger than population size");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        try {
            new HypergeometricDistribution(new MersenneTwister(), 10, 10, 11);
            fail("Should have thrown exception for sample size larger than population size");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        // valid construction
        new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        int[] domain = dist.getDomain();
        assertEquals(5, domain[0]);
        assertEquals(15, domain[1]);
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);

        double[] probabilities = new double[] { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0 };
        for (int i = 0; i < probabilities.length; i++) {
            double actual = dist.cumulativeProbability(i);
            double expected = probabilities[i];
            assertEquals(expected, actual, 1e-9);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);

        double[] probabilities = new double[] { 0.004945, 0.019608, 0.036708, 0.056235, 0.078095, 0.099956, 0.122742, 0.145449, 0.168056, 0.189563, 0.211072 };
        for (int i = 0; i < probabilities.length; i++) {
            double actual = dist.probability(i);
            double expected = probabilities[i];
            assertEquals(expected, actual, 1e-9);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);

        double[] probabilities = new double[] { 0.995055, 0.990142, 0.985197, 0.980182, 0.975101, 0.969962, 0.964765, 0.959499, 0.954162, 0.948753, 0.943272 };
        for (int i = 0; i < probabilities.length; i++) {
            double actual = dist.upperCumulativeProbability(i);
            double expected = probabilities[i];
            assertEquals(1.0 - expected, actual, 1e-9);
        }
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        double numericalVariance = dist.getNumericalVariance();
        double expected = 0.000402788665316152;
        assertEquals(expected, numericalVariance, 1e-12);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        double numericalMean = dist.getNumericalMean();
        double expected = 5.0;
        assertEquals(expected, numericalMean, 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        int supportLowerBound = dist.getSupportLowerBound();
        int expected = 5;
        assertEquals(expected, supportLowerBound);
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        int supportUpperBound = dist.getSupportUpperBound();
        int expected = 15;
        assertEquals(expected, supportUpperBound);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 15);
        boolean isSupportConnected = dist.isSupportConnected();
        assertTrue(isSupportConnected);
    }
}