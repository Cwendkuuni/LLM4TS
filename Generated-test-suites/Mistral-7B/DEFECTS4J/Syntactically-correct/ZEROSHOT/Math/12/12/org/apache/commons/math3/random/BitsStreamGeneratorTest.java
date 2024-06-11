package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MersenneTwisterFast();
        generator.setSeed(System.currentTimeMillis());
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result);
        result = generator.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        byte[] expected = {1, 2, 3, 4, 5};
        byte[] result   = new byte[5];
        generator.nextBytes(result);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertEquals(0.0, result, 0.0001);
        result = generator.nextDouble();
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertEquals(0.0f, result, 0.001f);
        result = generator.nextFloat();
        assertEquals(1.0f, result, 0.001f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertTrue(result > -3.0);
        assertTrue(result < 3.0);
        result = generator.nextGaussian();
        assertTrue(result > -3.0);
        assertTrue(result < 3.0);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertEquals(Integer.MIN_VALUE, result);
        result = generator.nextInt();
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testNextIntWithNegativeSize() {
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextIntWithNonPowerOfTwoSize() {
        int result = generator.nextInt(5);
        assertEquals(0, result);
        result = generator.nextInt(5);
        assertEquals(1, result);
        result = generator.nextInt(5);
        assertEquals(1, result);
        result = generator.nextInt(5);
        assertEquals(1, result);
        result = generator.nextInt(5);
        assertEquals(1, result);
        result = generator.nextInt(5);
        assertEquals(0, result);
    }

    @Test
    public void testNextIntWithPowerOfTwoSize() {
        int result = generator.nextInt(16);
        assertEquals(0, result);
        result = generator.nextInt(16);
        assertEquals(1, result);
        result = generator.nextInt(16);
        assertEquals(1, result);
        result = generator.nextInt(16);
        assertEquals(1, result);
        result = generator.nextInt(16);
        assertEquals(1, result);
        result = generator.nextInt(16);
        assertEquals(0, result);
    }

    @Test
    public void testNextIntWithZeroSize() {
        int result = generator.nextInt(0);
        assertEquals(0, result);
    }

    @Test
    public void testNextIntWithSizeGreaterThanIntMaxValue() {
        try {
            generator.nextInt(Integer.MAX_VALUE + 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertEquals(-9223372036854775807L, result);
        result = generator.nextLong();
        assertEquals(9223372036854775807L, result);
    }

    @Test
    public void testSetSeed() {
        long seed = System.currentTimeMillis();
        generator.setSeed(seed);
        long expected = seed;
        long result   = generator.nextLong();
        assertEquals(expected, result);
        generator.clear();
        result = generator.nextLong();
        assertFalse(result == expected);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedWithNegativeSeed() {
        generator.setSeed(-1);
    }
}