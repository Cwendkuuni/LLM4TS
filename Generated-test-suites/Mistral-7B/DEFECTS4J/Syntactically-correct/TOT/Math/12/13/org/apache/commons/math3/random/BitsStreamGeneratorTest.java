package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test object. */
    private BitsStreamGenerator generator;

    /** Set up test. */
    @Before
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(System.currentTimeMillis());
    }

    /** Test nextBoolean(). */
    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    /** Test nextBytes(). */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04}, bytes);
        generator.nextBytes(new byte[0]);
    }

    /** Test nextDouble(). */
    @Test
    public void testNextDouble() {
        double expected = 0.23456789;
        double actual = generator.nextDouble();
        assertEquals(expected, actual, 1e-10);
        double[] doubles = new double[100000];
        for (int i = 0; i < doubles.length; ++i) {
            doubles[i] = generator.nextDouble();
        }
        double sum = 0.0;
        for (int i = 0; i < doubles.length; ++i) {
            sum += doubles[i];
        }
        assertEquals(doubles.length, sum, 1e-10 * doubles.length);
    }

    /** Test nextFloat(). */
    @Test
    public void testNextFloat() {
        float expected = 0.456789f;
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 1e-6f);
        float[] floats = new float[10000];
        for (int i = 0; i < floats.length; ++i) {
            floats[i] = generator.nextFloat();
        }
        float sum = 0.0f;
        for (int i = 0; i < floats.length; ++i) {
            sum += floats[i];
        }
        assertEquals(floats.length, sum, 1e-6f * floats.length);
    }

    /** Test nextGaussian(). */
    @Test
    public void testNextGaussian() {
        double expectedMean = 0.0;
        double expectedSD = 1.0;
        for (int i = 0; i < 10000; ++i) {
            double actual = generator.nextGaussian();
            double mean = FastMath.mean(expectedMean, actual);
            double sd = FastMath.standardDeviation(expectedSD, actual);
            assertEquals(expectedMean, mean, 1e-3);
            assertEquals(expectedSD, sd, 1e-3);
        }
    }

    /** Test nextInt(). */
    @Test
    public void testNextInt() {
        int expected = 5;
        int actual = generator.nextInt();
        assertFalse(expected == actual);
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        int[] ints = new int[10000];
        for (int i = 0; i < ints.length; ++i) {
            ints[i] = generator.nextInt(min, max + 1);
        }
        int sum = 0;
        for (int i = 0; i < ints.length; ++i) {
            sum += ints[i];
        }
        assertEquals(min, sum);
    }

    /** Test nextInt(int). */
    @Test
    public void testNextIntNegative() {
        try {
            generator.nextInt(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /** Test nextInt(int). */
    @Test
    public void testNextIntZero() {
        try {
            generator.nextInt(0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /** Test nextInt(int, int). */
    @Test
    public void testNextIntUpperBound() {
        int expected = 10;
        int actual = generator.nextInt(1, expected + 1);
        assertEquals(expected, actual);
    }

    /** Test nextLong(). */
    @Test
    public void testNextLong() {
        long expected = 1234567890123456789L;
        long actual = generator.nextLong();
        assertFalse(expected == actual);
        long min = Long.MIN_VALUE;
        long max = Long.MAX_VALUE;
        long[] longs = new long[10000];
        for (int i = 0; i < longs.length; ++i) {
            longs[i] = generator.nextLong(min, max + 1);
        }
        long sum = 0;
        for (int i = 0; i < longs.length; ++i) {
            sum += longs[i];
        }
        assertEquals(min, sum);
    }

    /** Test setSeed(). */
    @Test
    public void testSetSeed() {
        long seed = System.currentTimeMillis();
        generator.setSeed((int) seed);
        long nextLong = generator.nextLong();
        assertEquals(seed, nextLong);
        generator.setSeed(seed);
        long nextLong2 = generator.nextLong();
        assertEquals(nextLong, nextLong2);
    }

    /** Test setSeed(int[]). */
    @Test
    public void testSetSeedArray() {
        int[] seed = new int[]{1, 2, 3};
        generator.setSeed(seed);
        long nextLong = generator.nextLong();
        assertNotSame(seed[0], nextLong);
        assertNotSame(seed[1], nextLong >> 32);
        assertNotSame(seed[2], (nextLong >> 64) & 0xffffffffL);
        generator.setSeed(seed);
        long nextLong2 = generator.nextLong();
        assertEquals(nextLong, nextLong2);
    }

    /** Test setSeed(long). */
    @Test
    public void testSetSeedLong() {
        long seed = 1234567890123456789L;
        generator.setSeed(seed);
        long nextLong = generator.nextLong();
        assertEquals(seed, nextLong);
        generator.setSeed(seed);
        long nextLong2 = generator.nextLong();
        assertEquals(nextLong, nextLong2);
    }

    /** Test clear(). */
    @Test
    public void testClear() {
        double expectedMean = 0.0;
        double expectedSD = 1.0;
        for (int i = 0; i < 100; ++i) {
            generator.nextGaussian();
            double mean = FastMath.mean(expectedMean, nextGaussian);
            double sd = FastMath.standardDeviation(expectedSD, nextGaussian);
            assertEquals(expectedMean, mean, 1e-3);
            assertEquals(expectedSD, sd, 1e-3);
        }
        generator.clear();
        for (int i = 0; i < 100; ++i) {
            double nextGaussian = generator.nextGaussian();
            double mean = FastMath.mean(expectedMean, nextGaussian);
            double sd = FastMath.standardDeviation(expectedSD, nextGaussian);
            assertEquals(Double.NaN, mean);
            assertEquals(Double.NaN, sd);
        }
    }

}