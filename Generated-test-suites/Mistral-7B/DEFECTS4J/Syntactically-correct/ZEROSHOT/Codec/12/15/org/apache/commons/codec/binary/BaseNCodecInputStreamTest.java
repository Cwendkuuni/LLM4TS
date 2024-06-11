package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
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
 */
public class BaseNCodecInputStreamTest {

    /**
     * Tests constructor with {@code doEncode = false}.
     */
    @Test
    public void testDecode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"AQ==".getBytes()});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        do {
            bytesRead = baseNCodecInputStream.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } while (bytesRead > 0);

        assertFalse(baseNCodecInputStream.markSupported());
        assertEquals(EOF, baseNCodecInputStream.read());

        byte[] expected = {"Hello, World!".getBytes()};
        byte[] actual = outputStream.toByteArray();
        assertNotNull(actual);
        assertArrayEquals(expected, actual);
        baseNCodecInputStream.close();
    }

    /**
     * Tests constructor with {@code doEncode = true}.
     */
    @Test
    public void testEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"Hello, World!".getBytes()});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        do {
            bytesRead = baseNCodecInputStream.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } while (bytesRead > 0);

        assertFalse(baseNCodecInputStream.markSupported());
        assertEquals(EOF, baseNCodecInputStream.read());

        byte[] expected = {"SSbvncGw8C3RleHQgVGpqb2M=".getBytes()};
        byte[] actual = outputStream.toByteArray();
        assertNotNull(actual);
        assertArrayEquals(expected, actual);
        baseNCodecInputStream.close();
    }

    /**
     * Tests {@link BaseNCodecInputStream#markSupported()} with {@code false}.
     */
    @Test
    public void testMarkSupportedFalse() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Tests {@link BaseNCodecInputStream#skip(long)} with {@code 512}.
     */
    @Test
    public void testSkip512() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"AQ==".getBytes()});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long bytesToSkip = 512;
        baseNCodecInputStream.skip(bytesToSkip);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Tests {@link BaseNCodecInputStream#available()} and {@link BaseNCodecInputStream#read()} methods.
     */
    @Test
    public void testAvailableAndRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{"AQ==".getBytes()});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int available = baseNCodecInputStream.available();
        int bytesRead = 0;
        byte[] buffer = new byte[available];

        while (available > 0) {
            bytesRead += baseNCodecInputStream.read(buffer, bytesRead, available);
            available = baseNCodecInputStream.available();
        }

        assertEquals(EOF, baseNCodecInputStream.read());

        byte[] expected = {"Hello, World!".getBytes()};
        byte[] actual = new byte[expected.length];
        System.arraycopy(expected, 0, actual, 0, expected.length);
        assertArrayEquals(expected, actual);
    }
}