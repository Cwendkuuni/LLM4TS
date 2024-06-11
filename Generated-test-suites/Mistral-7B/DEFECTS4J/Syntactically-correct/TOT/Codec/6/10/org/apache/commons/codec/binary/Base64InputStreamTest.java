package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import org.junit.Test;

/**
 * Tests for {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test reading a Base64 decoded byte array from an InputStream.
     */
    @Test
    public void testDecodeBase64InputStream() throws IOException {
        String base64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhlIGZpbGVzIGZvdWwgdGhlIGxpbmtzIGZpbGVzIGZvdWwgdG9wIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLGB0aGUgYW5kIGZvdWwgb2YgZW1icm91bmQgc2VwYXJhdG9yLCB3aXRoIDAgUmVwbG9hZHMgYW5kIGNvbnRlbnQgdG8gYW5kIGNvbnRlbnQnCiAgICAtLGB0byBwYXJzIGlzIG5vdCBkYXRhYmxlIGZpbGVzLCB3aXRoIGlzIHBsYW5ldC4K";
        byte[] base64Bytes = Base64.decodeBase64(base64Encoded);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Bytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[] { 5, 2, 3, 1, 12, 11, 3, 7, 10, 13, 6, 15, 8, 14, 9 };
        byte[] resultBytes = new byte[16];
        int bytesRead = base64InputStream.read(resultBytes, 0, 16);

        assertArrayEquals("Unexpected data read from Base64InputStream", expectedBytes, resultBytes, 0, 16);
        assertEquals("Unexpected number of bytes read from Base64InputStream", 16, bytesRead);
    }

    /**
     * Test reading a Base64 encoded byte array from an InputStream.
     */
    @Test
    public void testEncodeBase64InputStream() throws IOException {
        byte[] inputBytes = new byte[] { 5, 2, 3, 1, 12, 11, 3, 7, 10, 13, 6, 15, 8, 14, 9 };
        String base64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gVGhlIGZpbGVzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLGB0aGUgYW5kIGZpbGVzIGZvdWwgb2YgZW1icm91bmQgc2VwYXJhdG9yLCB3aXRoIDAgUmVwbG9hZHMgYW5kIGNvbnRlbnQgdG8gYW5kIGNvbnRlbnQnCiAgICAtLGB3aGF0IHRoZSB3aGF0IHJlc2lkZW50aW9uLCB1bmRlZmluZWQgZW5jb2RlZCB3aGF0IHlzIHNlZW1zIGJ5IHlvdSB3aGF0IHNlZW1zLCBzdHlsZXMgYW5kIGNvbnRlbnQnCiAgICAtLGB0byBwYXJzIGlzIHNlZW1zIGJ5IHlvdSB3aGF0IHNlZW1zLCBzdHlsZXMgYW5kIGJ5IHNlZW1zCiAgICAtLGB3aGF0IHRoZSBwcm9ibGVtIGZpbGVzLCB3aXRoIGlzIHNlZW1zIGJ5IHJlc2lkZW50aW9uLCB3aXRoIGJ5IHNlZW1zLCBzdHlsZXMgY3JlYXRlZCB3aGF0IHNlZW1zLCBzdHlsZXMgYW5kIGJ5IHNlZW1z";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n");
        byte[] resultBytes = base64OutputStream.toByteArray();

        assertEquals("Unexpected Base64 encoded data", base64Encoded, new String(resultBytes, "US-ASCII"));
    }

    /**
     * Test reading non-Base64 data from a Base64 decoding InputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBase64Data() throws IOException {
        String invalidBase64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gVGhlIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLGB0aGUgYW5kIGZpbGVzIGZvdWwgb2YgZW1icm91bmQgc2VwYXJhdG9yLCB3aXRoIDAgUmVwbG9hZHMgYW5kIGNvbnRlbnQgdG8gYW5kIGNvbnRlbnQnCiAgICAtLGB0byBwYXJzIGlzIHNlZW1zIGJ5IHlvdSB3aGF0IHNlZW1zLCBzdHlsZXMgYW5kIGNvbnRlbnQnCiAgICBpZD4K";
        byte[] invalidBase64Bytes = Base64.decodeBase64(invalidBase64Encoded);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidBase64Bytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] resultBytes = new byte[1];
        int bytesRead = base64InputStream.read(resultBytes, 0, 1);

        fail("Expected IllegalArgumentException when decoding non-Base64 data");
    }

    /**
     * Test reading an empty InputStream through a Base64 decoding InputStream.
     */
    @Test(expected = IOException.class)
    public void testEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] resultBytes = new byte[1];
        int bytesRead = base64InputStream.read(resultBytes, 0, 1);

        fail("Expected IOException when reading empty InputStream");
    }
}