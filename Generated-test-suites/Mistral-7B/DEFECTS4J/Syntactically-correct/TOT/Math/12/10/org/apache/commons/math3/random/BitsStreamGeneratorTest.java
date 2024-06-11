package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;

import java.util.Random;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final boolean expectedTrue = true;
        final boolean expectedFalse = false;

        // Test typical use case
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final boolean result = generator.nextBoolean();
            if (i % 100_000 == 0) {
                assertTrue(result);
                assertTrue(generator.nextBoolean());
                assertFalse(generator.nextBoolean());
            }
        }

        // Test error case - uninitialized generator
        final BitsStreamGenerator uninitializedGenerator = new BitsStreamGenerator();
        assertFalse(uninitializedGenerator.nextBoolean());
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final byte[] expectedBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};

        // Test typical use case
        generator.nextBytes(new byte[4]);

        // Test edge cases
        final byte[] testBytes = new byte[256];
        generator.nextBytes(testBytes);

        // Test error cases
        final byte[] nullBytes = null;
        try {
            generator.nextBytes(nullBytes);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        final byte[] emptyBytes = new byte[0];
        try {
            generator.nextBytes(emptyBytes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final int tooLargeLength = 256 * 1024;
        try {
            generator.nextBytes(new byte[tooLargeLength]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;

        // Test typical use case
        final double result = generator.nextDouble();
        assertTrue(result >= expectedMin);
        assertTrue(result <= expectedMax);

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final double result = generator.nextDouble();
            if (i % 100_000 == 0) {
                final double expectedMinDouble = 0.0 + 0.001;
                final double expectedMaxDouble = 1.0 - 0.001;
                assertTrue(result >= expectedMinDouble);
                assertTrue(result <= expectedMaxDouble);
            }
        }

        // Test error case - uninitialized generator
        final BitsStreamGenerator uninitializedGenerator = new BitsStreamGenerator();
        final double result = uninitializedGenerator.nextDouble();
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;

        // Test typical use case
        final float result = generator.nextFloat();
        assertTrue(result >= expectedMin);
        assertTrue(result <= expectedMax);

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final float result = generator.nextFloat();
            if (i % 100_000 == 0) {
                final float expectedMinFloat = 0.0f + 0.001f;
                final float expectedMaxFloat = 1.0f - 0.001f;
                assertTrue(result >= expectedMinFloat);
                assertTrue(result <= expectedMaxFloat);
            }
        }

        // Test error case - uninitialized generator
        final BitsStreamGenerator uninitializedGenerator = new BitsStreamGenerator();
        final float result = uninitializedGenerator.nextFloat();
        assertEquals(Float.NaN, result, 0.0f);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final double expectedMean = 0.0;
        final double expectedStdDev = 1.0;

        // Test typical use case
        final double result = generator.nextGaussian();
        assertTrue(result >= expectedMean - 3.0 * expectedStdDev);
        assertTrue(result <= expectedMean + 3.0 * expectedStdDev);

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final double result = generator.nextGaussian();
            if (i % 100_000 == 0) {
                final double expectedMeanDouble = 0.0 + 0.001;
                final double expectedStdDevDouble = 1.0 - 0.001;
                final double expectedMeanGaussian = FastMath.erf(3.0 * expectedStdDevDouble / (2.0 * FastMath.SQRT_2_PI));
                assertTrue(result >= expectedMeanDouble - 3.0 * expectedStdDevDouble);
                assertTrue(result <= expectedMeanDouble + 3.0 * expectedStdDevDouble);
                assertTrue(result >= expectedMeanGaussian);
                assertTrue(result <= expectedMeanGaussian + 0.005);
            }
        }

        // Test error case - uninitialized generator
        final BitsStreamGenerator uninitializedGenerator = new BitsStreamGenerator();
        final double result = uninitializedGenerator.nextGaussian();
        assertEquals(Double.NaN, result, 0.0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;

        // Test typical use case
        final int result = generator.nextInt();
        assertTrue(result >= expectedMin);
        assertTrue(result <= expectedMax);

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final int result = generator.nextInt();
            if (i % 100_000 == 0) {
                final int expectedMinInt = expectedMin + 1;
                final int expectedMaxInt = expectedMax - 1;
                assertTrue(result >= expectedMinInt);
                assertTrue(result <= expectedMaxInt);
            }
        }

        // Test error cases
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final int negativeLimit = Integer.MIN_VALUE - 1;
        try {
            generator.nextInt(negativeLimit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final int tooLargeLimit = Integer.MAX_VALUE + 1;
        try {
            generator.nextInt(tooLargeLimit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final int zeroLimit = 0;
        try {
            generator.nextInt(zeroLimit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;

        // Test typical use case
        final long result = generator.nextLong();
        assertTrue(result >= expectedMin);
        assertTrue(result <= expectedMax);

        // Test edge cases
        for (int i = 0; i < 1_000_000; i++) {
            final long result = generator.nextLong();
            if (i % 100_000 == 0) {
                final long expectedMinLong = expectedMin + 1;
                final long expectedMaxLong = expectedMax - 1;
                assertTrue(result >= expectedMinLong);
                assertTrue(result <= expectedMaxLong);
            }
        }

        // Test error cases
        try {
            generator.nextLong(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final long negativeLimit = Long.MIN_VALUE - 1;
        try {
            generator.nextLong(negativeLimit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        final long tooLargeLimit = Long.MAX_VALUE + 1;
        try {
            generator.nextLong(tooLargeLimit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        final double expectedGaussian1 = generator.nextGaussian();
        generator.clear();
        final double expectedGaussian2 = generator.nextGaussian();
        assertNotEquals("Expected different gaussian values before and after clear", expectedGaussian1, expectedGaussian2);
    }
}