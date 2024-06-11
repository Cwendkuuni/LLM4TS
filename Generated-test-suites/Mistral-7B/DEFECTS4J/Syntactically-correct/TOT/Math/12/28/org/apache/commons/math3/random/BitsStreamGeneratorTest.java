package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The generator to test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator();
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextBoolean();
            assertTrue(result == true || result == false);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] >= 0 && bytes[i] <= 0xff);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextInt(5);
            assertTrue(result >= 0 && result < 5);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeParameter() {
        generator.nextInt(-1);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long result;
        for (int i = 0; i < 100; i++) {
            result = generator.nextLong();
            assertTrue(result >= 0L && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double[] gaussians = new double[100];
        for (int i = 0; i < 100; i++) {
            gaussians[i] = generator.nextGaussian();
        }
        generator.clear();
        for (int i = 0; i < 100; i++) {
            assertTrue(Double.isNaN(generator.nextGaussian()));
        }
    }

}