package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes1 = new byte[10];
        byte[] bytes2 = new byte[10];
        generator.nextBytes(bytes1);
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);

        byte[] bytes3 = new byte[0];
        generator.nextBytes(bytes3);
        assertEquals(0, bytes3.length);

        byte[] bytes4 = new byte[1];
        generator.nextBytes(bytes4);
        assertEquals(1, bytes4.length);
        assertEquals(0, bytes4[0] & 0xff);

        byte[] bytes5 = new byte[Integer.MAX_VALUE];
        generator.nextBytes(bytes5);
        assertEquals(Integer.MAX_VALUE, bytes5.length);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; i++) {
            float result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 1000; i++) {
            int result = generator.nextInt();
            assertTrue(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
        }

        for (int n = 1; n <= 32; n++) {
            int result = generator.nextInt(1 << n);
            int mask  = (1 << n) - 1;
            assertTrue((result & mask) == result);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimits() {
        int[] limits = new int[]{-128, 127, Integer.MIN_VALUE, Integer.MAX_VALUE / 2, Integer.MAX_VALUE};
        for (int n : limits) {
            try {
                generator.nextInt(n);
                fail("Exception expected for " + n);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }

        for (int n = 1; n <= limits.length; n++) {
            int result = generator.nextInt(limits[n]);
            assertTrue(result >= 0 && result < limits[n]);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 1000; i++) {
            long result = generator.nextLong();
            assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedWithInt() {
        int seed = 12345;
        generator.setSeed(seed);
        long expected = 0x123456789012345L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedWithIntArray() {
        int[] seeds = new int[]{12345, 24680};
        generator.setSeed(seeds);
        long expected = 0x123452468012345L;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedWithLong() {
        long seed = 0x123456789012345L;
        generator.setSeed(seed);
        long expected = seed;
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

}