package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Test;

/**
 * Test for FDistributionImpl class.
 */
public class FDistributionImplTest extends AbstractMathTestCase {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // check that instance variables have been set
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 1e-14);
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test cumulative probability.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // typical use case
        double p = 0.5;
        double x = distribution.inverseCumulativeProbability(p);
        double cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // edge case: p=0
        p = 0.0;
        x = distribution.inverseCumulativeProbability(p);
        cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // edge case: p=1
        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // error case: p<0
        try {
            p = -0.1;
            x = distribution.inverseCumulativeProbability(p);
            cdf = distribution.cumulativeProbability(x);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test inverse cumulative probability.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // typical use case
        double p = 0.5;
        double x = distribution.inverseCumulativeProbability(p);
        double cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // edge case: p=0
        p = 0.0;
        x = distribution.inverseCumulativeProbability(p);
        cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // edge case: p=1
        p = 1.0;
        x = distribution.inverseCumulativeProbability(p);
        cdf = distribution.cumulativeProbability(x);
        assertEquals(p, cdf, 1e-14);
        // error case: p<0
        try {
            p = -0.1;
            x = distribution.inverseCumulativeProbability(p);
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected
        }
        // error case: p>1
        try {
            p = 1.1;
            x = distribution.inverseCumulativeProbability(p);
            fail();
        } catch (MathException e) {
            // expected
        }
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // check that instance variable has been set
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 1e-14);
        // test set and get
        double newDegreesOfFreedom = 5.0;
        distribution.setNumeratorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(newDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        // check that instance variable has been set
        assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 1e-14);
        // test set and get
        double newDegreesOfFreedom = 6.0;
        distribution.setDenominatorDegreesOfFreedom(newDegreesOfFreedom);
        assertEquals(newDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 1e-14);
    }

    /**
     * Test Beta function.
     */
    @Test
    public void testBetaFunction() {
        double a = 1.5;
        double b = 2.5;
        double expected = Beta.regularizedBeta(a * 0.1, 0.5 * a, 0.5 * b);
        double result = new Beta().regularizedBeta(a * 0.1, 0.5 * a, 0.5 * b);
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test inverse CDF.
     */
    @Test
    public void testInverseCdf() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        double p = 0.5;
        double result = distribution.inverseCumulativeProbability(p);
        double expected = Beta.regularizedBeta((2.0 * result) / (4.0 + 2.0 * result),
                0.5 * 2.0, 0.5 * 4.0);
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test CDF.
     */
    @Test
    public void testCdf() throws MathException {
        FDistribution distribution = new FDistributionImpl(2.0, 4.0);
        double x = 2.5;
        double result = distribution.cumulativeProbability(x);
        double expected = Beta.cumulativeProbability(x / 2.0, 0.5 * 2.0, 0.5 * 4.0);
        assertEquals(expected, result, 1e-14);
    }
}