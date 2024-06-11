package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = new byte[]{0x01, 0x02, 0x03, 0x04};
        inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testConstructor() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(byteOrder, bitInputStream.byteOrder);
        assertEquals(inputStream, bitInputStream.in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeCount() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBigCount() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits() throws IOException {
        // Test reading 1 bit
        assertEquals(1L, bitInputStream.readBits(1));

        // Test reading multiple bits
        assertEquals(0b01101110L, bitInputStream.readBits(7));

        // Test reading until EOS
        assertEquals(-1L, bitInputStream.readBits(1));

        // Test clearing cache
        bitInputStream.clearBitCache();
        assertEquals(0L, bitInputStream.readBits(1));

        // Test reading multiple bits from the same byte
        inputStream = new ByteArrayInputStream(new byte[]{0x80});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(128L, bitInputStream.readBits(7));
        assertEquals(0L, bitInputStream.readBits(1));

        // Test reading multiple bytes
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(128L, bitInputStream.readBits(7));
        assertEquals(0x02L, bitInputStream.readBits(8));
        assertEquals(0x03L, bitInputStream.readBits(8));
        assertEquals(-1L, bitInputStream.readBits(1));
    }

    @Test(expected = IOException.class)
    public void testReadBitsIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
        inputStream.close();
        bitInputStream.readBits(1);
    }

    @Test
    public void testByteOrderLittleEndian() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0x01L, bitInputStream.readBits(1));
        assertEquals(0x02L, bitInputStream.readBits(1));
        assertEquals(0x03L, bitInputStream.readBits(1));
        assertEquals(0x04L, bitInputStream.readBits(1));
        assertEquals(-1L, bitInputStream.readBits(1));
    }

    @Test
    public void testByteOrderBigEndian() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(0x04L, bitInputStream.readBits(1));
        assertEquals(0x03L, bitInputStream.readBits(1));
        assertEquals(0x02L, bitInputStream.readBits(1));
        assertEquals(0x01L, bitInputStream.readBits(1));
        assertEquals(-1L, bitInputStream.readBits(1));
    }
}