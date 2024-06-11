package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private InputStream inputStream;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x41, 0x41, 0x41, 0x41, 0x41 });
    }

    @Test
    public void testBaseNCodecInputStreamConstruction() throws IOException {
        // Test construction with false doEncode flag
        BaseNCodecInputStream baseNCodecInputStreamFalse = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertTrue(baseNCodecInputStreamFalse instanceof BaseNCodecInputStream);

        // Test construction with true doEncode flag
        BaseNCodecInputStream baseNCodecInputStreamTrue = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertTrue(baseNCodecInputStreamTrue instanceof BaseNCodecInputStream);
    }

    @Test
    public void testRead() throws IOException {
        // Test reading with false doEncode flag
        BaseNCodecInputStream baseNCodecInputStreamFalse = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] input = new byte[6];
        int count = baseNCodecInputStreamFalse.read(input, 0, input.length);
        assertEquals(count, input.length);
        assertNull(baseNCodecInputStreamFalse.markSupported());
        assertEquals(baseNCodecInputStreamFalse.read(), EOF);

        // Test reading with true doEncode flag
        BaseNCodecInputStream baseNCodecInputStreamTrue = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        byte[] output = new byte[BaseNCodec.BASE_32_ENCODED_LENGTH];
        count = baseNCodecInputStreamTrue.read(output, 0, output.length);
        assertEquals(count, BaseNCodec.BASE_32_ENCODED_LENGTH);
        byte[] decodedOutput = new byte[5];
        int decodedCount = baseNCodec.decode(new ByteArrayInputStream(output), 0, decodedOutput, 0, decodedOutput.length);
        assertEquals(decodedCount, input.length);
        assertArrayEquals(input, decodedOutput);
        assertEquals(baseNCodecInputStreamTrue.read(), EOF);
    }

    @Test
    public void testReadWithNegativeSkipValue() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        try {
            baseNCodecInputStream.skip(100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.close();
        assertEquals(inputStream.read(), EOF);
    }

    @Test
    public void testCloseTwice() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.close();
        baseNCodecInputStream.close();
        assertEquals(inputStream.read(), EOF);
    }

    @Test
    public void testReadWithMark() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);
        int mark = baseNCodecInputStream.markSupported();
        assertNull(mark);
        byte[] input = new byte[5];
        int read = baseNCodecInputStream.read(input, 0, input.length);
        assertEquals(read, input.length);
        baseNCodecInputStream.mark(100);
        read = baseNCodecInputStream.read(input, 0, input.length);
        assertEquals(read, input.length);
        baseNCodecInputStream.reset();
        read = baseNCodecInputStream.read(input, 0, input.length);
        assertEquals(read, input.length);
        baseNCodecInputStream.close();
        assertEquals(inputStream.read(), EOF);
    }
}