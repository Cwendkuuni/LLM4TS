package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest extends TestCase {

    /**
     * Test constructor.
     */
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);
        assertEquals(byteOrder, bitInputStream.getByteOrder());
        assertEquals(input, bitInputStream.getInputStream());
    }

    /**
     * Test close method.
     */
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);
        bitInputStream.close();
        try {
            bitInputStream.readBits(1);
            fail("Expected IOException after close");
        } catch (IOException e) {
            // Expected exception
        }
    }

    /**
     * Test clearBitCache method.
     */
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);
        long bits = bitInputStream.readBits(16);
        assertNotEquals(-1, bits);
        bitInputStream.clearBitCache();
        long clearedBits = bitInputStream.readBits(16);
        assertEquals(-1, clearedBits);
    }

    /**
     * Test readBits method with different number of bits to read.
     */
    public void testReadBits() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        // Test read 1 bit
        long bit1 = bitInputStream.readBits(1);
        assertEquals(bit1, 0x1 & ((long) 0x1a >> 7));

        // Test read 2 bits
        long bit2 = bitInputStream.readBits(2);
        assertEquals(bit2, ((long) 0x1a >> 5) & 0x3);

        // Test read 3 bits
        long bit3 = bitInputStream.readBits(3);
        assertEquals(bit3, ((long) 0x1a >> 2) & 0x7);

        // Test read 4 bits
        long bit4 = bitInputStream.readBits(4);
        assertEquals(bit4, ((long) 0x1a >> 0) & 0xf);

        // Test read 5 bits
        long bit5 = bitInputStream.readBits(5);
        assertEquals(bit5, ((long) 0x2b >> 3) & 0x1f);

        // Test read 6 bits
        long bit6 = bitInputStream.readBits(6);
        assertEquals(bit6, ((long) 0x2b >> 0) & 0x3f);

        // Test read 7 bits
        long bit7 = bitInputStream.readBits(7);
        assertEquals(bit7, ((long) 0x3c >> 1) & 0x7f);

        // Test read 8 bits
        long bit8 = bitInputStream.readBits(8);
        assertEquals(bit8, ((long) 0x3c >> 0) & 0xff);

        // Test read more bits than available
        try {
            bitInputStream.readBits(9);
            fail("Expected IOException for reading more bits than available");
        } catch (IOException e) {
            // Expected exception
        }

        // Test read all bits from the given input
        long allBits = bitInputStream.readBits(24);
        assertEquals(allBits, 0x1a2b3cL);
    }

    /**
     * Test getByteOrder method.
     */
    public void testGetByteOrder() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{}), byteOrder);
        assertEquals(byteOrder, bitInputStream.getByteOrder());
    }

    /**
     * Test getInputStream method.
     */
    public void testGetInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c});
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(input, byteOrder);
        assertEquals(input, bitInputStream.getInputStream());
    }
}