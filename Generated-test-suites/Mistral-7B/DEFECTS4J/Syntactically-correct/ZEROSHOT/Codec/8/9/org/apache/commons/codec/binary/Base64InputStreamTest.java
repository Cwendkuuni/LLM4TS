package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * Test class for the {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test the base case where a {@link Base64InputStream} is created without any parameters.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testBaseCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = new byte[] { (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35 };
        byte[] buffer = new byte[6];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 6);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Test the case where a {@link Base64InputStream} is created to decode data.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testDecodeCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expected = new byte[] { 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f };
        byte[] buffer = new byte[6];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 6);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Test the case where a {@link Base64InputStream} is created to encode data.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testEncodeCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expected = new byte[] { 0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49 };
        byte[] buffer = new byte[11];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 11);
        outputStream.write(buffer, 0, actual);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    /**
     * Test the case where a {@link Base64InputStream} is created to decode data with a custom line length.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testDecodeCustomLineLengthCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 5, new byte[] { 0x0A });
        byte[] expected = new byte[] { 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f, 0x0A };
        byte[] buffer = new byte[7];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 7);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Test the case where a {@link Base64InputStream} is created to encode data with a custom line length.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testEncodeCustomLineLengthCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 5, new byte[] { 0x0A });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expected = new byte[] { 0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x0A };
        byte[] buffer = new byte[10];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 10);
        outputStream.write(buffer, 0, actual);
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    /**
     * Test the case where a {@link Base64InputStream} returns EOF.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testEOFCase() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[7];
        int actual = base64InputStream.read(buffer);
        assertEquals(actual, 6);
        assertArrayEquals(new byte[] { 0x40, 0x41, 0x42, 0x43, 0x44, 0x45 }, buffer);
        actual = base64InputStream.read(buffer);
        assertNull(actual);
    }

    /**
     * Test the case where a {@link Base64InputStream} is created with a null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputStreamCase() {
        new Base64InputStream(null);
    }

    /**
     * Test the case where a {@link Base64InputStream} is created with a negative line length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLineLengthCase() {
        new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, -1, new byte[0]);
    }
}