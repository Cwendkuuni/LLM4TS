package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** Test subject. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator(); // use a concrete implementation
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final boolean expectedTrue  = true;
        final boolean expectedFalse = false;

        // typical use case
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());

        // edge cases
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());

        // error scenarios
        try {
            generator.nextBoolean(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expected = new byte[] {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10,
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18,
            0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20
        };

        final byte[] result = new byte[expected.length];
        generator.nextBytes(result);

        // typical use case
        assertArrayEquals(expected, result);

        // edge cases
        final byte[] empty = new byte[0];
        generator.nextBytes(empty);
        assertEquals(0, empty.length);

        final byte[] nullResult = null;
        try {
            generator.nextBytes(nullResult);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;

        // typical use case
        double actual = generator.nextDouble();
        assertTrue(expectedMin <= actual && actual <= expectedMax);

        // edge cases
        double actualMin = generator.nextDouble();
        assertTrue(expectedMin <= actualMin);
        double actualMax = generator.nextDouble();
        assertTrue(actualMax <= expectedMax);

        // error scenarios
        try {
            generator.nextDouble(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;

        // typical use case
        float actual = generator.nextFloat();
        assertTrue(expectedMin <= actual && actual <= expectedMax);

        // edge cases
        float actualMin = generator.nextFloat();
        assertTrue(expectedMin <= actualMin);
        float actualMax = generator.nextFloat();
        assertTrue(actualMax <= expectedMax);

        // error scenarios
        try {
            generator.nextFloat(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;

        // typical use case
        double actual = generator.nextGaussian();
        assertTrue(expectedMean - 3.0 * expectedStdDev <= actual &&
                   expectedMean + 3.0 * expectedStdDev >= actual);

        // edge cases
        double actualMean = generator.nextGaussian();
        assertTrue(expectedMean - 3.0 * expectedStdDev <= actualMean);
        double actualStdDev = generator.nextGaussian();
        assertTrue(actualStdDev >= 0.0);

        // error scenarios
        try {
            generator.nextGaussian(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.clear();
            double actual1 = generator.nextGaussian();
            double actual2 = generator.nextGaussian();
            double actual3 = generator.nextGaussian();

            // check that gaussians are independent
            double covariance = (actual1 - expectedMean) * (actual2 - expectedMean) +
                               (actual2 - expectedMean) * (actual3 - expectedMean) +
                               (actual3 - expectedMean) * (actual1 - expectedMean);
            assertEquals(0.0, covariance, 1e-5);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected no exception");
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;

        // typical use case
        int actual = generator.nextInt();
        assertTrue(expectedMin <= actual && actual <= expectedMax);

        // edge cases
        int actualMin = generator.nextInt();
        assertTrue(expectedMin <= actualMin);
        int actualMax = generator.nextInt();
        assertTrue(actualMax <= expectedMax);

        // error scenarios
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        final int limit = 10;

        // typical use case
        int actual = generator.nextInt(limit + 1);
        assertTrue(actual >= 0 && actual <= limit);

        // edge cases
        int actualMin = generator.nextInt(limit);
        assertTrue(actual >= 0 && actual <= limit);
        int actualMax = generator.nextInt(limit);
        assertTrue(actual >= 0 && actual <= limit);

        // error scenarios
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.nextInt(limit + 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;

        // typical use case
        long actual = generator.nextLong();
        assertTrue(expectedMin <= actual && actual <= expectedMax);

        // edge cases
        long actualMin = generator.nextLong();
        assertTrue(expectedMin <= actualMin);
        long actualMax = generator.nextLong();
        assertTrue(actualMax <= expectedMax);

        // error scenarios
        try {
            generator.nextLong(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        // typical use case
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(nextGaussian));
    }

}