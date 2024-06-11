package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.compress.utils.ByteOrder.LITTLE_ENDIAN;

/**
 * Test cases for the BitInputStream class.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        final byte[] data = new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111};
        inputStream = new ByteArrayInputStream(data);
    }

    /**
     * Test reading 1 bit.
     * @throws IOException on error
     */
    @Test
    public void testReadOneBit() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
        final long bit = bitInputStream.readBits(1);

        Assert.assertEquals("Expected 1 bit to be read.", 1L, bit);
    }

    /**
     * Test reading multiple bits.
     * @throws IOException on error
     */
    @Test
    public void testReadMultipleBits() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
        final long bits = bitInputStream.readBits(6);

        Assert.assertEquals("Expected 6 bits to be read.", 0b111111_011111_010111_1111L, bits);
    }

    /**
     * Test reading more bits than available.
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadMoreBitsThanAvailable() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    /**
     * Test clearing the bit cache.
     * @throws IOException on error
     */
    @Test
    public void testClearBitCache() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
        bitInputStream.readBits(6);
        bitInputStream.clearBitCache();
        final long bit = bitInputStream.readBits(1);

        Assert.assertEquals("Expected the cache to be cleared.", 0L, bit);
    }

    /**
     * Test reading bits from an empty stream.
     * @throws IOException on error
     */
    @Test(expected = IOException.class)
    public void testReadBitsFromEmptyStream() throws IOException {
        final InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        final BitInputStream bitInputStream = new BitInputStream(emptyStream, LITTLE_ENDIAN);

        bitInputStream.readBits(1);
    }
}