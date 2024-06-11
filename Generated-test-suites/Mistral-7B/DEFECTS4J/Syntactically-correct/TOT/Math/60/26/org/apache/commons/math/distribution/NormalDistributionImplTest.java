package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Test;

/**
 * Test cases for {@link NormalDistributionImpl}.
 */
public class NormalDistributionImplTest {

    /**
     * Test construction with valid arguments.
     */
    @Test
    public void testConstruction() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    /**
     * Test construction with negative standard deviation.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    /**
     * Test density calculation.
     */
    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double result = dist.density(1);
        assertEquals(0.39894228040143271, result, 0.0000001);
    }

    /**
     * Test cumulative probability calculation.
     */
    @Test
    public void testCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.5;
        double result = dist.cumulativeProbability(0);
        assertEquals(0, result, 0);

        result = dist.cumulativeProbability(1);
        assertEquals(0.84134474606848275, result, 0.0000001);

        result = dist.cumulativeProbability(p);
        assertEquals(p, result, 0.0000001);
    }

    /**
     * Test inverse cumulative probability calculation.
     */
    @Test
    public void testInverseCumulativeProbability() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);

        // Test edge cases
        double p = 0;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0);

        p = 1;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 0);

        // Test typical case
        p = 0.5;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(0, result, 0);

        // Test error cases
        try {
            dist.inverseCumulativeProbability(-0.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            dist.inverseCumulativeProbability(1.1);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    /**
     * Test sampling.
     */
    @Test
    public void testSampling() throws Exception {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        RandomDataGenerator rng = new RandomDataGenerator();

        for (int i = 0; i < 100; i++) {
            double result = dist.sample();
            double mean = dist.getMean();
            double stdev = dist.getStandardDeviation();
            double lowerBound = mean - 3 * stdev;
            double upperBound = mean + 3 * stdev;

            assertTrue(result >= lowerBound);
            assertTrue(result <= upperBound);
        }
    }
}