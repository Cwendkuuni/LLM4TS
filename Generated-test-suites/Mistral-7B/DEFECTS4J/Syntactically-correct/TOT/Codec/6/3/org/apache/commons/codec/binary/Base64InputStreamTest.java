package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecodingBehavior() throws IOException {
        // Given
        String inputData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzcGFjZSB0aGUtc2VjYXJpdHk=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // Then
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, len);
        }
        base64OutputStream.close();

        // Check the decoded data is equal to the original data
        assertEquals(inputData, outputStream.toString());

        // Clean up
        base64InputStream.close();
        outputStream.close();
    }

    /**
     * Test constructor with custom encoding behavior.
     */
    @Test
    public void testCustomEncodingBehavior() throws IOException {
        // Given
        String inputData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzcGFjZSB0aGUtc2VjYXJpdHk=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes());
        Base64 base64 = new Base64(true);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, base64);

        // When
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, base64);

        // Then
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, len);
        }
        base64OutputStream.close();

        // Check the encoded data is equal to the original data
        assertEquals(inputData, outputStream.toString());

        // Clean up
        base64InputStream.close();
        outputStream.close();
    }

    /**
     * Test reading from an empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        byte[] buffer = new byte[1024];
        int len;

        // Then
        assertEquals(-1, base64InputStream.read(buffer, 0, buffer.length));

        // Clean up
        base64InputStream.close();
    }

    /**
     * Test reading from an input stream with invalid data.
     */
    @Test(expected = IOException.class)
    public void testInvalidInputData() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream("InvalidData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When & Then
        byte[] buffer = new byte[1024];
        int len = base64InputStream.read(buffer, 0, buffer.length);

        // The test should throw an IOException due to invalid data
        fail();

        // Clean up
        base64InputStream.close();
    }
}