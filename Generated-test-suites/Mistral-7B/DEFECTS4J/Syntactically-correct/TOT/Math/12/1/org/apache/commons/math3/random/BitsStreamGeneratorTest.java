package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{0x2a, 0x5c, 0x5d, 0x1a, 0x2d, 0x3a, 0x63, 0x7a, 0x74, 0x68}, bytes);
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(0.4662486692385396, generator.nextDouble(), 0.0000001);
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(0.64220456f, generator.nextFloat(), 0.000001f);
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(1.23456789, generator.nextGaussian(), 0.0001);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(1, generator.nextInt(2));
        assertEquals(-1, generator.nextInt(2) ^ 0xffffffff);
        assertEquals(Integer.MAX_VALUE >> 1, generator.nextInt(Integer.MAX_VALUE >> 1));
        try {
            generator.nextInt(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntSpecific() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(17, generator.nextInt(255));
        assertEquals(-128, generator.nextInt(128) ^ 0xffffff80);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        long expected = 0x123456789abcdefL;
        assertEquals(expected, generator.nextLong());
        assertEquals(expected ^ 0xffffffffffffL, generator.nextLong() ^ 0xffffffffffffL);
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextDouble();
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian());
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(123456789);
        long expected = 0x123456789L;
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        final int[] seed = {1, 2, 3};
        generator.setSeed(seed);
        long expected = 0x102030L;
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(123456789123L);
        long expected = 0x123456789123L;
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
        assertEquals(expected, generator.nextLong() ^ 0xffffffffffffL);
    }

}