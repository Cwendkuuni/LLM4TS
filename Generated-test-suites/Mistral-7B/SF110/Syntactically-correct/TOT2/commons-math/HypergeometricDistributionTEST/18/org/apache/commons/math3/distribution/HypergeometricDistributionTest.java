package org.junit.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorValidParameters() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
        assertEquals(5, hd.getPopulationSize());
        assertEquals(2, hd.getNumberOfSuccesses());
        assertEquals(3, hd.getSampleSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativePopulationSize() {
        try {
            new HypergeometricDistribution(-1, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeNumberOfSuccesses() {
        try {
            new HypergeometricDistribution(5, -2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativeSampleSize() {
        try {
            new HypergeometricDistribution(5, 2, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorNumberOfSuccessesLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(5, 6, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorSampleSizeLargerThanPopulationSize() {
        try {
            new HypergeometricDistribution(5, 2, 6);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
        int[] domain = hd.getDomain();
        int x = domain[1] - 1;
        double expected = hd.probability(x) / 100.0;
        double actual = hd.cumulativeProbability(x);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
        int[] domain = hd.getDomain();
        int lowerBoundExpected = hd.getSupportLowerBound();
        int upperBoundExpected = hd.getSupportUpperBound();
        int[] domainExpected = { lowerBoundExpected, upperBoundExpected };
        assertArrayEquals(domainExpected, domain);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
        int[] domain = hd.getDomain();
        double x = domain[1] / 2.0;
        double expected = hd.cumulativeProbability(Math.min(domain[1], (int) Math.ceil(x))) - hd.cumulativeProbability(Math.min(domain[1], (int) Math.floor(x)));
        double actual = hd.probability((int) x);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 10);
        double expected = hd.calculateNumericalVariance();
        double actual = hd.getNumericalVariance();
        assertEquals(expected, actual, 0.001);
    }
}