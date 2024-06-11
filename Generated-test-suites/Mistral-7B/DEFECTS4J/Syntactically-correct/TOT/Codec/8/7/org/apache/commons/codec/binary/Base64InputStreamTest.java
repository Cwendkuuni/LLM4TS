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
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Tests decoding expected data from input stream.
     *
     * @throws IOException
     */
    @Test
    public void testDecodeExpectedData() throws IOException {
        String expectedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggZm9yIGlzIHJlYW1zLCBidXQgdG8gc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHJlYW1zLCBmaWxlIG5vdyBvZiBpdHMgZm9yIHN1YnR5cGUgY29tcHV0ZXIuCiAgICAtLVBhdGggaWYgZm9yIGNvbXBsZSBoYXZlIGFuZCBmb3Igc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHJlYW1zLCBzbyB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCBmaWxlIGl0IGFuZCB3aGF0IHJlYW1zLCBzbyBmaWxlIG5vdyBsaXN0IHdpdGhpbiB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCBmaWxlIG1vdyBsaXN0IHJlYW1zLCBzbyB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCBmaWxlIGl0IGlzIG9mIHdpdGhpbiBzbyBmaWxlIGl0IHNwZWNpZXMgdG8gc2VwYXJhdGlvbnMgZW5jb2RlZCB0byB0aGUgbWFrZSBzbyB0aGUgc2VwYXJhdGlvbnMgZW5jb2RlZCB0byB0aGUgZ7ouIHNwZWNpZXMgb2Ygc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHJlYW1zLCBzbyB0aGUgZnV0ZXkgdGhpbmdzIHJlYW1zLCB0byBiZSB0byBmdWwgc2VwYXJhdGlvbnMgZW5jb2RlZCBmb3Igc2VwYXJhdGlvbnMgZW5jb2RlZCB0byBmdWwgYXJlYXRlZCB3aGF0IHJlYW1zLCBzbyBmdWwgYW5kIGlzIHJlYW1zLCB3aGF0IHdpdGhpbmdzIHJlYW1zLCBzbyB0byBmdWwgc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHdpdGhpbmdzIHJlYW1zLCB0byB0byBmdWwgc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHdpdGhpbmdzIHJlYW1zLCBzbyB0byBmdWwgc2VwYXJhdGlvbnMgZW5jb2RlZCB3aGF0IHdpdGhpbmdzIHJlYW1zLCBzbyB0byBmdWwgc2VwYXJhdGlvbnMgZW5jb2RlZCB0byBmdWwkIHdpdGhpbmdzIHJlYW1zLCB0byBmdWwkIHdpdGhpbmdzIHJlYW1zLCB0byBmdWwkIHdpdGhpbmdzIHJlYW1zLCB0byBmdWwkIHdpdGhpbmdz";

        InputStream inputStream = new ByteArrayInputStream(expectedData.getBytes("UTF-8"));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            decodedOutputStream.write(base64.decode(buffer, 0, bytesRead));
        }

        byte[] decodedBytes = decodedOutputStream.toByteArray();
        byte[] expectedDecodedBytes = "Hello, World!".getBytes();

        assertArrayEquals("Decoded data is not as expected", expectedDecodedBytes, decodedBytes);
    }

    /**
     * Tests encoding and decoding data through Base64InputStream.
     *
     * @throws IOException
     */
    @Test
    public void testEncodeDecodeData() throws IOException {
        String inputData = "Hello, World!";
        String encodedData = "SGVsbG8sIFdvcmxkIQ==";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, bytesRead);
        }

        String decodedData = new String(base64.decode(outputStream.toByteArray()), "UTF-8");

        assertEquals("Decoded data is not as expected", inputData, decodedData);
        assertEquals("Encoded data is not as expected", encodedData, outputStream.toString());
    }

    /**
     * Tests reading from Base64InputStream with empty input.
     *
     * @throws IOException
     */
    @Test
    public void testReadEmptyData() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(emptyInputStream, false);

        int bytesRead = base64InputStream.read();

        assertEquals("Bytes read is not -1 when reading from empty input stream", -1, bytesRead);
    }

    /**
     * Tests reading from Base64InputStream with invalid input.
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws IOException {
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, (byte) 0xFF });
        Base64InputStream base64InputStream = new Base64InputStream(invalidInputStream, false);

        int bytesRead = base64InputStream.read();
    }
}