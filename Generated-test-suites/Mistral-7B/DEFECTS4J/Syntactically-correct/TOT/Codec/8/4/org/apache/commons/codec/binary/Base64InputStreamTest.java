package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test {@link Base64InputStream#Base64InputStream(InputStream)} constructor.
     * <p>
     * Given: A {@link ByteArrayInputStream} containing encoded data.
     * <p>
     * When: Creating a new {@link Base64InputStream} instance.
     * <p>
     * Then: The decoded data should be equal to the original data.
     */
    @org.junit.Test
    public void testBase64InputStreamDecodingFromInputStream() throws IOException {
        String encodedData = "VG8gZXhpZG5faGFzaCIKCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());

        byte[] buffer = new byte[originalData.length];
        int bytesRead = new Base64InputStream(inputStream).read(buffer);

        assertEquals(originalData.length, bytesRead);
        assertArrayEquals(originalData, buffer);
    }

    /**
     * Test {@link Base64InputStream#Base64InputStream(InputStream, boolean)} constructor.
     * <p>
     * Given: A {@link ByteArrayInputStream} containing encoded data.
     * <p>
     * When: Creating a new {@link Base64InputStream} instance with encoding flag set to false.
     * <p>
     * Then: The decoded data should be equal to the original data.
     */
    @org.junit.Test
    public void testBase64InputStreamEncodingFromInputStream() throws IOException {
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(originalData);
        base64OutputStream.close();
        byte[] encodedData = outputStream.toByteArray();

        byte[] buffer = new byte[originalData.length];
        int bytesRead = new Base64InputStream(new ByteArrayInputStream(encodedData), false).read(buffer);

        assertEquals(originalData.length, bytesRead);
        assertArrayEquals(originalData, buffer);
    }

    /**
     * Test {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor.
     * <p>
     * Given: A {@link ByteArrayInputStream} containing encoded data.
     * <p>
     * When: Creating a new {@link Base64InputStream} instance with encoding flag set to false, line length set to 50,
     * and line separator as CRLF.
     * <p>
     * Then: The decoded data should be equal to the original data.
     * <p>
     * And: The lines in the decoded data should have a length of 50 or less.
     */
    @org.junit.Test
    public void testBase64InputStreamDecodingFromInputStreamWithLineLength() throws IOException {
        String encodedData = "VGhlIGZpbGVzIGRhdGFbMF0gVG8gZXhpZG5faGFzaCIKCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());

        byte[] buffer = new byte[originalData.length];
        int bytesRead = new Base64InputStream(inputStream, false, 50, new byte[] { (byte) 0x0D, (byte) 0x0A })
                .read(buffer);

        assertEquals(originalData.length, bytesRead);
        assertArrayEquals(originalData, buffer);

        String decodedData = new String(buffer, "UTF-8");
        String[] lines = decodedData.split("\r\n");
        for (String line : lines) {
            assertEquals(50, line.length());
        }
    }

    /**
     * Test {@link Base64InputStream#read()} method.
     * <p>
     * Given: A {@link Base64InputStream} instance.
     * <p>
     * When: Reading a byte from the stream.
     * <p>
     * Then: The byte read should be equal to the first byte in the input stream.
     * <p>
     * And: Reading another byte should return the second byte in the input stream.
     * <p>
     * And: Reading another byte should return the third byte in the input stream.
     * <p>
     * And: Reading another byte should return the fourth byte in the input stream.
     * <p>
     * And: Reading another byte should return the fifth byte in the input stream.
     * <p>
     * And: Reading another byte should return -1, indicating EOF has been reached.
     */
    @org.junit.Test
    public void testBase64InputStreamRead() throws IOException {
        String encodedData = "VG8gZXhpZG5faGFzaCIKCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte byte1 = base64InputStream.read();
        assertEquals(originalData[0], byte1);

        byte byte2 = base64InputStream.read();
        assertEquals(originalData[1], byte2);

        byte byte3 = base64InputStream.read();
        assertEquals(originalData[2], byte3);

        byte byte4 = base64InputStream.read();
        assertEquals(originalData[3], byte4);

        byte byte5 = base64InputStream.read();
        assertEquals(originalData[4], byte5);

        byte byte6 = base64InputStream.read();
        assertNull(byte6);
    }

    /**
     * Test {@link Base64InputStream#read(byte[], int, int)} method.
     * <p>
     * Given: A {@link Base64InputStream} instance.
     * <p>
     * When: Reading 5 bytes into a buffer.
     * <p>
     * Then: The decoded data in the buffer should be equal to the first 5 bytes in the input stream.
     */
    @org.junit.Test
    public void testBase64InputStreamReadByteArray() throws IOException {
        String encodedData = "VG8gZXhpZG5faGFzaCIKCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());

        byte[] buffer = new byte[5];
        int bytesRead = new Base64InputStream(inputStream).read(buffer);

        assertArrayEquals(new byte[] { originalData[0], originalData[1], originalData[2], originalData[3], originalData[4] }, buffer);
        assertEquals(5, bytesRead);
    }

    /**
     * Test {@link Base64InputStream#Base64InputStream(InputStream, boolean, int, byte[])} constructor with invalid line length.
     * <p>
     * Given: A {@link ByteArrayInputStream} containing encoded data.
     * <p>
     * When: Creating a new {@link Base64InputStream} instance with encoding flag set to false, line length set to -1,
     * and line separator as CRLF.
     * <p>
     * Then: An {@link IllegalArgumentException} should be thrown.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamDecodingFromInputStreamWithNegativeLineLength() throws IOException {
        String encodedData = "VG8gZXhpZG5faGFzaCIKCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] originalData = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());

        new Base64InputStream(inputStream, false, -1, new byte[] { (byte) 0x0D, (byte) 0x0A });
    }

    /**
     * Test {@link Base64InputStream#Base64InputStream(InputStream, boolean)} constructor with null input stream.
     * <p>
     * Given: A null {@link InputStream}.
     * <p>
     * When: Creating a new {@link Base64InputStream} instance.
     * <p>
     * Then: An {@link IllegalArgumentException} should be thrown.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamDecodingFromInputStreamWithNullInputStream() {
        new Base64InputStream(null, false);
    }
}