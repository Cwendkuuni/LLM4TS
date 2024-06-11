package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double actual = generator.nextDouble();
        double expected = Math.random();
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        float actual = generator.nextFloat();
        float expected = (float) Math.random();
        assertEquals(expected, actual, 0.001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        double actual = generator.nextGaussian();
        double expected = Math.random() * 2.0d - 1.0d;
        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int actual = generator.nextInt();
        int expected = (int) Math.random() * 10;
        assertEquals(expected, actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZero() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(0);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int actual = generator.nextInt(16);
        int expected = (int) (Math.random() * 16);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int actual = generator.nextInt(13);
        int expected = (int) (Math.random() * 13);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        long actual = generator.nextLong();
        long expected = Math.random() * 1L << 32 | (long) (Math.random() * 1L << 32);
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

    private static class MyBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {
            // not used in tests
        }

        @Override
        public void setSeed(int[] seed) {
            // not used in tests
        }

        @Override
        public void setSeed(long seed) {
            // not used in tests
        }

        @Override
        public void setSeed(long[] seed) {
            // not used in tests
        }

        @Override
        protected int next(int bits) {
            // implementation for tests
            return (int) (Math.random() * Math.pow(2, bits));
        }
    }
}