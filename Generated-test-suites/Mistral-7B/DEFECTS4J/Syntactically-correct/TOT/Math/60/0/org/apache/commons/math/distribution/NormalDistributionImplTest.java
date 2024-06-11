package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.apache.commons.math3.util.Precision;

public class NormalDistributionImplTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, dist.getMean(), 0.0);
        assertEquals(1.0, dist.getStandardDeviation(), 0.0);
    }

    /**
     * Test constructor with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplNegativeStandardDeviation() {
        new NormalDistributionImpl(0.0, -1.0);
    }

    /**
     * Test cumulative probability method with several inputs.
     */
    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test values within normal range
        assertEquals(0.154308063481525, dist.cumulativeProbability(0.5), 0.001);
        assertEquals(0.5, dist.cumulativeProbability(0.5), 0.0);
        assertEquals(0.845691936518475, dist.cumulativeProbability(1.5), 0.001);

        // Test values at extremes
        assertEquals(0.0, dist.cumulativeProbability(-10), 0.0);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0);
    }

    /**
     * Test inverse cumulative probability method with several inputs.
     */
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test values within normal range
        assertEquals(1.28453528253295, dist.inverseCumulativeProbability(0.15865525), 0.001);
        assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.0);
        assertEquals(1.28453528253295, dist.inverseCumulativeProbability(1.0), 0.001);

        // Test values at extremes
        assertTrue(Double.isInfinite(dist.inverseCumulativeProbability(0.0)));
        assertTrue(Double.isInfinite(dist.inverseCumulativeProbability(1.0)));
    }

    /**
     * Test density method with several inputs.
     */
    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        // Test values within normal range
        assertEquals(0.398942280401433, dist.density(0.5), 0.001);
        assertEquals(0.0, dist.density(3.0), 0.001);

        // Test values at extremes
        assertTrue(Double.isNaN(dist.density(-10)));
        assertTrue(Double.isNaN(dist.density(10)));
    }

    /**
     * Test sample method with several inputs.
     */
    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0.0, 1.0);

        RandomData rand = new RandomData();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(Precision.isClose(dist.density(sample), 1.0 / (SQRT2PI * dist.getStandardDeviation()), 0.01));
            assertEquals(dist.getMean(), sample, 0.01);
        }
    }
}