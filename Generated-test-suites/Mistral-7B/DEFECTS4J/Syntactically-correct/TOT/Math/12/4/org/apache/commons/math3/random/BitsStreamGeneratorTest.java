package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Tests that a new instance generates random bits.
     */
    @Test
    public void testNextBit() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    /**
     * Tests that {@link #nextBytes(byte[])} generates random bytes.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; ++i) {
            assertNotEquals((byte) i, bytes[i]);
            assertTrue(RandomUtils.isUniformlyDistributed(bytes));
        }
    }

    /**
     * Tests that {@link #nextDouble()} generates random numbers.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        for (int i = 0; i < 1000; ++i) {
            final double generated = generator.nextDouble();
            assertTrue(generated >= expectedMin);
            assertTrue(generated <= expectedMax);
        }
    }

    /**
     * Tests that {@link #nextFloat()} generates random numbers.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        for (int i = 0; i < 1000; ++i) {
            final float generated = generator.nextFloat();
            assertTrue(generated >= expectedMin);
            assertTrue(generated <= expectedMax);
        }
    }

    /**
     * Tests that {@link #nextGaussian()} generates random gaussian numbers.
     */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new GaussRng();
        final double expectedMean = 0.0;
        final double expectedVariance = 1.0;
        final int expectedNumSamples = 1000;

        final double[] samples = new double[expectedNumSamples];
        for (int i = 0; i < expectedNumSamples; ++i) {
            samples[i] = generator.nextGaussian();
        }

        final double mean = Precision.doubleArraySum(samples);
        final double variance = Precision.doubleArraySum(Precision.pow(Precision.subtract(samples[i], mean), 2.0)) / (expectedNumSamples - 1.0);
        assertEquals(expectedMean, mean, 0.001);
        assertEquals(expectedVariance, variance, 0.001);
    }

    /**
     * Tests that {@link #nextInt()} generates random integers.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        for (int i = 0; i < 1000; ++i) {
            final int generated = generator.nextInt();
            assertTrue(generated >= expectedMin);
            assertTrue(generated <= expectedMax);
        }
    }

    /**
     * Tests that {@link #nextInt(int)} generates random numbers within the
     * specified range.
     */
    @Test
    public void testNextIntRange() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // test positive range
        final int min = 1;
        final int max = 10;
        for (int i = 0; i < 1000; ++i) {
            final int generated = generator.nextInt(max - min + 1) + min;
            assertTrue(generated >= min);
            assertTrue(generated <= max);
        }

        // test negative range
        final int nmin = -10;
        final int nmax = 1;
        for (int i = 0; i < 1000; ++i) {
            final int generated = generator.nextInt(max - min + 1) + min;
            assertTrue(generated >= nmin);
            assertTrue(generated <= nmax);
        }
    }

    /**
     * Tests that {@link #nextLong()} generates random longs.
     */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        for (int i = 0; i < 1000; ++i) {
            final long generated = generator.nextLong();
            assertTrue(generated >= expectedMin);
            assertTrue(generated <= expectedMax);
        }
    }

    /**
     * Tests that {@link #clear()} clears the gaussian number cache.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new GaussRng();
        generator.nextGaussian();
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.isNextGaussianAvailable());
    }

    /**
     * Tests that {@link #setSeed(int)} sets the seed correctly.
     */
    @Test
    public void testSetSeedInt() {
        final BitsStreamGenerator generator1 = new MersenneTwisterFastRandomGenerator();
        final long seed1 = generator1.nextLong();

        final BitsStreamGenerator generator2 = new MersenneTwisterFastRandomGenerator();
        generator2.setSeed(seed1);

        final boolean areEqual = generator1.nextBoolean() == generator2.nextBoolean();
        assertTrue(areEqual);
    }

    /**
     * Tests that {@link #setSeed(int[])} sets the seed correctly.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = { 1, 2, 3 };
        final BitsStreamGenerator generator1 = new MersenneTwisterFastRandomGenerator();
        final long seed1 = generator1.nextLong();

        final BitsStreamGenerator generator2 = new MersenneTwisterFastRandomGenerator();
        generator2.setSeed(seed);

        final boolean areEqual = generator1.nextBoolean() == generator2.nextBoolean();
        assertTrue(areEqual);
    }

    /**
     * Tests that {@link #setSeed(long)} sets the seed correctly.
     */
    @Test
    public void testSetSeedLong() {
        final long seed = 1234567890123456789L;
        final BitsStreamGenerator generator1 = new MersenneTwisterFastRandomGenerator();
        final long seed1 = generator1.nextLong();

        final BitsStreamGenerator generator2 = new MersenneTwisterFastRandomGenerator();
        generator2.setSeed(seed);

        final boolean areEqual = generator1.nextBoolean() == generator2.nextBoolean();
        assertTrue(areEqual);
    }

    /**
     * Tests that {@link #nextBoolean()} throws an exception with negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextBooleanNegativeSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(-1);
        generator.nextBoolean();
    }

    /**
     * Tests that {@link #nextBoolean()} throws an exception with zero seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextBooleanZeroSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(0);
        generator.nextBoolean();
    }

    /**
     * Tests that {@link #nextInt()} throws an exception with negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(-1);
        generator.nextInt();
    }

    /**
     * Tests that {@link #nextInt()} throws an exception with zero seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZeroSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(0);
        generator.nextInt();
    }

    /**
     * Tests that {@link #nextInt(int)} throws an exception with negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeSeedRange() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(-1);
        generator.nextInt(10);
    }

    /**
     * Tests that {@link #nextInt(int)} throws an exception with zero seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntZeroSeedRange() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(0);
        generator.nextInt(10);
    }

    /**
     * Tests that {@link #nextInt(int)} throws an exception with negative range size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeRangeSize() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    /**
     * Tests that {@link #nextInt(int)} throws an exception with zero range size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroRangeSize() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(0);
    }

    /**
     * Tests that {@link #nextLong()} throws an exception with negative seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextLongNegativeSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(-1);
        generator.nextLong();
    }

    /**
     * Tests that {@link #nextLong()} throws an exception with zero seed.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextLongZeroSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(0);
        generator.nextLong();
    }
}