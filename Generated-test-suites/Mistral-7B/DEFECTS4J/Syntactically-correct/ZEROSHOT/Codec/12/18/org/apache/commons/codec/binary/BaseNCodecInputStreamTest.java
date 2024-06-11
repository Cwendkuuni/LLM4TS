package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @version $Id:BaseNCodecInputStreamTest.java 1478288 2013-03-21 01:18:11Z ggregory $
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertNotNull(codecInputStream);
    }

    /**
     * Test reading a single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(1, codecInputStream.read());
        assertEquals(1, codecInputStream.read());
        assertEquals(EOF, codecInputStream.read());
    }

    /**
     * Test reading multiple bytes.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[3];
        assertEquals(3, codecInputStream.read(buffer, 0, 3));
        assertArrayEquals(new byte[] { 1, 2, 3 }, buffer);
        assertEquals(EOF, codecInputStream.read());
    }

    /**
     * Test reading multiple bytes with a buffer of insufficient size.
     */
    @Test
    public void testReadMultipleBytesWithBufferOfInsufficientSize() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3, 4, 5 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[2];
        assertEquals(2, codecInputStream.read(buffer, 0, 2));
        assertEquals(EOF, codecInputStream.read());
    }

    /**
     * Test markSupported.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        codecInputStream.markSupported();
    }

    /**
     * Test skip.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3, 4, 5 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        codecInputStream.skip(5);
        assertEquals(4, codecInputStream.read(new byte[4], 0, 4));
        assertEquals(EOF, codecInputStream.read());
    }

    /**
     * Test reading bytes from an encoded input stream.
     */
    @Test
    public void testReadBytesFromEncodedInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(BaseNCodec.BASE32.encode(new byte[] { 1, 2, 3, 4, 5 }));
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[5];
        assertEquals(5, codecInputStream.read(buffer, 0, 5));
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, buffer);
        assertEquals(EOF, codecInputStream.read());
    }

    /**
     * Test reading bytes from a decoded input stream.
     */
    @Test
    public void testReadBytesFromDecodedInputStream() throws IOException {
        byte[] input = BaseNCodec.BASE32.decode(new byte[] { 1, 2, 3, 4, 5 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream decodedInputStream = new FilterInputStream(new ByteArrayInputStream(input)) {
            @Override
            public int read() throws IOException {
                return baseNCodec.decodeNextByte(super.read());
            }
        };
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(decodedInputStream, baseNCodec, true);
        byte[] buffer = new byte[5];
        assertEquals(5, codecInputStream.read(buffer, 0, 5));
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, buffer);
        assertEquals(EOF, codecInputStream.read());
    }
}