package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        // nothing to test
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        byte[] result = new byte[4];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        double expected = 0.123456789;
        double result = generator.nextDouble();
        assertEquals(expected, result, 0.0000001);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        float expected = 0.123456f;
        float result = generator.nextFloat();
        assertEquals(expected, result, 0.00001f);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        int numTrials = 10000;
        double sum = 0.0;
        for (int i = 0; i < numTrials; i++) {
            sum += Math.pow(generator.nextGaussian() - expectedMean, 2);
        }
        double variance = sum / numTrials;
        assertEquals(expectedStdDev * expectedStdDev, variance, 0.01);
        assertEquals(expectedMean, generator.nextGaussian(), 0.01);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        assertEquals(0, generator.nextInt());
        assertEquals(1, generator.nextInt());
        assertEquals(Integer.MAX_VALUE, generator.nextInt());
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
        assertEquals(42, generator.nextInt());
        assertEquals(42, generator.nextInt());
        assertEquals(42, generator.nextInt());
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntN() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        int expected = 42;
        generator.nextInt(10);
        assertEquals(expected, generator.nextInt(2));
        assertEquals(expected, generator.nextInt(3));
        assertEquals(expected, generator.nextInt(5));
        assertEquals(expected, generator.nextInt(10));
        assertEquals(expected, generator.nextInt(10000));
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        long expected = 0x1234567890ABCDEFL;
        long result = generator.nextLong();
        assertEquals(expected, result);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        generator.setSeed(1);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        int[] seed = new int[1];
        generator.setSeed(seed);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        long seed = 0x1234567890ABCDEF1L;
        generator.setSeed(seed);
        long expected = seed;
        long result = generator.nextLong();
        assertEquals(expected, result);
        generator.nextLong();
        generator.nextLong();
        assertEquals(expected, generator.nextLong());
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new TestBitsStreamGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

    /**
     * Test bits stream generator.
     */
    private static class TestBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        protected int next(int bits) {
            return 42;
        }
    }

}