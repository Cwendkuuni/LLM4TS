package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading a single byte from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = inputStreamFilter.read();
        assertEquals(0x01, result);
    }

    /**
     * Test reading multiple bytes from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int numRead = inputStreamFilter.read(result, 0, 3);
        assertEquals(3, numRead);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
    }

    /**
     * Test reading beyond the available data in the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testReadBeyondAvailableData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int numRead = inputStreamFilter.read(result, 0, 3);
        assertEquals(2, numRead);
        assertArrayEquals(new byte[]{0x01, 0x02}, result);

        // now read from the empty buffer
        numRead = inputStreamFilter.read(result, 0, 3);
        assertEquals(-1, numRead);
    }

    /**
     * Test marking and resetting the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        inputStreamFilter.mark(0);
        inputStreamFilter.reset();
    }

    /**
     * Test skipping bytes in the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testSkipBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // skip 1 byte
        inputStreamFilter.skip(1);
        byte[] result = new byte[2];
        int numRead = inputStreamFilter.read(result, 0, 2);
        assertEquals(2, numRead);
        assertArrayEquals(new byte[]{0x03}, result);

        // skip 3 bytes
        inputStreamFilter.skip(3);
        numRead = inputStreamFilter.read(result, 0, 2);
        assertEquals(0, numRead);
    }

    /**
     * Test encoding and decoding bytes using BaseNCodecInputStream and
     * BaseNCodecOutputStream.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeDecodeBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec("BaseN", 32, false);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x61, 0x62, 0x63});
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecOutputStream outputStreamFilter = new BaseNCodecOutputStream(outputStream, baseNCodec, false);

        byte[] input = new byte[3];
        byte[] output = new byte[4];

        int numRead = 0;
        while ((numRead = inputStreamFilter.read(input, 0, 3)) > 0) {
            outputStreamFilter.write(input, 0, numRead);
        }

        outputStreamFilter.finish();
        outputStreamFilter.flush();

        byte[] decodedOutput = new byte[3];
        InputStream decoderInput = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream decoderFilter = new BaseNCodecInputStream(decoderInput, baseNCodec, true);

        numRead = decoderFilter.read(decodedOutput, 0, 3);
        assertEquals(3, numRead);
        assertArrayEquals(new byte[]{0x61, 0x62, 0x63}, decodedOutput);
    }
}