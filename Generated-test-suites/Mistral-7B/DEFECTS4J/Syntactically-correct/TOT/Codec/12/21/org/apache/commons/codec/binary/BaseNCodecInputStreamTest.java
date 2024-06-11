package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 *
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with decoding.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testDecodeBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        byte[] test = new byte[5];
        int bytesRead;

        // Test reading a single byte
        assertEquals(1, baseNCodecInputStream.read(test, 0, 1));
        assertArrayEquals(expected, test);

        // Test reading multiple bytes
        bytesRead = baseNCodecInputStream.read(test, 0, 5);
        assertArrayEquals(expected, test);

        // Test reading past EOF
        assertEquals(-1, baseNCodecInputStream.read(test, 0, 1));

        // Test decoding a BaseN encoded stream
        ByteArrayInputStream encodedInput = new ByteArrayInputStream(Base64.decodeBase64("VG8gZXhwIHlvdSBuZWVkIGlzIG15IHByZWZpeCArIGZpbGw="));
        BaseNCodecInputStream encodedStream = new BaseNCodecInputStream(encodedInput, baseNCodec, false);
        int i = 0;
        while ((bytesRead = encodedStream.read(test, 0, 5)) > 0) {
            outputStream.write(test, 0, bytesRead);
            i += bytesRead;
        }
        assertEquals(-1, encodedStream.read(test, 0, 1));
        assertTrue(i > 0);
        assertArrayEquals(new byte[] { 0x32, 0x30, 0x33, 0x31, 0x37, 0x32, 0x34, 0x32, 0x37, 0x34, 0x33, 0x31 }, outputStream.toByteArray());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} with encoding.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEncodeBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { '0', '1', '2', '3', '4', '5' });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expected = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };
        byte[] test = new byte[6];
        int bytesRead;

        // Test reading a single byte
        assertEquals(1, baseNCodecInputStream.read(test, 0, 1));
        assertArrayEquals(expected, test);

        // Test reading multiple bytes
        bytesRead = baseNCodecInputStream.read(test, 0, 6);
        assertArrayEquals(expected, test);

        // Test encoding a BaseN decoded stream
        ByteArrayInputStream decodedInput = new ByteArrayInputStream(new byte[] { 0x32, 0x30, 0x33, 0x31, 0x37, 0x32, 0x34, 0x32, 0x37, 0x34, 0x33, 0x31 });
        BaseNCodecInputStream decodedStream = new BaseNCodecInputStream(decodedInput, baseNCodec, true);
        int j = 0;
        while ((bytesRead = decodedStream.read(test, 0, 1)) > 0) {
            outputStream.write(test, 0, bytesRead);
            j++;
        }
        assertEquals(-1, decodedStream.read(test, 0, 1));
        assertTrue(j > 0);
        assertArrayEquals(new byte[] { '0', '1', '2', '3', '4', '5' }, outputStream.toByteArray());
    }
}