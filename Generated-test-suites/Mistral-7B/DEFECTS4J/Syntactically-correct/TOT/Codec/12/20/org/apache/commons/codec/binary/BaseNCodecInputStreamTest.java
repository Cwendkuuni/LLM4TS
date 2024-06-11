package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private static final BaseNCodec BASE_N_CODEC = new BaseNCodec();

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        byte[] b = new byte[10];
        int read = baseNCodecInputStream.read(b, 0, 10);
        assertEquals(10, read);
        for (int i = 0; i < read; i++) {
            assertEquals(i < 10 ? (byte) ('A' + i) : (byte) ('a' + i - 10), b[i]);
        }

        read = baseNCodecInputStream.read();
        assertEquals(EOF, read);
        assertFalse(baseNCodecInputStream.hasNextByte());
    }

    @Test
    public void testReadWithEncoding() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("A==".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, true);

        byte[] b = new byte[1];
        int read = baseNCodecInputStream.read(b, 0, 1);
        assertEquals(1, read);
        assertEquals((byte) 'A', b[0]);

        read = baseNCodecInputStream.read();
        assertEquals(EOF, read);
        assertFalse(baseNCodecInputStream.hasNextByte());
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetOrLength() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        baseNCodecInputStream.read(new byte[10][], -1, 1);
        baseNCodecInputStream.read(new byte[10][], 10, 1);
    }

    @Test
    public void testMarkSupported() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        byte[] b = new byte[10];
        baseNCodecInputStream.skip(256 * 1024); // skip 256 KB
        int read = baseNCodecInputStream.read(b, 0, 10);
        assertEquals(0, read);

        baseNCodecInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        // check if available method returns correct amount of data
        int available = baseNCodecInputStream.available();
        assertEquals(output.size(), available);
    }

    @Test
    public void testClosed() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, BASE_N_CODEC, false);

        baseNCodecInputStream.close();
        int read = baseNCodecInputStream.read();
        assertNull(read);
        assertFalse(baseNCodecInputStream.hasNextByte());
    }
}