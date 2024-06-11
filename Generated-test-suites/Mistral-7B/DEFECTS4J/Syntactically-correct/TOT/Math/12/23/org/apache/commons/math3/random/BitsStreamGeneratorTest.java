package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The tested generator instance. */
    private final BitsStreamGenerator generator = new XorShift128Plus();

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes1 = new byte[10];
        byte[] bytes2 = new byte[10];
        generator.nextBytes(bytes1);
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);
        assertArrayNotEquals(bytes1, bytes1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0 && result <= 1);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; i++) {
            float result = generator.nextFloat();
            assertTrue(result >= 0 && result <= 1);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 1000; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3 && result <= 3);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 1000; i++) {
            int result = generator.nextInt(10);
            assertTrue(result >= 0 && result <= 9);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)} with negative n.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt(int)} with n > Integer.MAX_VALUE.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntMaxValue() {
        generator.nextInt(Integer.MAX_VALUE + 1);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertTrue(Long.MIN_VALUE <= result && result <= Long.MAX_VALUE);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double[] gaussian = new double[2];
        generator.clear();
        generator.nextGaussian();
        generator.nextGaussian();
        generator.nextGaussian();
        generator.nextGaussian();
        generator.nextGaussian();
        generator.nextGaussian();
        for (int i = 0; i < 1000; i++) {
            generator.nextGaussian();
            int index = (int) (Math.random() * 2);
            gaussian[index] = generator.nextGaussian();
        }
        for (int i = 0; i < 2; i++) {
            double expected = FastMath.sqrt(FastMath.RANDOM.nextGaussian() * 2);
            double result = gaussian[i];
            assertEquals("Gaussian " + i + " is not correct", expected, result, 0.001);
        }
    }
}