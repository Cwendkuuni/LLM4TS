package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Tests for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding Base64 encoded data from InputStream.
     */
    @Test
    public void testDecodeBase64InputStream() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] decodedData = new byte[] { -61, -48, -61, -48, -67, -65, -66, -66, -66, -63, -61, -66, -66, -65, -62, -65, -68,
                -68, -67, -68, -61, -70, -70, -65, -62, -65, -63, -75, -61, -67, -65, -74, -65, -64, -65, -64 };

        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        Base64InputStream base64Input = new Base64InputStream(inputStream);
        byte[] readData = new byte[decodedData.length];
        int len = base64Input.read(readData, 0, decodedData.length);
        assertArrayEquals(decodedData, readData);
        base64Input.close();
        inputStream.close();
    }

    /**
     * Test encoding Base64 data from InputStream.
     */
    @Test
    public void testEncodeBase64InputStream() throws IOException {
        byte[] dataToEncode = new byte[] { -61, -48, -61, -48 };
        String base64EncodedData = "VG8gZXJyIHdpdGggWSBQYXVsIFggWzFdICsgVG8gZW5jb2RlZCBmaWxlIGlzIGlzIHVwIHlvdSBpcyBhbmQgZm9yIGlzIGxpbmsuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToEncode);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream);
        Base64InputStream base64Input = new Base64InputStream(inputStream, true);
        int len = base64Input.read(base64Output.buffer, 0, base64Output.buffer.length);
        base64Input.close();
        inputStream.close();
        byte[] readData = outputStream.toByteArray();
        assertEquals(base64EncodedData, new String(readData));
    }

    /**
     * Test decoding an empty InputStream.
     */
    @Test
    public void testDecodeEmptyBase64InputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(inputStream);
        assertEquals(-1, base64Input.read());
        base64Input.close();
        inputStream.close();
    }

    /**
     * Test decoding Base64 encoded data with errors.
     */
    @Test(expected = IOException.class)
    public void testDecodeBase64InputStreamWithErrors() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g==\r\n";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        Base64InputStream base64Input = new Base64InputStream(inputStream);
        byte[] readData = new byte[10];
        int len = base64Input.read(readData, 0, readData.length);
        base64Input.close();
        inputStream.close();
    }
}