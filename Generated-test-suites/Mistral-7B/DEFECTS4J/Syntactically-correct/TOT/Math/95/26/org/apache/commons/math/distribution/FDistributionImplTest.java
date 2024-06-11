package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Tests {@link FDistributionImpl#FDistributionImpl(double, double)} with valid
     * parameters.
     */
    @Test
    public void testFDistributionImplConstruction() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertEquals(5.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(10.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests {@link FDistributionImpl#cumulativeProbability(double)} with valid
     * input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double result = fd.cumulativeProbability(2.5);
        double expectedResult = Beta.regularizedBeta(12.5, 2.5 * 5.0 / 12.5, 5.0 / 2.0);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests {@link FDistributionImpl#cumulativeProbability(double)} with invalid
     * input (a non-positive value).
     */
    @Test(expected = MathException.class)
    public void testCumulativeProbabilityInvalidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.cumulativeProbability(-1.0);
    }

    /**
     * Tests {@link FDistributionImpl#inverseCumulativeProbability(double)} with
     * valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double result = fd.inverseCumulativeProbability(0.95);
        double expectedResult = 1.6705389936767185;
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests {@link FDistributionImpl#inverseCumulativeProbability(double)} with
     * invalid input (a value less than or equal to 0).
     */
    @Test
    public void testInverseCumulativeProbabilityInvalidInput() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(0.0));
        assertThrows(MathException.class, () -> fd.inverseCumulativeProbability(-1.0));
    }

    /**
     * Tests {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} with
     * valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInput() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setNumeratorDegreesOfFreedom(10.0);
        assertEquals(10.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)} with
     * invalid input (a non-positive value).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomInvalidInput() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Tests {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}
     * with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInput() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setDenominatorDegreesOfFreedom(20.0);
        assertEquals(20.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} with
     * invalid input (a non-positive value).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomInvalidInput() {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        fd.setDenominatorDegreesOfFreedom(-1.0);
    }
}