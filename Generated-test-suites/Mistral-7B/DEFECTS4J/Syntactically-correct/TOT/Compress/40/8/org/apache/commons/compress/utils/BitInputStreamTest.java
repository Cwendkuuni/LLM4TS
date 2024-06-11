package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @After
    public void tearDown() throws IOException {
        outputStream.close();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final InputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04});
        final BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
        assertNotNull(bitInputStream);
    }

    /**
     * Test {@link #clearBitCache()}.
     */
    @Test
    public void testClearBitCache() throws IOException {
        outputStream.write("01110101".getBytes());
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
    }

    /**
     * Test reading bits from an InputStream.
     */
    @Test
    public void testReadBits() throws IOException {
        outputStream.write("01110101".getBytes());
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading one bit
        assertEquals(0x1, bitInputStream.readBits(1));
        assertEquals(0x0, bitInputStream.readBits(1));

        // Test reading multiple contiguous bits
        assertEquals(0b1111_0101L, bitInputStream.readBits(5));
        assertEquals(0b1010_1010L, bitInputStream.readBits(6));

        // Test reading multiple non-contiguous bits
        assertEquals(0b1111_0101L, bitInputStream.readBits(5));
        assertEquals(0b0101_0101L, bitInputStream.readBits(5));

        // Test reading more bits than available
        assertEquals(-1, bitInputStream.readBits(9));
    }

    /**
     * Test reading bits from an empty InputStream.
     */
    @Test
    public void testReadBitsFromEmptyInputStream() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1, bitInputStream.readBits(3));
    }

    /**
     * Test reading bits from an InputStream with an error.
     */
    @Test(expected = IOException.class)
    public void testReadBitsWithIOException() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test reading bits from an InputStream with zero length.
     */
    @Test
    public void testReadBitsWithZeroLengthInputStream() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(-1, bitInputStream.readBits(3));
    }

    /**
     * Test reading bits with BigEndian byte order.
     */
    @Test
    public void testReadBitsWithBigEndianByteOrder() throws IOException {
        outputStream.write("10101011".getBytes());
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test reading one bit
        assertEquals(0x1, bitInputStream.readBits(1));
        assertEquals(0x0, bitInputStream.readBits(1));

        // Test reading multiple contiguous bits
        assertEquals(0b1010_1010L, bitInputStream.readBits(6));
        assertEquals(0b0101_0101L, bitInputStream.readBits(6));

        // Test reading multiple non-contiguous bits
        assertEquals(0b1010_1010L, bitInputStream.readBits(6));
        assertEquals(0b0101_0101L, bitInputStream.readBits(6));

        // Test reading more bits than available
        assertEquals(-1, bitInputStream.readBits(9));
    }
}