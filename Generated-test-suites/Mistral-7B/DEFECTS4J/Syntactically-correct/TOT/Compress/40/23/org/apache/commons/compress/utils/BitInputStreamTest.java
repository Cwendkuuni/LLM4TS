package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

/**
 * Test cases for the BitInputStream class.
 */
public class BitInputStreamTest {

    /**
     * Test constructor with valid InputStream and ByteOrder.
     */
    @Test
    public void testConstructorValid() {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.getByteOrder());
        assertEquals(input, bitInputStream.getInputStream());
    }

    /**
     * Test constructor with null InputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullInputStream() {
        new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Test constructor with null ByteOrder.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullByteOrder() {
        new BitInputStream(new ByteArrayInputStream(new byte[] { 0x01 }), null);
    }

    /**
     * Test clearBitCache method.
     */
    @Test
    public void testClearBitCache() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    /**
     * Test readBits method with count equals 1.
     */
    @Test
    public void testReadBitsOneBit() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0x00000001L, bitInputStream.readBits(1));
        assertEquals(0x00000001L, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(1));
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Test readBits method with count equals 4.
     */
    @Test
    public void testReadBitsFourBits() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x05, 0x06 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0x00000101L, bitInputStream.readBits(1));
        assertEquals(0x00001100L, bitInputStream.readBits(3));
        assertEquals(0x00011100L, bitInputStream.readBits(4));
        assertEquals(0x00011111L, bitInputStream.readBits(5));
        assertEquals(-1, bitInputStream.readBits(1));
    }

    /**
     * Test readBits method with count greater than 63.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximum() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }

    /**
     * Test readBits method with negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        final BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }
}