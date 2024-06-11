package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class HypergeometricDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testHypergeometricDistribution_populationSizeIsNotStrictlyPositive() {
        new HypergeometricDistribution(new Well19937c(), -1, 1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistribution_numberOfSuccessesIsNotPositive() {
        new HypergeometricDistribution(new Well19937c(), 2, -1, 1);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistribution_sampleSizeIsNotPositive() {
        new HypergeometricDistribution(new Well19937c(), 2, 1, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_numberOfSuccessesExceedsPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 2, 3, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistribution_sampleSizeExceedsPopulationSize() {
        new HypergeometricDistribution(new Well19937c(), 2, 1, 3);
    }

    @Test
    public void testHypergeometricDistribution_getNumberOfSuccesses() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    @Test
    public void testHypergeometricDistribution_getPopulationSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(10, distribution.getPopulationSize());
    }

    @Test
    public void testHypergeometricDistribution_getSampleSize() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);
        assertEquals(10, distribution.getSampleSize());
    }

    @Test
    public void testHypergeometricDistribution_cumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);

        assertEquals(0.0, distribution.cumulativeProbability(1), 0.001);
        assertEquals(0.1761, distribution.cumulativeProbability(2), 0.001);
        assertEquals(0.3602, distribution.cumulativeProbability(3), 0.001);
        assertEquals(0.5, distribution.cumulativeProbability(5), 0.001);
        assertEquals(0.8239, distribution.cumulativeProbability(8), 0.001);
        assertEquals(1.0, distribution.cumulativeProbability(10), 0.001);

        try {
            distribution.cumulativeProbability(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHypergeometricDistribution_probability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);

        assertEquals(0.0455, distribution.probability(1), 0.001);
        assertEquals(0.091, distribution.probability(2), 0.001);
        assertEquals(0.1366, distribution.probability(3), 0.001);
        assertEquals(0.1761, distribution.probability(4), 0.001);
        assertEquals(0.1366, distribution.probability(5), 0.001);
        assertEquals(0.091, distribution.probability(6), 0.001);
        assertEquals(0.0455, distribution.probability(7), 0.001);
        assertEquals(0.0155, distribution.probability(8), 0.001);
        assertEquals(0.0049, distribution.probability(9), 0.001);
        assertEquals(0.0011, distribution.probability(10), 0.001);
    }

    @Test
    public void testHypergeometricDistribution_upperCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);

        assertEquals(0.9989, distribution.upperCumulativeProbability(1), 0.001);
        assertEquals(0.9851, distribution.upperCumulativeProbability(2), 0.001);
        assertEquals(0.9486, distribution.upperCumulativeProbability(3), 0.001);
        assertEquals(0.875, distribution.upperCumulativeProbability(5), 0.001);
        assertEquals(0.4398, distribution.upperCumulativeProbability(8), 0.001);
        assertEquals(0.0, distribution.upperCumulativeProbability(11), 0.001);

        try {
            distribution.upperCumulativeProbability(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHypergeometricDistribution_numericalVariance() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), 10, 5, 10);

        assertEquals(0.03545, distribution.getNumericalVariance(), 0.001);

        try {
            distribution.getNumericalVariance();
            distribution.getNumericalVariance(); // Try to calculate it twice to ensure it's calculated only once
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }
}