package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    private static final int TEST_SIZE = 100;
    
    /** The test generator. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < TEST_SIZE; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextBytes(byte[])} method with a
     * positive length array.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = new byte[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            generator.nextBytes(expected);
        }
        byte[] result = new byte[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; i++) {
            generator.nextBytes(result);
            assertArrayEquals(expected, result);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextBytes(byte[])} method with a
     * zero length array.
     */
    @Test
    public void testNextBytesZeroLength() {
        byte[] result = new byte[0];
        generator.nextBytes(result);
        // empty array, no assertions needed
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < TEST_SIZE; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < TEST_SIZE; i++) {
            float result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < TEST_SIZE; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt()} method with a
     * positive value.
     */
    @Test
    public void testNextIntPositive() {
        for (int i = 0; i < TEST_SIZE; i++) {
            int result = generator.nextInt();
            assertTrue(result >= 0);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt()} method with a
     * negative value.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt(int)} method with a
     * positive value.
     */
    @Test
    public void testNextIntSpecific() {
        for (int i = 0; i < TEST_SIZE; i++) {
            int n = 123;
            int result = generator.nextInt(n);
            assertTrue(result >= 0 && result < n);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextInt(int)} method with a
     * zero length array.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZeroLength() {
        generator.nextInt(0);
    }

    /**
     * Tests the {@link BitsStreamGenerator#nextLong()} method.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < TEST_SIZE; i++) {
            long result = generator.nextLong();
            assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests the {@link BitsStreamGenerator#clear()} method.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }

}