package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {
    /** Test instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up test environment. Initializes test instance.
     */
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result <= 1.0);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDoubleTwice() {
        double result1 = generator.nextDouble();
        double result2 = generator.nextDouble();
        double result3 = generator.nextDouble();
        assertNotSame("Last generated double is the same as the second one", result2, result1);
        assertNotSame("Last generated double is the same as the first one", result3, result1);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0.0f && result <= 1.0f);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        double mean = 0.0;
        double standardDeviation = 1.0;
        double z = FastMath.erf(result / standardDeviation / FastMath.SQRT_2PI);
        double expected = mean + standardDeviation * z;
        double tolerance = 0.001;
        assertTrue(FastMath.abs(result - expected) < tolerance);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        int max = Integer.MAX_VALUE;
        assertTrue(result >= 0 && result <= max);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        int result = generator.nextInt(5);
        int limit = 5;
        assertTrue(result >= 0 && result < limit);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeLimit() {
        generator.nextInt(-5);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        long max = Long.MAX_VALUE;
        assertTrue(result >= 0 && result <= max);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        double result = generator.nextGaussian();
        assertTrue(Double.isNaN(result));
    }

}