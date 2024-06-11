package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** The instance to test. */
    private BitsStreamGenerator generator = new XorShift64StarGenerator();

    /**
     * Tests the {@link #nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 100; i++) {
            assertTrue(generator.nextBoolean());
            assertTrue(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
        }
    }

    /**
     * Tests the {@link #nextBytes(byte[])} method with a byte array of length 1.
     */
    @Test
    public void testNextBytesWithLengthOne() {
        byte[] bytes = new byte[1];
        generator.nextBytes(bytes);
        int byteValue = Byte.toUnsignedInt(bytes[0]);
        assertTrue(byteValue > 0 && byteValue < 256);
    }

    /**
     * Tests the {@link #nextBytes(byte[])} method with a byte array of length greater than 1.
     */
    @Test
    public void testNextBytesWithLengthGreaterThanOne() {
        byte[] bytes = new byte[5];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            int byteValue = Byte.toUnsignedInt(bytes[i]);
            assertTrue(byteValue > 0 && byteValue < 256);
        }
    }

    /**
     * Tests the {@link #nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        double expectedMin = 0.0;
        double expectedMax = 1.0;
        for (int i = 0; i < 1000; i++) {
            double value = generator.nextDouble();
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
            assertEquals(Precision.round(value, 15), expectedMin + (expectedMax - expectedMin) * nextGaussian(), 1e-12);
        }
    }

    /**
     * Tests the {@link #nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        float expectedMin = 0.0f;
        float expectedMax = 1.0f;
        for (int i = 0; i < 1000; i++) {
            float value = generator.nextFloat();
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
            assertEquals(value, expectedMin + (expectedMax - expectedMin) * nextGaussian(), 1e-12f);
        }
    }

    /**
     * Tests the {@link #nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        double expectedMean = 0.0;
        double expectedStdDev = 1.0;
        double expectedMin = -3.0;
        double expectedMax = 3.0;
        for (int i = 0; i < 1000; i++) {
            double value = generator.nextGaussian();
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
            assertEquals(Precision.round(value, 15), expectedMean + expectedStdDev * FastMath.sqrt(2.0) * nextGaussian(), 1e-12);
        }
    }

    /**
     * Tests the {@link #nextInt()} method with a positive argument.
     */
    @Test
    public void testNextIntWithPositiveArgument() {
        int max = 10;
        for (int i = 0; i < 100; i++) {
            int value = generator.nextInt(max);
            assertTrue(value >= 0 && value < max);
        }
    }

    /**
     * Tests the {@link #nextInt()} method with a negative argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeArgument() {
        int n = -12;
        generator.nextInt(n);
    }

    /**
     * Tests the {@link #nextInt(int)} method with a power of 2 argument.
     */
    @Test
    public void testNextIntWithPowerOfTwoArgument() {
        int n = 16;
        for (int i = 0; i < 100; i++) {
            int value = generator.nextInt(n);
            assertEquals(value, n * ((int) ((generator.next(31)) >> 31)));
        }
    }

    /**
     * Tests the {@link #nextInt(int)} method with a non-power-of-2 argument.
     */
    @Test
    public void testNextIntWithNonPowerOfTwoArgument() {
        int n = 21;
        for (int i = 0; i < 100; i++) {
            int value = generator.nextInt(n);
            int remainder = n % Integer.MAX_VALUE;
            while (value % remainder != 0) {
                value = generator.nextInt(n);
            }
            assertEquals(value, remainder * ((int) ((generator.next(31)) >> 31)));
        }
    }

    /**
     * Tests the {@link #nextLong()} method.
     */
    @Test
    public void testNextLong() {
        long expectedMin = Long.MIN_VALUE;
        long expectedMax = Long.MAX_VALUE;
        for (int i = 0; i < 1000; i++) {
            long value = generator.nextLong();
            assertTrue(value >= expectedMin);
            assertTrue(value <= expectedMax);
        }
    }

    /**
     * Tests the {@link #clear()} method.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.isNextGaussianCached());
    }

    /**
     * Utility method to get the next gaussian value.
     * @return the next gaussian value
     */
    private double nextGaussian() {
        return generator.nextGaussian();
    }

}