package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            new HypergeometricDistribution(0, 1, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, -1, 1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 10, -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 10, 10);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(10, 10, 11);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
            assertNotNull(new HypergeometricDistribution(new Well19937c(), 10, 5, 5).getNumericalVariance());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = (x <= 4) ? x / 10.0 : (9 - x) / 10.0;
            assertEquals("Unexpected cumulative probability for x = " + x, expected, dist.cumulativeProbability(x), 1e-10);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[0]; x <= domain[1]; x++) {
            double expected = binomialCoefficient(5, x) * Math.pow(10.0 / 15.0, x) * Math.pow(15.0 / 10.0, 5 - x) / Math.pow(16.0, 5);
            assertEquals("Unexpected probability for x = " + x, expected, dist.probability(x), 1e-15);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int[] domain = dist.getDomain();
        for (int x = domain[1]; x >= domain[0]; x--) {
            double expected = 1.0 - dist.cumulativeProbability(x);
            assertEquals("Unexpected upper cumulative probability for x = " + x, expected, dist.upperCumulativeProbability(x), 1e-10);
        }
    }

    @Test
    public void testMeanAndVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        assertEquals("Unexpected numerical mean", 5.0, dist.getNumericalMean(), 1e-10);
        assertEquals("Unexpected numerical variance", 0.5, dist.getNumericalVariance(), 1e-10);
    }

    @Test
    public void testSupportBounds() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        int lowerBound = dist.getSupportLowerBound();
        int upperBound = dist.getSupportUpperBound();

        assertTrue("Unexpected lower support bound", lowerBound >= 0);
        assertTrue("Unexpected upper support bound", upperBound >= 0);
        assertTrue("Unexpected upper support bound", upperBound <= 5);
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

        assertTrue("Unexpected connected support", dist.isSupportConnected());
    }

    private long binomialCoefficient(int n, int k) {
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - i + 1) / i;
        }
        return result;
    }
}