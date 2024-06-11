package org.apache.commons.math.distribution;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        // Test creation with valid parameters
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.001);
        assertEquals(1, dist.getStandardDeviation(), 0.001);

        // Test creation with invalid standard deviation
        assertThrows(NotStrictlyPositiveException.class, () -> {
            new NormalDistributionImpl(0, -1);
        });
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        // Test density at mean
        assertEquals(0.3989422804010195d, dist.density(0), 0.001);

        // Test density at standard deviation
        assertEquals(0.2419702512292755d, dist.density(1), 0.001);

        // Test density at negative value
        assertEquals(0, dist.density(-1), 0.001);

        // Test density at very large value
        assertEquals(0, dist.density(1000), 0.001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        // Test cumulative probability at mean
        assertEquals(0.5, dist.cumulativeProbability(0), 0.001);

        // Test cumulative probability at standard deviation
        assertEquals(0.8413447464558707d, dist.cumulativeProbability(1), 0.001);

        // Test cumulative probability at negative value
        assertEquals(0, dist.cumulativeProbability(-1), 0.001);

        // Test cumulative probability at very large value
        assertEquals(1, dist.cumulativeProbability(1000), 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException, OutOfRangeException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);

        // Test inverse cumulative probability at mean
        assertEquals(0, dist.inverseCumulativeProbability(0.5), 0.001);

        // Test inverse cumulative probability at standard deviation
        assertEquals(1, dist.inverseCumulativeProbability(0.8413447464558707), 0.001);

        // Test inverse cumulative probability at negative probability
        assertThrows(OutOfRangeException.class, () -> {
            dist.inverseCumulativeProbability(0);
        });

        // Test inverse cumulative probability at probability greater than 1
        assertThrows(OutOfRangeException.class, () -> {
            dist.inverseCumulativeProbability(1.2);
        });
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        RandomDataGenerator rdg = new RandomDataGenerator();

        // Test sample against density
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            double density = dist.density(sample);
            assertEquals(density, 0.3989422804010195d * Math.exp(-0.5 * Math.pow(sample, 2)), 0.001);
        }
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist1 = new NormalDistributionImpl(1, 1);
        NormalDistributionImpl dist2 = new NormalDistributionImpl(0, 1);

        assertEquals(1, dist1.getMean(), 0.001);
        assertEquals(0, dist2.getMean(), 0.001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist1 = new NormalDistributionImpl(1, 1);
        NormalDistributionImpl dist2 = new NormalDistributionImpl(0, 2);

        assertEquals(1, dist1.getStandardDeviation(), 0.001);
        assertEquals(2, dist2.getStandardDeviation(), 0.001);
    }
}