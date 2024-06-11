package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
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
 * Test cases for Base64InputStream class.
 *
 * @author John Doe
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_DefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'Base64' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedOutput = new byte[] { 'T', 'e', 's', 't', ' ', 'B', 'a', 's', 'e', '6', '4' };
        byte[] actualOutput = new byte[5];
        int bytesRead = base64InputStream.read(actualOutput, 0, 5);

        assertArrayEquals("Unexpected output from Base64InputStream", expectedOutput, actualOutput);
        assertEquals("Unexpected number of bytes read", 5, bytesRead);
    }

    /**
     * Test constructor with encoding behavior.
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_EncodingConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'Base64' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write("Test Base64".getBytes());
        base64OutputStream.flush();
        byte[] encodedOutput = outputStream.toByteArray();

        byte[] expectedInput = new byte[] { 'T', 'e', 's', 't', ' ', 'B', 'a', 's', 'e', '6', '4' };
        byte[] actualInput = new byte[12];
        int bytesRead = base64InputStream.read(actualInput, 0, 12);

        assertArrayEquals("Unexpected output from Base64InputStream", encodedOutput, actualInput);
        assertEquals("Unexpected number of bytes read", 12, bytesRead);
    }

    /**
     * Test constructor with custom line length and line separator.
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_CustomLineLengthAndSeparatorConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'Base64' });
        byte[] lineSeparator = new byte[] { 0x0D, 0x0A }; // CRLF

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, lineSeparator);

        byte[] expectedOutput = new byte[] { 'T', 'e', 's', 't', ' ' };
        byte[] actualOutput = new byte[5];
        int bytesRead = base64InputStream.read(actualOutput, 0, 5);

        assertArrayEquals("Unexpected output from Base64InputStream", expectedOutput, actualOutput);
        assertEquals("Unexpected number of bytes read", 5, bytesRead);
    }

    /**
     * Test read method with empty buffer.
     *
     * @throws IOException
     */
    @Test
    public void testRead_EmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] emptyBuffer = new byte[0];
        int bytesRead = base64InputStream.read(emptyBuffer, 0, 0);

        assertFalse("Unexpected positive number of bytes read", bytesRead > 0);
    }

    /**
     * Test read method with null buffer.
     *
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testRead_NullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] nullBuffer = null;
        int bytesRead = base64InputStream.read(nullBuffer, 0, 0);
    }

    /**
     * Test read method with buffer size larger than remaining data.
     *
     * @throws IOException
     */
    @Test
    public void testRead_BufferSizeLargerThanRemainingData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[10];

        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);

        assertArrayEquals("Unexpected output from Base64InputStream", new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' }, buffer, 0, 10);
        assertEquals("Unexpected number of bytes read", 10, bytesRead);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse("Base64InputStream should not support marking", base64InputStream.markSupported());
    }

    /**
     * Test close method.
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'T', 'e', 's', 't', 'B', 'a', 's', 'e', '6', '4' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.close();

        try {
            base64InputStream.read();
        } catch (IOException e) {
            assertNull("Unexpectedly non-null exception", e);
        }
    }
}