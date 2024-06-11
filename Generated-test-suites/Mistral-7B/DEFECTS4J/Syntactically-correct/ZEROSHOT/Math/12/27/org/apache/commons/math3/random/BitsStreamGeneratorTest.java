package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] >= 0 && bytes[i] <= 255);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result <= 1.0);
        }
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            float result = generator.nextFloat();
            assertTrue(result >= 0.0f && result <= 1.0f);
        }
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            double result = generator.nextGaussian();
            assertTrue(result >= -3.0 && result <= 3.0);
        }
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            int result = generator.nextInt();
            assertTrue(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int n = 16;
        int result = generator.nextInt(n);
        int mask = n - 1;
        assertTrue((result & mask) == result);
    }

    @Test
    public void testNextIntNotPowerOfTwo() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        int n = 17;
        int result;
        do {
            result = generator.nextInt(n);
        } while (result % n != 0);
        assertEquals(result % n, 0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            long result = generator.nextLong();
            assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
        }
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MyBitsStreamGenerator();
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }

    private static class MyBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        protected int next(int bits) {
            return (int) (Math.random() * (1 << bits));
        }
    }

}