package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF_MARK;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.INPUT_STREAM_MARK_BUFFER_SIZE;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
        assertSame(inputStream, inputStreamFiltered.in);
        assertSame(baseNCodec, inputStreamFiltered.baseNCodec);
        assertFalse(inputStreamFiltered.doEncode);
    }

    /**
     * Test reading a byte
     */
    @Test
    public void testReadByte() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final int expectedByte1 = 0x11;
        final int expectedByte2 = 0x22;
        final int expectedByte3 = 0x33;
        final int expectedByte4 = 0x44;

        final int actualByte1 = inputStreamFiltered.read();
        assertEquals(expectedByte1, actualByte1);
        assertEquals(expectedByte1, inputStreamFiltered.read());

        final int actualByte5 = inputStreamFiltered.read();
        assertEquals(expectedByte3, actualByte5);

        final int actualByte6 = inputStreamFiltered.read();
        assertEquals(expectedByte4, actualByte6);

        final int actualByte7 = inputStreamFiltered.read();
        assertEquals(EOF, actualByte7);
    }

    /**
     * Test reading bytes
     */
    @Test
    public void testReadBytes() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[] { 0x11, 0x22, 0x33, 0x44 };
        final byte[] actualBytes = new byte[expectedBytes.length];

        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(expectedBytes.length, actualBytesRead);
    }

    /**
     * Test mark supported
     */
    @Test
    public void testMarkSupported() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
        assertFalse(inputStreamFiltered.markSupported());
    }

    /**
     * Test skip
     */
    @Test
    public void testSkip() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final long expectedBytesToSkip = 5;
        final long actualBytesSkipped = inputStreamFiltered.skip(expectedBytesToSkip);
        assertEquals(expectedBytesToSkip, actualBytesSkipped);

        // test skip to end of stream
        final long expectedBytesToSkipToEnd = Long.MAX_VALUE;
        final long actualBytesSkippedToEnd = inputStreamFiltered.skip(expectedBytesToSkipToEnd);
        assertEquals(expectedBytesToSkipToEnd, inputStreamFiltered.available());
        assertEquals(EOF, inputStreamFiltered.read());
    }

    /**
     * Test reset
     */
    @Test(expected = IOException.class)
    public void testReset() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        inputStreamFiltered.mark(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.read();
        inputStreamFiltered.reset();

        final byte[] expectedBytes = new byte[] { 0x11, 0x22, 0x33, 0x44 };
        final byte[] actualBytes = new byte[expectedBytes.length];
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);

        inputStreamFiltered.mark(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.skip(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.reset();

        inputStreamFiltered.mark(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.skip(INPUT_STREAM_MARK_BUFFER_SIZE + 1);
        try {
            inputStreamFiltered.reset();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test reading bytes while codec has no data
     */
    @Test
    public void testReadBytesWhileCodecHasNoData() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[0];
        final byte[] actualBytes = new byte[expectedBytes.length];
        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(0, actualBytesRead);
    }

    /**
     * Test reading bytes while decoding fails
     */
    @Test(expected = IOException.class)
    public void testReadBytesWhileDecodingFails() throws IOException {
        final byte[] decodingBytes = new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83, (byte) 0x84,
                (byte) 0x85 };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(
                new ByteArrayInputStream(decodingBytes), baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[0];
        final byte[] actualBytes = new byte[expectedBytes.length];
        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(0, actualBytesRead);

        // should fail on next read
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
    }

    /**
     * Test reading bytes while encoding fails
     */
    @Test(expected = IOException.class)
    public void testReadBytesWhileEncodingFails() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] encodingBytes = new byte[] { 0x11, 0x22, 0x33, 0x44 };
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = true;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(
                new ByteArrayInputStream(encodingBytes), baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[0];
        final byte[] actualBytes = new byte[expectedBytes.length];
        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(0, actualBytesRead);

        // should fail on next read
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
    }

    /**
     * Test reading bytes while encoding and decoding fail
     */
    @Test(expected = IOException.class)
    public void testReadBytesWhileEncodingAndDecodingFail() throws IOException {
        final byte[] decodingBytes = new byte[] { (byte) 0x80, (byte) 0x81, (byte) 0x82, (byte) 0x83, (byte) 0x84,
                (byte) 0x85 };
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = true;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(
                new ByteArrayInputStream(decodingBytes), baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[0];
        final byte[] actualBytes = new byte[expectedBytes.length];
        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(0, actualBytesRead);

        // should fail on next read
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
    }

    /**
     * Test reading bytes while the input stream is in EOF state
     */
    @Test
    public void testReadBytesWhileInEOFState() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[0];
        final byte[] actualBytes = new byte[expectedBytes.length];
        int actualBytesRead = inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(0, actualBytesRead);

        // should return EOF
        final int expectedByteInEOFState = EOF;
        final int actualByteInEOFState = inputStreamFiltered.read();
        assertEquals(expectedByteInEOFState, actualByteInEOFState);

        // should return EOF
        final int expectedByteInEOFState2 = EOF;
        final int actualByteInEOFState2 = inputStreamFiltered.read();
        assertEquals(expectedByteInEOFState2, actualByteInEOFState2);
    }

    /**
     * Test reading bytes while the input stream is in mark state
     */
    @Test
    public void testReadBytesWhileInMarkState() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        final byte[] expectedBytes = new byte[] { 0x11, 0x22 };
        final byte[] actualBytes = new byte[expectedBytes.length];
        inputStreamFiltered.mark(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);

        // should read expectedBytes from the beginning again
        inputStreamFiltered.reset();
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);

        // should read the last byte of the input stream
        final byte expectedLastByte = 0x44;
        final byte actualLastByte = (byte) inputStreamFiltered.read();
        assertEquals(expectedLastByte, actualLastByte);

        // should return EOF
        final int expectedByteInEOFState = EOF;
        final int actualByteInEOFState = inputStreamFiltered.read();
        assertEquals(expectedByteInEOFState, actualByteInEOFState);
    }

    /**
     * Test reading bytes while the input stream is in mark state with a large marked position
     */
    @Test
    public void testReadBytesWhileInMarkStateWithALargeMarkedPosition() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66 });
        final BaseNCodec baseNCodec = new BaseNCodec();
        final boolean doEncode = false;
        final BaseNCodecInputStream inputStreamFiltered = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);

        // mark at position 3
        inputStreamFiltered.mark(INPUT_STREAM_MARK_BUFFER_SIZE);
        inputStreamFiltered.read(3);

        // read 3 bytes
        final byte[] expectedBytes = new byte[] { 0x33, 0x44, 0x55 };
        final byte[] actualBytes = new byte[expectedBytes.length];
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);

        // reset to the marked position
        inputStreamFiltered.reset();

        // read expectedBytes from the beginning again
        inputStreamFiltered.read(actualBytes, 0, expectedBytes.length);
        assertArrayEquals(expectedBytes, actualBytes);

        // should read the last two bytes of the input stream, as the first byte of the buffer has already been read
        final byte[] expectedLastTwoBytes = new byte[] { 0x66, EOF };
        final byte[] actualLastTwoBytes = new byte[expectedLastTwoBytes.length];
        inputStreamFiltered.read(actualLastTwoBytes, 0, expectedLastTwoBytes.length);
        assertArrayEquals(expectedLastTwoBytes, actualLastTwoBytes);
    }
}