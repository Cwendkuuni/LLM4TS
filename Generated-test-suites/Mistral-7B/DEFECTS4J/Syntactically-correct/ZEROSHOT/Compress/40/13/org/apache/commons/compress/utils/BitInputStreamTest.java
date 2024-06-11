package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private static final int TEST_BIT_COUNT = 64;
    private static final int TEST_BYTE_COUNT = TEST_BIT_COUNT / 8;

    private InputStream testInputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setup() throws IOException {
        testInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        assertNotNull(bitInputStream.in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidByteOrder() {
        new BitInputStream(testInputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(TEST_BIT_COUNT);
        bitInputStream.clearBitCache();
        assertLongEquals(0, bitInputStream.readBits(TEST_BIT_COUNT));
    }

    @Test
    public void testReadBits() throws IOException {
        long expected = 0b01101101101101101101101101101101;
        long actual = 0;
        for (int bitCount = 1; bitCount <= TEST_BIT_COUNT; bitCount += 8) {
            actual = bitInputStream.readBits(bitCount);
            assertEquals("Failed to read expected bits with bit count: " + bitCount, expected >>> (32 - bitCount), actual);
            expected &= MASKS[bitCount - 1];
        }
        assertEquals("Failed to reach end of stream", -1, bitInputStream.readBits(TEST_BIT_COUNT));
    }

    @Test
    public void testReadBitsWithInvalidBitCount() throws IOException {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadBitsWithInvalidBitCountGreaterThanMaximum() throws IOException {
        try {
            bitInputStream.readBits(TEST_BIT_COUNT + 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}