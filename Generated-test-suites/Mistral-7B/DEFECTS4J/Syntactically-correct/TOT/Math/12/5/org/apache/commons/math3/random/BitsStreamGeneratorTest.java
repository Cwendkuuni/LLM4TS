package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The generator to test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("PMD.AssumePositive")
    @Before
    public void setUp() {
        generator = new MersenneTwisterFastRandomGenerator(); // or any subclass of BitsStreamGenerator
    }

    /**
     * Test nextBoolean().
     */
    @Test
    public void testNextBoolean() {
        Assert.assertTrue(generator.nextBoolean());
        Assert.assertTrue(generator.nextBoolean());
        Assert.assertFalse(generator.nextBoolean());
        Assert.assertFalse(generator.nextBoolean());
    }

    /**
     * Test nextBytes().
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; ++i) {
            Assert.assertNotEquals(i, bytes[i]);
            Assert.assertTrue(bytes[i] >= 0);
            Assert.assertTrue(bytes[i] <= 0xff);
        }
    }

    /**
     * Test nextDouble().
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 100; ++i) {
            final double value = generator.nextDouble();
            Assert.assertTrue(Precision.isClose(value, 0.5, 5E-5));
        }
    }

    /**
     * Test nextFloat().
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 100; ++i) {
            final float value = generator.nextFloat();
            Assert.assertTrue(Precision.isClose(value, 0.5f, 5E-5f));
        }
    }

    /**
     * Test nextGaussian().
     */
    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 100; ++i) {
            final double mu = 0.0;
            final double sigma = 1.0;
            final double value = generator.nextGaussian(mu, sigma);
            Assert.assertTrue(value > -3 * sigma);
            Assert.assertTrue(value < 3 * sigma);
        }
    }

    /**
     * Test nextInt().
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 10; ++i) {
            final int value = generator.nextInt();
            Assert.assertTrue(value >= 0);
            Assert.assertTrue(value <= Integer.MAX_VALUE);
        }

        for (int i = 1; i < 10; ++i) {
            final int value = generator.nextInt(i);
            Assert.assertTrue(value >= 0);
            Assert.assertTrue(value < i);
        }

        try {
            generator.nextInt(-1);
            Assert.fail("Should have thrown an exception.");
        } catch (final NotStrictlyPositiveException e) {
            // expected
        }
    }

    /**
     * Test nextInt(int) with very large numbers.
     */
    @Test
    public void testNextIntWithLargeNumbers() {
        final long maxValue = Long.MAX_VALUE;
        for (int i = 0; i < 10; ++i) {
            final long value = generator.nextInt((int) maxValue);
            Assert.assertTrue(value >= 0);
            Assert.assertTrue(value <= maxValue);
        }
    }

    /**
     * Test nextLong().
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 10; ++i) {
            final long value = generator.nextLong();
            Assert.assertTrue(value >= 0);
            Assert.assertTrue(value <= Long.MAX_VALUE);
        }
    }

    /**
     * Test clear().
     */
    @Test
    public void testClear() {
        final double mu = 0.0;
        final double sigma = 1.0;
        for (int i = 0; i < 10; ++i) {
            final double value1 = generator.nextGaussian(mu, sigma);
            generator.clear();
            final double value2 = generator.nextGaussian(mu, sigma);
            Assert.assertNotEquals(value1, value2);
        }
    }

}