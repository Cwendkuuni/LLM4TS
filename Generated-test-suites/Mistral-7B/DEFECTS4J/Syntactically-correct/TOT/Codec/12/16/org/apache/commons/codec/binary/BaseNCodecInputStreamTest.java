package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.apache.commons.codec.binary.BaseNCodec.decodeBase32;
import static org.apache.commons.codec.binary.BaseNCodec.encodeBase32;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test read method with regular data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadWithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedOutputBytes = "Kw==".getBytes(StandardCharsets.UTF_8);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        baseNCodec.encode(inputBytes, 0, inputBytes.length, byteArrayOutputStream, 0, byteArrayOutputStream.size());

        final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final int actualOutputByte = baseNCodecInputStream.read();
        assertEquals(EOF, actualOutputByte);

        final byte[] actualOutputBytes = new byte[4];
        final int actualReadLength = baseNCodecInputStream.read(actualOutputBytes, 0, actualOutputBytes.length);
        assertEquals(expectedOutputBytes.length, actualReadLength);
        assertArrayEquals(expectedOutputBytes, actualOutputBytes);
    }

    /**
     * Test read method with empty input data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadWithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final int actualOutputByte = baseNCodecInputStream.read();
        assertEquals(EOF, actualOutputByte);
    }

    /**
     * Test read method with error scenario: EOF.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadWithEOF() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final byte[] actualOutputBytes = new byte[1];
        final int actualReadLength = baseNCodecInputStream.read(actualOutputBytes, 0, actualOutputBytes.length);
        assertEquals(EOF, actualReadLength);
        assertArrayEquals(new byte[0], actualOutputBytes);

        final int actualOutputByte = baseNCodecInputStream.read();
        assertEquals(EOF, actualOutputByte);
    }

    /**
     * Test markSupported method with error scenario: mark not supported.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupportedWithErrorScenario() {
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), true);
        baseNCodecInputStream.markSupported();
    }

    /**
     * Test skip method with regular data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testSkipWithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedOutputBytes = "Kw==".getBytes(StandardCharsets.UTF_8);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        baseNCodec.encode(inputBytes, 0, inputBytes.length, byteArrayOutputStream, 0, byteArrayOutputStream.size());

        final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final long expectedSkippedBytes = 512L;
        baseNCodecInputStream.skip(expectedSkippedBytes);

        final byte[] actualOutputBytes = new byte[expectedOutputBytes.length];
        final int actualReadLength = baseNCodecInputStream.read(actualOutputBytes, 0, actualOutputBytes.length);
        assertEquals(expectedOutputBytes.length, actualReadLength);
        assertArrayEquals(expectedOutputBytes, actualOutputBytes);

        final long actualSkippedBytes = baseNCodecInputStream.skip(Long.MAX_VALUE);
        assertEquals(expectedSkippedBytes, actualSkippedBytes);
    }

    /**
     * Test skip method with error scenario: negative length.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        baseNCodecInputStream.skip(-1);
    }

    /**
     * Test skip method with empty input data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testSkipWithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final long expectedSkippedBytes = 512L;
        baseNCodecInputStream.skip(expectedSkippedBytes);

        final int actualOutputByte = baseNCodecInputStream.read();
        assertEquals(EOF, actualOutputByte);
    }

    /**
     * Test the readResults method with error scenario: input data with non-base32 characters.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadResultsWithErrorScenario() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567#".getBytes(StandardCharsets.UTF_8);
        final InputStream inputStream = new ByteArrayInputStream(inputBytes);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final byte[] outputBytes = new byte[10];
        final int actualReadLength = baseNCodecInputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(0, actualReadLength);

        final byte[] actualOutputBytes = new byte[outputBytes.length];
        final int actualReadLength2 = baseNCodecInputStream.read(actualOutputBytes, 0, actualOutputBytes.length);
        assertArrayEquals(new byte[0], actualOutputBytes);
        assertFalse(baseNCodecInputStream.hasData());
    }

    /**
     * Test the readResults method with regular base32 data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadResultsWithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final InputStream inputStream = new ByteArrayInputStream(inputBytes);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final byte[] expectedOutputBytes = "Kw==".getBytes(StandardCharsets.UTF_8);
        final byte[] outputBytes = new byte[expectedOutputBytes.length];
        final int actualReadLength = baseNCodecInputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(expectedOutputBytes.length, actualReadLength);
        assertArrayEquals(expectedOutputBytes, outputBytes);

        final int actualReadLength2 = baseNCodecInputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(0, actualReadLength2);
        assertFalse(baseNCodecInputStream.hasData());
    }

    /**
     * Test the readResults method with empty input data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadResultsWithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final byte[] outputBytes = new byte[10];
        final int actualReadLength = baseNCodecInputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(0, actualReadLength);

        final byte[] actualOutputBytes = new byte[outputBytes.length];
        final int actualReadLength2 = baseNCodecInputStream.read(actualOutputBytes, 0, actualOutputBytes.length);
        assertArrayEquals(new byte[0], actualOutputBytes);
        assertFalse(baseNCodecInputStream.hasData());
    }

    /**
     * Test the hasData method with regular data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testHasDataWithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final InputStream inputStream = new ByteArrayInputStream(inputBytes);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final boolean actualHasData = baseNCodecInputStream.hasData();
        assertTrue(actualHasData);
    }

    /**
     * Test the hasData method with empty input data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testHasDataWithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        final boolean actualHasData = baseNCodecInputStream.hasData();
        assertFalse(actualHasData);
    }

    /**
     * Test the decodeBase32 method with regular base32 data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testDecodeBase32WithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final String inputBase32 = "Kw==";
        final byte[] expectedOutputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final byte[] outputBytes = new byte[expectedOutputBytes.length];

        final int actualReadLength = baseNCodec.decode(inputBase32, 0, inputBase32.length(), outputBytes, 0, outputBytes.length);
        assertEquals(expectedOutputBytes.length, actualReadLength);
        assertArrayEquals(expectedOutputBytes, outputBytes);
    }

    /**
     * Test the decodeBase32 method with empty base32 data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testDecodeBase32WithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final String inputBase32 = "";
        final byte[] outputBytes = new byte[10];

        final int actualReadLength = baseNCodec.decode(inputBase32, 0, inputBase32.length(), outputBytes, 0, outputBytes.length);
        assertEquals(0, actualReadLength);
        assertArrayEquals(new byte[0], outputBytes);
    }

    /**
     * Test the encodeBase32 method with regular data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeBase32WithRegularData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = "ABCDEFGHIGJKLMNOPQRSTUVWXYZ234567".getBytes(StandardCharsets.UTF_8);
        final String expectedOutputBase32 = "Kw==";
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final int actualEncodedLength = baseNCodec.encode(inputBytes, 0, inputBytes.length, byteArrayOutputStream, 0, byteArrayOutputStream.size());
        assertEquals(expectedOutputBase32.length(), actualEncodedLength);
        assertEquals(expectedOutputBase32, new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    /**
     * Test the encodeBase32 method with empty input data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeBase32WithEmptyData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final byte[] inputBytes = new byte[0];
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final int actualEncodedLength = baseNCodec.encode(inputBytes, 0, inputBytes.length, byteArrayOutputStream, 0, byteArrayOutputStream.size());
        assertEquals(0, actualEncodedLength);
        assertNull(byteArrayOutputStream.toString(StandardCharsets.UTF_8));
    }
}