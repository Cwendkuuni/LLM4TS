package org.apache.commons.math3.random.BitsStreamGeneratorTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BitsStreamGeneratorTest {
    private final int testValue;
    private final int limit;

    public BitsStreamGeneratorTest(int testValue, int limit) {
        this.testValue = testValue;
        this.limit = limit;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[][]{
                {-1, 10},
                {0, 10},
                {1, 10},
                {Integer.MAX_VALUE, 10},
                {Integer.MIN_VALUE, 10},
                {10, 10},
                {15, 10},
                {Integer.MAX_VALUE, Integer.MAX_VALUE},
                {-1, Integer.MIN_VALUE},
                {0, Integer.MIN_VALUE},
                {1, Integer.MIN_VALUE},
                {Integer.MAX_VALUE, Integer.MIN_VALUE}
        };
    }

    @Test
    public void testNextIntWithLimit() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(testValue, generator.nextInt(limit));
        assertEquals(limit, generator.nextInt(limit));
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertTrue(!generator.nextBoolean());
        assertTrue(!generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        byte[] resultBytes = new byte[TEST_BYTE_ARRAY.length];
        generator.nextBytes(resultBytes);
        assertEquals(TEST_BYTE_ARRAY, resultBytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(Double.isFinite(generator.nextDouble()));
        assertTrue(Double.isFinite(generator.nextDouble()));
        assertTrue(Double.isNaN(generator.nextDouble()));
        assertTrue(Double.isNaN(generator.nextDouble()));
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(Float.isFinite(generator.nextFloat()));
        assertTrue(Float.isFinite(generator.nextFloat()));
        assertTrue(Float.isNaN(generator.nextFloat()));
        assertTrue(Float.isNaN(generator.nextFloat()));
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(Double.isFinite(generator.nextGaussian()));
        assertTrue(Double.isFinite(generator.nextGaussian()));
        assertTrue(Double.isNaN(generator.nextGaussian()));
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(testValue, generator.nextInt());
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeLimit() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextInt(-1);
    }
}