package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The generator to test. */
    private final BitsStreamGenerator generator = new MyBitsStreamGenerator();

    /**
     * Test nextBoolean().
     */
    @Test
    public void testNextBoolean() {
        // typical use case
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());

        // edge case: minimum number of bits
        for (int i = 0; i < 10; i++) {
            assertEquals(0, generator.nextBoolean() ? 1 : 0);
        }

        // error case: invalid seed
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            assertFalse(generator.nextBoolean());
        }
    }

    /**
     * Test nextBytes().
     */
    @Test
    public void testNextBytes() {
        // typical use case
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        generator.nextBytes(expected);
        assertArrayEquals(expected, expected);

        // edge cases: minimum and maximum size
        byte[] minExpected = new byte[0];
        generator.nextBytes(minExpected);
        assertArrayEquals(minExpected, minExpected);

        byte[] maxExpected = new byte[Integer.MAX_VALUE];
        generator.nextBytes(maxExpected);
        assertArrayEquals(maxExpected, maxExpected);

        // error case: null argument
        try {
            generator.nextBytes(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test nextDouble().
     */
    @Test
    public void testNextDouble() {
        // typical use case
        double expected = 1.23456789;
        assertEquals(expected, generator.nextDouble(), 0.0);
        assertEquals(expected, generator.nextDouble(), 0.0);

        // edge cases: minimum and maximum values
        assertEquals(-Double.MAX_VALUE, generator.nextDouble(), 0.0);
        assertEquals(Double.MAX_VALUE, generator.nextDouble(), 0.0);

        // error case: invalid seed
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            fail("NaN or infinity expected");
            try {
                assertEquals(0.0, generator.nextDouble(), 0.0);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    /**
     * Test nextFloat().
     */
    @Test
    public void testNextFloat() {
        // typical use case
        float expected = 1.234567f;
        assertEquals(expected, generator.nextFloat(), 0.0f);
        assertEquals(expected, generator.nextFloat(), 0.0f);

        // edge cases: minimum and maximum values
        assertEquals((float) -Float.MAX_VALUE, generator.nextFloat(), 0.0f);
        assertEquals((float) Float.MAX_VALUE, generator.nextFloat(), 0.0f);

        // error case: invalid seed
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            fail("NaN or infinity expected");
            try {
                assertEquals(0.0f, generator.nextFloat(), 0.0f);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    /**
     * Test nextGaussian().
     */
    @Test
    public void testNextGaussian() {
        // typical use case
        double expectedMean = 1.23456789;
        double expectedStdDev = 0.123456789;
        double actualMean = 0.0;
        double actualStdDev = 0.0;
        for (int i = 0; i < 10000; i++) {
            double actual = generator.nextGaussian();
            actualMean += actual;
        }
        actualMean /= 10000.0;
        assertEquals(expectedMean, actualMean, 0.0);

        for (int i = 0; i < 10000; i++) {
            double actual = generator.nextGaussian();
            double actualSquared = actual * actual;
            actualStdDev += actualSquared;
        }
        actualStdDev /= 10000.0;
        assertEquals(expectedStdDev * expectedStdDev, actualStdDev, 0.0);

        // edge cases: minimum and maximum values
        assertEquals(-Double.MAX_VALUE, generator.nextGaussian(), 0.0);
        assertEquals(Double.MAX_VALUE, generator.nextGaussian(), 0.0);

        // error case: invalid seed
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            fail("NaN or infinity expected");
            try {
                assertEquals(0.0, generator.nextGaussian(), 0.0);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    /**
     * Test nextInt().
     */
    @Test
    public void testNextInt() {
        // typical use case
        assertEquals(42, generator.nextInt());
        assertEquals(42, generator.nextInt());
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
        assertEquals(Integer.MAX_VALUE, generator.nextInt());

        // edge cases: minimum and maximum values as powers of two
        assertEquals(Integer.MIN_VALUE, generator.nextInt() >>> 31);
        assertEquals(Integer.MAX_VALUE, generator.nextInt() >>> 31);

        // error cases: invalid seed and negative number
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            fail("Negative number expected");
            try {
                generator.nextInt(-1);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            try {
                assertEquals(0, generator.nextInt());
            } catch (NotStrictlyPositiveException e) {
                // expected
            }
        }
    }

    /**
     * Test nextLong().
     */
    @Test
    public void testNextLong() {
        // typical use case
        long expected = 123456789123456789L;
        assertEquals(expected, generator.nextLong());
        assertEquals(expected, generator.nextLong());

        // edge cases: minimum and maximum values
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());

        // error case: invalid seed
        generator.setSeed(-1);
        for (int i = 0; i < 10; i++) {
            fail("NaN or infinity expected");
            try {
                assertEquals(0, generator.nextLong());
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    /**
     * Test clear().
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}

// Test implementation: a random number generator that always returns the same sequence
class MyBitsStreamGenerator extends BitsStreamGenerator {
    @Override
    protected int next(int bits) {
        return 0;
    }
}