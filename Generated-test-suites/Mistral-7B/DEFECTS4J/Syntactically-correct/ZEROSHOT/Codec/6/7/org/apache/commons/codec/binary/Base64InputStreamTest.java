package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82 });
        Base64InputStream test = new Base64InputStream(input);

        byte[] expected = new byte[] { 0x3c, 0x80, 0x81, 0x82 };
        byte[] result = new byte[4];
        int numRead = test.read(result, 0, 4);
        assertArrayEquals(expected, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test constructor with decoding and custom line length
     */
    @Test
    public void testDecoderWithLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82 });
        Base64InputStream test = new Base64InputStream(input, 42);

        byte[] expected = new byte[] { 0x3c, 0x80, 0x81, 0x82 };
        byte[] result = new byte[4];
        int numRead = test.read(result, 0, 4);
        assertArrayEquals(expected, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test constructor with encoding
     */
    @Test
    public void testEncoder() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true);

        byte[] expected = new byte[] {
            (byte) 0x40, (byte) 0x81, (byte) 0x91, (byte) 0x82, (byte) 0x91, (byte) 0x92, (byte) 0x83, (byte) 0x92
        };
        byte[] result = new byte[len];
        int numRead = test.read(result, 0, len);
        assertArrayEquals(expected, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test constructor with encoding and custom line length
     */
    @Test
    public void testEncoderWithLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true, 76);

        byte[] expected = new byte[] {
            (byte) 0x40, (byte) 0x81, (byte) 0x91, (byte) 0x82, (byte) 0x91, (byte) 0x92, (byte) 0x83, (byte) 0x92,
            (byte) 0x43
        };
        byte[] result = new byte[len];
        int numRead = test.read(result, 0, len);
        assertArrayEquals(expected, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test constructor with decoding and no input
     */
    @Test(expected = IOException.class)
    public void testDecoderWithNoInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream test = new Base64InputStream(input);
    }

    /**
     * Test constructor with encoding and no input
     */
    @Test(expected = IOException.class)
    public void testEncoderWithNoInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream test = new Base64InputStream(input, true);
    }

    /**
     * Test reading null byte array
     */
    @Test(expected = NullPointerException.class)
    public void testNullByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true);
        test.read(null, 0, 1);
    }

    /**
     * Test reading negative offset
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true);
        test.read(new byte[3], -1, 1);
    }

    /**
     * Test reading negative length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true);
        test.read(new byte[3], 0, -1);
    }

    /**
     * Test reading when there is no data
     */
    @Test
    public void testNoData() throws IOException {
        Base64InputStream test = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        byte[] result = new byte[3];
        int numRead = test.read(result, 0, 3);
        assertNull(result);
        assertEquals(-1, test.read());
    }

    /**
     * Test reading a large input
     */
    @Test
    public void testLargeInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[1024 * 10]);
        Base64InputStream test = new Base64InputStream(input, true);

        byte[] result = new byte[1024 * 10];
        int numRead = test.read(result, 0, result.length);
        // The actual length read will vary based on the input
        assertTrue(numRead > 0);
        assertEquals(-1, test.read());
    }

    /**
     * Test reading when there is no data left
     */
    @Test
    public void testNoDataLeft() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82 });
        Base64InputStream test = new Base64InputStream(input, false);

        byte[] result = new byte[3];
        int numRead = test.read(result, 0, 3);
        assertArrayEquals(new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82 }, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test reading when there is no data left and decoding
     */
    @Test
    public void testNoDataLeftDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82 });
        Base64InputStream test = new Base64InputStream(input, false);

        byte[] result = new byte[3];
        int numRead = test.read(result, 0, 3);
        assertArrayEquals(new byte[] { (byte) 0x3c, (byte) 0x80, (byte) 0x81 }, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test reading when there is no data left and encoding
     */
    @Test
    public void testNoDataLeftEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true, 76);

        byte[] result = new byte[14];
        int numRead = test.read(result, 0, 14);
        assertArrayEquals(new byte[] {
            (byte) 0x40, (byte) 0x81, (byte) 0x91, (byte) 0x82, (byte) 0x91, (byte) 0x92, (byte) 0x83, (byte) 0x92,
            (byte) 0x43, (byte) 0x3e, (byte) 0x3e
        }, result);
        assertEquals(-1, test.read());
    }

    /**
     * Test markSupported
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Base64InputStream test = new Base64InputStream(input, true);
        assertFalse(test.markSupported());
    }
}