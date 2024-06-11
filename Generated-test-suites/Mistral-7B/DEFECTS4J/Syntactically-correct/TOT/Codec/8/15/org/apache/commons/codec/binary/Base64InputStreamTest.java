package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test class for Base64InputStream and Base64OutputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor without encoding
     */
    @org.junit.Test
    public void testBase64InputStreamDecoder() throws IOException {
        // Given
        String testData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // When decoding
        int readBytes;
        byte[] buffer = new byte[1024];
        while ((readBytes = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, readBytes);
        }
        base64OutputStream.finish();

        // Then result should be equal to original test data
        assertEquals(testData, outputStream.toString());

        // Clean up
        base64InputStream.close();
        base64OutputStream.close();
        outputStream.close();
    }

    /**
     * Test constructor with encoding
     */
    @org.junit.Test
    public void testBase64InputStreamEncoder() throws IOException {
        // Given
        String testData = "Hello, World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);

        // When encoding
        int readBytes;
        byte[] buffer = new byte[1024];
        while ((readBytes = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, readBytes);
        }
        base64OutputStream.finish();

        // Then result should be equal to the encoded data
        assertEquals("SGVsbG8gV29ybGQh", outputStream.toString());

        // Clean up
        base64InputStream.close();
        base64OutputStream.close();
        outputStream.close();

        // Given
        ByteArrayInputStream decodedInputStream = new ByteArrayInputStream(Base64.decodeBase64("SGVsbG8gV29ybGQh".getBytes()));
        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();
        Base64OutputStream decodedBase64OutputStream = new Base64OutputStream(decodedOutputStream);

        // When decoding
        int decodedReadBytes;
        byte[] decodedBuffer = new byte[1024];
        while ((decodedReadBytes = decodedBase64OutputStream.read(decodedBuffer, 0, decodedBuffer.length)) != -1) {
            decodedBase64OutputStream.write(decodedBuffer, 0, decodedReadBytes);
        }
        decodedBase64OutputStream.finish();

        // Then result should be equal to original test data
        assertEquals(testData, new String(decodedOutputStream.toByteArray()));

        // Clean up
        decodedBase64OutputStream.close();
        decodedOutputStream.close();
        decodedInputStream.close();
    }

    /**
     * Test constructor with error handling
     */
    @org.junit.Test(expected = IOException.class)
    public void testBase64InputStreamErrorHandling() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // When reading
        int readBytes;
        byte[] buffer = new byte[1024];
        while ((readBytes = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            // Do nothing
        }

        // Then an IOException should be thrown due to end of stream
        base64InputStream.read(buffer, 0, buffer.length);
    }
}