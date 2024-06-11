package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test case for Base64InputStream constructor with default decoding behavior.
     */
    @Test
    public void testDefaultDecodingBehavior() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes(StandardCharsets.UTF_8));
        InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedByteArray = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] byteArray = new byte[11];
        int bytesRead = base64InputStream.read(byteArray, 0, byteArray.length);

        assertEquals(bytesRead, 11);
        assertArrayEquals(expectedByteArray, byteArray);
    }

    /**
     * Test case for Base64InputStream constructor with custom encoding behavior.
     */
    @Test
    public void testCustomEncodingBehavior() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes(StandardCharsets.UTF_8));
        InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedByteArray = new byte[] { 3, 5, 7, 9, 11, 13, 15 };
        byte[] byteArray = new byte[7];

        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        int bytesWritten = -1;

        while (bytesRead > -1) {
            bytesRead = base64InputStream.read(byteArray, 0, byteArray.length);
            base64OutputStream.write(byteArray, 0, bytesRead);
            if (bytesRead > 0) {
                bytesWritten += bytesRead;
            }
        }

        base64OutputStream.finish();
        byte[] actualByteArray = outputStream.toByteArray();

        assertArrayEquals(expectedByteArray, actualByteArray);
        assertEquals(bytesWritten, expectedByteArray.length);
    }

    /**
     * Test case for Base64InputStream with empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] byteArray = new byte[1];
        int bytesRead = base64InputStream.read(byteArray, 0, byteArray.length);

        assertEquals(bytesRead, -1);
    }

    /**
     * Test case for Base64InputStream with invalid base64 data.
     */
    @Test(expected = IOException.class)
    public void testInvalidBase64Data() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g==InvalidBase64Data!";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes(StandardCharsets.UTF_8));
        InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] byteArray = new byte[1];
        int bytesRead = base64InputStream.read(byteArray, 0, byteArray.length);
    }
}