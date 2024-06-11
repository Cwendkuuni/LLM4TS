package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
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

    /**
     * Sets up a new instance of {@link BitsStreamGenerator} before each test.
     */
    @Before
    public void setUp() {
        generator = new MersenneTwisterFast();
    }

    /**
     * Test method for {@link BitsStreamGenerator#BitsStreamGenerator()}.
     */
    @Test
    public void testConstructor() {
        // check that the default constructor works
        BitsStreamGenerator instance = new BitsStreamGenerator();
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        // set the seed and check if it is correctly set
        generator.setSeed(123);
        int seed = generator.nextInt();
        assertEquals(123, seed);
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        // set the seed and check if it is correctly set
        int[] seed = new int[] { 1, 2, 3 };
        generator.setSeed(seed);
        int seed0 = generator.nextInt();
        int seed1 = generator.nextInt();
        int seed2 = generator.nextInt();
        assertArrayEquals(new int[] { 1, 2, 3 }, new int[] { seed0, seed1, seed2 });
    }

    /**
     * Test method for {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        // set the seed and check if it is correctly set
        long seed = 123L;
        generator.setSeed(seed);
        long seed0 = generator.nextLong();
        assertEquals(seed, seed0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#next(int)}.
     */
    @Test
    public void testNextInt() {
        // generate random bits and check if they are correctly set
        int bits = generator.next(32);
        assertNotSame(0, bits);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        // generate a boolean and check its value
        boolean booleanValue = generator.nextBoolean();
        assertTrue(booleanValue);
        booleanValue = generator.nextBoolean();
        assertFalse(booleanValue);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        // generate random bytes and check if they are correctly set
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xff);
        }
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        // generate random doubles and check if they are within the expected range
        double doubleValue = generator.nextDouble();
        assertTrue(doubleValue >= 0.0 && doubleValue <= 1.0);
        doubleValue = generator.nextDouble();
        assertTrue(doubleValue >= 0.0 && doubleValue <= 1.0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        // generate random floats and check if they are within the expected range
        float floatValue = generator.nextFloat();
        assertTrue(Float.isFinite(floatValue));
        floatValue = generator.nextFloat();
        assertTrue(Float.isFinite(floatValue));
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        // generate random gaussians and check if they are within the expected range
        double gaussian0 = generator.nextGaussian();
        double gaussian1 = generator.nextGaussian();
        assertTrue(gaussian0 >= -3.0 && gaussian0 <= 3.0);
        assertTrue(gaussian1 >= -3.0 && gaussian1 <= 3.0);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextIntInt() throws NotStrictlyPositiveException {
        // generate random integers and check if they are within the expected range
        int intValue = generator.nextInt(5);
        assertTrue(intValue >= 0 && intValue <= 4);
        intValue = generator.nextInt(5);
        assertTrue(intValue >= 0 && intValue <= 4);
    }

    /**
     * Test method for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        // generate random longs and check if they are correctly set
        long longValue = generator.nextLong();
        assertNotSame(0L, longValue);
    }

    /**
     * Test method for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        // generate a gaussian number and clear the cache
        double gaussian = generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}