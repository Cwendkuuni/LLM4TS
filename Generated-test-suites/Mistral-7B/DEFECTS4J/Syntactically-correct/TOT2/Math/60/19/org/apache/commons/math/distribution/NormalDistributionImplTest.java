package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);
    }

    /**
     * Test constructor with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test density method.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 1.0;
        double density = dist.density(x);
        double expected = FastMath.exp(-0.5 * x * x) / (1 * SQRT2PI);
        assertEquals(expected, density, 1e-9);
    }

    /**
     * Test cumulativeProbability method for valid arguments.
     */
    @Test
    public void testCumulativeProbabilityValidArguments() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double x = 0;
        double p = dist.cumulativeProbability(x);
        double expected = 0.5;
        assertEquals(expected, p, 1e-9);
    }

    /**
     * Test cumulativeProbability method for extreme values.
     */
    @Test
    public void testCumulativeProbabilityExtremeValues() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double xMinus20StdDev = Mean.ZERO.doubleValue() - 20 * StandardDeviation.ONE;
        double xPlus20StdDev = Mean.ZERO.doubleValue() + 20 * StandardDeviation.ONE;
        double pMin = dist.cumulativeProbability(xMinus20StdDev);
        double pMax = dist.cumulativeProbability(xPlus20StdDev);
        assertTrue(Precision.isClose(0, pMin, 1e-9));
        assertTrue(Precision.isClose(1, pMax, 1e-9));
    }

    /**
     * Test inverseCumulativeProbability method for valid arguments.
     */
    @Test
    public void testInverseCumulativeProbabilityValidArguments() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.75;
        double x = dist.inverseCumulativeProbability(p);
        double expectedX = FastMath.erfInv(2 * p - 1) * StandardDeviation.ONE + Mean.ZERO.doubleValue();
        assertEquals(expectedX, x, 1e-9);
    }

    /**
     * Test inverseCumulativeProbability method for extreme values.
     */
    @Test
    public void testInverseCumulativeProbabilityExtremeValues() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double pMin = 0;
        double pMax = 1;
        double xMin = dist.inverseCumulativeProbability(pMin);
        double xMax = dist.inverseCumulativeProbability(pMax);
        assertTrue(Double.isNaN(xMin));
        assertTrue(Double.isNaN(xMax));
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(dist.density(sample) > 0);
    }
}