package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}
 */
public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    @Test
    public void testSetSeedInt() {
        final int seed = 123;
        generator.setSeed(seed);
        assertEquals(seed, generator.nextInt());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeInt() {
        generator.setSeed(-123);
    }

    @Test
    public void testSetSeedLong() {
        final long seed = 456L;
        generator.setSeed(seed);
        assertEquals(seed, generator.nextLong());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedNegativeLong() {
        generator.setSeed(-456L);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSetSeedIntArrayEmpty() {
        generator.setSeed(new int[]{});
    }

    @Test
    public void testSetSeedIntArray() {
        final int[] seed = {1, 2, 3};
        generator.setSeed(seed);
        for (int i = 0; i < seed.length; i++) {
            assertEquals(seed[i], generator.nextInt());
        }
    }

    @Test
    public void testNextBooleanTrue() {
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBooleanFalse() {
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertNotNull(b);
        }
    }

    @Test
    public void testNextDouble() {
        final double expectedDouble = FastMath.nextGauss(1.0, 0.1);
        final double actualDouble = generator.nextDouble();
        assertTrue(FastMath.abs(expectedDouble - actualDouble) <= 0.01);
    }

    @Test
    public void testNextDoubleSameValue() {
        double actualDouble = generator.nextDouble();
        double expectedDouble = actualDouble;
        for (int i = 0; i < 10; i++) {
            actualDouble = generator.nextDouble();
            assertTrue(FastMath.abs(expectedDouble - actualDouble) <= 0.01);
        }
    }

    @Test
    public void testNextFloat() {
        final float expectedFloat = (float) (FastMath.nextGauss(1.0f, 0.1f));
        final float actualFloat = generator.nextFloat();
        assertTrue(FastMath.abs(expectedFloat - actualFloat) <= 0.01f);
    }

    @Test
    public void testNextFloatSameValue() {
        float actualFloat = generator.nextFloat();
        float expectedFloat = actualFloat;
        for (int i = 0; i < 10; i++) {
            actualFloat = generator.nextFloat();
            assertTrue(FastMath.abs(expectedFloat - actualFloat) <= 0.01f);
        }
    }

    @Test
    public void testNextInt() {
        final int expectedInt = 42;
        generator.nextInt(); // discard previous value
        assertEquals(expectedInt, generator.nextInt());
    }

    @Test
    public void testNextIntSameValue() {
        int actualInt = generator.nextInt();
        int expectedInt = actualInt;
        for (int i = 0; i < 10; i++) {
            actualInt = generator.nextInt();
            assertEquals(expectedInt, actualInt);
        }
    }

    @Test
    public void testNextIntBoundary() {
        generator.nextInt(1);
        generator.nextInt(Integer.MIN_VALUE);
        generator.nextInt(Integer.MAX_VALUE);
        generator.nextInt(Integer.MAX_VALUE + 1);
    }

    @Test
    public void testNextIntNegativeBoundary() {
        generator.nextInt(-1);
        generator.nextInt(-Integer.MIN_VALUE);
        generator.nextInt(-123456);
    }

    @Test
    public void testNextLong() {
        final long expectedLong = 1234567890123L;
        generator.nextLong(); // discard previous value
        assertEquals(expectedLong, generator.nextLong());
    }

    @Test
    public void testNextLongSameValue() {
        long actualLong = generator.nextLong();
        long expectedLong = actualLong;
        for (int i = 0; i < 10; i++) {
            actualLong = generator.nextLong();
            assertEquals(expectedLong, actualLong);
        }
    }

    @Test
    public void testNextLongBoundary() {
        generator.nextLong(); // discard previous value
        generator.nextLong(1);
        generator.nextLong(Long.MIN_VALUE);
        generator.nextLong(Long.MAX_VALUE);
        generator.nextLong(Long.MAX_VALUE + 1);
    }

    @Test
    public void testNextLongNegativeBoundary() {
        generator.nextLong(-1);
        generator.nextLong(-Long.MIN_VALUE);
        generator.nextLong(-123456);
    }

    @Test
    public void testNextGaussian() {
        final double expectedMean = 0.5;
        final double expectedStdDev = 0.1;
        for (int i = 0; i < 1000; i++) {
            final double actualDouble = generator.nextGaussian();
            final double zScore = (actualDouble - expectedMean) / expectedStdDev;
            assertTrue(FastMath.abs(zScore) <= 3);
        }
    }

    @Test
    public void testClear() {
        final int expectedInt = 42;
        generator.setSeed(42);
        assertEquals(expectedInt, generator.nextInt());
        generator.clear();
        assertNull(generator.nextInt());
    }

}