package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwister();
    }

    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 100; i++) {
            assertTrue(generator.nextBoolean());
            assertTrue(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] actual  = new byte[10];
        generator.nextBytes(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testNextDouble() {
        for (int i = 0; i < 100; i++) {
            double expected = Math.random();
            double actual   = generator.nextDouble();
            assertEquals(expected, actual, 0.0001);
        }
    }

    @Test
    public void testNextFloat() {
        for (int i = 0; i < 100; i++) {
            float expected = (float) Math.random();
            float actual   = generator.nextFloat();
            assertEquals(expected, actual, 0.001f);
        }
    }

    @Test
    public void testNextGaussian() {
        for (int i = 0; i < 100; i++) {
            final double expectedMean = 0.0;
            final double expectedStdDev = 1.0;
            double actualMean = 0.0;
            double actualStdDev = 0.0;
            int count = 1000;

            for (int j = 0; j < count; j++) {
                double actual = generator.nextGaussian();
                actualMean += actual;
            }

            actualMean /= count;

            for (int j = 0; j < count; j++) {
                double actual = generator.nextGaussian();
                double diff = Math.pow(actual - expectedMean, 2.0);
                actualStdDev += diff;
            }

            actualStdDev /= count;

            assertEquals(expectedMean, actualMean, 0.01);
            assertEquals(expectedStdDev, actualStdDev, 0.01);
        }
    }

    @Test
    public void testNextInt() {
        for (int i = 0; i < 100; i++) {
            int expected = (int) (Math.random() * 10);
            int actual   = generator.nextInt(10);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeArgument() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        for (int i = 0; i < 10; i++) {
            int expected = (int) (Math.random() * (1 << i));
            int actual   = generator.nextInt(1 << i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        int expected = 7;
        int actual;

        do {
            actual = generator.nextInt(11);
        } while (actual % 11 != 0);

        assertEquals(expected, actual);
    }

    @Test
    public void testNextLong() {
        for (int i = 0; i < 100; i++) {
            long expected = (long) (Math.random() * Long.MAX_VALUE);
            long actual   = generator.nextLong();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }

}