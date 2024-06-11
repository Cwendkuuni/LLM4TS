package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FServiceRegistry;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;

import org.junit.Test;

/**
 * Test for {@link FDistributionImpl} class.
 */
public class FDistributionImplTest {

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#cumulativeProbability(double)} using valid
     * input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double x = 2.5;
        double expected = Beta.regularizedBeta(12.5, 2.5, 15.0);
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#cumulativeProbability(double)} using edge
     * cases.
     */
    @Test
    public void testCumulativeProbabilityEdgeCases() {
        FDistribution distribution = new FDistributionImpl(0.5, 0.5);
        double xMin = 0.0;
        double expectedMin = 0.0;
        double actualMin = distribution.cumulativeProbability(xMin);
        assertEquals(expectedMin, actualMin, 1e-8);

        double xMax = Double.POSITIVE_INFINITY;
        double expectedMax = 1.0;
        double actualMax = distribution.cumulativeProbability(xMax);
        assertEquals(expectedMax, actualMax, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#cumulativeProbability(double)} using error
     * cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testCumulativeProbabilityErrorCases() {
        FDistribution distribution = new FDistributionImpl(-5.0, 10.0);

        double x = 2.5;
        distribution.cumulativeProbability(x);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#inverseCumulativeProbability(double)} using
     * valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double p = 0.95;
        double expected = 2.553233725482135;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#inverseCumulativeProbability(double)} using
     * edge cases.
     */
    @Test
    public void testInverseCumulativeProbabilityEdgeCases() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double pMin = 0.0;
        double expectedMin = 0.0;
        double actualMin = distribution.inverseCumulativeProbability(pMin);
        assertEquals(expectedMin, actualMin, 1e-8);

        double pMax = 1.0;
        double expectedMax = Double.POSITIVE_INFINITY;
        double actualMax = distribution.inverseCumulativeProbability(pMax);
        assertEquals(expectedMax, actualMax, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * and {@link FDistributionImpl#inverseCumulativeProbability(double)} using
     * error cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityErrorCases() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        double p = -0.1;
        distribution.inverseCumulativeProbability(p);
    }

    /**
     * Test method for
     * {@link FDistributionImpl#FDistributionImpl(double, double)}
     * constructor with negative input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNegativeInput() {
        new FDistributionImpl(-5.0, 10.0);
    }

    /**
     * Test method for
     * {@link FDistributionImpl#FDistributionImpl(double, double)}
     * constructor with non-positive input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructorNonPositiveInput() {
        new FDistributionImpl(0.0, 10.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#getNumeratorDegreesOfFreedom()} using valid
     * input.
     */
    @Test
    public void testSetGetNumeratorDegreesOfFreedomValidInput() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        double expected = 5.0;
        double actual = distribution.getNumeratorDegreesOfFreedom();
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#getNumeratorDegreesOfFreedom()} using error
     * cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetGetNumeratorDegreesOfFreedomErrorCases() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(-5.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#getDenominatorDegreesOfFreedom()} using valid
     * input.
     */
    @Test
    public void testSetGetDenominatorDegreesOfFreedomValidInput() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        double expected = 5.0;
        double actual = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#getDenominatorDegreesOfFreedom()} using error
     * cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetGetDenominatorDegreesOfFreedomErrorCases() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setDenominatorDegreesOfFreedom(-5.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} using
     * valid input.
     */
    @Test
    public void testSetBothDegreesOfFreedomValidInput() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        double expectedNumerator = 5.0;
        double expectedDenominator = 5.0;
        double actualNumerator = distribution.getNumeratorDegreesOfFreedom();
        double actualDenominator = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(expectedNumerator, actualNumerator, 1e-8);
        assertEquals(expectedDenominator, actualDenominator, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} using
     * error cases.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetBothDegreesOfFreedomErrorCases() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(-5.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}
     * and {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)} using
     * edge cases.
     */
    @Test
    public void testSetBothDegreesOfFreedomEdgeCases() {
        FDistribution distribution = new FDistributionImpl(10.0, 10.0);
        distribution.setNumeratorDegreesOfFreedom(0.0);
        distribution.setDenominatorDegreesOfFreedom(5.0);
        double expectedNumerator = 0.0;
        double expectedDenominator = 5.0;
        double actualNumerator = distribution.getNumeratorDegreesOfFreedom();
        double actualDenominator = distribution.getDenominatorDegreesOfFreedom();
        assertEquals(expectedNumerator, actualNumerator, 1e-8);
        assertEquals(expectedDenominator, actualDenominator, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}
     * with large values using {@link FServiceRegistry}.
     */
    @Test
    public void testCumulativeProbabilityLargeValues() {
        FServiceRegistry registry = new FServiceRegistry();
        FDistribution distribution = registry.getFDistribution(100.0, 200.0);
        double x = 1000.0;
        double expected = Beta.regularizedBeta(102000.0, 50100.0, 202000.0);
        double actual = distribution.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-8);
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}
     * with large values using {@link FServiceRegistry}.
     */
    @Test
    public void testInverseCumulativeProbabilityLargeValues() {
        FServiceRegistry registry = new FServiceRegistry();
        FDistribution distribution = registry.getFDistribution(100.0, 200.0);
        double p = 0.999;
        double expected = 1000.0;
        double actual = distribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 1e-8);
    }
}