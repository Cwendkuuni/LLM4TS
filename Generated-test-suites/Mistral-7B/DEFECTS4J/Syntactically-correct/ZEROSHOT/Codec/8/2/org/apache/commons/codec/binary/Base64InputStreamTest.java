package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Tests for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in).
     */
    @org.junit.Test
    public void testBase64InputStream_InputStream() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        final byte[] result = new byte[inputStream.available()];
        final int read = base64InputStream.read(result);
        assertEquals(inputStream.available(), read);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, result);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode).
     */
    @org.junit.Test
    public void testBase64InputStream_InputStream_boolean() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        final byte[] result = new byte[inputStream.available()];
        final int read = base64InputStream.read(result);
        assertEquals(inputStream.available(), read);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, result);
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator).
     */
    @org.junit.Test
    public void testBase64InputStream_InputStream_boolean_int_byteArray() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final byte[] lineSeparator = new byte[] { 0x0D, 0x0A };
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);

        final byte[] result = new byte[76];
        final int read = base64InputStream.read(result);
        assertEquals(76, read);
        assertFalse(base64InputStream.hasNextLine());

        final byte[] nextLineResult = new byte[76];
        final int nextLineRead = base64InputStream.read(nextLineResult);
        assertNull(nextLineRead);
        assertFalse(base64InputStream.hasNextLine());

        inputStream.reset();
        final int len = inputStream.available();
        final byte[] input = new byte[len];
        inputStream.read(input);

        final byte[] decoded = new byte[len];
        final int decodedLen = Base64.decode(input, decoded, 0, len);
        final byte[] encoded = new byte[len];
        final int encodedLen = Base64.encode(decoded, 0, decodedLen, encoded, 0, encoded.length);

        final Base64InputStream base64InputStreamEncoded = new Base64InputStream(new ByteArrayInputStream(encoded), true, 76, lineSeparator);
        final byte[] encodedLine = new byte[76];
        final int encodedLineRead = base64InputStreamEncoded.read(encodedLine);
        assertEquals(encodedLineRead, encodedLen);
        assertArrayEquals(encoded, encodedLine);
        final boolean hasNextLineEncoded = base64InputStreamEncoded.hasNextLine();
        assertTrue(hasNextLineEncoded);

        final byte[] nextLineEncodedResult = new byte[76];
        final int nextLineEncodedRead = base64InputStreamEncoded.read(nextLineEncodedResult);
        assertNull(nextLineEncodedRead);
        assertFalse(base64InputStreamEncoded.hasNextLine());
    }

    /**
     * Test method read().
     */
    @org.junit.Test
    public void testRead() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        final byte[] result = new byte[inputStream.available()];
        final int read = base64InputStream.read(result);
        assertEquals(inputStream.available(), read);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, result);

        final byte[] emptyResult = new byte[1];
        final int emptyRead = base64InputStream.read(emptyResult);
        assertEquals(-1, emptyRead);
    }

    /**
     * Test method read(byte[]).
     */
    @org.junit.Test
    public void testReadByteArray() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        final byte[] result = new byte[inputStream.available()];
        final int read = base64InputStream.read(result);
        assertEquals(inputStream.available(), read);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, result);

        final byte[] emptyResult = new byte[1];
        final int emptyRead = base64InputStream.read(emptyResult);
        assertEquals(-1, emptyRead);
    }

    /**
     * Test method markSupported().
     */
    @org.junit.Test
    public void testMarkSupported() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        final boolean markSupported = base64InputStream.markSupported();
        assertFalse(markSupported);
    }

    /**
     * Test method hasNextLine().
     */
    @org.junit.Test
    public void testHasNextLine() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33 });
        final byte[] lineSeparator = new byte[] { 0x0D, 0x0A };
        final Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, lineSeparator);

        final boolean hasNextLine = base64InputStream.hasNextLine();
        assertFalse(hasNextLine);

        final InputStream inputStreamEncoded = new ByteArrayInputStream(Base64.encode(new byte[] { 0x31, 0x32, 0x33 }, 0, 3, new byte[3], 0, 3));
        final Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputStreamEncoded, true, 76, lineSeparator);

        final boolean hasNextLineEncoded = base64InputStreamEncoded.hasNextLine();
        assertTrue(hasNextLineEncoded);

        final byte[] result = new byte[76];
        final int read = base64InputStreamEncoded.read(result);
        assertEquals(3, read);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, result, 0, 3);
        final boolean hasNextLineEncodedNext = base64InputStreamEncoded.hasNextLine();
        assertFalse(hasNextLineEncodedNext);

        inputStreamEncoded.reset();

        final byte[] line = new byte[76];
        final int readLine = base64InputStreamEncoded.read(line);
        assertEquals(3, readLine);
        assertArrayEquals(new byte[] { 0x31, 0x32, 0x33 }, line, 0, 3);
        final boolean hasNextLineEncodedNextLine = base64InputStreamEncoded.hasNextLine();
        assertFalse(hasNextLineEncodedNextLine);
    }
}