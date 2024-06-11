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

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor.
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstructor() {
        InputStream input = new ByteArrayInputStream("ABCDEFGHIGKL1234567890".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(input, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertFalse(inputStream.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)} constructor with
     * encoding.
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamConstructorWithEncoding() {
        InputStream input = new ByteArrayInputStream("ABCDEFGHIGKL1234567890".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(input, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertTrue(inputStream.doEncode);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read()} method.
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.read(), 'A');
        assertEquals(inputStream.read(), 'B');
        assertEquals(inputStream.read(), 'C');
        assertEquals(inputStream.read(), 'D');
        assertEquals(inputStream.read(), 'E');
        assertEquals(inputStream.read(), 'F');
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method.
     */
    @org.junit.Test
    public void testReadByteArray() throws IOException {
        String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        byte[] expected = testData.getBytes();
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[testData.length()];
        int readLength = inputStream.read(buffer, 0, testData.length());
        assertEquals(readLength, testData.length());
        assertArrayEquals(expected, buffer);

        // test reading less than the buffer size
        readLength = inputStream.read(buffer, 0, testData.length() / 2);
        assertArrayEquals(Arrays.copyOfRange(expected, 0, testData.length() / 2), buffer, 0, testData.length() / 2);

        // test reading more than the buffer size
        readLength = inputStream.read(buffer, testData.length() / 2, testData.length() - testData.length() / 2);
        assertArrayEquals(Arrays.copyOfRange(expected, testData.length() / 2, testData.length()), buffer, testData.length() / 2, testData.length() - testData.length() / 2);

        // test reading EOF
        readLength = inputStream.read(buffer, 0, testData.length());
        assertArrayEquals(new byte[0], buffer);
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertNull(inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#markSupported()} method.
     */
    @org.junit.Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test for {@link BaseNCodecInputStream#skip(long)} method.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        long toSkip = 10;
        long skipped = inputStream.skip(toSkip);
        assertEquals(skipped, toSkip);
        assertEquals(inputStream.read(), 'I');
        assertEquals(inputStream.read(), 'J');
        assertEquals(inputStream.read(), 'K');
        assertEquals(inputStream.read(), 'L');
        assertEquals(inputStream.read(), 'M');
        assertEquals(inputStream.read(), 'N');
        assertEquals(inputStream.read(), 'O');
        assertEquals(inputStream.read(), 'P');
        assertEquals(inputStream.read(), 'Q');
        assertEquals(inputStream.read(), 'R');
        assertEquals(inputStream.read(), 'S');
        assertEquals(inputStream.read(), 'T');
        assertEquals(inputStream.read(), 'U');
        assertEquals(inputStream.read(), 'V');
        assertEquals(inputStream.read(), 'W');
        assertEquals(inputStream.read(), 'X');
        assertEquals(inputStream.read(), 'Y');
        assertEquals(inputStream.read(), 'Z');
        assertEquals(inputStream.read(), '1');
        assertEquals(inputStream.read(), '2');
        assertEquals(inputStream.read(), '3');
        assertEquals(inputStream.read(), '4');
        assertEquals(inputStream.read(), '5');
        assertEquals(inputStream.read(), '6');
        assertEquals(inputStream.read(), '7');
        assertEquals(inputStream.read(), '8');
        assertEquals(inputStream.read(), '9');
        assertEquals(inputStream.read(), '0');
        assertEquals(inputStream.read(), EOF);
        assertEquals(inputStream.read(), EOF);
        assertEquals(inputStream.read(), EOF);
        assertNull(inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#read(byte[], int, int)} method with encoding.
     */
    @org.junit.Test
    public void testReadByteArrayWithEncoding() throws IOException {
        String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        byte[] expected = testData.getBytes();
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] buffer = new byte[testData.length()];
        int readLength = inputStream.read(buffer, 0, testData.length());
        assertArrayEquals(expected, buffer);

        // test reading less than the buffer size
        readLength = inputStream.read(buffer, 0, testData.length() / 2);
        assertArrayEquals(Arrays.copyOfRange(expected, 0, testData.length() / 2), buffer, 0, testData.length() / 2);

        // test reading more than the buffer size
        readLength = inputStream.read(buffer, testData.length() / 2, testData.length() - testData.length() / 2);
        assertArrayEquals(Arrays.copyOfRange(expected, testData.length() / 2, testData.length()), buffer, testData.length() / 2, testData.length() - testData.length() / 2);

        // test reading EOF
        readLength = inputStream.read(buffer, 0, testData.length());
        assertEquals(readLength, testData.length());
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertNull(inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#close()} method.
     */
    @org.junit.After
    public void testClose() throws IOException {
        String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertNull(inputStream.read());
    }

    /**
     * Test for {@link BaseNCodecInputStream#close()} method with encoding.
     */
    @org.junit.After
    public void testCloseWithEncoding() throws IOException {
        String testData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        InputStream input = new ByteArrayInputStream(testData.getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        inputStream.close();
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertEquals(EOF, inputStream.read());
        assertNull(inputStream.read());
    }
}