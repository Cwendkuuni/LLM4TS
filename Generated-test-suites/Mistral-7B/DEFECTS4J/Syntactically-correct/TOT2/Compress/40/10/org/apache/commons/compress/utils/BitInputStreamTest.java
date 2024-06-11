package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {

    // Test case for constructor taking a null InputStream
    @Test(expected = NullPointerException.class)
    public void testConstructorNullInputStream() {
        assertThrows(NullPointerException.class, () -> new BitInputStream(null, ByteOrder.LITTLE_ENDIAN));
    }

    // Test case for constructor and readBits method with valid arguments
    @Test
    public void testReadBitsValidArguments() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading 1 bit
        assertEquals(1L, bitInputStream.readBits(1));
        // Test reading 6 bits
        assertEquals(23L, bitInputStream.readBits(6));
        // Test reading 63 bits
        assertEquals(Long.parseLong("11111111111111111111111111111111111111111111111111111111", 2), bitInputStream.readBits(63));

        // Reset bit cache and repeat the tests
        bitInputStream.clearBitCache();
        assertEquals(1L, bitInputStream.readBits(1));
        // ...
    }

    // Test case for constructor and readBits method with invalid arguments
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidArguments() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test negative count
        bitInputStream.readBits(-1);

        // Test count bigger than 63
        bitInputStream.readBits(64);
    }

    // Test case for constructor and readBits method with different byte orders
    @Test
    public void testReadBitsDifferentByteOrders() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream littleEndianBitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        BitInputStream bigEndianBitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Read the same number of bits from both inputs and compare the results
        for (int i = 1; i <= 63; i++) {
            assertEquals(littleEndianBitInputStream.readBits(i), bigEndianBitInputStream.readBits(i));
        }
    }

    // Test case for close method
    @Test
    public void testClose() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();

        // Check that the input stream is closed
        assertThrows(IOException.class, () -> inputStream.read());
    }

    // Test case for constructor with invalid ByteOrder
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidByteOrder() {
        assertThrows(IllegalArgumentException.class, () -> new BitInputStream(new ByteArrayInputStream(new byte[0]), new ByteOrder("INVALID")));
    }

    // Test case for end of stream
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Read all bits from the input stream
        bitInputStream.readBits(1);
        bitInputStream.readBits(6);
        bitInputStream.readBits(63);

        // Test reading more bits
        assertEquals(-1L, bitInputStream.readBits(1));
    }

    // Test case for clearBitCache method
    @Test
    public void testClearBitCache() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111});
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Read some bits
        bitInputStream.readBits(1);
        bitInputStream.readBits(6);
        bitInputStream.readBits(63);

        // Clear the bit cache and check that the readBits method returns the correct value
        bitInputStream.clearBitCache();
        assertEquals(0L, bitInputStream.readBits(1));
    }
}