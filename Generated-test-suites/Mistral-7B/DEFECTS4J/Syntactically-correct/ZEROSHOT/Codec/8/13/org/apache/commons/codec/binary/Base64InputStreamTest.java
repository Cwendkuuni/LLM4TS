package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test file for the Base64InputStream class.
 *
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in).
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStream() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertNotNull(base64InputStream);
        assertTrue(base64InputStream instanceof FilterInputStream);
        assertEquals(inputStream, base64InputStream.in);
        assertFalse(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode).
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamDoEncode() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        assertNotNull(base64InputStream);
        assertTrue(base64InputStream instanceof FilterInputStream);
        assertEquals(inputStream, base64InputStream.in);
        assertTrue(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator).
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testBase64InputStreamDoEncodeLineLengthLineSeparator() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        final Base64 base64 = new Base64(72, "\r\n".getBytes());

        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, "\r\n".getBytes());

        assertNotNull(base64InputStream);
        assertTrue(base64InputStream instanceof FilterInputStream);
        assertEquals(inputStream, base64InputStream.in);
        assertTrue(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
        assertEquals(base64, base64InputStream.base64);
    }

    /**
     * Test method read().
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testRead() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        final byte expected = (byte) 0x63; // 'c' char value
        final byte[] expectedByteArray = new byte[] { expected };

        final int result = base64InputStream.read();

        if (result > 0) {
            assertEquals(expectedByteArray[0], base64InputStream.singleByte);
        } else if (result == -1) {
            assertTrue(base64InputStream.markSupported()); // test markSupported ()
            base64InputStream.mark(0);
            assertTrue(base64InputStream.reset());
            assertEquals(inputData[0], base64InputStream.read());
        } else {
            fail("Unexpected result.");
        }
    }

    /**
     * Test method read(byte[], int, int).
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadByteArray() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        final byte[] expected = new byte[] { 0x63, 0x61, 0x6d, 0x70, 0x6f, 0x20, 0x64, 0x61, 0x74, 0x61 }; // 'cas eInputData '

        final int result = base64InputStream.read(new byte[expected.length], 0, expected.length);

        assertArrayEquals(expected, base64InputStream.singleByte, 0, result);
    }

    /**
     * Test method read(byte[], int, int) with fewer bytes read than available.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadByteArrayFewerBytes() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        final byte[] expected = new byte[] { 0x63, 0x61, 0x6d }; // 'cas e'

        final int result = base64InputStream.read(new byte[expected.length], 0, expected.length);

        assertArrayEquals(expected, base64InputStream.singleByte, 0, result);
    }

    /**
     * Test method read(byte[], int, int) with more bytes read than available.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadByteArrayMoreBytes() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        final byte[] result = new byte[5];
        final int readResult = base64InputStream.read(result, 0, 5);

        assertArrayEquals(new byte[] { 0x63, 0x61, 0x6d, 0x70, 0x6f }, result, 0, readResult);
    }

    /**
     * Test encode and read.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        final byte[] inputData = "inputData".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);

        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        final byte[] expectedEncodedData = new byte[] { 0x31, 0x8a, 0x82, 0x49, 0x1b, 0x54, 0x4f, 0x4b, 0x0a, 0x32, 0x8a,
                0x61, 0x30, 0x69, 0x2d, 0x31, 0x2e, 0x63, 0x61, 0x6d, 0x65, 0x64, 0x75, 0x64, 0x65, 0x2e, 0x64, 0x65, 0x66,
                0x61, 0x75, 0x6c, 0x2c, 0x20, 0x42, 0x65, 0x61, 0x6d, 0x20, 0x49, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x61, 0x6d, 0x79 };

        base64OutputStream.write(inputData, 0, inputData.length);
        base64OutputStream.flush();
        final byte[] encodedData = outputStream.toByteArray();

        final byte[] buffer = new byte[encodedData.length];

        final int result = base64InputStream.read(buffer, 0, encodedData.length);

        assertArrayEquals(encodedData, buffer, 0, result);
    }
}