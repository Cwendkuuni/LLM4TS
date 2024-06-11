package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading a single byte from an empty input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadSingleByteFromEmptyInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);
        assertEquals(-1, inputStream.read());
        assertFalse(inputStream.hasNext());
    }

    /**
     * Test reading a single byte from an input stream with data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadSingleByteFromInputStreamWithData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(1, inputStreamWrapper.read());
        assertEquals(1, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test reading multiple bytes from an empty input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadMultipleBytesFromEmptyInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);
        final byte[] buffer = new byte[10];
        assertEquals(0, inputStream.read(buffer, 0, buffer.length));
        assertNull(inputStream.read());
        assertFalse(inputStream.hasNext());
    }

    /**
     * Test reading multiple bytes from an input stream with data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadMultipleBytesFromInputStreamWithData() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        final byte[] buffer = new byte[3];
        assertEquals(2, inputStreamWrapper.read(buffer, 0, buffer.length));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test marking and resetting an input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkAndResetInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWrapper.mark(1);
        inputStreamWrapper.reset();
    }

    /**
     * Test skipping data from an input stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testSkipDataFromInputStream() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWrapper.skip(2);
        assertEquals(3, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test the behavior when attempting to read from an empty input stream after {@link BaseNCodec#decode(byte[], int, int)} or
     * {@link BaseNCodec#encode(byte[], int, int)} have been called on it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadFromEmptyInputStreamAfterDecodeOrEncode() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodec.decode(new byte[0], 0, 0);
        assertEquals(-1, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());

        inputStreamWrapper.reset();
        baseNCodec.encode(new byte[0], 0, 0);
        assertEquals(-1, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test reading data from an input stream after {@link BaseNCodec#decode(byte[], int, int)} or
     * {@link BaseNCodec#encode(byte[], int, int)} have been called on it.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadDataFromInputStreamAfterDecodeOrEncode() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(2);
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodec.decode(new byte[0], 0, 0);
        final byte[] buffer = new byte[2];
        assertEquals(2, inputStreamWrapper.read(buffer, 0, buffer.length));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);

        inputStreamWrapper.reset();
        baseNCodec.encode(new byte[0], 0, 0);
        final byte[] buffer2 = new byte[2];
        assertEquals(2, inputStreamWrapper.read(buffer2, 0, buffer2.length));
        assertEquals(1, buffer2[0]);
        assertEquals(2, buffer2[1]);
    }

    /**
     * Test reading from an input stream with invalid input.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadFromInputStreamWithInvalidInput() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        outputStream.write(256);
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(2, inputStreamWrapper.read());
        assertEquals(1, inputStreamWrapper.read());
        assertEquals(1, inputStreamWrapper.read()); // returns EOF
        assertEquals(-1, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test reading from an input stream with EOF reached.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadFromInputStreamWithEOF() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(0, inputStreamWrapper.read());
        assertEquals(-1, inputStreamWrapper.read());
        assertFalse(inputStreamWrapper.hasNext());
    }

    /**
     * Test the {@link BaseNCodecInputStream#read()} method with a negative length.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWrapper.read(-1);
    }

    /**
     * Test the {@link BaseNCodecInputStream#read(byte[], int, int)} method with a negative length.
     *
     * @throws IOException if an I/O error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLengthArray() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWrapper.read(new byte[0], -1, 0);
    }
}