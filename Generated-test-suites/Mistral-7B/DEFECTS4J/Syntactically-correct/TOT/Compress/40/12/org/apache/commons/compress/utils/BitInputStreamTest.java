package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    @org.junit.Before
    public void setUp() {
        // Set up
    }

    @org.junit.After
    public void tearDown() {
        // Clean up
    }

    /**
     * Test constructor with valid arguments.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testConstructorValidArguments() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
        // Exercise and verify
    }

    /**
     * Test constructor with null InputStream argument.
     * @expected Exception message "InputStream cannot be null"
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNullInputStream() {
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        new BitInputStream(null, byteOrder);
        // Not reached
    }

    /**
     * Test constructor with null ByteOrder argument.
     * @expected Exception message "ByteOrder cannot be null"
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNullByteOrder() {
        final InputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45});
        new BitInputStream(in, null);
        // Not reached
    }

    /**
     * Test readBits with valid arguments.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testReadBitsValidArguments() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        // Test reading 1 bit
        final long bits = bitInputStream.readBits(1);
        assertEquals(0x01 & 0x01L, bits);

        // Test reading 63 bits
        for (int i = 1; i <= MAXIMUM_CACHE_SIZE; i++) {
            final long bits = bitInputStream.readBits(i);
            assertEquals(0x01L << (i - 1), bits);
        }

        // Test reading 64 bits
        final long bits = bitInputStream.readBits(64);
        assertEquals(-1L, bits);

        // Test reading multiple bits from different bytes
        final long bits1 = bitInputStream.readBits(3);
        assertEquals((0x01 << 6) | (0x23 & 0x3F) | (0x45 >>> 5), bits1);

        // Test reading multiple bits from the same byte
        final long bits2 = bitInputStream.readBits(7);
        assertEquals((0x01 << 1) | (0x23 & 0xE0) | (0x45 >>> 1), bits2);

        // Test reading multiple bits from the same byte with byte order as big endian
        final ByteOrder byteOrderBE = ByteOrder.BIG_ENDIAN;
        final BitInputStream bitInputStreamBE = new BitInputStream(in, byteOrderBE);
        final long bits3 = bitInputStreamBE.readBits(7);
        assertEquals((0x45 >>> 1) | (0x23 & 0xE0) | (0x01 << 1), bits3);

        // Test reading multiple bits from the same byte with byte order as big endian and bits from different bytes
        final long bits4 = bitInputStreamBE.readBits(11);
        assertEquals((0x45 >>> 1) | (0x23 << 3) | (0x01 << 6), bits4);

        // Test clearing the cache
        bitInputStream.clearBitCache();
        final long bits5 = bitInputStream.readBits(7);
        assertEquals(-1L, bits5);
    }

    /**
     * Test readBits with invalid arguments.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testReadBitsInvalidArguments() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        // Test negative count
        final long bits = bitInputStream.readBits(-1);
        assertEquals(-1L, bits);

        // Test count larger than 63
        final long bits = bitInputStream.readBits(64);
        assertEquals(-1L, bits);
    }

    /**
     * Test closing the stream.
     * @throws IOException on error
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        // Exercise and verify
        bitInputStream.close();
        final InputStream inputStream = bitInputStream.in;
        assertFalse(inputStream.isOpen());

        // Test reading after closing
        final long bits = bitInputStream.readBits(1);
        assertEquals(-1L, bits);
    }
}