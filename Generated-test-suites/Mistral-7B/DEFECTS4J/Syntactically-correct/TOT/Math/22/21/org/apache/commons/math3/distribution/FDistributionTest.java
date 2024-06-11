package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for FDistribution class.
 */
public class FDistributionTest {

    /**
     * Test constructor with valid arguments.
     * @throws NotStrictlyPositiveException if degrees of freedom are not positive
     */
    @Test
    public void testValidConstruction() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertNotNull(fd);
    }

    /**
     * Test constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstruction() {
        new FDistribution(0.0, 5.0);
    }

    /**
     * Test getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        for (double x = 0.0; x <= 10.0; x += 1.0) {
            double density = fd.density(x);
            Assert.assertNotNull(density);
            Assert.assertTrue(density >= 0.0);
        }
    }

    /**
     * Test cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        for (double x = 0.0; x <= 10.0; x += 1.0) {
            double cdf = fd.cumulativeProbability(x);
            Assert.assertNotNull(cdf);
            Assert.assertTrue(cdf >= 0.0);
            Assert.assertTrue(cdf <= 1.0);
        }
    }

    /**
     * Test getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double mean = fd.getNumericalMean();
        Assert.assertEquals(5.5, mean, 0.001);
    }

    /**
     * Test getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double variance = fd.getNumericalVariance();
        Assert.assertEquals(1.6726142126155235, variance, 0.001);
    }

    /**
     * Test getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
    }

    /**
     * Test getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
    }

    /**
     * Test isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertTrue(fd.isSupportLowerBoundInclusive());
    }

    /**
     * Test isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertFalse(fd.isSupportUpperBoundInclusive());
    }

    /**
     * Test isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        Assert.assertTrue(fd.isSupportConnected());
    }
}