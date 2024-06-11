package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwisterFast();
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // ... add more tests if needed
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        final double value       = generator.nextDouble();
        assertEquals(expectedMin, value, 0.0);
        final double value2 = generator.nextDouble();
        assertEquals(expectedMax, value2, 0.0);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        final float value = generator.nextFloat();
        assertEquals(expectedMin, value, 0.0f);
        final float value2 = generator.nextFloat();
        assertEquals(expectedMax, value2, 0.0f);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;
        final double mean;
        final double stdDev;
        for (int i = 0; i < 100_000; i++) {
            final double value = generator.nextGaussian();
            mean += value;
            stdDev += value * value;
        }
        mean /= 100_000.0;
        stdDev /= 100_000.0;
        assertEquals(expectedMean, mean, 0.001);
        assertEquals(expectedStdDev, FastMath.sqrt(stdDev), 0.01);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int minValue = Integer.MIN_VALUE;
        final int maxValue = Integer.MAX_VALUE;
        final int value = generator.nextInt();
        assertTrue(value >= minValue);
        final int value2 = generator.nextInt();
        assertTrue(value2 <= maxValue);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        final int limit = 10;
        for (int i = 0; i < 100; i++) {
            final int value = generator.nextInt(limit);
            assertTrue(value >= 0);
            assertTrue(value < limit);
        }
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long minValue = Long.MIN_VALUE;
        final long maxValue = Long.MAX_VALUE;
        final long value = generator.nextLong();
        assertTrue(value >= minValue);
        final long value2 = generator.nextLong();
        assertTrue(value2 <= maxValue);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedWithInt() {
        final int seed = 123;
        generator.nextInt(); // prime the RNG
        generator.setSeed(seed);
        final int value = generator.nextInt();
        final int seed2 = generator.nextInt();
        assertEquals(seed, seed2);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedWithIntArray() {
        final int[] seeds = { 1, 2, 3 };
        generator.nextInt(); // prime the RNG
        generator.setSeed(seeds);
        final int[] seeds2 = generator.nextIntArray(3);
        assertArrayEquals(seeds, seeds2);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedWithLong() {
        final long seed = 123;
        generator.nextInt(); // prime the RNG
        generator.setSeed(seed);
        final long seed2 = generator.nextLong();
        assertEquals(seed, seed2);
        // add more tests for edge cases and error scenarios
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
        // add more tests for edge cases and error scenarios
    }
}