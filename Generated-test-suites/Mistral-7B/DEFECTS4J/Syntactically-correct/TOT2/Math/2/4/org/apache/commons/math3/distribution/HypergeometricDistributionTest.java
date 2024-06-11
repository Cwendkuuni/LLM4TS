package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link HypergeometricDistribution}.
 */
public class HypergeometricDistributionTest {

    /**
     * Test constructing a new hypergeometric distribution with valid parameters.
     */
    @Test
    public void testValidConstruction() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(10, dist.getPopulationSize());
        assertEquals(5, dist.getNumberOfSuccesses());
        assertEquals(3, dist.getSampleSize());
    }

    /**
     * Test constructing a new hypergeometric distribution with invalid population size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidPopulationSize() {
        new HypergeometricDistribution(-1, 5, 3);
    }

    /**
     * Test constructing a new hypergeometric distribution with invalid number of successes.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidNumberOfSuccesses() {
        new HypergeometricDistribution(10, -1, 3);
    }

    /**
     * Test constructing a new hypergeometric distribution with invalid sample size.
     */
    @Test(expected = NotPositiveException.class)
    public void testInvalidSampleSize() {
        new HypergeometricDistribution(10, 5, -1);
    }

    /**
     * Test constructing a new hypergeometric distribution with population size greater than number of successes and/or sample size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testInvalidParameters() {
        new HypergeometricDistribution(5, 7, 3);
    }

    /**
     * Test methods {@link HypergeometricDistribution#getLowerDomain(int, int, int)} and {@link HypergeometricDistribution#getUpperDomain(int, int)}.
     */
    @Test
    public void testGetDomain() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        final int[] domain = dist.getDomain();
        assertEquals(3, domain[0]);
        assertEquals(5, domain[1]);
    }

    /**
     * Test method {@link HypergeometricDistribution#probability(int)}.
     */
    @Test
    public void testProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.25, dist.probability(2), 0.001);
        assertEquals(0.1, dist.probability(1), 0.001);
        assertEquals(0.01, dist.probability(0), 0.001);
        assertEquals(0.3529411765, dist.probability(3), 0.00001);
        assertEquals(0.1767805326, dist.probability(4), 0.00001);
        assertEquals(0.0688888889, dist.probability(0), 0.00001);
    }

    /**
     * Test method {@link HypergeometricDistribution#cumulativeProbability(int)}.
     */
    @Test
    public void testCumulativeProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
        assertEquals(0.1666666667, dist.cumulativeProbability(1), 0.001);
        assertEquals(0.3333333333, dist.cumulativeProbability(2), 0.001);
        assertEquals(0.4999999999, dist.cumulativeProbability(3), 0.001);
        assertEquals(0.6666666667, dist.cumulativeProbability(4), 0.001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
    }

    /**
     * Test method {@link HypergeometricDistribution#upperCumulativeProbability(int)}.
     */
    @Test
    public void testUpperCumulativeProbability() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(0.6666666667, dist.upperCumulativeProbability(3), 0.001);
        assertEquals(1.0, dist.upperCumulativeProbability(4), 0.001);
    }

    /**
     * Test method {@link HypergeometricDistribution#getNumericalMean()}.
     */
    @Test
    public void testNumericalMean() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(3.5, dist.getNumericalMean(), 0.001);
    }

    /**
     * Test method {@link HypergeometricDistribution#getNumericalVariance()}.
     */
    @Test
    public void testNumericalVariance() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(1.1111111111, dist.getNumericalVariance(), 0.001);
    }

    /**
     * Test method {@link HypergeometricDistribution#getSupportLowerBound()}.
     */
    @Test
    public void testSupportLowerBound() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(2, dist.getSupportLowerBound());
    }

    /**
     * Test method {@link HypergeometricDistribution#getSupportUpperBound()}.
     */
    @Test
    public void testSupportUpperBound() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertEquals(5, dist.getSupportUpperBound());
    }

    /**
     * Test method {@link HypergeometricDistribution#isSupportConnected()}.
     */
    @Test
    public void testSupportConnected() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        assertTrue(dist.isSupportConnected());
    }

    /**
     * Test generating random values using the distribution.
     */
    @Test
    public void testRandomValues() {
        final HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
        final Well19937c rng = new Well19937c();
        dist.reseedRandomGenerator(rng);
        for (int i = 0; i < 1000; i++) {
            final int x = dist.sample();
            assertTrue(x >= dist.getSupportLowerBound());
            assertTrue(x <= dist.getSupportUpperBound());
        }
    }
}