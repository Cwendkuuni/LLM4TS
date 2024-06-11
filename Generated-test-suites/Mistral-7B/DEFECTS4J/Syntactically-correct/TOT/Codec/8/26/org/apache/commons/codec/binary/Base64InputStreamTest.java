package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test construction with default decoding behavior.
     */
    @Test
    public void testBase64InputStreamDefaultDecoding() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', ' ', 'B', 'a', 's', 'e', '6', '4', 'I', 'n', 'P', 'U', 'T', 'S', 't', 'r', 'a', 'm', 'I', 'n', 'P', 'S', 't', 'r', 'e', 'a', 'm', '\r', '\n' });
        final Base64InputStream base64InputStream = new Base64InputStream(input);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] bytes = new byte[1];
        int len;

        // Test reading single bytes
        len = base64InputStream.read(bytes, 0, 1);
        assertEquals('T', bytes[0]);
        len = base64InputStream.read(bytes, 0, 1);
        assertEquals('e', bytes[0]);
        // ... and so on, until EOF is reached

        // Test reading multiple bytes
        len = base64InputStream.read(bytes, 0, 4);
        assertEquals("Test", new String(bytes, 0, 4));
        len = base64InputStream.read(bytes, 0, 15);
        assertEquals("Base64InputStreamTest", new String(bytes, 0, 15));

        // Test closing the input stream
        base64InputStream.close();
        assertNull(base64InputStream.in);
    }

    /**
     * Test construction with encoding behavior.
     */
    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't' });
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Base64OutputStream base64OutputStream = new Base64OutputStream(output, 76, new byte[] { '\r', '\n' });
        final Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, new byte[] { '\r', '\n' });

        // Test reading single bytes
        int len = base64InputStream.read();
        assertEquals(len, 1);
        assertEquals(base64OutputStream.toString(), "VGpzdA==");

        // Test reading multiple bytes
        byte[] bytes = new byte[4];
        len = base64InputStream.read(bytes, 0, bytes.length);
        assertEquals(len, 4);
        assertEquals(base64OutputStream.toString(), "VGpzdABvAw==");

        // Test closing the input and output streams
        base64InputStream.close();
        base64OutputStream.close();
    }

    /**
     * Test construction with invalid input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamNullInputStream() {
        new Base64InputStream(null, false);
    }
}