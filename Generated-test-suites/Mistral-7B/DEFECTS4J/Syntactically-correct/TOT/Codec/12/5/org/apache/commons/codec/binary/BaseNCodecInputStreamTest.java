package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x34, 0x34, 0x34 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int read = baseNCodecInputStream.read(result, 0, result.length);
        assertEquals(read, result.length);
        assertTrue(read == result.length && baseNCodec.isBaseN(result, 0, result.length));
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with EOF.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamEof() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x34, 0x34, 0x34, EOF });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int read = baseNCodecInputStream.read(result, 0, result.length);
        assertEquals(read, 3);
        assertTrue(read == result.length && baseNCodec.isBaseN(result, 0, result.length));

        read = baseNCodecInputStream.read(result, 0, result.length);
        assertEquals(read, EOF);
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with encoding.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamEncoding() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x34, 0x34, 0x34, 0x34, 0x34, 0x34, 0x34, 0x34 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[1];
        do {
            read = baseNCodecInputStream.read(buffer, 0, buffer.length);
            outputStream.write(buffer, 0, read);
        } while (read > 0);

        byte[] expected = new byte[] { 0x3, (byte) 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a };
        byte[] result = outputStream.toByteArray();
        assertTrue(result.length == expected.length);
        assertFalse(result.length > expected.length);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with decoding and EOF.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamDecodingEof() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3, (byte) 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, EOF });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[5];
        int read = baseNCodecInputStream.read(result, 0, result.length);
        assertEquals(read, 5);
        assertTrue(read == result.length && baseNCodec.isBaseN(result, 0, result.length));

        read = baseNCodecInputStream.read(result, 0, result.length);
        assertEquals(read, EOF);
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with decoding.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamDecoding() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3, (byte) 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[13];
        int read;
        byte[] buffer = new byte[1];
        do {
            read = baseNCodecInputStream.read(buffer, 0, buffer.length);
            result[baseNCodec.decodeLength(buffer[0])] = buffer[0];
        } while (read > 0);

        assertEquals(new String(result), "abcdefghijklmnopqrstuvwxyz");
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()}.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testBaseNCodecInputStreamMarkSupportedNegativeSkipLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3, (byte) 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.mark(12345); // should throw exception
    }

    /**
     * Test for {@link BaseNCodecInputStream#reset()}.
     * @throws IOException if an I/O error occurs
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamReset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x3, (byte) 0x1a, 0x1a, 0x1a, 0x1a, 0x1a, 0x1a });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[1];
        int read;
        do {
            read = baseNCodecInputStream.read(buffer, 0, buffer.length);
            if (read == EOF) {
                break;
            }
            assertEquals(buffer[0], baseNCodec.decode(buffer[0]));
        } while (true);

        baseNCodecInputStream.reset();

        do {
            read = baseNCodecInputStream.read(buffer, 0, buffer.length);
            if (read == EOF) {
                break;
            }
            assertEquals(buffer[0], baseNCodec.decode(buffer[0]));
        } while (true);
    }
}