package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /** The input byte array. */
    private static final byte[] INPUT_BYTE_ARRAY = { 1, 2, 3, 4, 5, 6, 7, 8 };

    /** The base64 encoded input string. */
    private static final String BASE64_INPUT_STRING = "VG8gZXhwIHlvdSBuZWVkIGlzIG15IHBsYW5ldC4=";

    /** The base64 decoded output byte array. */
    private static final byte[] OUTPUT_BYTE_ARRAY = { 49, 50, 51, 52, 53, 54, 55, 56 };

    /**
     * Test constructor with default decoding behavior.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testDefaultDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT_BYTE_ARRAY);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] out = new byte[INPUT_BYTE_ARRAY.length];
        int length = base64InputStream.read(out);
        assertEquals(INPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(INPUT_BYTE_ARRAY, out);
        base64InputStream.close();
    }

    /**
     * Test constructor with custom decoding behavior.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT_BYTE_ARRAY);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] out = new byte[INPUT_BYTE_ARRAY.length];
        int length = base64InputStream.read(out);
        assertEquals(INPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(INPUT_BYTE_ARRAY, out);
        base64InputStream.close();
    }

    /**
     * Test constructor with custom encoding and decoding behaviors.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomEncoderDecoder() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT_BYTE_ARRAY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes());
        Base64 base64 = new Base64(76, "\r\n".getBytes());

        int length = inputStream.read(INPUT_BYTE_ARRAY);
        base64OutputStream.write(INPUT_BYTE_ARRAY, 0, length);
        base64OutputStream.flush();

        byte[] encodedOutput = outputStream.toByteArray();
        byte[] decodedOutput = new byte[encodedOutput.length];

        InputStream decoderInput = new ByteArrayInputStream(encodedOutput);
        Base64InputStream base64InputStream = new Base64InputStream(decoderInput);

        length = base64InputStream.read(decodedOutput);
        assertEquals(INPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(INPUT_BYTE_ARRAY, decodedOutput);
        base64InputStream.close();

        outputStream.close();
        base64OutputStream.close();
        inputStream.close();
    }

    /**
     * Test reading with mark unsupported.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkUnsupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(INPUT_BYTE_ARRAY);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.mark(0);
    }

    /**
     * Test reading with invalid input.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IOException.class)
    public void testInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read();
        base64InputStream.read();
        base64InputStream.read();
    }

    /**
     * Test reading an empty input stream.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int length = base64InputStream.read();
        assertFalse(length > 0);
        base64InputStream.close();
    }

    /**
     * Test decoding invalid base64 data.
     *
     * @throws IOException
     * @throws DecoderException
     */
    @org.junit.Test(expected = DecoderException.class)
    public void testInvalidBase64Data() throws IOException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5, 'X' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] out = new byte[INPUT_BYTE_ARRAY.length + 1];
        int length = base64InputStream.read(out);
        assertEquals(length, -1);
    }

    /**
     * Test encoding and decoding the same data multiple times.
     *
     * @throws IOException
     * @throws EncoderException
     * @throws DecoderException
     */
    @org.junit.Test
    public void testEncodeDecodeMultipleTimes() throws IOException, EncoderException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(INPUT_BYTE_ARRAY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64 base64 = new Base64();

        int length = inputStream.read(INPUT_BYTE_ARRAY);
        base64OutputStream.write(INPUT_BYTE_ARRAY, 0, length);
        base64OutputStream.flush();

        byte[] encodedOutput = outputStream.toByteArray();
        byte[] decodedOutput = new byte[encodedOutput.length];

        InputStream decoderInput = new ByteArrayInputStream(encodedOutput);
        Base64InputStream base64InputStream = new Base64InputStream(decoderInput);

        length = base64InputStream.read(decodedOutput);
        assertEquals(INPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(INPUT_BYTE_ARRAY, decodedOutput);
        base64InputStream.close();

        encodedOutput = base64.encode(INPUT_BYTE_ARRAY);
        decodedOutput = base64.decode(encodedOutput);
        assertArrayEquals(INPUT_BYTE_ARRAY, decodedOutput);
    }

    /**
     * Test encoding and decoding large data.
     *
     * @throws IOException
     * @throws EncoderException
     * @throws DecoderException
     */
    @org.junit.Test
    public void testEncodeDecodeLargeData() throws IOException, EncoderException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(new byte[1024 * 1024]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64 base64 = new Base64();

        int length = inputStream.read(new byte[1024]);
        base64OutputStream.write(new byte[1024], 0, 1024);
        base64OutputStream.flush();

        byte[] encodedOutput = outputStream.toByteArray();
        byte[] decodedOutput = new byte[encodedOutput.length];

        InputStream decoderInput = new ByteArrayInputStream(encodedOutput);
        Base64InputStream base64InputStream = new Base64InputStream(decoderInput);

        length = base64InputStream.read(decodedOutput);
        assertEquals(1024, length);
        assertArrayEquals(new byte[1024], decodedOutput);
        base64InputStream.close();

        encodedOutput = base64.encode(new byte[1024 * 1024]);
        decodedOutput = base64.decode(encodedOutput);
        assertArrayEquals(new byte[1024 * 1024], decodedOutput);
    }

    /**
     * Test reading base64 data with custom line length.
     *
     * @throws IOException
     * @throws DecoderException
     */
    @org.junit.Test
    public void testCustomLineLength() throws IOException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(BASE64_INPUT_STRING.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, null);

        byte[] out = new byte[OUTPUT_BYTE_ARRAY.length];
        int length = base64InputStream.read(out);
        assertEquals(OUTPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(OUTPUT_BYTE_ARRAY, out);
        base64InputStream.close();
    }

    /**
     * Test reading base64 data with custom line separator.
     *
     * @throws IOException
     * @throws DecoderException
     */
    @org.junit.Test
    public void testCustomLineSeparator() throws IOException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(BASE64_INPUT_STRING.getBytes());
        byte[] lineSeparator = "\r\n\r\n".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);

        byte[] out = new byte[OUTPUT_BYTE_ARRAY.length];
        int length = base64InputStream.read(out);
        assertEquals(OUTPUT_BYTE_ARRAY.length, length);
        assertArrayEquals(OUTPUT_BYTE_ARRAY, out);
        base64InputStream.close();
    }
}