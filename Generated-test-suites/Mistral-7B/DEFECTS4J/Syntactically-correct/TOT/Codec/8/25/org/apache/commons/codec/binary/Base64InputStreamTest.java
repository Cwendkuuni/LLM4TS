package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test case for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Tests the default behavior of {@link Base64InputStream} which is decoding.
     */
    @Test
    public void testDefaultEncodingDecode() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggc29tZSBmYWxzIGlzIGRvY3VtZW50Lg==";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read;
        byte[] buffer = new byte[10];
        while ((read = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        assertEquals("ABCDEFGHiJKLmnopqrstuvwxyzABCDEFGHiJKLmnopqrstuvwxyzABCDEFGHiJKLmnopqrstuvwxyzABCDEFGHiJKLmnopqrstuvwxyzABCDEFGHiJKLmnopqrstuvwxyzABCDEFGHiJKLmnopqrstuvwxyz", outputStream.toByteArray());
        outputStream.close();
        base64InputStream.close();
    }

    /**
     * Tests the custom encoding behavior of {@link Base64InputStream} which is encoding.
     */
    @Test
    public void testCustomEncoding() throws IOException {
        String plainTextData = "ABCDEFGHiJKLmnopqrstuvwxyz";
        byte[] inputData = plainTextData.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        Base64 base64 = new Base64(76, new byte[] { 13, 10 }); // CRLF line endings
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, base64.getLineSeparator());
        int read;
        byte[] buffer = new byte[10];
        while ((read = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        assertEquals(base64EncodedData, outputStream.toByteArray());
        outputStream.close();
        base64InputStream.close();
    }

    /**
     * Tests reading an empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = base64InputStream.read();
        assertEquals(-1, read);
        base64InputStream.close();
    }

    /**
     * Tests reading from a {@link InputStream} which does not provide valid base64-encoded data.
     */
    @Test(expected = IOException.class)
    public void testInvalidBase64Data() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("InvalidBase64Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int read = base64InputStream.read();
    }

}