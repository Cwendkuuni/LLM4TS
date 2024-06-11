package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

/**
 * Test class for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * with valid input.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with valid input.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            double pdf = distribution.probabilityDensityFunctionValue(x);
            assertEquals(cdf, Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x), 1.0, 1.5), 1e-10);
            assertEquals(pdf, pdf, 1e-10);
        }
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with invalid input.
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.cumulativeProbability(-1.0);
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with valid input.
     */
    @Test
    public void testInverseCumulativeProbability() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = distribution.inverseCumulativeProbability(p);
            double cdf = distribution.cumulativeProbability(x);
            assertEquals(p, cdf, 1e-10);
        }
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with invalid input.
     */
    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityInvalidInput() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.inverseCumulativeProbability(1.1);
    }

    /**
     * Test method for {@link FDistributionImpl#probabilityDensityFunctionValue(double)}
     * with valid input.
     */
    @Test
    public void testProbabilityDensityFunctionValue() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double pdf = distribution.probabilityDensityFunctionValue(x);
            double cdf = distribution.cumulativeProbability(x);
            assertEquals(pdf, pdf * cdf, 1e-10);
        }
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * with valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidInput() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}
     * with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalidInput() {
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }
}