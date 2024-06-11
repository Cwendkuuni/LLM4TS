package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    @Test
    public void testReadBits() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45, 0x67});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading a single bit
        long bit = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x01, bit);

        // Test reading multiple bits from the same byte
        bit = bitInputStream.readBits(3);
        assertEquals(0x0123 & ((1 << 3) - 1), bit);

        // Test reading bits across byte boundaries in little endian order
        bit = bitInputStream.readBits(5);
        assertEquals(0x2345 & ((1 << 5) - 1), bit << 3);

        // Test reading bits across byte boundaries in big endian order
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bit = bitInputStream.readBits(5);
        assertEquals(0x6723 & ((1 << 5) - 1), bit >>> (32 - 5));

        // Test reading past the end of the stream
        try {
            bit = bitInputStream.readBits(1);
            fail();
        } catch (IOException e) {
            // Expected
        }

        // Test clearing the bit cache
        bitInputStream.clearBitCache();
        long expected = 0x01234567L;
        for (int i = 0; i < 24; i += 8) {
            long bits = bitInputStream.readBits(8);
            assertEquals(expected >>> i, bits);
            expected >>>= 8;
        }
        bit = bitInputStream.readBits(1);
        assertEquals(0, bit);
    }

    @Test
    public void testByteOrder() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x67, 0x23, 0x45, 0x01});
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        BitInputStream bitInputStreamBigEndian = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test reading the same bits in little endian and big endian orders
        long bitLittleEndian = bitInputStreamLittleEndian.readBits(32);
        long bitBigEndian = bitInputStreamBigEndian.readBits(32);
        assertEquals(bitBigEndian, bitLittleEndian);
    }

    @Test
    public void testIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        try {
            bitInputStream.readBits(1);
        } catch (IOException e) {
            // Expected
        }
    }
}