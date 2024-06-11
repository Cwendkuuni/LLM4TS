package org.apache.commons.codec.binary;

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
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test cases for {@link Base64InputStream} class.
 *
 * @author John Doe
 */
public class Base64InputStreamTest {

    /**
     * Test constructor {@link Base64InputStream#Base64InputStream(InputStream)} with a {@code InputStream} instance.
     */
    @Test
    public void testBase64InputStreamDefaultDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[12];
        int length = base64InputStream.read(result);

        InputStream originalInputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] expected = new byte[12];
        int len = originalInputStream.read(expected, 0, 12);

        assertTrue(length == len);
        for (int i = 0; i < length; i++) {
            assertEquals(expected[i], result[i]);
        }

        base64InputStream.close();
        originalInputStream.close();
    }

    /**
     * Test constructor {@link Base64InputStream#Base64InputStream(InputStream, boolean)} with a {@code InputStream} instance
     * and {@code false} decoder flag.
     */
    @Test
    public void testBase64InputStreamEncoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes());

        int length = base64InputStream.read(base64OutputStream.getBuffer(), 0, base64OutputStream.getBuffer().length);
        base64OutputStream.flush();
        base64OutputStream.close();

        String result = outputStream.toString();
        assertEquals("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n", result);

        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test constructor {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} with a {@code InputStream}
     * instance, {@code false} decoder flag, and custom line length and line separator.
     */
    @Test
    public void testBase64InputStreamCustomDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 48, "\\r\\n".getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes());

        int length = base64InputStream.read(base64OutputStream.getBuffer(), 0, base64OutputStream.getBuffer().length);
        base64OutputStream.flush();
        base64OutputStream.close();

        String result = outputStream.toString();
        assertEquals("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n", result);

        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test method {@link Base64InputStream#read()} with a {@code Base64InputStream} instance.
     */
    @Test
    public void testBase64InputStreamRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[12];
        int length = base64InputStream.read(result);

        InputStream originalInputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] expected = new byte[12];
        int len = originalInputStream.read(expected, 0, 12);

        assertTrue(length == len);
        for (int i = 0; i < length; i++) {
            assertEquals(expected[i], result[i]);
        }

        base64InputStream.close();
        originalInputStream.close();
    }

    /**
     * Test method {@link Base64InputStream#read(byte[], int, int)} with a {@code Base64InputStream} instance.
     */
    @Test
    public void testBase64InputStreamReadWithByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[12];
        int length = base64InputStream.read(result);

        InputStream originalInputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] expected = new byte[12];
        int len = originalInputStream.read(expected, 0, 12);

        assertTrue(length == len);
        for (int i = 0; i < length; i++) {
            assertEquals(expected[i], result[i]);
        }

        base64InputStream.close();
        originalInputStream.close();
    }

    /**
     * Test method {@link Base64InputStream#markSupported()} with a {@code Base64InputStream} instance.
     */
    @Test
    public void testBase64InputStreamMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
    }

    /**
     * Test method {@link Base64InputStream#close()} with a {@code Base64InputStream} instance.
     */
    @Test
    public void testBase64InputStreamClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXhhbXBsZSBoYXZlIGlzIGlzIGlzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.close();

        inputStream.close();
    }
}