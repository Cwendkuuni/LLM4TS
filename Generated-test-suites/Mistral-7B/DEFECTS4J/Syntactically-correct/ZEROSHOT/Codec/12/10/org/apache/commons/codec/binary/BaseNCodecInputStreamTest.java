package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35 });
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testBaseNCodecInputStream() {
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertSame(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        assertEquals(0x31, baseNCodecInputStream.read());
        assertEquals(0x32, baseNCodecInputStream.read());
        assertEquals(0x33, baseNCodecInputStream.read());
        assertEquals(0x34, baseNCodecInputStream.read());
        assertEquals(0x35, baseNCodecInputStream.read());
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35 };
        byte[] result = new byte[expected.length];
        assertEquals(expected.length, baseNCodecInputStream.read(result));
        assertSame(expected, result);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     */
    @Test
    public void testReadByteArrayLimit() throws IOException {
        byte[] expected = new byte[] { 0x31, 0x32 };
        byte[] result = new byte[expected.length];
        assertEquals(expected.length, baseNCodecInputStream.read(result, 2, 3));
        assertNull(result);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        baseNCodecInputStream.markSupported();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)}.
     *
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38 };
        InputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        long countToSkip = 5;
        long bytesSkipped = baseNCodecInputStream.skip(countToSkip);
        assertEquals(countToSkip, bytesSkipped);
        assertEquals(0x36, baseNCodecInputStream.read());
        assertEquals(0x37, baseNCodecInputStream.read());
        assertEquals(0x38, baseNCodecInputStream.read());
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }
}