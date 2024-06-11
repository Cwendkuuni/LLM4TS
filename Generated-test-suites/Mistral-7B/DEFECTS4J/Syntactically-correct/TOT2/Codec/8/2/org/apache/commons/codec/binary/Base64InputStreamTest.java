package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with no arguments.
     */
    @Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertNotNull(base64InputStream);
        assertEquals(Base64.DEFAULT, base64InputStream.getBase64());
    }

    /**
     * Test constructor with arguments.
     */
    @Test
    public void testArgumentsConstructor() {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64 base64 = new Base64(false, '\r', '\n');
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 76, new byte[] { '\r', '\n' });
        assertNotNull(base64InputStream);
        assertEquals(base64, base64InputStream.getBase64());
    }

    /**
     * Test reading single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte readByte = (byte) base64InputStream.read();
        assertEquals((byte) 'R', readByte);
    }

    /**
     * Test reading multiple bytes.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] expectedBytes = new byte[] { 'R', 'n', 'J', 'v', 'b', 'S', 'B', 'z', 'Y', 'W', '5', 'j', 'Z', 'Q', '=' };
        byte[] bytes = new byte[expectedBytes.length];
        int length = base64InputStream.read(bytes, 0, expectedBytes.length);
        assertNotNull(bytes);
        assertNotNull(length);
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], bytes[i]);
        }
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertFalse(base64InputStream.markSupported());
    }

    /**
     * Test decoding incorrect base64 data.
     */
    @Test
    public void testDecodeIncorrectBase64Data() {
        InputStream input = new ByteArrayInputStream("InvalidBase64Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertThrows(IOException.class, () -> base64InputStream.read());
    }

    /**
     * Test encoding empty data.
     */
    @Test
    public void testEncodeEmptyData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64 base64 = new Base64(false);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, base64);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, new byte[] { '\r', '\n' });
        base64InputStream.transferTo(base64OutputStream);
        String decodedData = new String(Base64.decodeBase64(output.toByteArray()));
        assertNull(decodedData);
    }
}