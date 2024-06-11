package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private static final byte[] ENCODED_DATA = BaseNCodec.BASE32.encode("TestData".getBytes());
    private static final byte[] DECODED_DATA = "TestData".getBytes();

    /**
     * Test reading a single byte from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        assertEquals(ENCODED_DATA[0], baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test reading multiple bytes from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        byte[] readBytes = new byte[ENCODED_DATA.length];
        int bytesRead = baseNCodecInputStream.read(readBytes, 0, ENCODED_DATA.length);
        assertEquals(ENCODED_DATA.length, bytesRead);
        assertArrayEquals(ENCODED_DATA, readBytes);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test reading multiple bytes (less than the length of the input) from the input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadPartialBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        byte[] readBytes = new byte[ENCODED_DATA.length / 2];
        int bytesRead = baseNCodecInputStream.read(readBytes, 0, readBytes.length);
        assertEquals(readBytes.length, bytesRead);
        assertArrayEquals(new byte[] { ENCODED_DATA[0], ENCODED_DATA[1] }, readBytes);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test reading from the input stream with no data.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testReadEmptyStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        byte[] readBytes = new byte[1];
        int bytesRead = baseNCodecInputStream.read(readBytes, 0, readBytes.length);
        assertEquals(0, bytesRead);
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test reading from the input stream with invalid offset.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        byte[] readBytes = new byte[1];
        int bytesRead = baseNCodecInputStream.read(readBytes, -1, readBytes.length);
    }

    /**
     * Test reading from the input stream with invalid length.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, new BaseNCodec(BaseNCodec.BASE32), false);

        byte[] readBytes = new byte[1];
        int bytesRead = baseNCodecInputStream.read(readBytes, 0, ENCODED_DATA.length + 1);
    }

    /**
     * Test encoding and decoding data using the BaseNCodecInputStream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        InputStream input = new ByteArrayInputStream(DECODED_DATA);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.BASE32);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] encodedData = new byte[1024];
        int bytesRead = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        baseNCodec.encode(encodedData, 0, bytesRead, output, 0);

        assertEquals(DECODED_DATA.length, output.size());
        assertArrayEquals(DECODED_DATA, output.toByteArray());
    }

    /**
     * Test encoding and decoding data using the BaseNCodecInputStream with a large chunk size.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeDecodeLargeChunkSize() throws IOException {
        InputStream input = new ByteArrayInputStream(DECODED_DATA);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.BASE32);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] encodedData = new byte[8192];
        int bytesRead = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        baseNCodec.encode(encodedData, 0, bytesRead, output, 0);

        assertEquals(DECODED_DATA.length, output.size());
        assertArrayEquals(DECODED_DATA, output.toByteArray());
    }

    /**
     * Test encoding and decoding data using the BaseNCodecInputStream with an empty input stream.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test
    public void testEncodeDecodeEmptyStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.BASE32);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] encodedData = new byte[1024];
        int bytesRead = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        baseNCodec.encode(encodedData, 0, bytesRead, output, 0);

        assertEquals(0, output.size());
    }

    /**
     * Test encoding and decoding data using the BaseNCodecInputStream with invalid input.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @org.junit.Test(expected = IOException.class)
    public void testEncodeDecodeInvalidInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        BaseNCodec baseNCodec = new BaseNCodec(BaseNCodec.BASE32);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] encodedData = new byte[1024];
        int bytesRead = baseNCodecInputStream.read(encodedData, 0, encodedData.length);
        baseNCodec.encode(encodedData, 0, bytesRead, output, 0);

        // The test should fail with an IOException as we're trying to read more
        // data than available in the input stream.
        baseNCodecInputStream.read(encodedData, 0, encodedData.length);
    }
}