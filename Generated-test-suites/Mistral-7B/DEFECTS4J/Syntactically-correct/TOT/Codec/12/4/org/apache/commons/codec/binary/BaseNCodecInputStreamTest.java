package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

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
     * Test reading bytes from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading a single byte
        assertEquals((int) 0x41, inputStreamWrapper.read());

        // Test reading multiple bytes
        byte[] output = new byte[4];
        assertEquals(4, inputStreamWrapper.read(output, 0, 4));

        // Test reaching EOF
        assertFalse(inputStreamWrapper.hasNextByte());
    }

    /**
     * Test reading bytes from the input stream and decoding them.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testDecodeBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2a, 0x3a, 0x31, 0x35, 0x30, 0x38, 0x32, 0x32, 0x33, 0x33 });
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Test decoding a single byte
        assertEquals('*', (char) inputStreamWrapper.read());

        // Test decoding multiple bytes
        byte[] output = new byte[13];
        assertEquals(13, inputStreamWrapper.read(output, 0, 13));

        // Test the decoded bytes
        String decoded = new String(output);
        assertEquals("JUnit4JavadocTests", decoded);

        // Test reaching EOF
        assertFalse(inputStreamWrapper.hasNextByte());
    }

    /**
     * Test reading bytes from the input stream and encoding them.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 65, 66, 67, 68 });
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] encodedOutput = new byte[17];

        // Test encoding a single byte
        assertEquals(4, inputStreamWrapper.read(encodedOutput, 0, 4));
        assertEquals("QQ==", new String(encodedOutput, 0, 3));

        // Test encoding multiple bytes
        assertEquals(13, inputStreamWrapper.read(encodedOutput, 3, 13));
        assertEquals("RnZidGhpbmdzIHRoZSBmaWxlIHVzZXJuYW1lIHBsYW5ldCArMQ==", new String(encodedOutput, 0, 17));

        // Test reaching EOF
        assertFalse(inputStreamWrapper.hasNextByte());
    }

    /**
     * Test reading bytes from the input stream with empty input.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEmptyInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading a single byte
        assertFalse(inputStreamWrapper.hasNextByte());

        // Test decoding multiple bytes
        byte[] output = new byte[1];
        assertEquals(0, inputStreamWrapper.read(output, 0, 1));

        // Test reaching EOF
        assertFalse(inputStreamWrapper.hasNextByte());
    }

    /**
     * Test reading bytes from the input stream with invalid input.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test(expected = IOException.class)
    public void testInvalidInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0xFF }); // invalid baseN character
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading a single byte
        inputStreamWrapper.read();
    }

    /**
     * Test reading bytes from the input stream with invalid encoding.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test(expected = IOException.class)
    public void testInvalidEncoding() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodec baseNCodecInvalid = new BaseNCodec() {
            @Override
            public int decode(byte[] input, int offset, int length) {
                throw new RuntimeException("Invalid encoding");
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        BaseNCodecInputStream inputStreamWrapper = new BaseNCodecInputStream(inputStream, baseNCodecInvalid, false);

        // Test reading a single byte
        inputStreamWrapper.read();
    }
}