package org.junit.tests;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        generator.clear();
        assertFalse(generator.nextBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSeed() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.setSeed(-1);
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        BitsStreamGenerator generator = new BitsStreamGenerator();
        generator.nextBytes(new byte[4]);
        byte[] actual = new byte[4];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);

        byte[] empty = new byte[0];
        generator.nextBytes(empty);

        byte[] large = new byte[Integer.MAX_VALUE + 1];
        generator.nextBytes(large);

        generator.nextBytes(null);

        byte[] invalidSeed = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05}; // 5 bytes
        generator.setSeed(invalidSeed);
        generator.nextBytes(new byte[5]);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        double random = generator.nextDouble();
        assertTrue(Double.isFinite(random));
        assertTrue(random >= 0.0);
        assertTrue(random <= 1.0);

        generator.clear();
        double random2 = generator.nextDouble();
        assertFalse(Double.isFinite(random2));

        generator.setSeed(Double.NaN);
        double random3 = generator.nextDouble();
        assertTrue(Double.isNaN(random3));

        generator.setSeed(Double.POSITIVE_INFINITY);
        double random4 = generator.nextDouble();
        assertTrue(Double.isInfinite(random4));
    }
}