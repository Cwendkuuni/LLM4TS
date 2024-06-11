package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test implementation of {@link BitsStreamGenerator}. */
    private static class TestBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        protected int next(int bits) {
            // implementation of next method for testing purposes
            return (int) (Math.random() * (1 << bits));
        }
    }

    /** Test instance of {@link TestBitsStreamGenerator}. */
    private final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();

    /**
     * Test method {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final int count = 10000;
        final boolean[] expected = new boolean[count];
        for (int i = 0; i < count; i++) {
            expected[i] = Math.random() > 0.5;
        }
        final int countTrue = (int) Math.ceil(count * 0.5);

        final boolean[] actual = new boolean[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextBoolean();
        }

        assertTrue("Expected number of true values is not equal to actual number.", countTrue == Integer.bitCount(FastMath.nextSetBit(0, count) + 1));
        assertArrayEquals("Expected and actual arrays are not equal.", expected, actual);
    }

    /**
     * Test method {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final int size = 10;
        final byte[] expected = new byte[size];
        for (int i = 0; i < size; i++) {
            expected[i] = (byte) (Math.random() * 256);
        }

        final byte[] actual = new byte[size];
        generator.nextBytes(actual);

        assertArrayEquals("Expected and actual arrays are not equal.", expected, actual);
    }

    /**
     * Test method {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final int count = 10000;
        final double[] expected = new double[count];
        for (int i = 0; i < count; i++) {
            expected[i] = Math.random();
        }

        final double[] actual = new double[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextDouble();
        }

        assertTrue("Expected and actual arrays are not close enough.", FastMath.allEQ(expected, actual, 1e-9));
    }

    /**
     * Test method {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final int count = 10000;
        final float[] expected = new float[count];
        for (int i = 0; i < count; i++) {
            expected[i] = (float) Math.random();
        }

        final float[] actual = new float[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextFloat();
        }

        assertTrue("Expected and actual arrays are not close enough.", FastMath.allEQ(expected, actual, 1e-6f));
    }

    /**
     * Test method {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final int count = 1000;
        final double[] expected = new double[count];
        final double mean = 0.0;
        final double stdDev = 1.0;
        final double r = FastMath.SQRT2 / 2.0;
        for (int i = 0; i < count; i++) {
            double z = Math.random() * 2.0 - 1.0;
            expected[i] = mean + stdDev * FastMath.sqrt(-2.0 * FastMath.log(1.0 - z * z) / 2.0) * FastMath.cos(2.0 * FastMath.PI * Math.random());
        }

        final double[] actual = new double[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextGaussian();
        }

        assertTrue("Expected and actual arrays are not close enough.", FastMath.allEQ(expected, actual, 1e-6));
    }

    /**
     * Test method {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int count = 10000;
        final int[] expected = new int[count];
        for (int i = 0; i < count; i++) {
            expected[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }

        final int[] actual = new int[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextInt();
        }

        assertTrue("Expected and actual arrays are not equal.", FastMath.allEQ(expected, actual));
    }

    /**
     * Test method {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithPowerOfTwo() {
        int n = 128;
        int expected;
        for (int i = 0; i < 10; i++) {
            expected = (int) ((n * (long) Math.random()) >> 31);
            int actual = generator.nextInt(n);
            assertTrue("Expected and actual values are not equal.", expected == actual);
        }
    }

    /**
     * Test method {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithNonPowerOfTwo() {
        final int n = 7;
        final int count = 10000;
        final int[] expected = new int[count];
        for (int i = 0; i < count; i++) {
            int r = 0;
            do {
                r = next(31);
            } while (r % n >= Integer.MAX_VALUE / n);
            expected[i] = r % n;
        }

        final int[] actual = new int[count];
        for (int i = 0; i < count; i++) {
            int r;
            do {
                r = generator.nextInt(31);
            } while (r % n >= Integer.MAX_VALUE / n);
            actual[i] = r % n;
        }

        assertTrue("Expected and actual arrays are not equal.", FastMath.allEQ(expected, actual));
    }

    /**
     * Test method {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final int count = 10000;
        final long[] expected = new long[count];
        for (int i = 0; i < count; i++) {
            expected[i] = (long) (Math.random() * Long.MAX_VALUE);
        }

        final long[] actual = new long[count];
        for (int i = 0; i < count; i++) {
            actual[i] = generator.nextLong();
        }

        assertTrue("Expected and actual arrays are not equal.", FastMath.allEQ(expected, actual));
    }

    /**
     * Test method {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse("NextGaussian value should be reset.", Double.isNaN(generator.nextGaussian()));
    }

}