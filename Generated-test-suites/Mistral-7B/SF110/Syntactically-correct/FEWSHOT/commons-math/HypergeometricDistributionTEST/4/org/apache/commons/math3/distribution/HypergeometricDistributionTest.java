package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test
    public void testHypergeometricDistributionConstruction() {
        try {
            new HypergeometricDistribution(-1, 2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, -2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 3, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 2, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(1, 5, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(null, 2, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), -1, 2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, -2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 2, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 2, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 3, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 2, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 1, 5, 4);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 10, 2, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 5, 10, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 5, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 5, 5, 6);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }

        try {
            new HypergeometricDistribution(new Well19937c(), 5, 5, 4);
            assertEquals(5, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).getNumberOfSuccesses());
            assertEquals(10, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).getPopulationSize());
            assertEquals(5, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).getSampleSize());
            assertEquals(0.5, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).probability(2));
            assertEquals(0.16666666666666664, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).probability(3), 0.00001);
            assertEquals(0.03333333333333333, new HypergeometricDistribution(new Well19937c(), 5, 5, 4).probability(4), 0.00001);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = i * 1.0 / 10.0;
            double actual = dist.cumulativeProbability(i);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        for (int i = 0; i <= 10; i++) {
            double actual = dist.probability(i);
            double expected = FastMath.comb(10, i) * Math.pow(5.0 / 10.0, i) * Math.pow(5.0 / 10.0, 10 - i);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        int[] domain = dist.getDomain();
        for (int i = domain[0]; i <= domain[1]; i++) {
            double expected = 1.0 - FastMath.comb(10, i) * Math.pow(5.0 / 10.0, i) * Math.pow(5.0 / 10.0, 10 - i);
            double actual = dist.upperCumulativeProbability(i);
            assertEquals(expected, actual, 0.00001);
        }
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        assertEquals(5.0, dist.getNumericalMean(), 0.00001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        double numericalVariance = dist.getNumericalVariance();
        assertEquals(2.97896875, numericalVariance, 0.00001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        assertEquals(-3, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        assertEquals(5, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 5, 10);
        assertTrue(dist.isSupportConnected());
    }
}