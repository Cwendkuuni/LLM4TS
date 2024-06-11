package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private BaseNCodec baseNCodec;

    private BaseNCodecInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Override
    public void setUp() throws Exception {
        baseNCodec = new BaseNCodec(BaseNCodec.BASE32, false);
        outputStream = new ByteArrayOutputStream();
    }

    @org.junit.Test
    public void testReadSingleByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading a single byte
        assertEquals((int) 'a', inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[2];
        int length = inputStreamWrapped.read(result, 0, 2);

        // Test reading multiple bytes
        assertEquals(length, 2);
        assertEquals((int) 'a', result[0]);
        assertEquals((int) 'b', result[1]);
    }

    @org.junit.Test
    public void testReadEmptyStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test reading an empty stream
        assertFalse(inputStreamWrapped.hasNext());
        assertEquals(-1, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testEncodeAndDecode() throws IOException {
        String input = "TestBaseNCodecInputStream";
        byte[] encodedBytes = baseNCodec.encode(input.getBytes(StandardCharsets.UTF_8));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] decodedBytes = new byte[input.length()];
        inputStreamWrapped.read(decodedBytes, 0, input.length());

        // Test encoding and decoding a string
        assertEquals(input, new String(decodedBytes, StandardCharsets.UTF_8));
    }

    @org.junit.Test
    public void testEncodeAndDecodeFailed() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] decodedBytes = new byte[10];
        int length = inputStreamWrapped.read(decodedBytes, 0, 10);

        // Test encoding and decoding fails
        assertFalse(inputStreamWrapped.hasNext());
        assertNull(length);
    }

    @org.junit.Test
    public void testMarkSupported() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test mark supported
        assertFalse(inputStreamWrapped.markSupported());
    }

    @org.junit.Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62, (byte) 0x63 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test skip
        inputStreamWrapped.skip(3);
        assertEquals(0, inputStreamWrapped.read());
        assertEquals(EOF, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testClosingStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test closing the stream
        inputStreamWrapped.close();
        assertEquals(EOF, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testClosingStreamAfterEOF() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test closing the stream after EOF
        inputStreamWrapped.close();
        assertEquals(EOF, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testClosingStreamWhileReading() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] decodedBytes = new byte[1];

        // Test closing the stream while reading
        inputStreamWrapped.read(decodedBytes, 0, 1);
        inputStreamWrapped.close();
        assertEquals(EOF, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testClosingStreamWhileSkipping() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62, (byte) 0x63 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test closing the stream while skipping
        inputStreamWrapped.skip(3);
        inputStreamWrapped.close();
        assertEquals(EOF, inputStreamWrapped.read());
    }

    @org.junit.Test
    public void testClosingStreamWhileDecoding() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62, (byte) 0x63 });
        BaseNCodecInputStream inputStreamWrapped = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] decodedBytes = new byte[3];

        // Test closing the stream while decoding
        inputStreamWrapped.read(decodedBytes, 0, 3);
        inputStreamWrapped.close();
        assertEquals(EOF, inputStreamWrapped.read());
    }
}