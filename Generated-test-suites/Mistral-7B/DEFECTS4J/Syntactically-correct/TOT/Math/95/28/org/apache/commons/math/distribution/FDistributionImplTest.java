package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FStudentT;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;

/**
 * Test cases for {@link FDistributionImpl}.
 */
public class FDistributionImplTest {

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}.
     */
    @Test
    public void testFDistributionImpl() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#cumulativeProbability(double)}.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        double[] xValues = new double[] { 0.0, 5.0, 10.0, 15.0 };
        double[] expectedValues = new double[] { 0.0, 0.9173158055585888,
                0.9968466761224854, 0.9999729773987241 };
        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double p = fDist.cumulativeProbability(x);
            assertEquals(expectedValues[i], p, 1e-8);
        }
    }

    /**
     * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        double[] pValues = new double[] { 0.0, 0.5, 0.9, 0.99 };
        double[] expectedValues = new double[] { 0.0, 1.5, 4.54748376701729,
                12.63365355074626 };
        for (int i = 0; i < pValues.length; i++) {
            double p = pValues[i];
            double x = fDist.inverseCumulativeProbability(p);
            assertEquals(expectedValues[i], x, 1e-8);
        }

        // Test with negative probability
        assertThrows(MathException.class, () -> fDist.inverseCumulativeProbability(-0.1));

        // Test with probability greater than 1
        assertThrows(MathException.class, () -> fDist.inverseCumulativeProbability(1.1));
    }

    /**
     * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        fDist.setNumeratorDegreesOfFreedom(5.0);
        assertEquals(5.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}.
     */
    @Test
    public void testSetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        fDist.setDenominatorDegreesOfFreedom(4.0);
        assertEquals(4.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#getNumeratorDegreesOfFreedom()}.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#getDenominatorDegreesOfFreedom()}.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    /**
     * Test method for FDistribution with negative and zero degrees of freedom.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAndZeroDOF() {
        new FDistributionImpl(-2.0, 0.0);
    }

    /**
     * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}
     * with F-student t distribution.
     */
    @Test
    public void testFDistributionWithFStudentT() {
        FDistribution fDist = new FDistributionImpl(2.0, 3.0);
        FStudentT studentTDist = new FStudentT(5.0);
        double[] xValues = new double[] { 0.0, 5.0, 10.0, 15.0 };
        double[] expectedValues = new double[] { 0.0, 0.913458589844772,
                0.993326731726549, 0.9996927235094016 };
        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double p = studentTDist.cumulativeProbability(x);
            double fDistValue = fDist.cumulativeProbability(x);
            assertEquals(expectedValues[i], fDistValue, 1e-8);
        }
    }

    /**
     * Test method for FDistribution with non-integer degrees of freedom.
     */
    @Test
    public void testFDistributionWithNonIntegerDOF() {
        FDistribution fDist = new FDistributionImpl(3.5, 4.5);
        double[] xValues = new double[] { 0.0, 5.0, 10.0, 15.0 };
        double[] expectedValues = new double[] { 0.0, 0.934349106898347,
                0.996576850326657, 0.9999389942557625 };
        for (int i = 0; i < xValues.length; i++) {
            double x = xValues[i];
            double p = fDist.cumulativeProbability(x);
            assertEquals(expectedValues[i], p, 1e-8);
        }
    }
}