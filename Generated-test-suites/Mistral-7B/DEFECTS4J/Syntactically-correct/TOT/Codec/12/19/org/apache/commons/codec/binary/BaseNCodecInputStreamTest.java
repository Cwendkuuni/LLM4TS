package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test cases for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#read()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testRead() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 'A' }), baseNCodec, false);
        final int expectedByte = 'A' & 0xFF;
        final int actualByte = input.read();

        assertTrue(input.markSupported()); // markSupported should return true for subclasses that override it
        assertFalse(input.markSupported()); // markSupported should return false for BaseNCodecInputStream

        assertEquals(expectedByte, actualByte);
        assertEquals(expectedByte, input.read());
        assertEquals(EOF, input.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadByteArray() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] expected = { 'A', 'B' };
        final byte[] result = new byte[2];
        final InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 'A', 'B' }), baseNCodec, false);

        inputStream.read(result, 0, 2);
        assertEquals(expected, result);

        outputStream.write(expected);
        final InputStream inputStreamFromEncodedStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, true);

        inputStreamFromEncodedStream.read(result, 0, 2);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()} method.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupportedThrowsException() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), baseNCodec, false);
        inputStream.mark(0);
    }

    /**
     * Test for {@link BaseNCodecInputStream#skip(long)} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] input = { 'A', 'B', 'C' };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final InputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInput.skip(1);
        final int bytesRead = testInput.read(new byte[2], 0, 2);
        final byte[] expected = { 'B' };
        final byte[] result = new byte[2];

        assertEquals(1, bytesRead);
        assertEquals(expected, result);

        outputStream.write(input);
        final InputStream encodedInput = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, true);
        encodedInput.skip(1);
        bytesRead = encodedInput.read(new byte[2], 0, 2);
        assertEquals(1, bytesRead);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link BaseNCodecInputStream#available()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testAvailable() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] input = { 'A', 'B', 'C' };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final InputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(2, testInput.available());

        outputStream.write(input);
        final InputStream encodedInput = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, true);

        assertEquals(3, encodedInput.available());
    }

    /**
     * Test for {@link BaseNCodecInputStream#close()} method.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final BaseNCodec baseNCodec = new Base32();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        final BaseNCodecInputStream testInput = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInput.close();
        assertNull(testInput.in);
    }
}