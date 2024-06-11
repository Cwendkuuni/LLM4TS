package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {
    private Random mockRandom;
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        mockRandom = new Random();
        generator = new MockBitsStreamGenerator(mockRandom);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNull(generator.nextGaussian());
    }

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        boolean expected = mockRandom.nextBoolean();
        boolean actual = generator.nextBoolean();
        assertEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = new byte[10];
        mockRandom.nextBytes(expected);
        generator.nextBytes(new byte[10]);
        assertArrayEquals(expected, generator.nextBytes(new byte[10]));
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double expected = mockRandom.nextDouble();
        double actual = generator.nextDouble();
        assertEquals(Precision.round(expected, 16), Precision.round(actual, 16), 0.0);
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float expected = mockRandom.nextFloat();
        float actual = generator.nextFloat();
        assertEquals(expected, actual, 0.0f);
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double expected = mockRandom.nextGaussian();
        double actual = generator.nextGaussian();
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int expected = mockRandom.nextInt();
        int actual = generator.nextInt();
        assertEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithFixedSize() {
        int expected = mockRandom.nextInt(5);
        int actual   = generator.nextInt(5);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeSize() {
        generator.nextInt(-5);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroSize() {
        generator.nextInt(0);
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithMaxValue() {
        int expected = Integer.MAX_VALUE;
        int actual = generator.nextInt(Integer.MAX_VALUE);
        assertEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long expected = mockRandom.nextLong();
        long actual = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Test {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double gaussian = generator.nextGaussian();
        generator.clear();
        assertFalse(generator.nextGaussian() == gaussian);
    }

}

class MockBitsStreamGenerator extends BitsStreamGenerator {
    private final Random random;

    public MockBitsStreamGenerator(Random random) {
        this.random = random;
    }

    @Override
    public void setSeed(int seed) {
        random.setSeed(seed);
    }

    @Override
    public void setSeed(int[] seed) {
        random.setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    @Override
    public int next(int bits) {
        return random.nextInt(1 << bits);
    }

    @Override
    public void clear() {
        // do nothing
    }
}