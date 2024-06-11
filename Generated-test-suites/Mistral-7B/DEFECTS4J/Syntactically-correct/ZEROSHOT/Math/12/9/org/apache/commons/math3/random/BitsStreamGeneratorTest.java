package org.apache.commons.math3.random;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.eq;;

/**
 * Tests for the {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** A test instance of the class under test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test. Creates a new instance of the class under test.
     */
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests the {@link #nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        final boolean expectedTrue  = true;
        final boolean expectedFalse = false;

        for (int i = 0; i < 10; ++i) {
            final boolean result = generator.nextBoolean();
            if (i % 2 == 0) {
                assertTrue("Expected true, but got false.", result == expectedTrue);
            } else {
                assertTrue("Expected false, but got true.", result == expectedFalse);
            }
        }
    }

    /**
     * Tests the {@link #nextBytes(byte[])} method.
     */
    @Test
    public void testNextBytes() {
        final int size = 20;
        final byte[] expected = new byte[size];
        generator.nextBytes(expected);

        for (int i = 0; i < size; ++i) {
            final byte byteI = expected[i];
            final int index  = i * 8;
            for (int j = 0; j < 8; ++j) {
                final int bit  = (1 << (7 - j));
                final int mask  = 1 << (7 - j);
                final int bitPos = index + j;
                final boolean expectedBit = (byteI & mask) != 0;

                final byte resultByte = generator.nextByte();
                final int resultBit   = (resultByte >> j) & 1;

                assertTrue("Expected bit " + expectedBit + " at position " + bitPos + ", but got " + resultBit,
                          eq(expectedBit, resultBit));
            }
        }

        // Test for overflow
        final byte[] output = new byte[1];
        try {
            generator.nextBytes(output);
            fail("Expected an ArrayIndexOutOfBoundsException.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            // expected
        }
    }

    /**
     * Tests the {@link #nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;

        for (int i = 0; i < 10; ++i) {
            final double result = generator.nextDouble();
            assertTrue("Expected a number between " + expectedMin + " and " + expectedMax + ", but got " + result,
                      abs(result - expectedMin) <= abs(expectedMax - expectedMin));
        }
    }

    /**
     * Tests the {@link #nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;

        for (int i = 0; i < 10; ++i) {
            final float result = generator.nextFloat();
            assertTrue("Expected a number between " + expectedMin + " and " + expectedMax + ", but got " + result,
                      abs(result - expectedMin) <= abs(expectedMax - expectedMin));
        }
    }

    /**
     * Tests the {@link #nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStd = 1.0;

        for (int i = 0; i < 10; ++i) {
            final double result = generator.nextGaussian();
            assertTrue("Expected a normal distributed number with mean " + expectedMean
                      + " and standard deviation " + expectedStd + ", but got " + result,
                      abs(result - expectedMean) <= expectedStd);
        }
    }

    /**
     * Tests the {@link #nextInt()} method.
     */
    @Test
    public void testNextInt() {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;

        for (int i = 0; i < 10; ++i) {
            final int result = generator.nextInt();
            assertTrue("Expected a number between " + expectedMin + " and " + expectedMax + ", but got " + result,
                      result >= expectedMin && result <= expectedMax);
        }

        // Test for negative values
        try {
            final int result = generator.nextInt(-1);
            fail("Expected an IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests the {@link #nextInt(int)} method.
     */
    @Test
    public void testNextIntLimited() {
        final int limit = 10;
        final int expectedMin = 0;
        final int expectedMax = limit - 1;

        for (int i = 0; i < 10; ++i) {
            final int result = generator.nextInt(limit);
            assertTrue("Expected a number between " + expectedMin + " and " + expectedMax + ", but got " + result,
                      result >= expectedMin && result <= expectedMax);
        }

        // Test for zero as limit
        try {
            final int result = generator.nextInt(0);
            fail("Expected an IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests the {@link #nextLong()} method.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;

        for (int i = 0; i < 10; ++i) {
            final long result = generator.nextLong();
            assertTrue("Expected a number between " + expectedMin + " and " + expectedMax + ", but got " + result,
                      result >= expectedMin && result <= expectedMax);
        }
    }

    /**
     * Tests the {@link #setSeed(int)} method.
     */
    @Test
    public void testSetSeedInt() {
        final int seed = 12345;
        generator.nextBoolean(); // consume next seed

        generator.setSeed(seed);
        final byte[] output = new byte[1];
        generator.nextBytes(output);
        final long expectedSeed = Long.parseLong(new String(output), 16);
        assertEquals("Expected seed " + seed + ", but got " + expectedSeed, seed, expectedSeed);
    }

    /**
     * Tests the {@link #setSeed(int[])} method.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3 };
        generator.nextBoolean(); // consume next seed

        generator.setSeed(seed);
        final byte[] output = new byte[seed.length * 4];
        generator.nextBytes(output);

        for (int i = 0; i < seed.length; ++i) {
            final long expected = Long.parseLong(new String(new byte[] { seed[i] }), 16);
            final long result   = Long.parseLong(new String(output, i * 4, 8), 16);
            assertEquals("Expected seed " + seed[i] + " at index " + i + ", but got " + result,
                         expected, result);
        }
    }

    /**
     * Tests the {@link #setSeed(long)} method.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 7L;
        generator.nextBoolean(); // consume next seed

        generator.setSeed(seed);
        final byte[] output = new byte[8];
        generator.nextBytes(output);

        final long expectedSeed = seed;
        final long result       = Long.parseLong(new String(output), 16);
        assertEquals("Expected seed " + seed + ", but got " + result, seed, result);
    }

    /**
     * Tests the {@link #clear()} method.
     */
    @Test
    public void testClear() {
        final int numGaussiansGenerated = 10;
        for (int i = 0; i < numGaussiansGenerated; ++i) {
            generator.nextGaussian();
        }

        generator.clear();
        final double generatedGaussian = generator.nextGaussian();
        assertTrue("Expected NaN, but got " + generatedGaussian, Double.isNaN(generatedGaussian));
    }
}