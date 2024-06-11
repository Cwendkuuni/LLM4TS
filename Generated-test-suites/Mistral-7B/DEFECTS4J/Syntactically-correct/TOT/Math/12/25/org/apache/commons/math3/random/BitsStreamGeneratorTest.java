package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private static final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

    /**
     * Test nextBoolean().
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; i++) {
            boolean b = generator.nextBoolean();
            assertTrue(b || !b);
        }
    }

    /**
     * Test nextBytes().
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue((b & 0xff) >= 0 && (b & 0xff) <= 0xff);
        }
    }

    /**
     * Test nextDouble().
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; i++) {
            double d = generator.nextDouble();
            assertTrue(d >= 0.0 && d <= 1.0);
        }
    }

    /**
     * Test nextFloat().
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; i++) {
            float f = generator.nextFloat();
            assertTrue(f >= 0.0f && f <= 1.0f);
        }
    }

    /**
     * Test nextGaussian().
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 1000; i++) {
            double gaussian = generator.nextGaussian();
            double mean = 0.0;
            double stddev = 1.0;
            assertTrue(FastMath.abs(gaussian - mean) <= stddev * 3);
        }
    }

    /**
     * Test nextInt().
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 1000; i++) {
            int n = 10;
            int result = generator.nextInt(n);
            assertTrue(result >= 0 && result < n);
        }
        try {
            generator.nextInt(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test nextLong().
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 1000; i++) {
            long result = generator.nextLong();
            long maxLong = Long.MAX_VALUE;
            assertTrue(result >= 0 && result <= maxLong);
        }
    }

    /**
     * Test setSeed().
     */
    @Test
    public void testSetSeed() {
        long seed = 1234567890L;
        generator.setSeed(seed);
        long generatedSeed = generator.nextLong();
        assertTrue(generatedSeed == seed);
    }

    /**
     * Test setSeed(int[]).
     */
    @Test
    public void testSetSeedArray() {
        int[] seed = {1, 2, 3};
        generator.setSeed(seed);
        long generatedSeed = generator.nextLong();
        long expectedSeed = FastMath.combine(seed[0], seed[1], seed[2]);
        assertTrue(generatedSeed == expectedSeed);
    }

    /**
     * Test setSeed(int).
     */
    @Test
    public void testSetSeedInt() {
        int seed = 12345;
        generator.setSeed(seed);
        long generatedSeed = generator.nextLong();
        long expectedSeed = FastMath.combine(seed);
        assertTrue(generatedSeed == expectedSeed);
    }
}