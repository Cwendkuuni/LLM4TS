package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64-encoded input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testDecode() throws IOException {
        String base64Input = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZW1zIHN1ZmZpeA==";

        InputStream input = new ByteArrayInputStream(base64Input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] expected = { 32, 101, 99, 111, 112, 100, 32, 108, 105, 112, 101, 32, 117, 112, 112, 115, 58, 44, 32, 32, 55, 53, 53, 44, 32, 32, 41, 56, 41, 57, 41, 52, 41, 51, 53, 44, 32, 32, 48, 51, 50, 48, 48, 55, 44, 32, 32, 45, 43 };
        byte[] actual = new byte[expected.length];

        int length = base64InputStream.read(actual);
        output.write(actual, 0, length);
        output.flush();
        assertArrayEquals(expected, output.toByteArray());
    }

    /**
     * Test encoding a Base64 input stream with a custom line length and line separator.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeCustom() throws IOException {
        String data = "hello world";

        InputStream input = new ByteArrayInputStream(data.getBytes());
        byte[] lineSeparator = new byte[] { 13, 10 };
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 50, lineSeparator);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] expected = new byte[] { 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 61, 63 };

        byte[] actual = new byte[expected.length];
        int length = base64InputStream.read(actual);
        output.write(actual, 0, length);
        output.flush();

        assertArrayEquals(expected, output.toByteArray());
    }

    /**
     * Test the error handling when reading an input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] actual = new byte[5];
        base64InputStream.read(actual);
        output.write(actual, 0, 5);
        output.flush();

        input.close(); // simulate an I/O error
        int length = base64InputStream.read(actual);
        assertFalse("Expected IOException, but read a byte instead.", length > -1);
    }

    /**
     * Test the error handling when creating a Base64InputStream with a null InputStream.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        new Base64InputStream(null, false);
    }

    /**
     * Test the error handling when creating a Base64InputStream with invalid parameters.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters() {
        new Base64InputStream(new ByteArrayInputStream("data".getBytes()), true, -1, null);
    }

    /**
     * Test the end of the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream("data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] actual = new byte[1];
        int length = base64InputStream.read(actual);
        assertEquals(-1, length);
        assertNull(actual);
    }
}