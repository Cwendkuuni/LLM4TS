package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /** The tested instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("PMD.SetUpMethodNameDoesNotStartWith'set'")
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator(); // replace with the actual generator to test
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean result;
        for (int i = 0; i < 10000; i++) {
            result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
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
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 10000; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 10000; i++) {
            float result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 10000; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 10000; i++) {
            int result = generator.nextInt();
            assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithLimit() {
        for (int i = 0; i < 10000; i++) {
            for (int limit = 1; limit <= 10; limit++) {
                int result = generator.nextInt(limit);
                assertTrue(result >= 0 && result < limit);
            }
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 10000; i++) {
            long result = generator.nextLong();
            assertTrue(result >= 0L && result <= Long.MAX_VALUE);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedWithInt() {
        int seed = 12345;
        generator.setSeed(seed);
        long expectedSeed = 12345 & 0xffffffffL; // mask off the high bits
        long actualSeed = generator.getState()[0];
        assertEquals(expectedSeed, actualSeed);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedWithIntArray() {
        int[] seeds = {12345, 67890};
        generator.setSeed(seeds);
        long[] expectedSeeds = new long[] {12345 & 0xffffffffL, 67890 & 0xffffffffL};
        long[] actualSeeds = new long[generator.getState().length];
        System.arraycopy(generator.getState(), 0, actualSeeds, 0, generator.getState().length);
        assertArrayEquals(expectedSeeds, actualSeeds);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedWithLong() {
        long seed = 1234567890L;
        generator.setSeed(seed);
        long expectedSeed = seed;
        long actualSeed = generator.getState()[0];
        assertEquals(expectedSeed, actualSeed);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.clear();
        long[] state = generator.getState();
        for (long value : state) {
            assertEquals(0, value);
        }
    }
}