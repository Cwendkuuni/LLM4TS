package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import org.junit.Test;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding with default settings.
     */
    @Test
    public void testDecodeDefault() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Data.getBytes(StandardCharsets.US_ASCII));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[inputStream.available()];
        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        assertEquals(new String(outputStream.toByteArray(), StandardCharsets.US_ASCII), "apachedemo");
        assertNull(base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test decoding with custom line length and line separator.
     */
    @Test
    public void testDecodeCustom() throws IOException {
        String base64Data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n"
                + "VGhlIGRhdGEpCiAgICAtLSBQYXVsIFRydW1pbiBvdywK";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64Data.getBytes(StandardCharsets.US_ASCII));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, new byte[] { '\r', '\n' });
        byte[] buffer = new byte[inputStream.available()];
        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        assertEquals(new String(outputStream.toByteArray(), StandardCharsets.US_ASCII), "teststring\r\n");
        assertNull(base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test encoding with default settings.
     */
    @Test
    public void testEncodeDefault() throws IOException {
        byte[] data = "apachedemo".getBytes(StandardCharsets.US_ASCII);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true);
        byte[] buffer = new byte[data.length];
        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        assertEquals(new String(outputStream.toByteArray(), StandardCharsets.US_ASCII), "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=");
        assertNull(base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test encoding with custom line length and line separator.
     */
    @Test
    public void testEncodeCustom() throws IOException {
        byte[] data = "teststring\r\n".getBytes(StandardCharsets.US_ASCII);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 50, new byte[] { '\r', '\n' });
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true, 50, new byte[] { '\r', '\n' });
        byte[] buffer = new byte[data.length];
        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        assertEquals(new String(outputStream.toByteArray(), StandardCharsets.US_ASCII), "VGhlIGRhdGEpCiAgICAtLSBQYXVsIFRydW1pbiBvdywK\r\n"
                + "VG8gc2VwYXJhdG9yIGl0IGFuZCBpcyBhbmQgc2VwYXJhdG9yLCB0byBzdGFuZCBjb250ZW50LCB3aGF0IHRoZSBwcm9ibGVtIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFRydW1pbiBvdyw=");
        assertNull(base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test reading from empty input stream.
     */
    @Test
    public void testEmptyStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        assertEquals(-1, base64InputStream.read(buffer, 0, buffer.length));
        assertNull(base64InputStream.read(buffer, 0, buffer.length));
    }

    /**
     * Test reading from closed input stream.
     */
    @Test(expected = IOException.class)
    public void testClosedStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        inputStream.close();
        base64InputStream.read(new byte[1], 0, 1);
    }

    /**
     * Test reading from input stream with invalid data.
     */
    @Test(expected = IOException.class)
    public void testInvalidData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[1], 0, 1);
    }
}