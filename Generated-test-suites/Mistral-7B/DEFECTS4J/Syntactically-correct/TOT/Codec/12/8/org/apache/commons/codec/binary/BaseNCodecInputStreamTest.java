package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test cases for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private static final byte[] TEST_DATA = Base32.decodeBase32("BLZXV2M8YQDZAQ==");

    /**
     * Tests reading a single byte from an {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testReadByte() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final int expected = TEST_DATA[0];
        final int actual = testee.read();

        assertEquals(expected, actual);
    }

    /**
     * Tests reading multiple bytes from an {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final byte[] expected = TEST_DATA;
        final byte[] actual = new byte[TEST_DATA.length];

        final int bytesRead = testee.read(actual, 0, TEST_DATA.length);

        assertArrayEquals(expected, actual);
        assertEquals(TEST_DATA.length, bytesRead);
    }

    /**
     * Tests reading past the end of the input stream.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testReadPastEnd() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final byte[] buffer = new byte[TEST_DATA.length + 1];

        final int bytesRead = testee.read(buffer, 0, buffer.length);

        assertEquals(TEST_DATA.length, bytesRead);
        assertEquals(EOF, testee.read());
    }

    /**
     * Tests marking an {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test(expected = IOException.class)
    public void testMarkSupported() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testee.markSupported();
    }

    /**
     * Tests skipping bytes in an {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final long bytesToSkip = TEST_DATA.length / 2;

        testee.skip(bytesToSkip);

        final byte[] buffer = new byte[TEST_DATA.length - (int) bytesToSkip];
        testee.read(buffer, 0, buffer.length);

        assertArrayEquals(Arrays.copyOfRange(TEST_DATA, (int) bytesToSkip, TEST_DATA.length), buffer);
    }

    /**
     * Tests reading from an empty {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testEmptyInputStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNcodec, false);

        final byte[] buffer = new byte[1];

        final int bytesRead = testee.read(buffer, 0, buffer.length);

        assertEquals(EOF, bytesRead);
        assertNull(testee.markSupported());
    }

    /**
     * Tests reading from a {@code BaseNCodecInputStream} instance with an underlying {@code InputStream} that throws
     * an I/O error.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testIOException() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA) {
            @Override
            public int read() throws IOException {
                throw new IOException("test");
            }
        };
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[TEST_DATA.length];

        assertThrows(IOException.class, () -> testee.read(buffer, 0, TEST_DATA.length));
        assertNull(testee.markSupported());
    }

    /**
     * Tests reading from a {@code BaseNCodecInputStream} instance with an underlying {@code InputStream} that returns
     * zero.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testZeroByte() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        final byte[] buffer = new byte[TEST_DATA.length];

        testee.read(buffer, 0, TEST_DATA.length);

        assertArrayEquals(new byte[0], buffer);
        assertEquals(EOF, testee.read());
    }

    /**
     * Tests encoding and decoding a byte array using an {@code BaseNCodecInputStream} instance.
     *
     * @throws IOException for any I/O error
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        final byte[] decoded = new byte[TEST_DATA.length];

        final byte[] encoded = new byte[TEST_DATA.length * 2];

        testee.read(encoded, 0, encoded.length);

        final BaseNCodec decoder = new Base32();
        final InputStream decoderInput = new ByteArrayInputStream(encoded);

        final int bytesRead = decoder.read(decoded, 0, TEST_DATA.length);

        assertEquals(TEST_DATA.length, bytesRead);
        assertArrayEquals(TEST_DATA, decoded);
    }
}