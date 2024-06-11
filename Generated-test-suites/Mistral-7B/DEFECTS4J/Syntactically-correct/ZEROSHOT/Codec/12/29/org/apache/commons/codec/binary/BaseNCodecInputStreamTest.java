package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF_MARK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    private static final byte[] BASE_N_ENCODED = new byte[] { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
            0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B,
            0x0C, 0x0D, 0x0E, 0x0F, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
            0x0F, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };

    private static final byte[] ORIGINAL_DATA = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B,
            0x1C, 0x1D, 0x1E, 0x1F };

    @Test
    public void testReadWithEncodeModeTrue() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] readData = new byte[BASE_N_ENCODED.length];

        int readLength;
        while ((readLength = testStream.read(readData, 0, readData.length)) != -1) {
            outputStream.write(readData, 0, readLength);
        }

        byte[] decodedData = outputStream.toByteArray();
        assertEquals(ORIGINAL_DATA.length, decodedData.length);
        assertNotNull(decodedData);
        assertEquals(ORIGINAL_DATA, decodedData);
    }

    @Test
    public void testReadWithEncodeModeFalse() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] readData = new byte[BASE_N_ENCODED.length];

        int readLength;
        while ((readLength = testStream.read(readData)) != -1) {
            // do nothing
        }

        byte[] decodedData = new byte[ORIGINAL_DATA.length];
        codec.decode(BASE_N_ENCODED, 0, BASE_N_ENCODED.length, decodedData, 0);
        assertEquals(ORIGINAL_DATA.length, decodedData.length);
        assertNotNull(decodedData);
        assertEquals(ORIGINAL_DATA, decodedData);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        assertTrue(testStream.markSupported() == false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        testStream.read(-1, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        testStream.read(new byte[BASE_N_ENCODED.length], 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeSkip() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        testStream.skip(Integer.MIN_VALUE);
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        int skipCount = BASE_N_ENCODED.length / 2;
        testStream.skip(skipCount);

        byte[] buffer = new byte[BASE_N_ENCODED.length];
        int readLength = testStream.read(buffer);
        assertEquals(ORIGINAL_DATA.length - skipCount, readLength);
    }

    @Test
    public void testReadWithEOF() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(BASE_N_ENCODED);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(inputStream, codec, false);

        byte[] readData = new byte[BASE_N_ENCODED.length];
        int readLength;

        // read all data
        while ((readLength = testStream.read(readData)) > 0) {
            // do nothing
        }

        // read EOF
        assertEquals(EOF, testStream.read());
    }
}