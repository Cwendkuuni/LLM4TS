package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    /**
     * Sets up a {@link BaseNCodecInputStream} for testing.
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        baseNCodec = new BaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#read()} method.
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        int read = -1;
        int expected = 0x01;
        byte[] singleByte = new byte[1];

        while (read != expected) {
            read = baseNCodecInputStream.read();
            if (read != expected) {
                assertSame(singleByte, baseNCodecInputStream.singleByte);
            }
        }

        assertEquals(expected, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Tests the {@link BaseNCodecInputStream#read(byte[], int, int)} method.
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] buffer = new byte[3];
        int len = baseNCodecInputStream.read(buffer, 0, buffer.length);

        assertEquals(expected.length, len);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#read(byte[], int, int)} method with less bytes available.
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayWithLessBytesAvailable() throws IOException {
        byte[] expected = new byte[] { 0x01 };
        byte[] buffer = new byte[2];
        int len = baseNCodecInputStream.read(buffer, 0, buffer.length);

        assertEquals(expected.length, len);
        assertArrayEquals(expected, buffer);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#markSupported()} method.
     */
    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Tests the {@link BaseNCodecInputStream#close()} method.
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        baseNCodecInputStream.close();
        assertNull(baseNCodecInputStream.in);
        assertNull(baseNCodecInputStream.baseNCodec);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor with
     * encoding.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamWithEncoding() throws IOException {
        InputStream inputStreamWithEncoding = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodecWithEncoding = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStreamWithEncoding = new BaseNCodecInputStream(inputStreamWithEncoding,
                baseNCodecWithEncoding, true);

        byte[] buffer = new byte[3];
        int len = baseNCodecInputStreamWithEncoding.read(buffer, 0, buffer.length);

        assertEquals(buffer.length, len);
        assertArrayEquals(new byte[] { 0x03, 0x01, 0x02 }, buffer);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor with
     * decoding.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamWithDecoding() throws IOException {
        InputStream inputStreamWithDecoding = new ByteArrayInputStream(new byte[] { 0x03, 0x01, 0x02 });
        BaseNCodec baseNCodecWithDecoding = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStreamWithDecoding = new BaseNCodecInputStream(inputStreamWithDecoding,
                baseNCodecWithDecoding, false);

        byte[] buffer = new byte[3];
        int len = baseNCodecInputStreamWithDecoding.read(buffer, 0, buffer.length);

        assertEquals(buffer.length, len);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);
    }

    /**
     * Tests the {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor with EOF.
     *
     * @throws IOException
     */
    @Test
    public void testBaseNCodecInputStreamWithEOF() throws IOException {
        InputStream inputStreamWithEOF = mock(InputStream.class);
        BaseNCodec baseNCodecWithEOF = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStreamWithEOF = new BaseNCodecInputStream(inputStreamWithEOF,
                baseNCodecWithEOF, false);

        when(inputStreamWithEOF.read()).thenReturn(-1);

        byte[] buffer = new byte[1];
        assertEquals(-1, baseNCodecInputStreamWithEOF.read(buffer, 0, buffer.length));
        assertEquals(EOF, baseNCodecInputStreamWithEOF.read());
    }
}