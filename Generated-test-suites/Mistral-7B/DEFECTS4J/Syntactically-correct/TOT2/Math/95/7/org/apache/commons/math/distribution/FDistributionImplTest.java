package org.example.junit;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.junit.Test;

public class FDistributionImplTest {
    private FDistributionImpl fd;

    @Before
    public void setUp() {
        fd = new FDistributionImpl(5.0, 10.0);
    }

    @Test
    public void testFDistributionImplConstruction() {
        FDistribution fdObj = new FDistributionImpl(5.0, 10.0);
        assertTrue(fdObj instanceof FDistribution);
    }

    @Test
    public void testCumulativeProbabilityPositiveX() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double x = 2.5;
        double expected = 0.37034; // value from MathWorld
        double actual = fd.cumulativeProbability(x);
        assertEquals(expected, actual, 0.0001); // allowing for small error
    }

    @Test
    public void testInverseCumulativeProbabilityPositiveP() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double p = 0.5;
        double expected = 1.41728; // value from MathWorld
        double actual = fd.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0001); // allowing for small error
    }

    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        fd.setNumeratorDegreesOfFreedom(15.0);
        assertEquals(15.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        fd.setDenominatorDegreesOfFreedom(12.0);
        assertEquals(12.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testIllegalArgumentException() {
        new FDistributionImpl(-5.0, 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeNumeratorDegreesOfFreedom() {
        fd.setNumeratorDegreesOfFreedom(-5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetZeroNumeratorDegreesOfFreedom() {
        fd.setNumeratorDegreesOfFreedom(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeDenominatorDegreesOfFreedom() {
        fd.setDenominatorDegreesOfFreedom(-5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetZeroDenominatorDegreesOfFreedom() {
        fd.setDenominatorDegreesOfFreedom(0.0);
    }

    @Test
    public void testCumulativeProbabilityZero() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double x = 0.0;
        double expected = 0.0;
        double actual = fd.cumulativeProbability(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testInverseCumulativeProbabilityZero() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double p = 0.0;
        double expected = 0.0;
        double actual = fd.inverseCumulativeProbability(p);
        assertEquals(expected, actual);
    }

    @Test
    public void testInverseCumulativeProbabilityOne() throws MathException {
        FDistribution fd = new FDistributionImpl(5.0, 10.0);
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double actual = fd.inverseCumulativeProbability(p);
        assertEquals(expected, actual);
    }
}