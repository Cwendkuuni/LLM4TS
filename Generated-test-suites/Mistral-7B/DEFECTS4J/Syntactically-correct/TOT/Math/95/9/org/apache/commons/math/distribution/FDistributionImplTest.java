package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistType;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Tests for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        assertEquals(FDistType.F_DISTRIBUTION, distribution.getDistributionType());
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test cumulative probability with valid input.
     */
    @Test
    public void testCumulativeProbabilityValidInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 0.25;
        double expected = 0.1162925;
        double result = 0.0;
        try {
            result = distribution.cumulativeProbability(Beta.regularizedBeta(3.0 * 0.25 / (4.0 + 3.0 * 0.25), 1.5, 1.5));
        } catch (MathException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(expected, result, 1e-6);
        p = 0.5;
        expected = 0.5;
        result = distribution.cumulativeProbability(distribution.inverseCumulativeProbability(p));
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test cumulative probability with negative input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.cumulativeProbability(-1.0);
    }

    /**
     * Test inverse cumulative probability with invalid input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.inverseCumulativeProbability(-1.0);
    }

    /**
     * Test inverse cumulative probability with valid input.
     */
    @Test
    public void testInverseCumulativeProbabilityValidInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        double p = 0.25;
        double x = distribution.inverseCumulativeProbability(p);
        double expected = 0.1162925;
        double epsilon = 1e-6;
        double result = 0.0;
        try {
            result = distribution.cumulativeProbability(x);
        } catch (MathException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(Math.abs(result - p) < epsilon);
        assertEquals(expected, x, epsilon);

        p = 0.5;
        x = distribution.inverseCumulativeProbability(p);
        assertEquals(0.5, x, epsilon);
    }

    /**
     * Test setNumeratorDegreesOfFreedom with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedomNegativeInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }

    /**
     * Test setNumeratorDegreesOfFreedom with valid input.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedomValidInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom with negative input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDenominatorDegreesOfFreedomNegativeInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(-1.0);
    }

    /**
     * Test setDenominatorDegreesOfFreedom with valid input.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedomValidInput() {
        FDistribution distribution = new FDistributionImpl(3.0, 4.0);
        distribution.setDenominatorDegreesOfFreedom(6.0);
        assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }
}